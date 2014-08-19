package org.bfoit.tg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * Perform a TurtleGraphics FILL graphics operation.
 * <p>
 * Complexity in performing a FILL has its roots in the AWT's
 * mechanism for manipulating the RGB values of pixels representing
 * a Component.  In org.bfoit.tg.TGCanvas' case, the pixels displayed are in an
 * in-memory Image that all drawing has been performed on.
 * <p>
 * Due to an early emphasis placed on supporting the WWW (Java
 * applets), Image manipulation is asynchronous.  Specifically,
 * Graphics.drawImage() schedules/requests that the task is to be
 * done. IT DOESN'T NECESSARILY DO IT.  The situation is the same
 * going in the other direction, but PixelGrabber.grabPixels()
 * waits for completion.
 * <p>
 * Soapbox Opinion: Asynchronous manipulation of an Image makes
 * sense when its source is bits out on the net, it makes no
 * sense when the bits are in memory.  In fact, the M$ Windows
 * implementation appears to treat the in-memory case specially
 * and do it in a synchronous manner.
 * <p>
 * Second Complexity... once the FILL has been done on the pixels,
 * the new Image needs to be generated.  This is done with the
 * createImage() method.  The problem is that this is a "peer"
 * method, i.e., is different for each system.  On Apple OS-X
 * and Sun systems, createImage() dithers the pixels in the
 * process.  Nasty!!!  This means that back-to-back FILLs do not
 * work!  Worse yet, the dithering effects all pixels in the
 * rectangle passed to createImage().  So, I've added complexity
 * to limit the rectangle of pixels given to createImage().
 * <p>
 * I'm not even going to comment on how ludicrus the decision
 * to dither supplied pixels was, especially with no alternative.
 * <p>
 * @author Guy Haas
 */

public class TGFillOp implements ImageObserver, TGGraphicsOp
{

   // constants
   //
   private static final int EXPAND_GRAB_SIZE = 200;
   private static final int INITIAL_GRAB_SIZE = 600;
   private static final String CLASS_NAME = "org.bfoit.tg.TGFillOp";


   // variables with class-wide scope
   //
   private Color fillColor;    // new Color pixels will be set to
   private TGPoint fillPoint;  // initial point of the FILL operation

   // Image that's the source of pixels to be examined and
   // conditionally changed to fillColor.
   private Image sourceImage;
   private int sourceHeight;
   private int sourceWidth;

   // true to denote Graphics.drawImage() has been performed. Needed for ImageObserver
   // interface - remnant of design reflecting Images obtained from the net, not local
   // memory like this situation.
   private boolean waitingForImage;

   // maximum and minimum X and Y values computed so that we can
   // return the rectangle that encompasses the modified pixels
   private int maxFloodX;
   private int maxFloodY;
   private int minFloodX;
   private int minFloodY;

   // destination of grabbed pixels - holds the RGB pixel
   // representation of sourceImage
   private int[] pixels;

   // rectangular bounds of pixels we've grabbed from sourceImage
   private int subPixHt;
   private int subPixLeftX;
   private int subPixTopY;
   private int subPixWd;



   //
   // Constructor
   //

   /**
    * Instantiation of a flood fill graphics operation.
    * <p>
    * @param color New Color to paint pixels with.
    * @param point Location in TurtleSpace of first pixel, start
    *              of flood fill process.
    */
   public TGFillOp( TGPoint point, Color color )
   {
      fillColor = color;
      fillPoint = point;
   }


   //
   // Implementation of org.bfoit.tg.TGGraphicsOp Interface
   //

   /**
    * DoIt - a Flood Fill Operation.  Get Color under the fillPoint and
    * fill this point and all its same-colored neighbors, and their
    * neighbors, etc... with this operation's fillColor.
    */
   // *NOTE* The point where the fill operation is to start may not be
   //        within current bounds of the graphics Image.  In this case,
   //        the operation can not be performed.
   //        
   public Rectangle doIt( Image graphicsImage )
   {
      sourceImage = graphicsImage;
      sourceWidth = graphicsImage.getWidth( this );
      if ( sourceWidth == -1 )
         return null;
      sourceHeight = graphicsImage.getHeight( this );
      if ( sourceHeight == -1 )
         return null;
      // convert org.bfoit.tg.TGPoint origin of the FILL operation to the x,y
      // coordinates within the provided Image
      int imageX = fillPoint.imageX( sourceWidth );
      if ( imageX < 0 || imageX >= sourceWidth )
         return null;
      int imageY = fillPoint.imageY( sourceHeight );
      if ( imageY < 0 || imageY >= sourceHeight )
         return null;
      pixels = new int[sourceWidth * sourceHeight];
      // compute bounds for initial grab of pixels around fillPoint
      subPixLeftX = imageX - INITIAL_GRAB_SIZE/2;
      if ( subPixLeftX < 0 )
         subPixLeftX = 0;
      subPixTopY = imageY - INITIAL_GRAB_SIZE/2;
      if ( subPixTopY < 0 )
         subPixTopY = 0;
      subPixHt = INITIAL_GRAB_SIZE;
      if ( subPixHt > sourceHeight )
         subPixHt = sourceHeight;
      subPixWd = INITIAL_GRAB_SIZE;
      if ( subPixWd > sourceWidth )
         subPixWd = sourceWidth;
      // grab initial bunch of pixels around fillPoint
      if ( ! getPixels(subPixLeftX, subPixTopY, subPixWd, subPixHt) )
         return null;
      int curRGB = pixels[ imageY * sourceWidth + imageX ];
      curRGB &= 0xFFFFFF;
      int newRGB = fillColor.getRGB();
      newRGB &= 0xFFFFFF;
      if ( curRGB == newRGB )
         return null;
      maxFloodX = minFloodX = imageX;
      maxFloodY = minFloodY = imageY;
      // perform the flood fill operation
      floodFill( imageX, imageY, sourceWidth, sourceHeight, curRGB, newRGB );
      // create an ImageProducer, based on the new pixel data
      int pixelOffset = (sourceWidth * minFloodY) + minFloodX;
      int floodWidth = (maxFloodX+1) - minFloodX;
      int floodHeight = (maxFloodY+1) - minFloodY;
      MemoryImageSource mis = new MemoryImageSource( floodWidth, floodHeight,
                                                     pixels, pixelOffset, sourceWidth
                                                   );
      Image newImage = Toolkit.getDefaultToolkit().createImage( mis );
      drawImage( newImage, minFloodX, minFloodY );
      return new Rectangle( minFloodX, minFloodY, floodWidth, floodHeight );

   } // end doIt()


   //
   // Implementation of ImageObserver Interface
   //


   /**
    * imageUpdate is invoked when information about an image which was
    * previously requested using an asynchronous interface becomes available.
    * <p>
    * This method should return true if further updates are needed or false
    * if the required information has been acquired. The image which was being
    * tracked is passed in using the img argument. Various constants are
    * combined to form the infoflags argument which indicates what information
    * about the image is now available. The interpretation of the x, y, width,
    * and height arguments depends on the contents of the infoflags argument. 
    */
   public synchronized boolean imageUpdate(Image img, int flags, int x, int y, int wd, int ht)
   {
      //System.out.print("org.bfoit.tg.TGFillOp.imageUpdate: flags=" );
      //if ( (flags & ImageObserver.ABORT) != 0 )
      //   System.out.print(" ABORT" );
      //if ( (flags & ImageObserver.ALLBITS) != 0 )
      //   System.out.print(" ALLBITS" );
      //if ( (flags & ImageObserver.ERROR) != 0 )
      //   System.out.print(" ERROR" );
      //if ( (flags & ImageObserver.FRAMEBITS) != 0 )
      //   System.out.print(" FRAMEBITS" );
      //if ( (flags & ImageObserver.HEIGHT) != 0 )
      //   System.out.print(" HEIGHT" );
      //if ( (flags & ImageObserver.PROPERTIES) != 0 )
      //   System.out.print(" PROPERTIES" );
      //if ( (flags & ImageObserver.SOMEBITS) != 0 )
      //   System.out.print(" SOMEBITS" );
      //if ( (flags & ImageObserver.WIDTH) != 0 )
      //   System.out.print(" WIDTH" );
      //System.out.print("\n                      x=" + x + ", y=" + y );
      //System.out.println( ", width=" + wd + ", height=" + ht );
      if ( ! waitingForImage )
         return false;
      if ( (flags & (ImageObserver.ABORT | ImageObserver.ALLBITS | ImageObserver.ERROR)) != 0 )
      {
         waitingForImage = false;
         notifyAll();
         return false;
      }
      return true;


   } // end imageUpdate()


   //
   // Methods with scope limited to the class
   //

   private void addLeftPixels()
   {
      int addedPixWidth = EXPAND_GRAB_SIZE;
      if ( subPixLeftX < EXPAND_GRAB_SIZE )
         addedPixWidth = subPixLeftX;
      subPixLeftX -= addedPixWidth;
      getPixels( subPixLeftX, subPixTopY, addedPixWidth, subPixHt );
      subPixWd += addedPixWidth;

   } // end addLeftPixels()


   private void addLowerPixels()
   {
      int topY = subPixTopY + subPixHt;
      int addedPixHeight = EXPAND_GRAB_SIZE;
      if ( topY+EXPAND_GRAB_SIZE > sourceHeight )
         addedPixHeight = sourceHeight - topY;
      getPixels( subPixLeftX, topY, subPixWd, addedPixHeight );
      subPixHt += addedPixHeight;

   } // end addLowerPixels()


   private void addRightPixels()
   {
      int leftX = subPixLeftX + subPixWd;
      int addedPixWidth = EXPAND_GRAB_SIZE;
      if ( leftX+EXPAND_GRAB_SIZE > sourceWidth )
         addedPixWidth = sourceWidth - leftX;
      getPixels( leftX, subPixTopY, addedPixWidth, subPixHt );
      subPixWd += addedPixWidth;

   } // end addRightPixels()


   private void addUpperPixels()
   {
      int addedPixHeight = EXPAND_GRAB_SIZE;
      if ( subPixTopY < EXPAND_GRAB_SIZE )
         addedPixHeight = subPixTopY;
      subPixTopY -= addedPixHeight;
      getPixels( subPixLeftX, subPixTopY, subPixWd, addedPixHeight );
      subPixHt += addedPixHeight;

   } // end addUpperPixels()


   /*
    * Paint the new Image we've constructed onto the source Image.
    */
   private synchronized void drawImage( Image newImg, int x, int y )
   {
      Graphics g = sourceImage.getGraphics();
      waitingForImage = true;
      boolean retVal = g.drawImage( newImg, x, y, this );
      if ( retVal )
         while ( waitingForImage )
         {
            try  { wait(); }
            catch (InterruptedException ie ) { }
         }
      waitingForImage = false;
      pixels = null;
      g.dispose();

   } // end drawImage()


   //
   // *NOTE* There is a simple, elegant algorithm for doing this. But it's
   //        recursive and exceeds the default stack size Java gives us...
   //        So... I've implemented a hybrid algorithm that's partially
   //        recursive, partially iterative.

   private void floodFill(int x, int y, int wd, int ht, int curRGB, int newRGB)
   {
      int pixIdx;

      int leftLimit = x, rightLimit = x;
      if ( y < minFloodY )
         minFloodY = y;
      if ( y > maxFloodY )
         maxFloodY = y;
      for ( int i = x; i >= 0; i-- )
      {
         if ( i < subPixLeftX )
            addLeftPixels();
         pixIdx = y * wd + i;
         if ( (pixels[pixIdx] & 0xFFFFFF) != curRGB )
            break;
         pixels[pixIdx] = (pixels[pixIdx] & 0xFF000000) | newRGB;
         leftLimit = i;
      }
      if ( leftLimit < minFloodX )
         minFloodX = leftLimit;
      for ( int i = x+1; i < wd; i++ )
      {
         if ( i == (subPixLeftX + subPixWd) )
            addRightPixels();
         pixIdx = y * wd + i;
         if ( (pixels[pixIdx] & 0xFFFFFF) != curRGB )
            break;
         pixels[pixIdx] = (pixels[pixIdx] & 0xFF000000) | newRGB;
         rightLimit = i;
      }
      if ( rightLimit > maxFloodX )
         maxFloodX = rightLimit;
      if ( y > 0 )
      {
         for ( int i = leftLimit; i <= rightLimit; i++ )
         {
            int newY = y - 1;
            if ( newY < subPixTopY )
               addUpperPixels();
            pixIdx = newY * wd + i;
            if ( (pixels[pixIdx] & 0xFFFFFF) == curRGB )
               floodFill( i, newY, wd, ht, curRGB, newRGB );
         }
      }
      if ( y < ht-1 )
      {
         for ( int i = leftLimit; i <= rightLimit; i++ )
         {
            int newY = y + 1;
            if ( newY == (subPixTopY + subPixHt) )
               addLowerPixels();
            pixIdx = newY * wd + i;
            if ( (pixels[pixIdx] & 0xFFFFFF) == curRGB )
               floodFill( i, newY, wd, ht, curRGB, newRGB );
         }
      }

   } // end floodFill


   /*
    * Grab the rectangular area of pixels, specified by the parameters,
    * from the sourceImage and store them into the pixels int array.
    */
   private boolean getPixels( int x, int y, int width, int height )
   {
      int pixelOffset = (sourceWidth * y) + x;
      PixelGrabber pg = new PixelGrabber( sourceImage,     // image to retrieve pixels from
                                          x, y,            // top-left corner of rectangle
                                          width, height,   // width, height of rectangle
                                          pixels,          // destination array of RGB ints
                                          pixelOffset,     // destination offset into array
                                          sourceWidth      // row width of destination array
                                        );
      try { pg.grabPixels(); }
      catch (InterruptedException e)
      {
         sysErr( ".getPixels(): PixelGrabber: grabPixels interrupted" );
         return false;
      }
      if ( (pg.getStatus() & ImageObserver.ABORT) != 0 )
      {
         sysErr( ".getPixels(): PixelGrabber: ImageObserver.ABORT" );
         return false;
      }
      return true;

   } // end getPixels()


   /*
    * DEBUG support...
    * Print pixels in TG-coordinate-based rectangle, i.e., x and y origins
    * are at center of the graphicsImage. X parameter is leftmostEdge of the
    * rectangle, Y is the topRow.
    */
   //private void printPixels( int x, int y, int wd, int ht )
   //{
   //   int xCenter = sourceWidth / 2;
   //   int column = xCenter + x;
   //   int lastColumn = column + (wd-1);
   //   int yCenter = sourceHeight / 2;
   //   int line = yCenter - y;
   //   int lastLine = line + (ht-1);
   //   System.out.print( "printPixels(): sourceWidth=" + sourceWidth + ", XCenter=" + xCenter );
   //   System.out.println( ", columns " + column + " .. " + lastColumn );
   //   System.out.print( "               sourceHeight=" + sourceHeight + ", YCenter=" + yCenter );
   //   System.out.println( ", lines " + line + " .. " + lastLine );
   //   while ( line <= lastLine )
   //   {
   //      if ( x >= 0 )
   //         System.out.print( " " );
   //      if ( x < 100 )
   //         System.out.print( " " );
   //      if ( x < 10 )
   //         System.out.print( " " );
   //      System.out.print( x + "," );
   //      if ( y >= 0 )
   //         System.out.print( " " );
   //      if ( y < 100 )
   //         System.out.print( " " );
   //      if ( y < 10 )
   //         System.out.print( " " );
   //      System.out.print( y + ": " );
   //      for ( int idx=column; idx <= lastColumn; idx++ )
   //      {
   //         System.out.print( " " );
   //         int pixel = pixels[line*sourceWidth + idx];
   //         int red = (pixel & 0xFF0000) >>> 16;
   //         if ( red < 100 )
   //            System.out.print( " " );
   //         if ( red < 10 )
   //            System.out.print( " " );
   //         System.out.print( red + " " );
   //         int green = (pixel & 0xFF00) >>> 8;
   //         if ( green < 100 )
   //            System.out.print( " " );
   //         if ( green < 10 )
   //            System.out.print( " " );
   //         System.out.print( green + " " );
   //         int blue = pixel & 0xFF;
   //         if ( blue < 100 )
   //            System.out.print( " " );
   //         if ( blue < 10 )
   //            System.out.print( " " );
   //         System.out.print( blue );
   //         if ( idx < lastColumn )
   //            System.out.print( " | " );
   //      }
   //      System.out.println( "" );
   //      line++;
   //      y--;
   //   }
   //  
   //} // end printPixels()


   /*
    * Print an error message to console, prefixing it with this class' name.
    */
   private void sysErr( String errTxt )
   { System.err.println( CLASS_NAME + errTxt ); }


} // end class org.bfoit.tg.TGFillOp
