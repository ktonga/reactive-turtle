package org.bfoit.tg;

import java.lang.Math;

/**
 * A org.bfoit.tg.TGPoint is a virtual point, a point in TurtleSpace.  In TurtleSpace,
 * 0.0,0.0  is at the center of the graphics canvas, like the way coordinate
 * spaces are represented in Algebra.  Likewise, TGPoints have real number
 * coordinates.  TGPoints may or may not be visible based upon the current
 * size of the graphics canvas window of TG.  Methods are provided to map
 * the point's coordinates from TurtleSpace to an Image's x and y indicies.
 * <p>
 * TurtleGraphics now works entirely with TGPoints - points in TurtleSpace.
 * Early versions of TG used int primitive values for X and Y coordinates.
 * This approach led to too many visual problems caused by the propagation
 * of rounding off values. org.bfoit.tg.TGPoint isolates actual implementation of points
 * to one place which can thus be more easily controlled.
 */


public class TGPoint implements Cloneable
{

   /*
    * This point's coordinates.
    */
   private final double x, y;

   //
   // constructors
   //

   /**
    * Default org.bfoit.tg.TGPoint is [0,0] which is the center of TurtleSpace.
    */
   public TGPoint() { x = 0.0; y = 0.0; }
   
   /**
    * Constructs a new org.bfoit.tg.TGPoint (a virtual point in TurtleSpace)
    * based upon provided double values for x and y coordinates.
    */
   public TGPoint( double x, double y )
   { this.x = x; this.y = y; }

   /**
    * Constructs a new org.bfoit.tg.TGPoint (a virtual point in TurtleSpace)
    * based upon provided float values for x and y coordinates.
    */
   public TGPoint( float x, float y )
   { this.x = (double)x; this.y = (double)y; }

   /**
    * Constructs a new org.bfoit.tg.TGPoint (a virtual point in TurtleSpace)
    * based upon provided int values for x and y coordinates.
    */
   public TGPoint( int x, int y )
   { this.x = (double)x; this.y = (double)y; }

   /**
    * Constructs a new org.bfoit.tg.TGPoint (a virtual point in TurtleSpace)
    * based upon provided long values for x and y coordinates.
    */
   public TGPoint( long x, long y )
   { this.x = (double)x; this.y = (double)y; }



   /**
    * Override Object.equals(Object) so that comparison is based on
    * the x and y fields.
    */
   public boolean equals( Object obj )
   {
      if ( obj == this )
         return true;
      if ( ! (obj instanceof TGPoint) )
         return false;
      TGPoint otherPoint = (TGPoint) obj;
      if ( (x != otherPoint.xDoubleValue()) || (y != otherPoint.yDoubleValue()) )
         return false;
      return true;
   }


   //
   // Methods Provided by This Class
   // ------- -------- -- ---- -----
   //

   /**
    * Return an Image's index equivilent for this point's X coordinate. The
    * index reflects a mapping based on a provided Image width.
    */
   public int imageX( int imageWidth )
   {
      int xCenter = imageWidth / 2;
      int xInt = (int) xLongValue();
      return xCenter + xInt;
   }


   /**
    * Return an Image's index equivilent for this point's X coordinate combined
    * with a provided offset. The index reflects a mapping based on a provided
    * Image width.
    */
   public int imageX( double offset, int imageWidth )
   {
      int xCenter = imageWidth / 2;
      int xInt = (int) Math.round(x + offset);
      return xCenter + xInt;
   }



   /**
    * Return an Image's index equivilent for this point's Y coordinate. The
    * index reflects a mapping based on a provided Image height.
    */
   public int imageY( int imageHeight )
   {
      int yCenter = imageHeight / 2;
      int yInt = (int) yLongValue();
      return yCenter - yInt;
   }


   /**
    * Return an Image's index equivilent for this point's Y coordinate combined
    * with a provided offset. The index reflects a mapping based on a provided
    * Image height.
    */
   public int imageY( double offset, int imageHeight )
   {
      int yCenter = imageHeight / 2;
      int yInt = (int) Math.round(y + offset);
      return yCenter - yInt;
   }


   /**
    * Creates and returns a copy of this org.bfoit.tg.TGPoint.
    */
   public Object clone()
   {
      try { return super.clone(); }
      catch (CloneNotSupportedException e) { /* impossible error */ }
      return new TGPoint( x, y );
   }


   /**
    * Given one end point of a line, its length and heading (in radians),
    * return its other end point.
    *
    * The idea of rounding deltaX and deltaY to zero when close came from
    * Berkeley Logo - absolutely necessary to make graphics look pretty
    */
   public TGPoint otherEndPoint( double radians, double length )
   {
      //System.out.print("otherEndPoint: p1=" + toString() );
      //System.out.println(", radians=" + radians + ", len=" + length);
      double sine = Math.sin(radians);
      double cosine = Math.cos(radians);
      double deltaX = cosine * length;
      if ( (deltaX < 0 && deltaX > -0.0001) || (deltaX > 0 && deltaX < 0.0001) )
         deltaX = 0;
      double deltaY = sine * length;
      if ( (deltaY < 0 && deltaY > -0.0001) || (deltaY > 0 && deltaY < 0.0001) )
         deltaY = 0;
      TGPoint p2 = new TGPoint( x + deltaX, y + deltaY );
      //System.out.println("               p2=" + p2);
      return p2;

   } // end otherEndPoint()



   /**
    * Returns the X coordinate of this point in double precision.
    */
   public double xDoubleValue()
   { return x; }


   /**
    * Returns the X coordinate of this point as a long int.
    */
   public long xLongValue()
   { return Math.round(x); }


   /**
    * Returns the Y coordinate of this point in double precision.
    */
   public double yDoubleValue()
   { return y; }


   /**
    * Returns the Y coordinate of this point as a long int.
    *
    */
   public long yLongValue()
   { return Math.round(y); }


   /**
    * Returns the String representation of this point. The coordinates are
    * enclosed in eliptical brackets and separated with a comma.
    */
   public String toString()
   { return "{" + x + "," + y + "}"; }

} // end class org.bfoit.tg.TGPoint
