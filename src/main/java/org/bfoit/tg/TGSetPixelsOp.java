package org.bfoit.tg;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * org.bfoit.tg.TGSetPixelsOp provides TurtleGraphics with a SETPIXELS graphics operation.
 * <p>
 * org.bfoit.tg.TGSetPixelsOp should be thought of as a BitBlt, a block transfer of pixels
 * from a source, an array of RGB pixel values, to the Image being displayed.
 * <p>
 * Invoking Turtle methods which paint pixels on the graphics canvas does not
 * result in immediate changes to TG's in-memory Image, only that they will be
 * performed soon.
 * <p>
 * @author Guy Haas
 */

public class TGSetPixelsOp implements ImageObserver, TGGraphicsOp
{
   //
   // Constants
   //

   private static final String CLASS_NAME = "org.bfoit.tg.TGSetPixelsOp";

   //
   // Variables with access limited to this class
   //

   // PixRect to be painted onto provided Image
   private TGPoint prTopLeft;   // top-left corner of the pixel rectangle in TurtleSpace
   private int prWidth;         // dimension for the rectangle (height is computed)
   private int[] sourcePixels;  // array of pixels to be merged onto graphics canvas

   // Image that's the destination for new pixels.  Its current pixel values are
   // grabbed and merged with sourcePixels.
   private Image destinationImage;
   private int destImgHeight;
   private int destImgWidth;

   // grabbed pixels - holds the existing RGB pixel representation of the rectangle
   // which will be modified in destinationImage, modified by merging with sourcePixels,
   // and then repainted out.
   private int[] existingPixels;
   private int epHeight;
   private int epWidth;

   // true to denote Graphics.drawImage() has been performed. Needed for ImageObserver
   // interface - remnant of design reflecting Images obtained from the net, not local
   // memory like this situation.
   private boolean waitingForImage;


   //
   // Constructor
   //

   /**
    * Instantiate a graphics operation to set a rectangular area of the
    * graphics canvas to provided pixel values.
    * @param topLeft Left-most x, top-most y, coordinates of rectangle of
    *                pixels to be replaced, destination.
    * @param width Number of pixels constituting one row.
    * @param pixels Array of the source pixels to be stored.
    */
   public TGSetPixelsOp( TGPoint topLeft, int width, int[] pixels )
   {
      prTopLeft = topLeft;
      prWidth = width;
      sourcePixels = pixels;
   }


   /**
    * Instantiate a graphics operation to set a rectangular area of the
    * graphics canvas to provided pixel values.
    * @param topLeft Left-most x, top-most y, coordinates of rectangle of
    *                pixels to be replaced - destination.
    * @param pixRect org.bfoit.tg.PixelRectangle which includes an array of the pixels
    *                to be stored and the width (number of columns) in
    *                the rectangle.
    */
   public TGSetPixelsOp( TGPoint topLeft, PixelRectangle pixRect )
   {
      prTopLeft = topLeft;
      prWidth = pixRect.width;
      sourcePixels = pixRect.pixels;
   }


   //
   // org.bfoit.tg.TGGraphicsOp Interface Methods
   //

   /**
    * Perform a read/modify/write cycle on a rectangular area of pixels
    * in the provided Image.
    * <p>
    * The current values of a rectangular area of pixels is extracted from
    * the provided Image.  Values of sourcePixels[] with opacity are stored
    * into the extracted group and the results are stored back into the
    * provided destinationImage.
    *
    * @param inMemoryImage Where my sourcePixels[] should be stored into.
    */
   public Rectangle doIt( Image inMemoryImage )
   {
      destinationImage = inMemoryImage;
      destImgWidth = inMemoryImage.getWidth( this );
      if ( destImgWidth == -1 )
         return null;
      destImgHeight = inMemoryImage.getHeight( this );
      if ( destImgHeight == -1 )
         return null;
      // convert org.bfoit.tg.TGPoint prTopLeft from TurtleSpace to x,y coordinates
      // within the provided inMemoryImage
      int imageX = prTopLeft.imageX( destImgWidth );
      if ( imageX < 0 || imageX >= destImgWidth )
         return null;
      int imageY = prTopLeft.imageY( destImgHeight );
      if ( imageY < 0 || imageY >= destImgHeight )
         return null;
      // compute bounds for grab of pixels
      epWidth = prWidth;
      if ( (imageX + prWidth) > destImgWidth )
         epWidth = destImgWidth - imageX;
      epHeight = sourcePixels.length / prWidth;
      if ( (imageY + epHeight)  > destImgHeight )
         epHeight = destImgHeight - imageY;
      existingPixels = new int[epWidth * epHeight];
      // grab values of pixels in specified rectangle
      if ( ! getPixels(imageX, imageY, epWidth, epHeight) )
         return null;
      mergePixels();
      // create an ImageProducer, based on the new pixel data, which
      // createImage() uses to construct a new Image to be painted
      MemoryImageSource mis
         = new MemoryImageSource( epWidth, epHeight, existingPixels, 0, epWidth );
      Image newImage = Toolkit.getDefaultToolkit().createImage( mis );
      // paint new Image onto the destinationImage (aka inMemoryImage)
      drawImage( newImage, imageX, imageY );
      return new Rectangle( imageX, imageY, epWidth, epHeight );

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

   /*
    * Paint the new Image we've constructed onto the source Image.
    */
   private synchronized void drawImage( Image newImg, int x, int y )
   {
      Graphics g = destinationImage.getGraphics();
      waitingForImage = true;
      boolean retVal = g.drawImage( newImg, x, y, this );
      if ( retVal )
         while ( waitingForImage )
         {
            try  { wait(); }
            catch (InterruptedException ie ) { }
         }
      waitingForImage = false;
      g.dispose();

   } // end drawImage()


   /*
    * Grab the rectangular area of pixels, specified by the parameters, from
    * the destinationImage and store them into the existingPixels int array.
    */
   private boolean getPixels( int x, int y, int width, int height )
   {
      PixelGrabber pg = new PixelGrabber( destinationImage,  // image to retrieve pixels from
                                          x, y,              // top-left corner of rectangle
					  width, height,     // width, height of rectangle
                                          existingPixels,    // destination array of RGB ints
					  0,                 // destination offset into array
					  epWidth            // row width of destination array
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
    * Iterate through pixel arrays, replacing pixels in existingPixels[] with
    * those in sourcePixels[] which have some opacity.
    */
   private void mergePixels()
   {
      int numPixels = sourcePixels.length;
      if ( numPixels != existingPixels.length )
      {
         sysErr(".mergePixels(): srcLen ("+numPixels+") != existLen ("+existingPixels.length+")");
         if ( existingPixels.length < numPixels )
            numPixels = existingPixels.length;
      }
      for ( int idx=0; idx < numPixels; idx++ )
         existingPixels[idx] = sourcePixels[idx];

   } // end mergePixels()


   /*
    * DEBUG support...
    * print pixels from an array
    * x,y is top left pixel (0,0 is first pixel in array)
    * wd,ht are the dimensions of the rectangle of pixels to be printed
    * pixels is the array
    * pixelsWd is the number of pixels in the array that make up one row
    */
   //private void printPixels( int x, int y, int wd, int ht, int[] pixels, int pixelsWd )
   //{
   //   int column = x;
   //   int lastColumn = column + (wd-1);
   //   if ( lastColumn >= pixelsWd )
   //      lastColumn = pixelsWd-1;
   //   int pixelsHt = pixels.length / pixelsWd;
   //   int line = y;
   //   int lastLine = line + (ht-1);
   //   if ( lastLine >= pixelsHt )
   //      lastLine = pixelsHt-1;
   //   System.out.print( "printPixels(): x=" + x + ", y=" + y + ", wd=" + wd );
   //   System.out.println( ", ht " + ht + ", pixelsWd=" + pixelsWd );
   //   while ( line <= lastLine )
   //   {
   //      for ( int idx=column; idx <= lastColumn; idx++ )
   //      {
   //         int pixel = pixels[line*pixelsWd + idx];
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
   //   }
   //
   //} // end printPixels()


   /*
    * Print an error message to console, prefixing it with this class' name.
    */
   private void sysErr( String errTxt )
   { System.err.println( CLASS_NAME + errTxt ); }


} // end class org.bfoit.tg.TGSetPixelsOp
