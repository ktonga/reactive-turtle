package org.bfoit.tg;

import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.Toolkit;

/**
 * org.bfoit.tg.PixelRectangle - a rectangular area of pixels.
 * <p>
 * To represent rectangular groups of pixels as a single entitly,
 * two things are needed:
 * <ul>
 * <li> an array of ints, the pixels, and </li>
 * <li> the width of the rectangle, the number of columns in a row. </li>
 * </ul>
 * <p>
 * @author Guy Haas
 */

public class PixelRectangle
{

   /**
    * Array of individual pixels represented as ints,
    * 4 bytes: opacity, red, green, and blue.
    */
   public int[] pixels;


  /**
   * The number of columns in a row of pixels in the array.
   */
   public int width;


  /**
   * Instantiate a org.bfoit.tg.PixelRectangle given its constituents.
   * @param pixels array of individual pixels in int format
   * @param numCol the number of columns making up a single
   *               row of pixels
   */
   public PixelRectangle( int[] pixels, int numCol )
   {
      this.pixels = pixels;
      width = numCol;
   }


   /**
    * Create an Image to match the array of picture's pixels.
    * AWT Graphics only supports painting of Image objects, no kind
    * of BitBlt for arrays of pixel values (?who know's why?)
    */
   public Image toImage()
   {
      Image image = null;
      MemoryImageSource imageProducer = null;
      Toolkit tk = Toolkit.getDefaultToolkit();
      while ( image == null )
      {
         if ( imageProducer == null )
            imageProducer = new MemoryImageSource( width, pixels.length/width, pixels, 0, width );
         image = tk.createImage( imageProducer );
      }
      return image;
   }


   /**
    * Override Object.toString() for debugging.
    * Return String with org.bfoit.tg.PixelRectangle dimensions in square brackets
    * and the first three pixel values enclosed in squiggle brackets,
    * broken down into a,r,g,b components.
    */
   public String toString()
   {
      StringBuffer sb = new StringBuffer( "[" );
      sb.append( width );
      sb.append( "x" );
      sb.append( pixels.length/width );
      sb.append( "] : {" );
      for ( int idx=0; idx < 3; idx++ )
      {
         int p = pixels[idx];
	 sb.append( (p >>> 24) & 0xFF );
	 sb.append( ' ' );
	 sb.append( (p >>> 16) & 0xFF );
	 sb.append( ' ' );
	 sb.append( (p >>> 8) & 0xFF );
	 sb.append( ' ' );
	 sb.append( p & 0xFF );
	 if ( idx < 2 )
            sb.append( " | " );
      }
      sb.append( '}' );
      return sb.toString();
   }
   
} // end class org.bfoit.tg.PixelRectangle
