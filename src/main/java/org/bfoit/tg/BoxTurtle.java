package org.bfoit.tg;

import java.awt.Color;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image is a rectangle.
 * <p>
 * @author Guy Haas
 */
public class BoxTurtle extends SpritePixels
{

   //
   // Symbolic Constants
   // -------- ---------

   /*
    * Default height and width of a rectangle, org.bfoit.tg.BoxTurtle's image
    */
   private static final int DEFAULT_BOX_HEIGHT = 30;
   private static final int DEFAULT_BOX_WIDTH = 30;

   /*
    * Minimum height and width of a rectangle
    */
   private static final int MIN_BOX_HEIGHT =   2;
   private static final int MIN_BOX_WIDTH =    2;



   //
   // org.bfoit.tg.BoxTurtle Constructors
   // --------- ------------

   /**
    * Return a org.bfoit.tg.BoxTurtle org.bfoit.tg.Sprite with specified color and heading.
    */
   public BoxTurtle( Color color, double heading )
   { this( DEFAULT_BOX_WIDTH, DEFAULT_BOX_HEIGHT, color, heading ); }

   /**
    * Return a org.bfoit.tg.BoxTurtle org.bfoit.tg.Sprite with the specified dimensions,
    * color and heading.
    */
   public BoxTurtle( int width, int height, Color color, double heading )
   { super( width, height, color, heading ); }


   /**
    * Return the minimum height of this SpriteImage.
    * Subclasses should override this method if they
    * support a different minimum height.
    */
   public int getMinimumHeight()
   { return MIN_BOX_HEIGHT; }


   /**
    * Return the minimum width of this SpriteImage.
    * Subclasses should override this method if they
    * support a different minimum width.
    *
    */
   public int getMinimumWidth()
   { return MIN_BOX_WIDTH; }


   /**
    * Initialize the pixels composing the org.bfoit.tg.Sprite's image, a rectangle.
    */
   protected void initSpritePixels( int turtleSideSize )
   {
      int spriteHeight = super.getHeight();
      int spriteWidth = super.getWidth();
      float center = ((float)turtleSideSize) / 2.0F;
      float leftX = center - ((float) spriteHeight)/2.0F;
      float bottomY = center - ((float) spriteWidth)/2.0F;
      int x1 = Math.round(leftX);
      int y1 = Math.round(bottomY);
      int x2 = x1 + spriteHeight-1;
      int y2 = y1 + spriteWidth-1;
      setLinePixels(x1, y1, x1, y2, SpritePixels.BLACK_OPAQUE_PIXEL);
      setLinePixels(x1, y1, x2, y1, SpritePixels.BLACK_OPAQUE_PIXEL);
      setLinePixels(x2, y1, x2, y2, SpritePixels.BLACK_OPAQUE_PIXEL);
      setLinePixels(x1, y2, x2, y2, SpritePixels.BLACK_OPAQUE_PIXEL);
   }


} // end class org.bfoit.tg.BoxTurtle
