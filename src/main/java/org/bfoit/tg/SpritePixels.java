package org.bfoit.tg;

import java.awt.Color;

/**
 * org.bfoit.tg.SpritePixels is an abstract class that another class extends when
 * it wants to provide the pixels that make up the image of a org.bfoit.tg.Sprite.
 * <P>
 * @author Guy Haas
 */

public class SpritePixels
{

   //
   // Symbolic Constants
   // -------- ---------

   /**
    * Class name as a String.
    */
   public static final String CLASS_NAME = "org.bfoit.tg.SpritePixels";

   /**
    * Default maximum sprite image height.
    */
   public static final int MAX_SPRITE_HEIGHT = 600;

   /**
    * Default maximum sprite image width.
    */
   public static final int MAX_SPRITE_WIDTH =  400;

   /**
    * Default minimum sprite image height.
    */
   public static final int MIN_SPRITE_HEIGHT =   6;

   /**
    * Default minimum sprite image width.
    */
   public static final int MIN_SPRITE_WIDTH =    6;


   /**
    * Value of a pixel which is solid black.
    */
   public static final int BLACK_OPAQUE_PIXEL = 0xff000000;


   /*
    * Value of a pixel which is solid white.
    */
   private static final int WHITE_OPAQUE_PIXEL = 0xffffffff;


   /*
    * Mask for isolating the bits specifying a pixel's opacity.
    */
   private static final int PIXEL_OPACITY_BITS = 0xff000000;


   /*
    * Mask for isolating the RGB color bits of a pixel.
    */
   private static final int PIXEL_COLOR_BITS = 0x00ffffff;


   /*
    * Value of RGB bits of a white pixel.
    */
   private static final int WHITE_RGB_BITS = WHITE_OPAQUE_PIXEL & PIXEL_COLOR_BITS;


   /*
    * Number of radians in a quarter of a circle (90 degrees).
    * This is the north heading in radians.
    */
   private static final double QTR_CIRCLE_RADIANS = Math.PI / 2.0;


   //
   // Private Fields
   // ------- ------

   /*
    * Used to determine which pixels in baseSpritePixels[] change when
    * the pen color changes.
    */
   private boolean[] colorFillMask;

   /*
    * Inner pixels Color or null if this org.bfoit.tg.Sprite is incapable of changing.
    */
   private Color spriteColor;

   /*
    * Radians in conventional/AWT manner.
    */
   private double spriteHeading;

   /*
    * this needs to be computed such that it is larger than either the
    * turtle's height or the turtle's width so the corners of the image
    * of the turtle are not clipped when drawn at 45 degree angles
    */
   private int pixRectSideSize;

   /*
    * the height of the turtle as given in the constructor
    */
   private int spriteHeight;

   /*
    * the width of the turtle as given in the constructor
    */
   private int spriteWidth;

   /*
    * Pixel array for the sprite's image in an EASTern orientation,
    * i.e., headed along the positive X axis
    */
   private int[] baseSpritePixels;

   /*
    * pixel array for a org.bfoit.tg.Sprite's current Image - if it's visible. It
    * is oriented in the current heading and its body is filled with
    * the current color
    */
   private int[] spritePixels;



   //
   // Constructors
   // ------------

   /**
    * Instantiate the pixels that make up a org.bfoit.tg.Sprite's Image given an
    * array of pixels.  The assumed orientation of the org.bfoit.tg.PixelRectangle
    * is upright, facing the way you want for the turtle heading of
    * 0, or north.
    */
   public SpritePixels( PixelRectangle pixRect )
   {
      int height = pixRect.pixels.length / pixRect.width;
      if ( height > MAX_SPRITE_HEIGHT )
         height = MAX_SPRITE_HEIGHT;
      int width = pixRect.width;
      if ( width > MAX_SPRITE_WIDTH )
         width = MAX_SPRITE_WIDTH;
      spriteHeight = height;
      spriteWidth = width;
      pixRectSideSize = (int) Math.ceil( Math.sqrt((height*height) + (width*width)) );
      if ( pixRectSideSize % 2 != 0 )
         pixRectSideSize++;
      baseSpritePixels = new int[pixRectSideSize * pixRectSideSize];
      for ( int idx=0; idx < baseSpritePixels.length; idx++ )
         baseSpritePixels[idx] = WHITE_OPAQUE_PIXEL;
      // copy, center, and rotate (90 degrees) the pixel rectangle
      int leftInset = (pixRectSideSize - spriteHeight) / 2;
      int topInset = (pixRectSideSize - spriteWidth) / 2;
      int iniDestIdx = (topInset * pixRectSideSize) + leftInset + height;
      int destIdx = 0;
      int rowNum = 0;
      for ( int srcIdx=0; srcIdx < pixRect.pixels.length; srcIdx++ )
      {
         if ( srcIdx % width == 0 )
            destIdx = iniDestIdx - ++rowNum;
         baseSpritePixels[destIdx] = pixRect.pixels[srcIdx];
         destIdx += pixRectSideSize;
      }
      setTransparentPixels();
      spriteHeading = QTR_CIRCLE_RADIANS;
      updateSpritePixels( spriteHeading );
   }


   /**
    * Instantiate a org.bfoit.tg.Sprite's Image given its dimensions, color, and
    * an implementation of the initSpritePixels() method. It is assumed
    * that the produced pixels are for the heading 0.0, so a desired
    * heading is also provided as a parameter.
    */
   public SpritePixels( int width, int height, Color color, double heading )
   {
      if ( height > MAX_SPRITE_HEIGHT )
         height = MAX_SPRITE_HEIGHT;
      if ( height < getMinimumHeight() )
         height = getMinimumHeight();
      if ( width > MAX_SPRITE_WIDTH )
         width = MAX_SPRITE_WIDTH;
      if ( width < getMinimumWidth() )
         width = getMinimumWidth();
      spriteHeight = height;
      spriteWidth = width;
      pixRectSideSize = (int) Math.ceil( Math.sqrt((height*height) + (width*width)) );
      if ( pixRectSideSize % 2 != 0 )
         pixRectSideSize++;
      baseSpritePixels = new int[pixRectSideSize * pixRectSideSize];
      for (int pixIdx=0; pixIdx < baseSpritePixels.length; pixIdx++)
         baseSpritePixels[pixIdx] = WHITE_OPAQUE_PIXEL;
      initSpritePixels( pixRectSideSize );
      setTransparentPixels();
      buildSpriteFillMask();
      spriteColor = color;
      int pixel = spriteColor.getRGB();
      pixel |= PIXEL_OPACITY_BITS;
      for (int pixIdx=0; pixIdx < baseSpritePixels.length; pixIdx++)
         if ( colorFillMask[pixIdx] == true )
            baseSpritePixels[pixIdx] = pixel;
      spriteHeading = heading;
      updateSpritePixels( heading );
   }



   //
   // Class Support Methods
   // ----- ------- -------


   /* Build a mask of pixels that are to be changed when
    * the foreground color is changed.  Starting with
    * the center pixel of the turtle image, a flood-fill
    * algorithm is used to identify all pixels with the
    * same color value.  These pixels are set to true in
    * a mask array. colorSprite() uses this mask to change
    * the color of the turtle.
    *
    * Although baseSpritePixels is manipulated as an array
    * with a single index, it is really a two dimensional
    * array, a series of rows of pixels.  Each row is
    * pixRectSideSize pixels in width.  This explains why
    * pixelIndex = rowNumber * pixRectSideSize + columnNumber
    * is used to calculate a pixel's index.
    */
   private void buildSpriteFillMask()
   {
      float center = ((float)pixRectSideSize ) / 2.0F;
      int row = Math.round(center) - 1;
      int column = row;
      int color = baseSpritePixels[ row * pixRectSideSize + column ];
      colorFillMask = new boolean[pixRectSideSize * pixRectSideSize];
      fillColor( row, column, color );
   }


   /* circles have an eight-way symmetry to them
    * only the points on a 45 degree segment (one eighth) of a circle
    * need to be computed - the rest are reflections of this point
    * given a center-point and deltas to a point on the circle, fill
    * eight points on the circle with a supplied value
    */ 
   private void circlePixels( int ctrX, int ctrY, int dX, int dY, int value )
   {
      setPixel(  ctrX + dX,  ctrY + dY, value );
      setPixel(  ctrX + dY,  ctrY + dX, value );
      setPixel(  ctrX + dY,  ctrY - dX, value );
      setPixel(  ctrX + dX,  ctrY - dY, value );
      setPixel(  ctrX - dX,  ctrY - dY, value );
      setPixel(  ctrX - dY,  ctrY - dX, value );
      setPixel(  ctrX - dY,  ctrY + dX, value );
      setPixel(  ctrX - dX,  ctrY + dY, value );
   }


   /*
    * Fill the internal pixels of baseSpritePixels[] with a specified
    * color. The determining factor whether or not a pixel is effected
    * is made by examining corresponding pixels in the colorFillMask[].
    *
    * A pure recursive implementation blows Java's stack...
    */
   private void fillColor( int row, int column, int color )
   {
      int pixIdx;
      int leftLimit = column, rightLimit = column;
      for ( int i = column; i >= 0; i-- )
      {
         pixIdx = row * pixRectSideSize + i;
         if ( baseSpritePixels[pixIdx] != color || colorFillMask[pixIdx] )
            break;
         colorFillMask[pixIdx] = true;
         leftLimit = i;
      }
      for ( int i = column+1; i < pixRectSideSize; i++ )
      {
         pixIdx = row * pixRectSideSize + i;
         if ( baseSpritePixels[pixIdx] != color || colorFillMask[pixIdx] )
            break;
         colorFillMask[pixIdx] = true;
         rightLimit = i;
      }
      if ( row > 0 )
         for ( int i = leftLimit; i <= rightLimit; i++ )
         {
            int newRow = row - 1;
            pixIdx = newRow * pixRectSideSize + i;
            if ( baseSpritePixels[pixIdx] == color && !colorFillMask[pixIdx] )
               fillColor( newRow, i, color );
         }
      if ( row < pixRectSideSize-1 )
         for ( int i = leftLimit; i <= rightLimit; i++ )
         {
            int newRow = row + 1;
            pixIdx = newRow * pixRectSideSize + i;
            if ( baseSpritePixels[pixIdx] == color && !colorFillMask[pixIdx] )
               fillColor( newRow, i, color );
         }
   }


   // NOTE: x1 MUST BE <= x2
   //
   private void fillHorizLine( int x1, int x2, int y, int value )
   {
      for (int x=x1; x <= x2; x++)
         setPixel( x, y, value );
   }


   // NOTE: y1 MUST BE <= y2
   //
   private void fillVertLine( int x, int y1, int y2, int value )
   {
      for (int y=y1; y <= y2; y++)
         setPixel( x, y, value );
   }


   /* Fill in pixels that make up a line where X is increasing in
    * integer units while Y values are changing by a specified delta.
    * NOTE: x0 MUST BE <= x1
    */
   private void fillXUnitLine(int x0, int y0, int x1, int y1, int value)
   {
      //System.out.print("org.bfoit.tg.SpritePixels.fillXUnitLine: x0="+x0+", y0="+y0);
      //System.out.println(", x1="+x1+", y1="+y1);
      int dX = x1 - x0;
      boolean negSlope = false;
      int dY = y1 - y0;
      int y = y0;
      if  ( dY < 0 )
      {
         negSlope = true;
         dY = -dY;
      }
      int d = 2 * dY - dX;
      int incrE = 2 * dY;
      int incrNE = 2 * (dY - dX);
      setPixel( x0, y0, value );
      while ( x0 < x1 )
      {
         x0++;
         if ( d <= 0 )
            d += incrE;
         else
         {
            d += incrNE;
            y++;
         }
         if ( negSlope )
            setPixel( x0, y0-(y-y0), value );
         else
            setPixel( x0, y, value );
      }

   } // end fillXUnitLine()


   /* Fill in pixels that make up a line where Y is increasing in
    * integer units while X values are changing by a specified delta.
    * NOTE: y1 MUST BE <= y2
    */
   private void fillYUnitLine(int x0, int y0, int  x1, int  y1, int value)
   {
      //System.out.print("org.bfoit.tg.SpritePixels.fillYUnitLine: x0="+x0+", y0="+y0);
      //System.out.println(", x1="+x1+", y1="+y1);
      boolean negSlope = false;
      int dX = x1 - x0;
      int x = x0;
      if  ( dX < 0 )
      {
         negSlope = true;
         dX = -dX;
      }
      int dY = y1 - y0;
      int d = 2 * dX - dY;
      int incrE = 2 * dX;
      int incrNE = 2 * (dX - dY);
      setPixel( x0, y0, value );
      while ( y0 < y1 )
      {
         y0++;
         if ( d <= 0 )
            d += incrE;
         else
         {
            d += incrNE;
            x++;
         }
         if ( negSlope )
            setPixel( x0-(x-x0), y0, value );
         else
            setPixel( x, y0, value );
      }
   }

   
   /*
    * Return the number (index) of the first column in spritePixels[]
    * that is not transparent.
    */
   private int findFirstNonTransparentColumn()
   {
      if ( spritePixels == null )
         return 0;
      for ( int colIdx = 0; colIdx < pixRectSideSize; colIdx++ )
         for ( int rowIdx = 0; rowIdx < pixRectSideSize; rowIdx++ )
         {
            int pixelOpacity = spritePixels[(rowIdx*pixRectSideSize)+colIdx] & PIXEL_OPACITY_BITS;
            if ( pixelOpacity != 0 )
               return colIdx;
         }
      return pixRectSideSize;
   }

   
   /*
    * Return the number (index) of the first line in spritePixels[]
    * that is not transparent.
    */
   private int findFirstNonTransparentLine()
   {
      if ( spritePixels == null )
         return 0;
      int pixelIndex = 0;
      while ( pixelIndex < pixRectSideSize * pixRectSideSize )
      {
         int pixelOpacity = spritePixels[pixelIndex] & PIXEL_OPACITY_BITS;
         if ( pixelOpacity != 0 )
            return pixelIndex / pixRectSideSize;
         pixelIndex++;
      }
      return pixRectSideSize-1;
   }

   
   /*
    * Return the number (index) of the last column in spritePixels[]
    * that is not transparent.
    */
   private int findLastNonTransparentColumn()
   {
      if ( spritePixels == null )
         return 0;
      for ( int colIdx = pixRectSideSize-1; colIdx >= 0; colIdx-- )
         for ( int rowIdx = 0; rowIdx < pixRectSideSize; rowIdx++ )
         {
            int pixelOpacity = spritePixels[(rowIdx*pixRectSideSize)+colIdx] & PIXEL_OPACITY_BITS;
            if ( pixelOpacity != 0 )
               return colIdx;
         }
      return pixRectSideSize-1;
   }
   

   /*
    * Return the number (index) of the last line in spritePixels[]
    * that is not transparent.
    */
   private int findLastNonTransparentLine()
   {
      if ( spritePixels == null )
         return 0;
      int pixelIndex = (pixRectSideSize * pixRectSideSize) - 1;
      while ( pixelIndex >= 0 )
      {
         int pixelOpacity = spritePixels[pixelIndex] & PIXEL_OPACITY_BITS;
         if ( pixelOpacity != 0 )
            return pixelIndex / pixRectSideSize;
         pixelIndex--;
      }
      return pixRectSideSize-1;
   }


   /*
    * Helper procedure for setTransparentPixels. It walks the perimeter
    * of baseSpritePixels[] and upon finding an opaque white pixel, invokes
    * setTPHelper() to flood-fill this pixel and other opaque white neighbors.
    */
   private void setTPHelper( int row, int column )
   {
      int pixIdx = (row * pixRectSideSize) + column;
      baseSpritePixels[pixIdx] = 0;
      int rightLimit = column;
      for ( int idx = column+1; idx < pixRectSideSize; idx++ )
      {
         pixIdx = (row * pixRectSideSize) + idx;
         if ( (baseSpritePixels[pixIdx] & PIXEL_COLOR_BITS) != WHITE_RGB_BITS )
            break;
         baseSpritePixels[pixIdx] = 0;
         rightLimit = idx;
      }
      int leftLimit = column;
      for ( int idx = column-1; idx >= 0; idx-- )
      {
         pixIdx = (row * pixRectSideSize) + idx;
         if ( (baseSpritePixels[pixIdx] & PIXEL_COLOR_BITS) != WHITE_RGB_BITS )
            break;
         baseSpritePixels[pixIdx] = 0;
         leftLimit = idx;
      }
      if ( row > 0 )
         for ( int idx = leftLimit; idx <= rightLimit; idx++ )
         {
            int newRow = row - 1;
            pixIdx = (newRow * pixRectSideSize) + idx;
            if ( (baseSpritePixels[pixIdx] & PIXEL_COLOR_BITS) == WHITE_RGB_BITS )
               setTPHelper( newRow, idx );
         }
      if ( row < pixRectSideSize-1 )
         for ( int idx = leftLimit; idx <= rightLimit; idx++ )
         {
            int newRow = row + 1;
            pixIdx = (newRow * pixRectSideSize) + idx;
            if ( (baseSpritePixels[pixIdx] & PIXEL_COLOR_BITS) == WHITE_RGB_BITS )
               setTPHelper( newRow, idx );
         }
   }


   /*
    * Set white pixels outside the perimeter of the org.bfoit.tg.Sprite's image to be transparent.
    */
   private void setTransparentPixels()
   {
      for (int row=0; row < pixRectSideSize; row++ )
      {
         int idx = row * pixRectSideSize;
         if ( (baseSpritePixels[idx] & PIXEL_COLOR_BITS) == WHITE_RGB_BITS )
            setTPHelper( row, 0 );
      }
      for (int row=0; row < pixRectSideSize; row++ )
      {
         int idx = (row * pixRectSideSize) + pixRectSideSize-1;
         if ( (baseSpritePixels[idx] & PIXEL_COLOR_BITS) == WHITE_RGB_BITS )
            setTPHelper( row, pixRectSideSize-1 );
      }
      for (int col=0; col < pixRectSideSize; col++ )
      {
         if ( (baseSpritePixels[col] & PIXEL_COLOR_BITS) == WHITE_RGB_BITS )
            setTPHelper( 0, col );
      }
      for (int col=0; col < pixRectSideSize; col++ )
      {
         int idx = ((pixRectSideSize-1) * pixRectSideSize) + col;
         if ( (baseSpritePixels[idx] & PIXEL_COLOR_BITS) == WHITE_RGB_BITS )
            setTPHelper( pixRectSideSize-1, col );
      }
   }


   //private void printPixels(String what, int[] pixels, int width, int height)
   //{
   //   for ( int y=0; y < height; y++ )
   //   {
   //      for ( int x=0; x < width; x++ )
   //      {
   //         int i = y * width + x;
   //         int a = (pixels[i] >> 24) & 0xff;
   //         int r = (pixels[i] >> 16) & 0xff;
   //         int g = (pixels[i] >> 8) & 0xff;
   //         int b = pixels[i] & 0xff;
   //         System.out.println(what+"["+i+"] a="+a+", r="+r+", g="+g+", b="+b);
   //      }
   //      System.out.println( "----------" );
   //   }
   //
   //} // end printPixels()


   private float slope( int x1, int y1, int x2, int y2 )
   { return ((float) y2 - y1) / ((float) x2 - x1); }


   /*
    * Fill spritePixels[] to reflect the specified heading (in radians)
    * which is the amount of rotation needed since baseSpritePixels[] is
    * aligned to 0.0.  I use a 'Reverse-Rotation' algorithm, computing
    * which pixel in the original image maps to every pixel in the new
    * image (vs projecting forward) to avoid holes due to rounding errors.
    * Since I'm rotating around the center of the image (not its origin),
    * three steps are needed:
    * (1) translate origin to center,
    * (2) reverse rotatation of pixel x,y, and
    * (3) translate back to initial origin.
    */
   private void updateSpritePixels( double heading )
   {
      if ( spritePixels == null )
         spritePixels = new int[pixRectSideSize * pixRectSideSize];
      if ( heading == 0.0F )
         // no translation if heading aligned with orig image
         for (int i=0; i < pixRectSideSize * pixRectSideSize; i++)
            spritePixels[i] = baseSpritePixels[i];
      else
      {
         int center = pixRectSideSize / 2;
         double theta = (double) heading;
         double cosTheta = Math.cos( theta );
         double sinTheta = Math.sin( theta );

         // first zap destination, make all pixels transparent
         for (int y=0; y < pixRectSideSize; y++)
            for (int x=0; x < pixRectSideSize; x++)
               spritePixels[y * pixRectSideSize + x] = 0;
         for (int row=0; row < pixRectSideSize; row++)
         {
            int rowIdx = row * pixRectSideSize;
            int rowPrime = 2*(row - center) + 1;
            for (int col=0; col < pixRectSideSize; col++)
            {
               int colPrime = 2*(col - center) + 1;
               int srcX = (int) Math.rint(colPrime*cosTheta - rowPrime*sinTheta);
               srcX =  (srcX - 1) / 2 + center;
               int srcY = (int) Math.rint(colPrime*sinTheta + rowPrime*cosTheta);
               srcY =  (srcY - 1) / 2 + center;
               if ( srcX < 0 || srcX >= pixRectSideSize )
                  continue;
               if ( srcY < 0 || srcY >= pixRectSideSize )
                  continue;
               int pixelIndex = srcY * pixRectSideSize + srcX;
               int pixel = baseSpritePixels[ pixelIndex ];
               spritePixels[rowIdx + col] = pixel;
            }
         }
      }

   } //end updateSpritePixels()



   //
   // Methods for Subclasses
   // ------- --- ----------


   /**
    * Return the default minimum height of this org.bfoit.tg.Sprite's image.
    * Subclasses should override this method if they support a
    * different minimum height.
    */
   protected int getMinimumHeight()
   { return MIN_SPRITE_HEIGHT; }


   /**
    * Return the default minimum width of this org.bfoit.tg.Sprite's image.
    * Subclasses should override this method if they support a
    * different minimum width.
    *
    */
   protected int getMinimumWidth()
   { return MIN_SPRITE_WIDTH; }


   /**
    * Initializes the pixels that make up the turtle's image. Overridden
    * by the child subclass. A org.bfoit.tg.SpritePixels constructor invokes this method.
    */
   protected void initSpritePixels( int pixRectSideSize )
   { }


   /**
    * Set the value of pixels along an approximation of the
    * circumference of a circle.
    * <p>
    * This is an implementation of the simplest form of the midpoint
    * algorithm as described in Computer Graphics, Foley and van Dam 
    *
    * @param ctrX the X coordinate of center of the circle.
    * @param ctrY the Y coordinate of center of the circle.
    * @param radius the distance from the center to pixels painted.
    * @param rgbVal the RGB value of the pixels effected.
    */
   protected void setCirclePixels( int ctrX, int ctrY, int radius, int rgbVal )
   {
      int deltaX = 0;
      int deltaY = radius;
      float midptVal = 5.0F / 4.0F - (float) radius;
      circlePixels( ctrX, ctrY, deltaX, deltaY, rgbVal );
      while ( deltaY > deltaX )
      {
         if ( midptVal < 0 )
         {
            midptVal += 2.0F * (float)deltaX + 3.0F;
            deltaX++;
         }
         else
         {
            midptVal += 2.0F * (float)(deltaX-deltaY) + 5.0F;
            deltaX++;
            deltaY--;
         }
         circlePixels( ctrX, ctrY, deltaX, deltaY, rgbVal );
      }
   }


   /**
    * Set the value of pixels along an approximation of a line.
    *
    * @param x0 the X coordinate of one end of the line.
    * @param y0 the Y coordinate of one end of the line.
    * @param x1 the X coordinate of the other end of the line.
    * @param y1 the Y coordinate of the other end of the line.
    * @param rgbVal the RGB value of the pixels effected.
    */
   protected void setLinePixels( int x0, int y0, int x1, int y1, int rgbVal )
   {
      //System.out.print("org.bfoit.tg.SpritePixels.setLinePixels: x0="+x0+", y0="+y0);
      //System.out.println(", x1="+x1+", y1="+y1);
      if ( x0 == x1 )
         if ( y0 <= y1 )
            fillVertLine( x0, y0, y1, rgbVal );
         else
            fillVertLine( x0, y1, y0, rgbVal );
      else if ( y0 == y1 )
         if ( x0 <= x1 )
            fillHorizLine( x0, x1, y0, rgbVal );
         else
            fillHorizLine( x1, x0, y0, rgbVal );
      else if ( Math.abs(slope(x0,y0,x1,y1)) > 1.0F )
         if ( y0 < y1 )
            fillYUnitLine(x0, y0, x1, y1, rgbVal);
         else
            fillYUnitLine(x1, y1, x0, y0, rgbVal);
      else if ( x0 < x1 )
         fillXUnitLine(x0, y0, x1, y1, rgbVal);
      else
         fillXUnitLine(x1, y1, x0, y0, rgbVal);
   }


   /**
    * Fill the specified (x,y) pixel in the org.bfoit.tg.Sprite's image with
    * an RGB value.  Used in child's initSpritePixels() to paint
    * single pixels in the turtle's image.
    * <p>
    * The origin for the coordinate system used is the top-left
    * corner with X values increasing left to right and Y values
    * increasing top to bottom.
    *
    * @param x the X coordinate of the pixel.
    * @param y the Y coordinate of the pixel.
    * @param rgbVal the new RGB value of the pixel.
    */
   protected void setPixel( int x, int y, int rgbVal )
   {
      String me = "org.bfoit.tg.SpritePixels.setPixel: ";
      String outOfBounds = " out of bounds";
      if ( x < 0 || x >= pixRectSideSize )
         System.err.println( me + "x=" + x + outOfBounds );
      else if ( y < 0 || y >= pixRectSideSize )
         System.err.println( me + "y=" + y + outOfBounds );
      else
         baseSpritePixels[ x + (y * pixRectSideSize) ] = rgbVal;
   }



   //
   // Public Methods
   // ------ -------


   /**
    * Return a copy of this org.bfoit.tg.Sprite's pixels.
    */
   public int[] getPixels()
   { return spritePixels; }


   /**
    * Return the length of a side of this org.bfoit.tg.Sprite's square
    * array of pixels.
    */
   public int getSideSize()
   { return pixRectSideSize; }


   /**
    * Return the current height of this org.bfoit.tg.Sprite's image. This
    * includes taking into account the current heading.
    */
   public int getHeight()
   {
      if ( spritePixels == null )
         return spriteHeight;
      return (findLastNonTransparentLine() - findFirstNonTransparentLine()) + 1;
   }


   /**
    * Return the current width of this org.bfoit.tg.Sprite's image. This
    * includes taking into account the current heading.
    */
   public int getWidth()
   {
      if ( spritePixels == null )
         return spriteWidth;
      return (findLastNonTransparentColumn() - findFirstNonTransparentColumn()) + 1;
   }


   /**
    * Fill inner pixels of the turtle with the specified color.
    */
   public boolean setSpriteColor( Color newColor )
   {
      if ( (spriteColor != null) && ( ! newColor.equals(spriteColor)) )
      {
         int pixel = PIXEL_OPACITY_BITS | newColor.getRGB();
         for (int pixIdx=0; pixIdx < baseSpritePixels.length; pixIdx++)
            if ( colorFillMask[pixIdx] == true )
               baseSpritePixels[pixIdx] = pixel;
         updateSpritePixels( spriteHeading );
         spriteColor = newColor;
         return true;
      }
      return false;
   }


   /**
    * Rotate the turtle to a specified heading (radians).
    * Return true if the org.bfoit.tg.Sprite's image changed, false if not.
    */
   public boolean setSpriteHeading( double newHeading )
   {
      if ( Math.abs(spriteHeading - newHeading) > 0.001 )
      {
         spriteHeading = newHeading;
         updateSpritePixels( newHeading );
         return true;
      }
      return false;
   }


} // end class org.bfoit.tg.SpritePixels
