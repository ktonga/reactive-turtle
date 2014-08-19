package org.bfoit.tg;

import java.awt.Color;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image is a circle.
 * <p>
 * @author Guy Haas
 */
public class BallTurtle extends SpritePixels
{

   //
   // Symbolic Constants
   // -------- ---------

   private static final int DEFAULT_TURTLE_DIAMETER = 30;


   //
   // Constructors
   // ------------

   public BallTurtle( Color color, double heading )
   { this( DEFAULT_TURTLE_DIAMETER, color, heading ); }

   public BallTurtle( int diameter, Color color, double heading )
   { super( diameter, diameter, color, heading ); }


   /**
    * Initialize the pixels composing the org.bfoit.tg.Sprite's image, a ball.
    */
   protected void initSpritePixels( int turtleSideSize )
   {
      int centerX = turtleSideSize / 2;
      int centerY = turtleSideSize / 2;
      int radius = super.getHeight() / 2;
      setCirclePixels( centerX, centerY, radius, SpritePixels.BLACK_OPAQUE_PIXEL );
   }


   /**
    * Rotate the turtle to a specified heading (radians).
    * Return true if the org.bfoit.tg.Sprite's image changed, false if not.
    *
    * Override org.bfoit.tg.SpritePixels.setSpriteHeading() and do nothing.
    * Rotating a ball does not change its image...
    */
   public boolean setSpriteHeading( double newHeading )
   { return false; }


} // end class org.bfoit.tg.BallTurtle
