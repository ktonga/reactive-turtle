package org.bfoit.tg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * Wrapper class which provides the TurtleGraphics support
 * from the TG Logo programmng environment to the world of
 * programming in Java.
 */
@SuppressWarnings("ALL")
public class TurtleGraphicsWindow extends Frame
                                  implements TGKeyHandler, TGMouseHandler, WindowListener
{

   //
   // Symbolic Constants
   // -------- ---------

   /**
    * Default height of the graphics canvas.
    */
   public final static int DEFAULT_CANVAS_HEIGHT = 600;

   /**
    * Default width of the graphics canvas.
    */
   public final static int DEFAULT_CANVAS_WIDTH = 600;



   /*
    * The following colors numbers, font numbers, headings,
    * and turtle shape numbers have been copied from org.bfoit.tg.Sprite and
    * org.bfoit.tg.TGCanvas so that classes extending org.bfoit.tg.TurtleGraphicsWindow
    * do not have to prefix them with their class names. Think
    * of this as scaffolding for beginners.
    */

   /**
    * The setpencolor method's input is a built-in color
    * number or an RGB value. Built-in colors numbers are
    * in the range of 0-31.
    */
   public static final int BLACK =      0;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int BLUE =       1;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int GREEN =      2;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int CYAN =       3;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int RED =        4;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int MAGENTA =    5;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int YELLOW =     6;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int WHITE =      7;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int BROWN =      8;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int TAN =        9;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int FOREST =    10;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int AQUA =      11;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int SALMON =    12;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int VIOLET =    13;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int ORANGE =    14;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int GRAY =      15;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int NAVY  =     16;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int SKYBLUE =   17;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int LIME =      18;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int STEELBLUE = 19;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int CHOCOLATE = 20;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int PURPLE =    21;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int GOLD =      22;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int LIGHTGRAY = 23;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int PERU =      24;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int WHEAT =     25;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int PALEGREEN = 26;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int LIGHTBLUE = 27;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int KHAKI =     28;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int PINK =      29;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int LAWNGREEN = 30;

   /**
    * Symbolic constant for input to setpencolor().
    */
   public static final int OLIVE =     31;


   /**
    * The maximum value for a built-in color which
    * require conversion to RGB colors used by Java.
    */
   public static final int MAX_LOGO_COLOR = 31;



   /*
    * The turtle can draw Strings of characters onto the graphics
    * canvas in a few different fonts and styles. The setlabelfont()
    * method has an integer parameter that represents a font and
    * style combination.  Following are symbolic constants for the
    * available fonts and styles.
    */

   /**
    * Symbolic constant for setlabelfont() to select a fixed-width
    * Courier font.
    * @see #setlabelfont
    */
   public static final int COURIER =  0;

   /**
    * Symbolic constant for setlabelfont() to select an emboldened
    * fixed-width Courier font.
    * @see #setlabelfont
    */
   public static final int COURIER_BOLD =  1;

   /**
    * Symbolic constant for setlabelfont() to select an italicized
    * fixed-width Courier font.
    * @see #setlabelfont
    */
   public static final int COURIER_ITALIC =  2;

   /**
    * Symbolic constant for setlabelfont() to select an emboldened
    * and italicized fixed-width Courier font.
    * @see #setlabelfont
    */
   public static final int COURIER_BOLD_ITALIC =  3;

   /**
    * Symbolic constant for setlabelfont() to select the
    * Sans Serif font.
    * @see #setlabelfont
    */
   public static final int SANS_SERIF =  4;

   /**
    * Symbolic constant for setlabelfont() to select an emboldened
    * Sans Serif font.
    * @see #setlabelfont
    */
   public static final int SANS_SERIF_BOLD =  5;

   /**
    * Symbolic constant for setlabelfont() to select an italicized
    * Sans Serif font.
    * @see #setlabelfont
    */
   public static final int SANS_SERIF_ITALIC =  6;

   /**
    * Symbolic constant for setlabelfont() to select an emboldened
    * and italicized Sans Serif font.
    * @see #setlabelfont
    */
   public static final int SANS_SERIF_BOLD_ITALIC =  7;

   /**
    * Symbolic constant for setlabelfont() to select the Serif font.
    * @see #setlabelfont
    */
   public static final int SERIF =  0;

   /**
    * Symbolic constant for setlabelfont() to select an emboldened
    * Serif font.
    * @see #setlabelfont
    */
   public static final int SERIF_BOLD =  1;

   /**
    * Symbolic constant for setlabelfont() to select an italicized
    * Serif font.
    * @see #setlabelfont
    */
   public static final int SERIF_ITALIC =  2;

   /**
    * Symbolic constant for setlabelfont() to select an emboldened
    * and italicized Serif font.
    * @see #setlabelfont
    */
   public static final int SERIF_BOLD_ITALIC =  3;



   /**
    * Turtle heading for the positive Y axis.
    *
    * The Logo turtle's heading in degrees does not match the
    * standard mathematics notion of having the positive X axis
    * as zero, with degrees increasing counter-clockwise. Logo
    * has the positive Y axis as zero with degrees increasing
    * clockwise. The use of abstract symbolic constants for
    * north, east, south, and west help.
    * @see #seth
    * @see #setheading
    */
   public static final int NORTH =   0;

   /**
    * Turtle heading for the positive X axis.
    *
    * The Logo turtle's heading in degrees does not match the
    * standard mathematics notion of having the positive X axis
    * as zero, with degrees increasing counter-clockwise. Logo
    * has the positive Y axis as zero with degrees increasing
    * clockwise. The use of abstract symbolic constants for
    * north, east, south, and west help.
    * @see #seth
    * @see #setheading
    */
   public static final int EAST =   90;

   /**
    * Turtle heading for the negative Y axis.
    *
    * The Logo turtle's heading in degrees does not match the
    * standard mathematics notion of having the positive X axis
    * as zero, with degrees increasing counter-clockwise. Logo
    * has the positive Y axis as zero with degrees increasing
    * clockwise. The use of abstract symbolic constants for
    * north, east, south, and west help.
    * @see #seth
    * @see #setheading
    */
   public static final int SOUTH = 180;

   /**
    * Turtle heading for the negative X axis.
    *
    * The Logo turtle's heading in degrees does not match the
    * standard mathematics notion of having the positive X axis
    * as zero, with degrees increasing counter-clockwise. Logo
    * has the positive Y axis as zero with degrees increasing
    * clockwise. The use of abstract symbolic constants for
    * north, east, south, and west help.
    * @see #seth
    * @see #setheading
    */
   public static final int WEST =  270;



   /*
    * The following turtle shape symbolic constants have been copied
    * from org.bfoit.tg.Sprite so that classes extending org.bfoit.tg.TurtleGraphicsWindow do
    * not have to prefix them with class name.
    */

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  The initial
    * shape roughly looks like a turtle (come-on... think artististic
    * license).  This default turtle image is shape number 0.
    */
   public static final int TURTLE =    0;

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  An image
    * which resembles an arrow is shape number 1.
    */
   public static final int ARROW =     1;

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  An image
    * which is a filled circle, a ball, is shape number 2.
    */
   public static final int BALL =      2;

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  An image
    * which is a filled rectangle, a box, is shape number 3.
    */
   public static final int BOX =       3;

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  An image
    * which resembles a filled plus-sign, a cross, is shape number 4.
    */
   public static final int CROSS =     4;

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  An image
    * which is a filled triangle is shape number 5.
    */
   public static final int TRIANGLE =  5;

   /**
    * jLogo comes with a few basic shapes (images) that the turtle
    * may take on. Each is assigned a fixed shape number.  An image
    * which is a filled diamond is shape number 6.
    */
   public static final int DIAMOND =  6;


   /*
    * Provide a range of indicies for the users to add their own
    * images for the turtle.
    */

   /**
    * The minimum shape number that users can assign an image of
    * their own to.
    */
   public static final int FIRST_USER_SUPPLIED_SHAPE = 32;

   /**
    * The number of shape numbers reserved for users to assign their
    * own images to.
    */
   public static final int NUM_USER_SUPPLIED_SHAPES = 96;

   /**
    * The maximum shape number that users can assign an image of
    * their own to.
    */
   public static final int LAST_USER_SUPPLIED_SHAPE =   FIRST_USER_SUPPLIED_SHAPE
                                                      + (NUM_USER_SUPPLIED_SHAPES-1);




   // 
   // Variables with class-wide visibility (scope)
   // --------- ---- ---------- ---------- -------

   /*
    * Position of the mouse when it was last clicked.
    */
   //private int mouseX, mouseY;


   /*
    * Component where drawing takes place.
    */
   private TGCanvas canvas;


   /*
    * Object that does the drawing.
    */
   private Sprite turtle;


   /*
    * Array of org.bfoit.tg.SpritePixels objects, images/shapes that Sprites
    * can take on supplied by a Logo program, see loadshape(). 
    */
   private static SpritePixels[] userSuppliedImages;
   static { userSuppliedImages = new SpritePixels[NUM_USER_SUPPLIED_SHAPES]; } 



   //
   // Constructors
   // ------------

   /**
    * Create and return an AWT Frame which contains a
    * turtle graphics canvas.  It comes with a full set
    * of methods (actually wrappers for org.bfoit.tg.Sprite methods)
    * for performing Logo-like turtle graphics stuff.
    * It has a default size of symbolic constants
    * DEFAULT_CANVAS_HEIGHT and DEFAULT_CANVAS_WIDTH.
    */
   public TurtleGraphicsWindow()
   { this( DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT ); }


   /**
    * Create and return an AWT Frame which contains a
    * turtle graphics Canvas.  It comes with a full set
    * of methods (actually wrappers for org.bfoit.tg.Sprite methods)
    * for performing Logo-like turtle graphics stuff.
    * Its size is provided as parameters.
    */
   public TurtleGraphicsWindow( int width, int height )
   {
      addWindowListener( this );
      setTitle( "TurtleGraphicsWindow" );
      canvas = new TGCanvas( width, height );
      canvas.setBackground( Color.white );
      canvas.addKeyHandler( this );
      canvas.addMouseHandler( this );
      this.add( "Center", canvas );
      pack();
      Insets insets = getInsets();
      width += insets.left + insets.right;
      height += insets.top + insets.bottom;
      setPreferredSize( new Dimension(width, height) );
      pack();
      setVisible(true);
      canvas.requestFocusInWindow();
      turtle = new Sprite( canvas );
   }



   //
   // org.bfoit.tg.TGKeyHandler Interface Methods
   // ------------ --------- -------


   /**
    * org.bfoit.tg.TurtleGraphicsWindow implements the org.bfoit.tg.TGKeyHandler interface
    * and registers with org.bfoit.tg.TGCanvas to receive keyboard key events.
    * <p>
    * Override this method to do something when a key on the
    * keyboard is pressed while the graphics canvas is active,
    * has focus.
    * @param keyNum key identifier - either a character or an
    *               action key code (e.g., arrow keys)
    * @see TGKeyHandler#ALT
    * @see TGKeyHandler#CONTROL
    * @see TGKeyHandler#DOWN_ARROW
    * @see TGKeyHandler#LEFT_ARROW
    * @see TGKeyHandler#RIGHT_ARROW
    * @see TGKeyHandler#SHIFT
    * @see TGKeyHandler#UP_ARROW
    */
   public void tgKeyPressed( int keyNum )
   { keypressed(keyNum); }


   /**
    * org.bfoit.tg.TurtleGraphicsWindow implements the org.bfoit.tg.TGKeyHandler interface
    * and registers with org.bfoit.tg.TGCanvas to receive keyboard key events.
    * <p>
    * Override this method to do something when a key which
    * generates a character is pressed and released while the
    * graphics canvas is active, has focus.
    * @param keyNum key identifier - either a character or an
    *               action key code (e.g., arrow keys)
    * @see TGKeyHandler#ALT
    * @see TGKeyHandler#CONTROL
    * @see TGKeyHandler#DOWN_ARROW
    * @see TGKeyHandler#LEFT_ARROW
    * @see TGKeyHandler#RIGHT_ARROW
    * @see TGKeyHandler#SHIFT
    * @see TGKeyHandler#UP_ARROW
    */
   public void tgKeyReleased( int keyNum ) { }


    /**
    * org.bfoit.tg.TurtleGraphicsWindow implements the org.bfoit.tg.TGKeyHandler interface
    * and registers with org.bfoit.tg.TGCanvas to receive keyboard key events.
    * <p>
    * Override this method to do something when a key on the
    * keyboard is released while the graphics canvas is active,
    * has focus.
    * @param keyChar a character generated by a keyboard key
    */
   public void tgKeyTyped( char keyChar ) { }


    //
   // org.bfoit.tg.TGMouseHandler Interface Methods
   // -------------- --------- -------


   /**
    * org.bfoit.tg.TurtleGraphicsWindow implements the org.bfoit.tg.TGMouseHandler interface
    * and registers with org.bfoit.tg.TGCanvas to receive mouse events.
    * <p>
    * Override this method to do something when the left mouse
    * button is clicked in the graphics canvas while it is active,
    * has focus.
    *
    * @param x the X-coordinate where mouse was clicked
    * @param y the Y-coordinate where mouse was clicked
    * @see #mousex
    * @see #mousey
    * @see #tgMouseMoved
    */
   public void tgMouseClicked( int x, int y )
   { }


    /**
    * org.bfoit.tg.TurtleGraphicsWindow implements the org.bfoit.tg.TGMouseHandler interface
    * and registers with org.bfoit.tg.TGCanvas to receive mouse events.
    * <p>
    * Override this method to do something when the mouse is moved
    * within the graphics canvas while it is active, has focus.
    *
    * @param x the X-coordinate where mouse was clicked.
    * @param y the Y-coordinate where mouse was clicked.
    * @see #mousex
    * @see #mousey
    * @see #tgMouseClicked
    */
   public void tgMouseMoved( int x, int y )
   { }


    //
   // WindowListener Interface Methods
   // -------------- --------- -------

   public void windowActivated(WindowEvent we) {}
   public void windowClosed(WindowEvent we) {}
   public void windowClosing(WindowEvent we) { System.exit(0); }
   public void windowDeactivated(WindowEvent we) {}
   public void windowDeiconified(WindowEvent we) {}
   public void windowIconified(WindowEvent we) {}
   public void windowOpened(WindowEvent we) {}



  /*
   * TurtleGraphics Methods
   *
   * Provided to allow the class extending org.bfoit.tg.TurtleGraphicsWindow
   * to invoke them without being forced to obtain and use the
   * org.bfoit.tg.Sprite object to reference/invoke them.
   */


  /**
   * Move the turtle backwards along its current heading.  If the
   * pen is currently in the DOWN position, a line is drawn.
   *
   * Long name for bk().  Both spellings need to provided for
   * compatibility.
   *
   * @param steps number of pixels to move
   * @see #bk
   */
   public void back( int steps )
   { turtle.bk( steps ); }

  /**
   * Move the turtle backwards along its current heading.  If the
   * pen is currently in the DOWN position, a line is drawn.
   *
   * Abbreviation for back().  Both spellings need to
   * provided for compatibility.
   *
   * @param steps Number of pixels to move
   * @see #back
   */
  public void bk( int steps )
  { turtle.bk( steps ); }


  /**
   * Clear the graphics canvas area of the org.bfoit.tg.TurtleGraphicsWindow.
   *
   * Note: Clean does not change the current position of
   * the turtle, its heading, the size of the pen it is
   * drawing with and/or the color of the pen it is
   * drawing with.
   */
   public void clean()
   { canvas.clean(); }


   /**
    * Return an array of the unique colors this turtle's image is positioned over.
    * <p>
    * @see #pencolor
    * @see #setbg
    * @see #setpc
    * @see #setpencolor
    */
   public int[] colorsunder()
   { return turtle.colorsunder(); }


   /**
    * Return the color under the center of the turtle's image.
    *
    * @see #pencolor
    * @see #setpc
    * @see #setpencolor
    */
   public int colorunder()
   { return turtle.colorunder(); }


   /**
    * Fill a bounded area in the graphics canvas.
    *
    * The current pixel, and any of its neighbors that are the
    * same color as it (and any of their neighbors that are the
    * same color as it, etc...) are changed to the current color.
    */
   public void fill()
   { turtle.fill(); }


  /**
   * Move the turtle forward along its current heading.  If the
   * pen is currently in the DOWN position, a line is drawn.
   *
   * Abbreviation for forward().  Both spellings need to be
   * provided for compatibility.
   *
   * @param steps Number of pixels (in this implementation) to take.
   * @see #forward
   */
  public void fd( int steps )
  { turtle.fd( steps ); }

  /**
   * Move the turtle forward along its current heading.  If the
   * pen is currently in the DOWN position, a line is drawn.
   *
   * Long name for fd().  Both spellings need to provided for
   * compatibility.
   *
   * @param steps number of pixels to move
   * @see #fd
   */
  public void forward( int steps )
  { turtle.fd( steps ); }


   /**
    * Return the width of the provided String, when rendered in
    * the current font, in turtle steps (pixels).
    *
    * @param label text to be measured
    * @see #label
    * @see #setlabelfont
    * @see #setlabelheight
    */
   public int labelwidth( String label )
   { return turtle.getLabelWidth( label ); }


  /**
   * Return the turtle's heading in Logo (turtle space) degrees.
   *
   * @see #seth
   * @see #setheading
   */
  public double heading()
  { return( turtle.heading() ); }


  /**
   * Hide the turtle; make it invisible.
   *
   * Long name for ht().  Both spellings need to provided for
   * compatibility.
   *
   * @see #ht
   * @see #showturtle
   * @see #st
   */
  public void hideturtle()
  { turtle.ht(); }


  /**
   * Move the turtle to the center of the display.  If the
   * pen is in the DOWN position, a line is drawn.
   *
   * Home is equivilent to setxy( 0, 0 )
   *
   * @see #setxy
   */
  public void home()
  { turtle.home(); }


  /**
   * Hide the turtle; make it invisible.
   *
   * Abbreviation for hideturtle().  Both spellings need to
   * provided for compatibility.
   *
   * @see #hideturtle
   * @see #showturtle
   * @see #st
   */
  public void ht()
  { turtle.ht(); }


  /**
   * Return the current status of the pen.
   *
   * Return true if the turtle's pen is down or
   * false if it in the up position.
   *
   * @see #pendown
   * @see #penup
   * @see #pd
   * @see #pu
   */
  public boolean ispendown() { return( turtle.ispendown() ); }


   /**
    * Empty method declaration which child programs override to
    * process keyboard key events.
    *
    * @param keyNum Character on the keyboard that was pressed
    *               while the graphics canvas has its focus.
    *
    * @deprecated
    * @see #tgKeyPressed
    * @see #tgKeyReleased
    * @see #tgKeyTyped
    */
   public void keypressed(int keyNum) {}


   /**
    * Draws a String of characters on the graphics canvas.
    * The text is drawn in the current pen's color,
    * starting at the current position of the turtle.
    *
    * The text is always drawn in the standard
    * horizontal manner, i.e., the heading of the
    * turtle is ignored.
    *
    * @param text characters to be drawn on the graphics canvas
    *
    * @see #labelwidth
    * @see #setlabelfont
    * @see #setlabelheight
    */
   public void label( String text )
   { turtle.label( text ); }


   /**
    * Rotate the turtle counterclockwise by the specified
    * angle, measured in degrees.
    *
    * @param degrees angle to rotate the turtle's heading
    * @see #lt
    */
   public void left( int degrees )
   { turtle.lt( degrees ); }


   /**
    * Draw a picture from a file onto the background of
    * the graphics canvas. Returns true if successful,
    * otherwise false.
    *
    * @param fileName String that identifies the source
    *                 image file
    * @see #clean
    */
   public boolean loadpicture( String fileName )
   {
      Image pictureImage = TGFileIO.getImage( fileName );
      if ( pictureImage == null )
         return false;
      canvas.loadPicture( pictureImage );
      return true;
   }


   /**
    * Load an image file; it can then be used for a turtle's
    * shape, its image. Returns true if successful, otherwise
    * false.
    *
    * @param fileName String that identifies the source
    *                 image file
    * @param shapeNum an integer that is between
    *                 FIRST_USER_SUPPLIED_SHAPE and
    *                 LAST_USER_SUPPLIED_SHAPE, inclusive
    *                 This number can then be used with
    *                 setshape() to change the turtle's
    *                 appearance
   * @see #setshape
    */
   public boolean loadshape( String fileName, int shapeNum )
   {
      PixelRectangle pixRect = TGFileIO.getPixRect( fileName );
      if ( pixRect == null )
         return false;
      int shapeIdx = shapeNum - FIRST_USER_SUPPLIED_SHAPE;
      if ( shapeNum < 0 || shapeNum > NUM_USER_SUPPLIED_SHAPES )
         return false;
      userSuppliedImages[shapeIdx] = new UserTurtle( pixRect );
      return true;
   }



   /**
    * Rotate the turtle counterclockwise by the specified
    * angle, measured in degrees.
    *
    * Abbreviation for left().  Both spellings need
    * to provided for compatibility.
    *
    * @param degrees Angle to change turtle's heading by.
    * @see #left
    */
   public void lt( int degrees ) { turtle.lt( degrees ); }


  /**
   * Return the x-coordinate where the mouse was last
   * moved or clicked within the graphics canvas.
   *
   * @see #mousey
   * @see #tgMouseClicked
   * @see #tgMouseMoved
   */
  public int mousex()
  { return( canvas.mousex() ); }


  /**
   * Return the Y-coordinate where the mouse was last
   * moved or clicked within the graphics canvas.
   *
   * @see #mousex
   * @see #tgMouseClicked
   * @see #tgMouseMoved
   */
  public int mousey()
  { return( canvas.mousey() ); }


  /**
   * Lower the turtle's pen into the down position.
   *
   * When the turtle moves, it will leave a trace from its
   * current position to its destination (its new position).
   * @see #ispendown
   * @see #pendown
   * @see #pu
   * @see #penup
   */
  public void pd()
  { turtle.pd(); }


   /**
    * Return the color the pen is currently drawing in.
    * <P>
    * @see #setpc
    * @see #setpencolor
    */
   public int pencolor()
   { return turtle.pencolor(); }


  /**
   * Lower the turtle's pen into the down position.
   *
   * When the turtle moves, it will leave a trace from its
   * current position to its destination (its new position).
   * @see #ispendown
   * @see #pd
   * @see #penup
   * @see #pu
   */
  public void pendown()
  { turtle.pd(); }


  /**
   * Lift the turtle's pen into the up position.
   *
   * When the turtle moves, it will leave no trace.
   * @see #ispendown
   * @see #pd
   * @see #pendown
   * @see #pu
   */
  public void penup()
  { turtle.pu(); }


  /**
   * Lift the turtle's pen into the up position.
   *
   * When the turtle moves, it will leave no trace.
   * @see #ispendown
   * @see #pd
   * @see #pendown
   * @see #penup
   */
  public void pu() { turtle.pu(); }


  /**
   * Rotate the turtle clockwise by the specified angle,
   * measured in degrees.
   *
   * @param degrees angle to rotate the turtle's heading
   * @see #rt
   */
  public void right( int degrees )
  { turtle.rt( degrees ); }


  /**
   * Rotate the turtle clockwise by the specified angle,
   * measured in degrees.
   *
   * Abbreviation for right().  Both spellings need
   * to provided for compatibility.
   *
   * @param degrees angle to rotate the turtle's heading
   * @see #right
   */
  public void rt( int degrees )
  { turtle.rt( degrees ); }


   /**
    * Set the background color of the graphics canvas.
    * <p>
    * *NOTE* a side-effect of changing the background color
    * is that all current graphics is cleared.
    * <p>
    * <pre>
    * Number Color        Number Color        Number Color
    * ------ ----------   ------ ----------   ------ ---------
    *    0   black          11   aqua           22   gold
    *    1   blue           12   salmon         23   lightgray
    *    2   green          13   purple         24   peru
    *    3   cyan           14   orange         25   wheat
    *    4   red            15   grey           26   palegreen
    *    5   magenta        16   navy           27   lightblue
    *    6   yellow         17   skyblue        28   khaki
    *    7   white          18   lime           29   pink
    *    8   brown          19   steelblue      30   lawngreen
    *    9   light brown    20   chocolate      31   olive
    *   10   forest         21   purple
    * </pre>
    * <p>
    * Color numbers greater than 31 will be treated as RGB
    * values. Assuming bits are numbered 0-31, left to right,
    * the red component is in bits 8-15, the green component
    * in bits 16-23, and the blue component in bits 24-31. The
    * actual color used in rendering will depend on finding
    * the best match given the color space available for a
    * given display.
    * @see #clean
    * @see #loadpicture
    * @see #setpencolor
    * @see #BLACK
    * @see #BLUE
    * @see #AQUA
    * @see #BROWN
    * @see #CHOCOLATE
    * @see #CYAN
    * @see #GOLD
    * @see #FOREST
    * @see #GRAY
    * @see #GREEN
    * @see #KHAKI
    * @see #LAWNGREEN
    * @see #LIGHTBLUE
    * @see #LIGHTGRAY
    * @see #LIME
    * @see #MAGENTA
    * @see #NAVY
    * @see #OLIVE
    * @see #ORANGE
    * @see #PALEGREEN
    * @see #PERU
    * @see #PINK
    * @see #PURPLE
    * @see #RED
    * @see #SALMON
    * @see #SKYBLUE
    * @see #STEELBLUE
    * @see #TAN
    * @see #VIOLET
    * @see #WHEAT
    * @see #WHITE
    * @see #YELLOW
    */
   public void setbg( int logoColor )
   { canvas.setbg( logoColor ); }


  /**
   * Turns the turtle to the specified absolute heading.
   * The heading is specified in degrees (units of 1/360th
   * of a circle) with 0 being North (+Y axis), increasing
   * clockwise.  So, East is 90 degrees, South is 180 degrees,
   * and West is 270 degrees.
   *
   * Abbreviation for setheading().  Both spellings
   * need to provided for compatibility.
   *
   * @param degrees number of 1/360ths increments clockwise
   *                from the positive Y axis
   * @see #setheading
   */
  public void seth( int degrees )
  { turtle.seth( degrees ); }

  /**
   * Turns the turtle to the specified absolute heading.
   * The heading is specified in degrees (units of 1/360th
   * of a circle) with 0 being North (+Y axis), increasing
   * clockwise.  So, East is 90 degrees, South is 180 degrees,
   * and West is 270 degrees.
   * @param degrees number of 1/360ths increments clockwise
   *                from the positive Y axis
   * @see #seth
   */
  public void setheading( int degrees )
  { turtle.seth( degrees ); }


   /**
    * Set the font for LABELs, text drawn on the graphics
    * canvas by the turtle.
    *
    * @param fontNumber an integer representing a font face
    *                   and font style
    * <p>
    * <pre>
    * Number   Font         Style
    * ------   ----------   -----------
    *    0     Courier      Plain
    *    1     Courier      Bold
    *    2     Courier      Italic
    *    3     Courier      Bold Italic
    *    4     Sans Serif   Plain
    *    5     Sans Serif   Bold
    *    6     Sans Serif   Italic
    *    7     Sans Serif   Bold Italic
    *    8     Serif        Plain
    *    9     Serif        Bold
    *   10     Serif        Italic
    *   11     Serif        Bold Italic
    * </pre>
    * @see #label
    * @see #labelwidth
    * @see #setlabelheight
    * @see #COURIER
    * @see #COURIER_BOLD
    * @see #COURIER_ITALIC
    * @see #COURIER_BOLD_ITALIC
    * @see #SANS_SERIF
    * @see #SANS_SERIF_BOLD
    * @see #SANS_SERIF_ITALIC
    * @see #SANS_SERIF_BOLD_ITALIC
    * @see #SERIF
    * @see #SERIF_BOLD
    * @see #SERIF_ITALIC
    * @see #SERIF_BOLD_ITALIC
    */
   public void setlabelfont( int fontNumber )
   { turtle.setlabelfont( fontNumber ); }


   /**
    * Set the height of the text drawn on the graphics canvas
    * with the label() method.
    *
    * @param size approximate number of pixels tall the
    *             uppercase characters should be (in the
    *             current font/style)
    * @see #label
    * @see #setlabelfont
    * @see #labelwidth
    */
  public void setlabelheight( int size )
  { turtle.setlabelheight( size ); }


   /**
    * Sets the color of the turtle's pen to the supplied number.
    * @param colorNum either a built-in Logo color number in
    *                 the range 0-31, else a 24-bit RGB color
    *                 value
    * <p>
    * <pre>
    * Number Color        Number Color        Number Color
    * ------ ----------   ------ ----------   ------ ---------
    *    0   black          11   aqua           22   gold
    *    1   blue           12   salmon         23   lightgray
    *    2   green          13   purple         24   peru
    *    3   cyan           14   orange         25   wheat
    *    4   red            15   grey           26   palegreen
    *    5   magenta        16   navy           27   lightblue
    *    6   yellow         17   skyblue        28   khaki
    *    7   white          18   lime           29   pink
    *    8   brown          19   steelblue      30   lawngreen
    *    9   light brown    20   chocolate      31   olive
    *   10   forest         21   purple
    * </pre>
    * <p>
    * Color numbers greater than 31 will be treated as RGB
    * values. Assuming bits are numbered 0-31, left to right,
    * the red component is in bits 8-15, the green component
    * in bits 16-23, and the blue component in bits 24-31. The
    * actual color used in rendering will depend on finding
    * the best match given the color space available for a
    * given display.
    * @see #setbg
    * @see #setpencolor
    * @see #BLACK
    * @see #BLUE
    * @see #AQUA
    * @see #BROWN
    * @see #CHOCOLATE
    * @see #CYAN
    * @see #GOLD
    * @see #FOREST
    * @see #GRAY
    * @see #GREEN
    * @see #KHAKI
    * @see #LAWNGREEN
    * @see #LIGHTBLUE
    * @see #LIGHTGRAY
    * @see #LIME
    * @see #MAGENTA
    * @see #NAVY
    * @see #OLIVE
    * @see #ORANGE
    * @see #PALEGREEN
    * @see #PERU
    * @see #PINK
    * @see #PURPLE
    * @see #RED
    * @see #SALMON
    * @see #SKYBLUE
    * @see #STEELBLUE
    * @see #TAN
    * @see #VIOLET
    * @see #WHEAT
    * @see #WHITE
    * @see #YELLOW
    */
  public void setpc( int colorNum )
  { turtle.setpc( colorNum ); }


   /**
    * Sets the color of the turtle's pen to the supplied number.
    *
    * @param colorNum either a built-in Logo color number in
    *                 the range 0-31, else a 24-bit RGB color
    *                 value
    * <p>
    * <pre>
    * Number Color        Number Color        Number Color
    * ------ ----------   ------ ----------   ------ ---------
    *    0   black          11   aqua           22   gold
    *    1   blue           12   salmon         23   lightgray
    *    2   green          13   purple         24   peru
    *    3   cyan           14   orange         25   wheat
    *    4   red            15   grey           26   palegreen
    *    5   magenta        16   navy           27   lightblue
    *    6   yellow         17   skyblue        28   khaki
    *    7   white          18   lime           29   pink
    *    8   brown          19   steelblue      30   lawngreen
    *    9   light brown    20   chocolate      31   olive
    *   10   forest         21   purple
    * </pre>
    * <p>
    * Color numbers greater than 31 will be treated as RGB
    * values. Assuming bits are numbered 0-31, left to right,
    * the red component is in bits 8-15, the green component
    * in bits 16-23, and the blue component in bits 24-31. The
    * actual color used in rendering will depend on finding
    * the best match given the color space available for a
    * given display.
    * @see #setbg
    * @see #setpc
    * @see #BLACK
    * @see #BLUE
    * @see #AQUA
    * @see #BROWN
    * @see #CHOCOLATE
    * @see #CYAN
    * @see #GOLD
    * @see #FOREST
    * @see #GRAY
    * @see #GREEN
    * @see #KHAKI
    * @see #LAWNGREEN
    * @see #LIGHTBLUE
    * @see #LIGHTGRAY
    * @see #LIME
    * @see #MAGENTA
    * @see #NAVY
    * @see #OLIVE
    * @see #ORANGE
    * @see #PALEGREEN
    * @see #PERU
    * @see #PINK
    * @see #PURPLE
    * @see #RED
    * @see #SALMON
    * @see #SKYBLUE
    * @see #STEELBLUE
    * @see #TAN
    * @see #VIOLET
    * @see #WHEAT
    * @see #WHITE
    * @see #YELLOW
    */
  public void setpencolor( int colorNum )
  { turtle.setpc( colorNum ); }


  /**
   * Sets the width of the turtle's pen to the supplied number.
   * @param width  small positive number; 1 (or less) results
   * in a single pixel line. increasing numbers incrementally
   * add approximately 2 extra pixels in width
   */
  public void setpensize( int width )
  { turtle.setpensize( width ); }
  
  public int pensize()
  { return turtle.pensize(); }


   /**
    * Sets the shape of the turtle - its pixel image. Returns
    * true if shape successfully set, else false failure.
    * <p>
    * @param shapeNum  small positive number for either a
    *                  built-in shape (see constants (e.g.,
    *                  BALL, BOX, etc...) or a user-supplied
    *                  image via loadShape() method
    * <p>
    * <pre>
    * Number  Shape
    * ------  --------
    *    0    Turtle
    *    1    Arrow
    *    2    Ball
    *    3    Box
    *    4    Cross
    *    5    Triangle
    *    6    Diamond
    * </pre>
    * @see #loadshape
    * @see #stamp
    * @see #ARROW
    * @see #BALL
    * @see #BOX
    * @see #CROSS
    * @see #DIAMOND
    * @see #TRIANGLE
    * @see #TURTLE
    * </pre>
    */
   public boolean setshape( int shapeNum )
   { return setshape( shapeNum, null ); }
  

   /**
    * Sets the shape of this org.bfoit.tg.Sprite - its pixel image.  Returns
    * true if shape successfully set, else false for failure.
    * <p>
    * @param shapeNum  small positive number; 0 for default
    *                  turtle image; see constants (e.g., BALL,
    *                  BOX, etc...) for other org.bfoit.tg.Sprite shapes
    * @param params  an optional int array containing sizing
    *                information hints, e.g. radius of a ball,
    *                width and height of a box, etc...
    * <p>
    * <pre>
    * Number  Shape      Optional Size Parameters
    * ------  --------   ------------------------
    *    0    Turtle
    *    1    Arrow      width, height
    *    2    Ball       diameter
    *    3    Box        width, height
    *    4    Cross      width, height
    *    5    Triangle   width, height
    *    6    Diamond    width, height
    * </pre>
    * @see #loadshape
    * @see #stamp
    * @see #ARROW
    * @see #BALL
    * @see #BOX
    * @see #CROSS
    * @see #DIAMOND
    * @see #TRIANGLE
    * @see #TURTLE
    */
   public boolean setshape( int shapeNum, int[] params )
   {
      if ( shapeNum >= FIRST_USER_SUPPLIED_SHAPE && shapeNum <= LAST_USER_SUPPLIED_SHAPE )
      {
         if ( userSuppliedImages[shapeNum - FIRST_USER_SUPPLIED_SHAPE] == null )
            return false;
         turtle.setshape( userSuppliedImages[shapeNum - FIRST_USER_SUPPLIED_SHAPE] );
         return true;
      }
      else
         return turtle.setshape( shapeNum, params );
   }
  

   /**
    * Sets the shape of this org.bfoit.tg.Sprite - its pixel image.
    * <p>
    * @param newSpritePixels 
    * <p>
    * @see #loadshape
    */
   public void setshape( SpritePixels newSpritePixels )
   { turtle.setshape( newSpritePixels ); }


   /**
    * Move the turtle to an absolute display position.
    *
    * Move the turtle horizontally to a new location
    * specified as an X coordinate argument.
    * @param newX the X-coordinate of destination
    * @see #home
    * @see #setxy
    * @see #sety
    */
   public void setx( int newX )
   { turtle.setx( newX ); }


   /**
    * Move the turtle to an absolute display position.
    *
    * Move the turtle to the x and y coordinates provided
    * as arguments.
    * @param newX the X-coordinate of destination
    * @param newY the Y-coordinate of destination
    * @see #home
    * @see #setx
    * @see #sety
    */
   public void setxy( int newX, int newY )
   { turtle.setxy( newX, newY ); }


   /**
    * Move this org.bfoit.tg.Sprite to an absolute display position.
    * <p>
    * Move this org.bfoit.tg.Sprite to the x and y coordinates provided in the
    * org.bfoit.tg.TGPoint parameter.
    * @param newPt a org.bfoit.tg.TGPoint objext containing the X-coordinate
    *              and Y-coordinate of destination
    * @see #home
    * @see #setx
    * @see #sety
    */
   public void setxy( TGPoint newPt )
   { turtle.setxy( newPt ); }


   /**
    * Move the turtle to an absolute display position.
    *
    * Move the turtle vertically to a new location
    * specified as an Y coordinate argument.
    * @param newY the Y-coordinate of destination
    * @see #home
    * @see #setx
    * @see #setxy
    */
   public void sety( int newY )
   { turtle.sety( newY ); }


   /**
    * Return the height of the image of the turtle,
    * as it appears for its current heading.
    *
    * @see #setshape(int)
    * @see #setshape(int, int[])
    */
   public int shapeheight()
   { return turtle.getSpritePixels().getHeight(); }


   /**
    * Return the width of the image of the turtle,
    * as it appears for its current heading.
    *
    * @see #setshape(int)
    * @see #setshape(int, int[])
    */
   public int shapewidth()
   { return turtle.getSpritePixels().getWidth(); }


   /**
    * Return true if this turtle's image is being displayed on
    * the graphics canvas, else return false.
    * <p>
    * @see #hideturtle
    * @see #ht
    * @see #showturtle
    * @see #st
    */
   public boolean shownp()
   { return turtle.shownp(); }


   /**
    * Show the turtle; make it visible.
    *
    * Long name for st().  Both spellings need to
    * provided for compatibility.
    *
    * @see #hideturtle
    * @see #ht
    * @see #st
    */
   public void showturtle()
   { turtle.st(); }


   /**
    * Show the turtle; make it visible.
    *
    * Abbreviation for showturtle().  Both spellings need to
    * provided for compatibility.
    *
    * @see #hideturtle
    * @see #ht
    * @see #showturtle
    */
   public void st()
   { turtle.st(); }


   /**
    * Draw the turtle's image onto the graphics canvas.
    *
    * @see #setshape(int)
    * @see #setshape(int, int[])
    */
   public void stamp()
   { turtle.stamp(); }


   /**
    * Return the turtle's X-coordinate 
    * @see #setxy
    * @see #setx
    * @see #sety
    * @see #ycor
    */
   public int xcor()
   { return( (int) turtle.xcor() ); }


   /**
    * Return the turtle's Y-coordinate
    * @see #setxy
    * @see #setx
    * @see #sety
    * @see #xcor
    */
   public int ycor()
   { return( (int) turtle.ycor() ); }


} // end class org.bfoit.tg.TurtleGraphicsWindow
