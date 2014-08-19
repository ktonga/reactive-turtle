package org.bfoit.tg;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.AccessControlException;
import javax.imageio.ImageIO;


/*
 * .BMP file support classes (for its data structures)
 *
 * For details on the format of a .BMP file, see:
 * http://en.wikipedia.org/wiki/BMP_file_format
 */

/**
 * Windows bitmap (.BMP) file header.
 */
class BMPHeader
{
   public final static int BMPHEADER_MAGIC_NUMBER = 0x424D;
   public final static int BMPHEADER_NUM_BYTES = 14;

   public short magicNumber;
   public int fileSize;
   public short reserved_1;
   public short reserved_2;
   public int imageOffset;


   public BMPHeader( byte[] bytes )
   {
      magicNumber = (short) (((int) bytes[0] & 0xff ) << 8);
      magicNumber |= (short) (bytes[1] & 0xff);
      fileSize = bytes[2] & 0xff;
      fileSize |= ((int) bytes[3] & 0xff ) << 8;
      fileSize |= ((int) bytes[4] & 0xff ) << 16;
      fileSize |= ((int) bytes[5] & 0xff ) << 24;
      imageOffset = bytes[10] & 0xff;
      imageOffset |= ((int) bytes[11] & 0xff ) << 8;
      imageOffset |= ((int) bytes[12] & 0xff ) << 16;
      imageOffset |= ((int) bytes[13] & 0xff ) << 24;
   }


   public String toString()
   {
      StringBuffer sb = new StringBuffer( "{" );
      sb.append( Integer.toHexString((int) magicNumber) );
      sb.append( ", " );
      sb.append( Integer.toString(fileSize) );
      sb.append( ", " );
      sb.append( Integer.toString(imageOffset) );
      sb.append( "}" );
      return sb.toString();
   }

} // end class org.bfoit.tg.BMPHeader


/**
 * Device-Independent Bitmap Header, which
 * follows the org.bfoit.tg.BMPHeader in a .BMP file.
 */
class DIBHeader
{
   public final static int BI_RGB = 0;

   public int compression;
   public int headerSize;
   public int height;
   public int imageSize;
   public int width;
   public short bitsPerPixel;


   public DIBHeader( byte[] bytes )
   {
      headerSize = bytesToInt( bytes, 0 );
      width = bytesToInt( bytes, 4 );
      height = bytesToInt( bytes, 8 );
      bitsPerPixel = (short) (bytes[14] & 0xff);
      bitsPerPixel |= (short) (((int) bytes[15] & 0xff ) << 8);
      compression = bytesToInt( bytes, 16 );
      imageSize = bytesToInt( bytes, 20 );
   }


   private int bytesToInt( byte[] bytes, int offset )
   {
      int retVal = bytes[offset] & 0xff;
      retVal |= ((int) bytes[offset+1] & 0xff ) << 8;
      retVal |= ((int) bytes[offset+2] & 0xff ) << 16;
      retVal |= ((int) bytes[offset+3] & 0xff ) << 24;
      return retVal;
   }


   public String toString()
   {
      StringBuffer sb = new StringBuffer( "{" );
      sb.append( Integer.toString(headerSize) );
      sb.append( ", " );
      sb.append( Integer.toString(width) );
      sb.append( ", " );
      sb.append( Integer.toString(height) );
      sb.append( ", " );
      sb.append( Short.toString(bitsPerPixel) );
      sb.append( ", " );
      sb.append( Integer.toString(compression) );
      sb.append( ", " );
      sb.append( Integer.toString(imageSize) );
      sb.append( "}" );
      return sb.toString();
   }

} // end class org.bfoit.tg.DIBHeader



/**
 * Utility methods for getting stuff from files on the local computer.
 * <p>
 * @author Guy Haas
 */
public class TGFileIO
{

   // Symbolic Constants
   // -------- ---------

   public static final String CLASS_NAME = "org.bfoit.tg.TGFileIO";


   private final static String CANT_CD_TO_Q = "Can't CD to '";
   private final static String Q_NOT_DIR = "' is not a directory";
   private final static String Q_NOT_PICT_FILE = "' is not a supported picture file";



   //
   // Class Variables
   // ----- ---------
 

   /*
    * The directory from which the applet/application was started.
    */
   private static String baseDirectory;


   /*
    * The directory where files referred to with relative paths
    * are located. This path is prefixed to any file name, which
    * may itself include directories, provided to getClip(),
    * getImage(), etc...
    */
   private static String currentDirectory;

 
   /*
    * String array of picture files available to the TG applet.
    */
   private static String[] pictFiles;

 
   /*
    * Root directories for system TG application is running on.
    */
   private static String[] roots;



   //
   // Support methods for this class
   // ------- ------- --- ---- -----


   /*
    * Return a file name with a default extension appended
    * if the provided fileName does not have an extension.
    */
   private static String addDefaultExtension( String fileName, String defaultSuffix )
   {
      int fromIdx = fileName.lastIndexOf( File.separatorChar );
      if ( fromIdx == -1 )
         fromIdx = 0;
      if ( fileName.indexOf(".", fromIdx) < 0 )
         fileName = fileName + defaultSuffix;
      return fileName;
   }


   /*
    * The block of bytes describes the image, pixel by pixel.
    * Pixels are stored "upside-down" with respect to normal
    * image raster scan order, starting in the lower left corner,
    * going from left to right, and then row by row from the
    * bottom to the top of the image.  If the number of bytes
    * matching a row (scanline) in the image is not divisible by
    * 4, the line is padded with one to three additional bytes
    * of unspecified value so that the next row will start on a
    * multiple of 4 byte location in memory or in the file.
    */
   private static PixelRectangle bmpBytesToPixRect( byte[] bytes, int width, int height )
   {
      int bytIdx = 0;
      int[] pixelArray = new int[width * height];
      for ( int rowNum=height-1; rowNum >= 0; rowNum-- )
      {
         for ( int colNum=0; colNum < width; colNum++ )
         {
            int pixel = 0xff << 24;
            pixel |= bytes[bytIdx++] & 0xff;
            pixel |= (bytes[bytIdx++] & 0xff) << 8;
            pixel |= (bytes[bytIdx++] & 0xff) << 16;
            pixelArray[rowNum*width + colNum] = pixel;
         }
         for ( int i=width%4; i > 0; i-- )
            bytIdx++;
      }
      return new PixelRectangle( pixelArray, width );
   }


   /*
    * Given an InputStream, use it to read data in .bmp
    * file format and convert it into a org.bfoit.tg.PixelRectangle
    */
   private static PixelRectangle bmpFileToPixRect( InputStream is ) throws IOException
   {
      String me = "bmpFileToPixRect(): ";
      BMPHeader hdr = null;
      DIBHeader dib = null;
      byte[] imgBytes = null;
      byte hdrBytes[] = new byte[BMPHeader.BMPHEADER_NUM_BYTES];
      inputStreamRead( is, hdrBytes, BMPHeader.BMPHEADER_NUM_BYTES );
      hdr = new BMPHeader( hdrBytes );
      if ( hdr.magicNumber != BMPHeader.BMPHEADER_MAGIC_NUMBER )
      {
         sysErr( me + "bad magic number" );
         return null;
      }
      int dibLen = hdr.imageOffset - BMPHeader.BMPHEADER_NUM_BYTES;
      byte dibBytes[] = new byte[dibLen];
      inputStreamRead( is, dibBytes, dibLen );
      dib = new DIBHeader( dibBytes );
      if ( dib.bitsPerPixel != 24 )
      {
         sysErr( me + "not 24-bits per pixel" );
         return null;
      }
      if ( dib.compression != DIBHeader.BI_RGB )
      {
         sysErr( me + "unsupported type of compression" );
         return null;
      }
      int imgLen = hdr.fileSize - hdr.imageOffset;
      imgBytes = new byte[imgLen];
      inputStreamRead( is, imgBytes, imgLen );
      return bmpBytesToPixRect( imgBytes, dib.width, dib.height );
   }


   /*
    * Given a BufferedImage representation of a org.bfoit.tg.Sprite image, return
    * a org.bfoit.tg.PixelRectangle representation.
    */
   private static PixelRectangle getPixRect( BufferedImage bufImg )
   {
      int height = bufImg.getHeight();
      int width = bufImg.getWidth();
      int[] rgbVals = bufImg.getRGB( 0, 0, width, height, null, 0, width );
      if ( rgbVals != null )
         return new PixelRectangle( rgbVals, width );
      return null;
   }


   /*
    * Fill a buffer with some number of bytes read from an InputStream.
    */
   private static int inputStreamRead( InputStream is, byte[] buffer, int bufSiz )
   throws IOException
   {
      int offset = 0;
      int readCount = bufSiz;
      while ( offset < bufSiz )
      {
         int bytCnt = is.read( buffer, offset, readCount );
         if ( bytCnt == -1 )
            return (offset > 0) ? offset : -1;
         readCount -= bytCnt;
         offset += bytCnt;
      }
      return offset;
   }


   /*
    * Read the contents of an InputStream into a byte array.
    */
   private static byte[] inputStreamToByteArray( InputStream is )
   {
      byte[] buffer = new byte[ 2048 ];
      int offset = 0;
      int readCnt = buffer.length;
      try
      {
         while ( (readCnt = is.read(buffer, offset, readCnt)) != -1 )
         {
            offset += readCnt;
            if ( offset == buffer.length )
            {
               byte[] oldBuf = buffer;
               buffer = new byte[ oldBuf.length + 2048 ];
               System.arraycopy( oldBuf, 0, buffer, 0, offset );
            }
            readCnt = buffer.length - offset;
         }
         is.close();
      }
      catch ( IOException ioe )
      { sysErr(".inputSteamToByteArray(): IOException '"+ioe+"'"); }
      byte[] retArray = new byte[ offset ];
      System.arraycopy( buffer, 0, retArray, 0, offset );
      return retArray;
   }


   /*
    * Convert a byte array containing text into an array
    * of Strings, one for each line in the byte array.
    */
   private static String[] linesToStrings( byte[] fileContents )
   {
      int numLines = 0;
      String[] lines = new String[128];
      StringBuffer sb = new StringBuffer(32);
      for ( int idx=0; idx < fileContents.length; idx++ )
      {
         byte ch = fileContents[idx];
         if ( ch == '\000' || ch == '\r' || ch == ' ' )
            continue;
         if ( fileContents[idx] == '\n' && sb.length() > 0 )
         {
            if ( numLines == lines.length )
            {
               String[] oldStrAry = lines;
               lines = new String[ oldStrAry.length + 32 ];
               System.arraycopy( oldStrAry, 0, lines, 0, oldStrAry.length );
            }
            lines[numLines++] = sb.toString();
            sb.setLength( 0 );
         }
         else
            sb.append( (char) ch );
      }
      if ( sb.length() > 0 )
      {
         if ( numLines == lines.length )
         {
            String[] oldStrAry = lines;
            lines = new String[ oldStrAry.length + 32 ];
            System.arraycopy( oldStrAry, 0, lines, 0, oldStrAry.length );
         }
         lines[numLines++] = sb.toString();
      }
      String[] retArray = new String[ numLines ];
      System.arraycopy( lines, 0, retArray, 0, numLines );
      return retArray;

   } // end linesToStrings()


   /*
    * Print an error message to System.err, tying it to
    * this class
    */
   private static void sysErr( String errTxt )
   { System.err.println( CLASS_NAME + "." + errTxt ); }



   //
   // Methods Available Outside This Class
   // ------- --------- ------- ---- -----


   /**
    * Return the current working directory.
    */
   public static String getCurrentDirectory()
   {
      if ( currentDirectory == null )
      {
         try { baseDirectory = System.getProperty( "user.dir" ); }
         catch ( SecurityException se )
         { sysErr("getCurrentDirectory(): SecurityException for getProperty(\"user.dir\")"); }
         currentDirectory = baseDirectory;
      }
      return currentDirectory;
   }


   /**
    * Given a file name, read the picture file in and
    * convert the contents into an Image.
    */
   public static Image getImage( String fileName )
   {
      PixelRectangle pixRect = getPixRect( fileName );
      if ( pixRect == null )
         return null;
      return pixRect.toImage();
   }


   /**
    * Given a file name (of a picture file), read it
    * in and convert its contents into a org.bfoit.tg.PixelRectangle.
    */
   public static PixelRectangle getPixRect( String fileName )
   {
      String me = ".getPixRect(): ";
      if ( fileName == null || fileName.length() == 0 )
         return null;
      fileName = addDefaultExtension( fileName, ".bmp" );
      if ( ! fileName.endsWith(".bmp") )
      {
         BufferedImage bufImg = null;
         File file = new File( getCurrentDirectory(), fileName );
         try { bufImg = ImageIO.read( file ); }
         catch ( IOException ioe )
         {
            sysErr( me + "'" + fileName + Q_NOT_PICT_FILE );
            return null;
         }
         return getPixRect( bufImg );
      }
      // .bmp file
      PixelRectangle pixRect = null;
      File file = null;
      if ( isAbsolutePath(fileName) )
         file = new File( fileName );
      else
         file = new File( getCurrentDirectory(), fileName );
      if ( file == null )
         return null;
      InputStream is = null;
      try
      { is = new FileInputStream( file ); }
      catch ( AccessControlException ace )
      {
         sysErr( me + "FileInputStream() threw AccessControlException '" + ace + "'" );
         return null;
      }
      catch (FileNotFoundException fnf )
      { return null; }
      if ( is == null )
         return null;
      try
      { pixRect = bmpFileToPixRect( is ); }
      catch ( IOException ioe )
      {
         sysErr( me + "getPixRect() threw IOException '" + ioe + "'" );
         return null;
      }
      try
      { is.close(); }
      catch ( IOException ioe )
      { sysErr( me + "close() threw IOException '" + ioe + "'" ); }
      return pixRect;
   }


   /**
    * Read the specified text file into an array of Strings, one for
    * each line of text.   Return null if the file can't be opened.
    */
   public static String[] getText( String fileName )
   {
      if ( fileName == null || fileName.length() == 0 )
         return null;
      File file = null;
      if ( isAbsolutePath(fileName) )
         file = new File( fileName );
      else
         file = new File( getCurrentDirectory(), fileName );
      InputStream inputStream = null;
      try { inputStream = new FileInputStream( file ); }
      catch ( FileNotFoundException fnf ) { return null; }
      if ( inputStream == null )
         return null;
      BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream) );
      String[] strAry = new String[1000];
      try
      {
         int numFilledStrs = 0;
         String line = null;
         while ( (line = reader.readLine()) != null )
         {
            if ( numFilledStrs == strAry.length )
            {
               String[] oldStrAry = strAry;
               strAry = new String[ oldStrAry.length + 200 ];
               System.arraycopy( oldStrAry, 0, strAry, 0, numFilledStrs );
            }
            strAry[ numFilledStrs++ ] = line;
         }
      }
      catch ( IOException ioe )
      { sysErr("getText(): File (" + fileName + ") IOException" + ioe); }
      return strAry;
   }


   /**
    * Determine if a path is absolute (starts with a component that is
    * a root of a file system).  If TG is running as an applet, false is
    * returned since file systems are inaccessible.  When TG running as
    * an application, return true if the path is absolute. If the path
    * is a relative path false is returned.
    */
   public static boolean isAbsolutePath( String path )
   {
      if ( roots == null )
      {
         File[] rootFiles = File.listRoots();
         roots = new String[rootFiles.length];
         for ( int i=0; i < rootFiles.length; i++ )
         {
            roots[i] = rootFiles[i].getAbsolutePath();
         }
      }
      for ( int i=0; i < roots.length; i++ )
      {
         int matchLength = roots[i].length();
         if ( path.length() < matchLength )
            matchLength = path.length();
         if ( path.regionMatches(true, 0, roots[i], 0, matchLength) )
            return true;
      }
      return false;
   }


   /**
    * Return a String array, each element is the name of a .bmp picture
    * file that is available on the server that provided the TG applet.
    */
   public static String[] pictFiles()
   {
      File curDir = new File( getCurrentDirectory() );
      String[] allEntries = curDir.list();
      int numPictFiles = 0;
      for ( int i=0; i < allEntries.length; i++ )
      {
         String fileName = allEntries[i];
         if (    fileName.endsWith(".bmp") || fileName.endsWith(".gif")
              || fileName.endsWith(".jpg") || fileName.endsWith(".png") )
            numPictFiles++;
      }
      pictFiles = new String[numPictFiles];
      int pfIdx = 0;
      for ( int i=0; i < allEntries.length; i++ )
      {
         String fileName = allEntries[i];
         if (    fileName.endsWith(".bmp") || fileName.endsWith(".gif")
              || fileName.endsWith(".jpg") || fileName.endsWith(".png") )
            pictFiles[pfIdx++] = fileName;
      }
      return pictFiles;
   }


   /**
    * Set the current directory. 
    */
   public static void setCurrentDirectory( String path )
   {
      if ( path == null || path.length() == 0 )
         currentDirectory = baseDirectory;
      else
      {
         String savCurDir = currentDirectory;
         if ( isAbsolutePath(path) )
         {
            if ( path.charAt(path.length()-1) != File.separatorChar )
               currentDirectory = path + File.separator;
            else
               currentDirectory = path;
         }
         else // relative path modification
         {
            String curDir = getCurrentDirectory();
            if ( curDir.charAt(curDir.length()-1) != File.separatorChar )
               curDir = curDir + File.separator;
            File newDir = new File( curDir + path + File.separator );
            if ( ! newDir.isDirectory() )
               sysErr( "'" + newDir.getAbsolutePath() + Q_NOT_DIR );
            try { currentDirectory = newDir.getCanonicalPath(); }
            catch (IOException ioe)
            {
               sysErr( CANT_CD_TO_Q + path + "'" );
            }
         }
         File file = new File( currentDirectory );
         if ( ! file.isDirectory() )
         {
            currentDirectory = savCurDir;
            sysErr( "'" + path + Q_NOT_DIR );
         }
      }
   }


} // end class FileIO
