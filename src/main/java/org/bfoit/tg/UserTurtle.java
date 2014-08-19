package org.bfoit.tg;

/**
 * This class provides the turtle's appearance, the array of pixels
 * that make up its image.  In this case, the image is supplied by the
 * current program, the user.
 * <p>
 * @author Guy Haas
 */
public class UserTurtle extends SpritePixels
{

   //
   // Constructor
   // -----------

   /**
    * Instantiate this org.bfoit.tg.Sprite's image based on the supplied org.bfoit.tg.PixelRectangle.
    *
    * It is assumed that the org.bfoit.tg.PixelRectangle is oriented north, towards
    * the top of the graphics canvas.
    */
   public UserTurtle( PixelRectangle pixRect )
   { super( pixRect ); }


}
