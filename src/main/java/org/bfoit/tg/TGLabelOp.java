package org.bfoit.tg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;

/**
 * This class implements a TurtleGraphics LABEL graphics operation.
 * <p>
 * @author Guy Haas
 */

public class TGLabelOp implements ImageObserver, TGGraphicsOp
{
   private Color color;
   private Font font;
   private String text;
   private TGPoint where;

   //
   // constructor
   //
   public TGLabelOp( String label, TGPoint where, Font font, Color color )
   {
      this.color = color;
      this.font = font;
      this.text = label;
      this.where = where;
   }


   public Rectangle doIt( Image inMemoryImage )
   {
      int canvasWidth = inMemoryImage.getWidth( this );
      if ( canvasWidth < 0 )
         return null;
      int imageX = where.imageX( canvasWidth );
      int canvasHeight = inMemoryImage.getHeight( this );
      if ( canvasHeight < 0 )
         return null;
      int imageY = where.imageY( canvasHeight );
      Graphics g = inMemoryImage.getGraphics();
      g.setColor( color );
      g.setFont( font );
      FontMetrics fm = g.getFontMetrics();
      int crX = imageX;
      int crWidth = fm.stringWidth( text );
      int crHeight = fm.getHeight();
      int crY = imageY - fm.getMaxAscent();
      g.setClip( crX, crY, crWidth, crHeight );
      g.drawString( text, imageX, imageY );
      g.dispose();
      //int chWidth = fm.charWidth( 'W' );
      //System.out.print("org.bfoit.tg.TGLabelOp: using " + font.getSize() + "pt font,");
      //System.out.println(" ht=" + crHeight + ", wd=" + chWidth);
      Rectangle clipRect = new Rectangle( crX, crY, crWidth, crHeight );
      return clipRect;

   } // end doIt()


   public boolean imageUpdate(Image img, int flags, int x, int y, int wd, int ht)
   {
      System.out.println( "org.bfoit.tg.TGLabelOp.imageUpdate: got here!" );
      return true;
   }

} // end class org.bfoit.tg.TGLabelOp
