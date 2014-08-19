package org.bfoit.tg;

import java.awt.Color;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image a diamond shape.
 * <p>
 * @author Guy Haas
 */
public class DiamondTurtle extends SpritePixels
{

   //
   // Symbolic Constants
   // -------- ---------

   /**
    * Default height of a org.bfoit.tg.DiamondTurtle's image.
    */
   public static final int DEFAULT_DIAMOND_HEIGHT = 32;

   /**
    * Default width of a org.bfoit.tg.DiamondTurtle's image.
    */
   private static final int DEFAULT_DIAMOND_WIDTH = 20;



   //
   // Constructors
   // ------------

   /**
    * Return a org.bfoit.tg.DiamondTurtle org.bfoit.tg.Sprite with specified color and heading.
    */
   public DiamondTurtle( Color color, double heading )
   { this( DEFAULT_DIAMOND_WIDTH, DEFAULT_DIAMOND_HEIGHT, color, heading ); }


   /**
    * Return a org.bfoit.tg.DiamondTurtle org.bfoit.tg.Sprite with the specified dimensions,
    * color and heading.
    */
   public DiamondTurtle( int width, int height, Color color, double heading )
   { super( width, height, color, heading ); }



   //
   // org.bfoit.tg.SpritePixels Methods Overridden
   // ------------ ------- ----------


   /**
    * Initialize the pixels composing the org.bfoit.tg.Sprite's image, a diamond.
    */
   protected void initSpritePixels( int turtleSideSize )
   {
      int spriteHeight = super.getHeight();
      int spriteWidth = super.getWidth();
      float center = ((float)turtleSideSize) / 2.0F;
      float leftX = center - ((float) spriteHeight)/2.0F;
      int rightX = (int)leftX + spriteHeight-1;
      float bottomY = center - ((float) spriteWidth)/2.0F;
      int topY = (int)bottomY + spriteWidth-1;

      setLinePixels( Math.round(leftX),
                     Math.round(center),
                     Math.round(center),
                     topY,
                     SpritePixels.BLACK_OPAQUE_PIXEL );
      setLinePixels( Math.round(leftX),
                     Math.round(center),
                     Math.round(center),
                     Math.round(bottomY),
                     SpritePixels.BLACK_OPAQUE_PIXEL );
      setLinePixels( Math.round(center),
                     Math.round(bottomY),
                     rightX,
                     Math.round(center),
                     SpritePixels.BLACK_OPAQUE_PIXEL );
      setLinePixels( Math.round(center),
                     topY,
                     rightX,
                     Math.round(center),
                     SpritePixels.BLACK_OPAQUE_PIXEL );
   }


} // end class org.bfoit.tg.DiamondTurtle
