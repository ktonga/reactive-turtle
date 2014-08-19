package org.bfoit.tg;

import java.awt.Color;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image is an isoseles
 * triangle - the original standard Logo turtle. 
 * <p>
 * @author Guy Haas
 */
public class TriangleTurtle extends SpritePixels
{

   //
   // Symbolic Constants
   // -------- ---------

   /*
    * Default height and width of an isosceles triangle for a
    * org.bfoit.tg.TriangleTurtle.  initSpritePixels() assumes that the height is
    * greater than or equal to the width.  Both the height and the
    * width should be odd numbers so that the image looks good when
    * rotated around its center pixel.
    */
   private static final int DEFAULT_TRIANGLE_HEIGHT = 30;
   private static final int DEFAULT_TRIANGLE_WIDTH = 30;

   private static final int BLACK_OPAQUE_PIXEL = 0xff000000;



   //
   // org.bfoit.tg.TriangleTurtle Constructors
   // -------------- ------------

   public TriangleTurtle( Color color, double heading )
   { super( DEFAULT_TRIANGLE_WIDTH, DEFAULT_TRIANGLE_HEIGHT, color, heading ); }

   public TriangleTurtle( int width, int height, Color color, double heading )
   { super( width, height, color, heading ); }


   /**
    * Initialize the turtle's pixels, an isosceles triangle, into the
    * provided int[] turtlePixels.  org.bfoit.tg.SpritePixels EXPECTS/REQUIRES the
    * image to be on its side, pointing RIGHT/EAST, aligned with the
    * positive X-axis, a heading of mathematical 0 degrees (not to be
    * confused with TurtleSpace's coordinate system where 0 degrees
    * is UP/NORTH, the positive Y-axis).
    */
   protected void initSpritePixels( int turtleSideSize )
   {
      int spriteHeight = super.getHeight();
      int spriteWidth = super.getWidth();
      float center = ((float)turtleSideSize) / 2.0F;
      float leftX = center - ((float) spriteHeight)/2.0F;
      float bottomY = center - ((float) spriteWidth)/2.0F;
      int x1 = Math.round(leftX);
      int x2 = x1 + spriteHeight-1;
      int bY = Math.round(bottomY);
      int tY = bY + spriteWidth-1;
      int cY = Math.round(center);
      setLinePixels(x1, bY, x1, tY, BLACK_OPAQUE_PIXEL);
      setLinePixels(x1, tY, x2, cY, BLACK_OPAQUE_PIXEL);
      setLinePixels(x1, bY, x2, cY, BLACK_OPAQUE_PIXEL);
   }

} // end class org.bfoit.tg.TriangleTurtle
