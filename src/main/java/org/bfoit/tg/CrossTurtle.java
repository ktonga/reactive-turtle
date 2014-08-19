package org.bfoit.tg;

import java.awt.Color;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image is a cross.
 * <p>
 * @author Guy Haas
 */
public class CrossTurtle extends SpritePixels
{

   //
   // Symbolic Constants
   // -------- ---------

   /**
    * Default height of a org.bfoit.tg.CrossTurtle's image.
    */
   public static final int DEFAULT_CROSS_HEIGHT = 30;

   /**
    * Default width of a org.bfoit.tg.CrossTurtle's image.
    */
   public static final int DEFAULT_CROSS_WIDTH = 30;


   //
   // Constructors
   // ------------

   public CrossTurtle( Color color, double heading )
   { this( DEFAULT_CROSS_WIDTH, DEFAULT_CROSS_HEIGHT, color, heading ); }

   public CrossTurtle( int width, int height, Color color, double heading )
   { super( (width>9) ? width : 9, (height>9) ? height : 9, color, heading ); }


   /*
    * drawLine is a wrapper-like method
    * it does 2 things that would distract from envisioning cross algorithm
    * it converts real numbers (points) to integer pixel row/column numbers
    * it subtracts 1 from all points since pixel array indicies start at 0
    */
   private void drawLine( float x1, float y1, float x2, float y2 )
   {
      int c1 = Math.round(x1) - 1;
      int r1 = Math.round(y1) - 1;
      int c2 = Math.round(x2) - 1;
      int r2 = Math.round(y2) - 1;
      setLinePixels( c1, r1, c2, r2, SpritePixels.BLACK_OPAQUE_PIXEL );
   }


   /**
    * Initialize the pixels composing the org.bfoit.tg.Sprite's image, a cross.
    */
   /*
    * *note* org.bfoit.tg.Sprite's image must be heading to the east
    */
   protected void initSpritePixels( int turtleSideSize )
   {
      float center = ((float)turtleSideSize) / 2.0F;
      int spriteHeight = super.getHeight();
      float hafHt = spriteHeight / 2.0F;
      int spriteWidth = super.getWidth();
      float hafWd = spriteWidth / 2.0F;
      float gap = spriteHeight / 8;
      if ( spriteWidth < spriteHeight )
         gap = spriteWidth / 8;
      float hafGap = gap / 2.0F;
      // right side of cross
      drawLine( center-hafGap, center+hafGap, center-hafGap, center+hafWd );
      drawLine( center-hafGap, center+hafWd, center+hafGap, center+hafWd );
      drawLine( center+hafGap, center+hafGap, center+hafGap, center+hafWd );
      // top of cross
      drawLine( center+hafGap, center+hafGap, center+hafHt, center+hafGap );
      drawLine( center+hafHt, center-hafGap, center+hafHt, center+hafGap );
      drawLine( center+hafGap, center-hafGap, center+hafHt, center-hafGap );
      // left side of cross
      drawLine( center+hafGap, center-hafGap, center+hafGap, center-hafWd );
      drawLine( center-hafGap, center-hafWd, center+hafGap, center-hafWd );
      drawLine( center-hafGap, center-hafGap, center-hafGap, center-hafWd );
      // bottom of cross
      drawLine( center-hafGap, center-hafGap, center-hafHt, center-hafGap );
      drawLine( center-hafHt, center-hafGap, center-hafHt, center+hafGap );
      drawLine( center-hafHt, center+hafGap, center-hafGap, center+hafGap );
   }


} // end class org.bfoit.tg.CrossTurtle
