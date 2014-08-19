package org.bfoit.tg;

import java.awt.Color;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image is an arrow.
 * <p>
 * @author Guy Haas
 */
public class ArrowTurtle extends SpritePixels
{
   //
   // Symbolic Constants
   // -------- ---------

   private static final int BLACK_OPAQUE_PIXEL = 0xff000000;

   /*
    * Default height and width of an org.bfoit.tg.ArrowTurtle's image
    */
   public static final int DEFAULT_ARROW_HEIGHT = 30;
   public static final int DEFAULT_ARROW_WIDTH = 30;

   public static final int MINIMUM_ARROW_HEIGHT = 10;
   public static final int MINIMUM_ARROW_WIDTH = 10;


   /**
    * Constructor for an arrow org.bfoit.tg.Sprite image of the default height.
    */
   public ArrowTurtle( Color color, double heading )
   { this( DEFAULT_ARROW_WIDTH, DEFAULT_ARROW_HEIGHT, color, heading ); }

   /**
    * Constructor for an arrow org.bfoit.tg.Sprite image of a given height and width.
    */
   public ArrowTurtle( int width, int height, Color color, double heading )
   { super( width < MINIMUM_ARROW_WIDTH ? MINIMUM_ARROW_WIDTH : width,
            height < MINIMUM_ARROW_HEIGHT ? MINIMUM_ARROW_HEIGHT : height,
	    color,
	    heading );
   }


   /**
    * Initialize the turtle's pixels, an arrow.
    *
    * The arrow's geometry has a shaft either half the spriteHeight (for
    * arrows with widths equal-to or greater than their height) or two-thirds
    * the spriteHeight (for arrows whose heights are greater than their widths).
    * The arrowhead is one-third the spriteHeight tall. The shaft's width is
    * one-third of spriteWidth.
    */
   protected void initSpritePixels( int turtleSideSize )
   {
      float center = ((float)turtleSideSize) / 2.0F;
      int spriteHeight = super.getHeight();
      int spriteWidth = super.getWidth();
      float halfShaftWidth = spriteWidth / 6.0F;
      int left = Math.round(center-(spriteWidth/2.0F));
      int right = left + spriteWidth;
      int bottom = Math.round(center-(spriteHeight/2.0F));
      int top = bottom + spriteHeight;
      int x1 = Math.round( center );
      if ( spriteHeight > spriteWidth )
         x1 += Math.round( spriteHeight/8.0F );
      int y1 = Math.round( center + halfShaftWidth );
      int y2 = Math.round( center - halfShaftWidth );
      setLinePixels(x1, y1, x1, right, BLACK_OPAQUE_PIXEL);                  // right base of arrowhead
      setLinePixels(x1, right, top, Math.round(center), BLACK_OPAQUE_PIXEL); // right side of arrowhead
      setLinePixels(x1, y2, x1, left, BLACK_OPAQUE_PIXEL);                   // left base of arrowhead
      setLinePixels(x1, left, top, Math.round(center), BLACK_OPAQUE_PIXEL);  // left side of arrowhead
      setLinePixels(x1, y1, bottom, y1, BLACK_OPAQUE_PIXEL);                 // right side of shaft
      setLinePixels(bottom, y1, bottom, y2, BLACK_OPAQUE_PIXEL);             // base of shaft
      setLinePixels(x1, y2, bottom, y2, BLACK_OPAQUE_PIXEL);                 // left side of shaft
   }


} // end class org.bfoit.tg.ArrowTurtle
