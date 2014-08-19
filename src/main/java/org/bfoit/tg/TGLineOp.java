package org.bfoit.tg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;

/**
 * org.bfoit.tg.TGLineOp provides a TurtleGraphics Line graphics Operation.
 * A line of a specified penWidth is drawn between two TurtleSpace
 * points.
 * <p>
 * *NOTE* The points supplied to a org.bfoit.tg.TGLineOp constructor are arranged
 * such that lines are always drawn left to right (increasing X
 * values). This consistency is necessary to insure identical pixels 
 * are painted for fat lines that should be identical lines, say
 * erasing a fat black line by drawing an identical white line over
 * it, e.g., (SETPS 2 SETC 0 FORWARD 100 WAIT 1000 SETC 7 BACK 100).
 * For an angular line, rounding differences can cause the BACK to
 * generate a line that has a different width.  The above sequence
 * with the turtle heading at 45 degrees is a good example.
 * <p>
 * @author Guy Haas
 */

public class TGLineOp implements ImageObserver, TGGraphicsOp
{

   //
   // Symbolic Constants
   // -------- ---------

   /*
    * Number of radians in an eighth of a degree.
    */
   private static final double EIGHTH_DEG_RADIANS = Math.PI / (180.0 * 8);


   /*
    * Number of radians in a circle (360 degrees)
    */
   private static final double CIRCLE_RADIANS = Math.PI * 2.0;


   /*
    * Number of radians in half a circle (180 degrees)
    */
   private static final double HALF_CIRCLE_RADIANS = Math.PI;


   /*
    * Number of radians in a quarter of a circle (90 degrees)
    */
   private static final double QTR_CIRCLE_RADIANS = Math.PI / 2.0;


   /*
    * Number of radians in three quarters of a circle (270 degrees)
    */
   private static final double THREE_QTR_CIRCLE_RADIANS = CIRCLE_RADIANS - QTR_CIRCLE_RADIANS;



   //
   // Private Data
   // ------- ----


   private Color color;

   /*
    * Turtle heading - angle to p2 from p1.
    */
   private double heading;

   private int penWidth;

   private int[] xPoints;

   private int[] yPoints;

   private TGPoint p1, p2;



   //
   // Constructor
   // -----------


   /**
    * Return a org.bfoit.tg.TGLineOp, a line drawing operation given its end points, its
    * heading, its color and the width of the line.
    */
   public TGLineOp(TGPoint pt1, TGPoint pt2, double hd, Color color, int wid)
   {
      if ( pt1.xDoubleValue() <= pt2.xDoubleValue() )
      {
         this.p1 = pt1;
         this.p2 = pt2;
         heading = hd;
      }
      else
      {
         this.p1 = pt2;
         this.p2 = pt1;
         heading = hd - HALF_CIRCLE_RADIANS;
      }
      if ( heading < 0 )
         heading += CIRCLE_RADIANS;
      if ( heading < EIGHTH_DEG_RADIANS )
         heading = 0.0;
      else if ( heading > (CIRCLE_RADIANS - EIGHTH_DEG_RADIANS) )
         heading = 0.0;
      else if ( Math.abs(heading - QTR_CIRCLE_RADIANS) < EIGHTH_DEG_RADIANS )
         heading = QTR_CIRCLE_RADIANS;
      else if ( Math.abs(heading - THREE_QTR_CIRCLE_RADIANS) < EIGHTH_DEG_RADIANS )
         heading = THREE_QTR_CIRCLE_RADIANS;

      this.color = color;
      this.penWidth = wid;
   }


   //
   // ImageObserver interface methods
   // ------------- --------- -------

   /**
    * ImageObserver interface method invoked when information about an image which
    * was previously requested using an asynchronous interface becomes available.
    */
   public boolean imageUpdate(Image img, int flags, int x, int y, int wd, int ht)
   {
      System.err.println( "org.bfoit.tg.TGLineOp.imageUpdate: got here!" );
      return true;
   }


   //
   // Class private helper methods
   // ----- ------- ------ -------


   /*
    * This line is a special case, heading either 0 or 180 degrees.
    * It can be drawn as a rectangle.
    */
   private Rectangle drawHorizontalFatLine(Graphics g, int canvasHeight, int canvasWidth)
   {
      //System.out.println( "org.bfoit.tg.TGLineOp.drawHorizontalFatLine()" );
      double hafWid =  ((double)penWidth) / 2.0;
      int y = p1.imageY( hafWid, canvasHeight );
      int p1X = p1.imageX(canvasWidth);
      int p2X = p2.imageX(canvasWidth);
      int x = (p1X < p2X) ? p1X : p2X;
      int lineWidth = Math.abs( p1X - p2X );
      g.setClip( x, y, lineWidth, penWidth );
      g.fillRect( x, y, lineWidth, penWidth );
      return new Rectangle(x, y, lineWidth, penWidth);
   }


   /*
    * Draw a line that is wider than 1 pixel; in Java AWT this may mean painting
    * a filled polygon.  I try to take advantage of some special cases, e.g., a
    * movement of only a single pixel (i.e., "FORWARD 1")...
    */
   private Rectangle drawFatLine( Graphics g, int canvasHeight, int canvasWidth )
   {
      //System.out.println( "org.bfoit.tg.TGLineOp.drawFatLine()" );
      if ( heading == 0.0 || heading == HALF_CIRCLE_RADIANS )
         return drawHorizontalFatLine( g, canvasHeight, canvasWidth );
      if ( heading == QTR_CIRCLE_RADIANS || heading == THREE_QTR_CIRCLE_RADIANS )
         return drawVerticalFatLine( g, canvasHeight, canvasWidth );
      double xDf = Math.abs( p1.xDoubleValue() - p2.xDoubleValue() );
      double yDf = Math.abs( p1.yDoubleValue() - p2.yDoubleValue() );
      if ( xDf < 1.5 && yDf < 1.5 )
         return drawOnePixelFatLine(g, canvasHeight, canvasWidth);
      int p1X = p1.imageX( canvasWidth );
      int p1Y = p1.imageY( canvasHeight );
      int p2X = p2.imageX( canvasWidth );
      int p2Y = p2.imageY( canvasHeight );
      // compute the width end points of perpendicular line at p1
      double hafWid = ((double) penWidth) / 2.0;
      double perpLineHead = heading + QTR_CIRCLE_RADIANS;
      if ( perpLineHead > CIRCLE_RADIANS )
         perpLineHead -= CIRCLE_RADIANS;
      TGPoint point = p1.otherEndPoint( perpLineHead, hafWid );
      int p1LeftX = point.imageX( canvasWidth );
      int p1LeftDX = p1LeftX - p1X;
      int p1LeftY = point.imageY( canvasHeight );
      int p1LeftDY = p1LeftY - p1Y;
      perpLineHead = heading - QTR_CIRCLE_RADIANS;
      if ( perpLineHead < 0.0 )
         perpLineHead += CIRCLE_RADIANS;
      point = p1.otherEndPoint( perpLineHead, hafWid );
      int p1RightX = point.imageX( canvasWidth );
      int p1RightDX = p1RightX - p1X;
      int p1RightY = point.imageY( canvasHeight );
      int p1RightDY = p1RightY - p1Y;
      if ( xPoints == null )
      {
         xPoints = new int[4];
         yPoints = new int[4];
      }
      xPoints[0] = p1LeftX;
      yPoints[0] = p1LeftY;
      xPoints[1] = p1RightX;
      yPoints[1] = p1RightY;
      xPoints[2] = p2X + p1RightDX;
      yPoints[2] = p2Y + p1RightDY;
      xPoints[3] = p2X + p1LeftDX;
      yPoints[3] = p2Y + p1LeftDY;
      int crX = min( xPoints );
      int crWidth = Math.abs( crX - max(xPoints) ) + 1;
      int crY = min( yPoints );
      int crHeight = Math.abs( crY - max(yPoints) ) + 1;
      g.setClip( crX, crY, crWidth, crHeight );
      g.fillPolygon( xPoints, yPoints, 4 );
      return new Rectangle( crX, crY, crWidth, crHeight );

   } // end drawFatLine()


   /*
    * draw a line perpendicular to current heading
    * with its midpoint at p1 (which is equal to p2)...
    */
   private Rectangle drawOnePixelFatLine(Graphics g, int canvasHeight, int canvasWidth)
   {
      //System.out.println( "org.bfoit.tg.TGLineOp.drawOnePixelFatLine()" );
      // compute the width end points of perpendicular line at p1
      double hafWid = ((double) penWidth) / 2.0;
      double perpLineHead = heading + QTR_CIRCLE_RADIANS;
      if ( perpLineHead > CIRCLE_RADIANS )
         perpLineHead -= CIRCLE_RADIANS;
      TGPoint leftPt = p1.otherEndPoint( perpLineHead, hafWid );
      int ptLeftX = leftPt.imageX( canvasWidth );
      int ptLeftY = leftPt.imageY( canvasHeight );
      perpLineHead = heading - QTR_CIRCLE_RADIANS;
      if ( perpLineHead < 0.0 )
         perpLineHead += CIRCLE_RADIANS;
      TGPoint rightPt = leftPt.otherEndPoint( perpLineHead, (double)penWidth );
      int ptRightX = rightPt.imageX( canvasWidth );
      int ptRightY = rightPt.imageY( canvasHeight );
      int crX = ptLeftX < ptRightX ? ptLeftX : ptRightX;
      int crWidth = Math.abs(ptLeftX - ptRightX) + 1;
      int crY = ptLeftY < ptRightY ? ptLeftY : ptRightY;
      int crHeight = Math.abs(ptLeftY - ptRightY) + 1;
      g.setClip( crX, crY, crWidth, crHeight );
      g.drawLine( ptLeftX, ptLeftY, ptRightX, ptRightY );
      return new Rectangle( crX, crY, crWidth, crHeight );
   }


   /*
    * This line is a special case, heading either 90 or 270 degrees.
    * It can be drawn as a rectangle.
    */
   private Rectangle drawVerticalFatLine( Graphics g, int canvasHeight, int canvasWidth )
   {
      //System.out.println( "org.bfoit.tg.TGLineOp.drawVerticalFatLine()" );
      double hafWid =  ((double)penWidth) / 2.0;
      int x = p1.imageX( -hafWid, canvasWidth );
      int p1Y = p1.imageY(canvasHeight);
      int p2Y = p2.imageY(canvasHeight);
      int y = (p1Y < p2Y) ? p1Y : p2Y;
      int height = Math.abs( p1Y - p2Y );
      g.setClip( x, y, penWidth, height );
      g.fillRect( x, y, penWidth, height );
      return new Rectangle(x, y, penWidth, height);
   }


   private int max( int[] ary )
   {
      int num = ary[0];
      for ( int i=1; i < ary.length; i++ )
         if ( ary[i] > num )
            num = ary[i];
      return num;
   }


   private int min( int[] ary )
   {
      int num = ary[0];
      for ( int i=1; i < ary.length; i++ )
         if ( ary[i] < num )
            num = ary[i];
      return num;
   }


   /**
    * Draw the line defined by this org.bfoit.tg.TGLineOp object.
    */
   public Rectangle doIt( Image inMemoryImage )
   {
      Rectangle clipRect;
      int imageWidth = inMemoryImage.getWidth( this );
      if ( imageWidth < 0 )
         return null;
      int imageHeight = inMemoryImage.getHeight( this );
      if ( imageHeight < 0 )
         return null;
      Graphics g = inMemoryImage.getGraphics();
      g.setColor(color);
      if ( penWidth == 1 )
      {
         int p1X = p1.imageX( imageWidth );
         int p1Y = p1.imageY( imageHeight );
         int p2X = p2.imageX( imageWidth );
         int p2Y = p2.imageY( imageHeight );
         int crX = p1X < p2X ? p1X : p2X;
         int crWidth = Math.abs( p1X - p2X ) + 1;
         int crY = p1Y < p2Y ? p1Y : p2Y;
         int crHeight = Math.abs( p1Y - p2Y ) + 1;
         g.setClip( crX, crY, crWidth, crHeight );
         g.drawLine( p1X, p1Y, p2X, p2Y );
         clipRect = new Rectangle( crX, crY, crWidth, crHeight );
      }
      else
         clipRect = drawFatLine( g, imageHeight, imageWidth );
      g.dispose();
      return clipRect;

   } // end doIt()


   public String toString()
   {
      return "org.bfoit.tg.TGLineOp[color="+color+",width="+penWidth+",p1="+p1+",p2="+p2+"]";
   }

} // end class org.bfoit.tg.TGLineOp
