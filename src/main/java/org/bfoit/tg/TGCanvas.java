package org.bfoit.tg;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.awt.Rectangle;
import java.lang.Math;


/**
 * This class is an implementation of a graphics canvas window for TG.
 * All graphics (pixels painted) are maintained in an Image for redisplay
 * in the event of resizing.
 * <p>
 * Other features of org.bfoit.tg.TGCanvas are:
 * <p>
 * - the coordinate space is the traditional mathematics; [0,0] is
 *   in the middle of the graphics window.
 * <p>
 * - KeyListener method invocations are handled and may be passed on.
 * <p>
 * - MouseListener.mouseReleased() and MouseMotionListener.mouseMoved()
 *   are captured and can be passed on.
 * <p>
 * - higher-level functionality (i.e., flood-fill provided). 
 * <p>
 * - in-memory Image maintained with buffered operations to improve
 *   graphics display performance. 
 * <p>
 * @author Guy Haas
 */
public class TGCanvas extends Component
                      implements   FocusListener, KeyListener,
                    /*implements*/ MouseListener, MouseMotionListener
{


   //
   // Symbolic Constants
   // -------- ---------

   /**
    * Class name as a String.
    */
   private static final String CLASS_NAME = "org.bfoit.tg.TGCanvas";


   /**
    * Turtle heading for the positive Y axis.
    *
    * The Logo turtle's heading in degrees does not match the
    * standard mathematics notion of having the positive X axis
    * as zero, with degrees increasing counter-clockwise. Logo
    * has the positive Y axis as zero with degrees increasing
    * clockwise. The use of abstract symbolic constants for
    * north, east, south, and west help.
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
    */
   public static final int WEST =  270;


   /**
    * The SETPENCOLOR command's input is a built-in color
    * number or an RGB value. Built-in colors numbers are in
    * range of 0-31 with 0 for black.
    */
   public static final int BLACK =      0;
   public static final int BLUE =       1;
   public static final int GREEN =      2;
   public static final int CYAN =       3;
   public static final int RED =        4;
   public static final int MAGENTA =    5;
   public static final int YELLOW =     6;
   public static final int WHITE =      7;
   public static final int BROWN =      8;
   public static final int TAN =        9;
   public static final int FOREST =    10;
   public static final int AQUA =      11;
   public static final int SALMON =    12;
   public static final int VIOLET =    13;
   public static final int ORANGE =    14;
   public static final int GRAY =      15;
   public static final int NAVY  =     16;
   public static final int SKYBLUE =   17;
   public static final int LIME =      18;
   public static final int STEELBLUE = 19;
   public static final int CHOCOLATE = 20;
   public static final int PURPLE =    21;
   public static final int GOLD =      22;
   public static final int LIGHTGRAY = 23;
   public static final int PERU =      24;
   public static final int WHEAT =     25;
   public static final int PALEGREEN = 26;
   public static final int LIGHTBLUE = 27;
   public static final int KHAKI =     28;
   public static final int PINK =      29;
   public static final int LAWNGREEN = 30;
   public static final int OLIVE =     31;

   /**
    * The maximum value for a Logo color which require conversion
    * to RGB colors used by Java.
    */
   public static final int MAX_LOGO_COLOR = 31;


   /**
    * All derivations of Logo have a basic, minimum set of colors
    * that are specified by numbers.  jLogo supports a set of sixteen,
    * compatible with Brian Harvey's Berkeley Logo.
    */
   public static final Color[] LOGO_COLORS = new Color[32];
   static
   {
      LOGO_COLORS[ BLACK ]     = Color.black;
      LOGO_COLORS[ BLUE ]      = Color.blue;
      LOGO_COLORS[ GREEN ]     = Color.green;
      LOGO_COLORS[ CYAN ]      = Color.cyan;
      LOGO_COLORS[ RED ]       = Color.red;
      LOGO_COLORS[ MAGENTA ]   = Color.magenta;
      LOGO_COLORS[ YELLOW ]    = Color.yellow;
      LOGO_COLORS[ WHITE ]     = Color.white;
      LOGO_COLORS[ BROWN ]     = new Color(150,  90,  55);
      LOGO_COLORS[ TAN ]       = new Color(210, 180, 140);
      LOGO_COLORS[ FOREST ]    = new Color( 34, 139,  34);
      LOGO_COLORS[ AQUA ]      = new Color(120, 187, 187);
      LOGO_COLORS[ SALMON ]    = new Color(250, 128, 114);
      LOGO_COLORS[ VIOLET ]    = new Color(238, 130, 238);
      LOGO_COLORS[ ORANGE ]    = new Color(255, 165,   0);
      LOGO_COLORS[ GRAY ]      = new Color(128, 128, 128);
      LOGO_COLORS[ NAVY ]      = new Color(  0,   0, 128);
      LOGO_COLORS[ SKYBLUE ]   = new Color(  0, 191, 255);
      LOGO_COLORS[ LIME ]      = new Color( 50, 205,  50);
      LOGO_COLORS[ STEELBLUE ] = new Color( 70, 130, 180);
      LOGO_COLORS[ CHOCOLATE ] = new Color(210, 105,  30);
      LOGO_COLORS[ PURPLE ]    = new Color(128,   0, 128);
      LOGO_COLORS[ GOLD ]      = new Color(255, 215,   0);
      LOGO_COLORS[ LIGHTGRAY ] = new Color(211, 211, 211);
      LOGO_COLORS[ PERU ]      = new Color(205, 133,  63);
      LOGO_COLORS[ WHEAT ]     = new Color(245, 222, 179);
      LOGO_COLORS[ PALEGREEN ] = new Color(152, 251, 152);
      LOGO_COLORS[ LIGHTBLUE ] = new Color(173, 216, 230);
      LOGO_COLORS[ KHAKI ]     = new Color(240, 230, 140);
      LOGO_COLORS[ PINK ]      = new Color(255, 192, 203);
      LOGO_COLORS[ LAWNGREEN ] = new Color(124, 252,   0);
      LOGO_COLORS[ OLIVE ]     = new Color(128, 128,   0);
   };

 
   /*
    * Initial color of the graphics canvas' background.
    */
   private static final Color INIT_BACKGROUND = Color.white;


   /*
    * Initial color of the pen used to draw on the graphics canvas.
    */
   private static final Color INIT_PEN_COLOR = Color.black;


   /*
    * The graphics canvas image dimensions should be odd to allow for
    * zero at center and an equal number of pixels above/below and
    * left/right of the center.
    */
   private static final int GI_HEIGHT = 1201;
   private static final int GI_WIDTH = 2001;


   /*
    * initial size of pending graphics operations buffer (an array)
    */
   private final static int INIT_NUM_GRAFOPS = 1000;


   /*
    * Incremental growth amount when need to expand the graphics
    * operations array.
    */
   private final static int INCR_NUM_GRAFOPS = 500;


   /*
    * Initial pen size, its width in pixels.
    */
   private static final int INIT_PEN_SIZE = 2;


   /*
    * Number of org.bfoit.tg.TGKeyHandler objects supported.  As of v.9.29 (09/17/08)
    * only one object (TGDriver) registers for callbacks.
    */
   private final static int NUM_KEYHANDLERS = 3;


   /*
    * Number of org.bfoit.tg.TGMouseHandler objects supported. As of v.9.29 (09/17/08)
    * only one object (TGDriver) registers for callbacks.
    */
   private final static int NUM_MOUSEHANDLERS = 3;

   
   /*
    * The following STATES are needed by paint() due to the async model
    * provided by drawImage()
    */
   private static final int PAINT_REFRESH = 0;
   private static final int PAINT_DRAW_GRAPHICS = 1;
   private static final int PAINT_ERASE_TURTLES = 2;
   private static final int PAINT_DRAW_TURTLES = 3;



   //
   // Class Fields
   // ----- ------

   /*
    * used to determine when to pass on mouseMoved Events to TGDriver
    */
   private boolean gotFocus;

   private int canvasHeight;
   private int canvasWidth;

   /*
    * number of graphics operations that are buffered in graphicsOps array
    */
   private int numGraphicsOps;

   /*
    * position of mouse when last clicked or moved within the graphics canvas
    */
   private int mouseX, mouseY;

   /*
    * used in paint() to determine what needs to be done. drawImage() does
    * not necessarily complete and thus forces follow-up passes of paint()
    */
   private int paintState;

   /*
    * used in paint() to determine which org.bfoit.tg.Sprite is being erased/painted
    */
   private int paintSpriteNum;

   /*
    * these AWT graphics coordinates will be [0,0] for the org.bfoit.tg.TGCanvas
    * coordinate space. *NOTE* i tried making these "float" but when they
    * had a .5 fractional part, pixel choice by AWT was poorer. i ended
    * up with lines with endpoints one pixel apart instead of straight
    */
   private int xCenter, yCenter;

   private Color backgroundColor;
   private Image backgroundPicture;

   /*
    * in-memory Image for the composite graphics - all the stuff on the
    * display except for the org.bfoit.tg.Sprite(s)
    */
   private Image graphicsImage;

   /*
    * clipRects used to draw org.bfoit.tg.Sprite images
    */
   private Rectangle[] spriteClipRect;

   /*
    * a list/queue of Graphics operations pending processing
    */
   private TGGraphicsOp[] graphicsOps;

   /*
    * array of Sprites that want to be displayed 
    */
   private Sprite[] sprites;

   /*
    * Objects that want their java.awt.event.KeyListener.keyXxx() method
    * invoked when org.bfoit.tg.TGCanvas' KeyListener interface methods are invoked.
    */
   private TGKeyHandler[] keyHandlers;

   /*
    * Objects that want their tgMouseClicked() method invoked when our
    * MouseListener interface: mouseReleased() method is invoked,
    * propagating mouse stuff
    */
   private TGMouseHandler[] tgMouseHandlers;



   //
   // Constructors
   // ------------

   /**
    * Instantiate a turtle graphics canvas with initial width of 700 turtle
    * steps and a height of 400 turtle steps.
    */
   public TGCanvas() { this( 700, 400 ); }

   /**
    * Instantiate a turtle graphics canvas of a specified width and height.
    */
   public TGCanvas( int width, int height )
   {
      canvasWidth = width;
      canvasHeight = height;
      super.setSize( canvasWidth, canvasHeight );
      addFocusListener(this);
      addKeyListener(this);
      addMouseListener(this);
      addMouseMotionListener(this);
      setFocusTraversalKeysEnabled(true);
      xCenter = canvasWidth / 2;
      yCenter = canvasHeight / 2;
      backgroundColor = INIT_BACKGROUND;
      graphicsOps = new TGGraphicsOp[ INIT_NUM_GRAFOPS ];
      numGraphicsOps = 0;
      keyHandlers = new TGKeyHandler[ NUM_KEYHANDLERS ];
      tgMouseHandlers = new TGMouseHandler[ NUM_MOUSEHANDLERS ];
      spriteClipRect = new Rectangle[ Params.MAX_TURTLES ];
      sprites = new Sprite[ Params.MAX_TURTLES ];
      paintState = PAINT_REFRESH;
      gotFocus = false;

   } // end org.bfoit.tg.TGCanvas()



   //
   // FocusListener Interface Methods
   // ------------- --------- -------

   /**
    * Invoked when this Component gets focus. Note that this has
    * happened, that the graphics canvas now gets keyboard and
    * mouse events.
    */
   public void focusGained(FocusEvent e)
   { gotFocus = true; }


   /**
    * Invoked when this Component loses focus. Note that this has
    * happened.
    */
   public void focusLost(FocusEvent e)
   { gotFocus = false; }



   //
   // KeyListener interface methods
   // ----------- --------- -------

   /*
    * Convert a KeyEvent.getKeyCode() value into a documented TG
    * equivilent if one exists. Otherwise, return the parameter
    * unchanged.
    */
   private int mapToTGKeys( int keyCode )
   {
      int tgKeyNum = 0;
      switch ( keyCode )
      {
         case KeyEvent.VK_ALT:
            tgKeyNum = TGKeyHandler.ALT;
            break;
         case KeyEvent.VK_CONTROL:
            tgKeyNum = TGKeyHandler.CONTROL;
            break;
            case KeyEvent.VK_DOWN:
            tgKeyNum = TGKeyHandler.DOWN_ARROW;
            break;
         case KeyEvent.VK_LEFT:
            tgKeyNum = TGKeyHandler.LEFT_ARROW;
            break;
         case KeyEvent.VK_RIGHT:
            tgKeyNum = TGKeyHandler.RIGHT_ARROW;
            break;
         case KeyEvent.VK_SHIFT:
            tgKeyNum = TGKeyHandler.SHIFT;
            break;
         case KeyEvent.VK_UP:
            tgKeyNum = TGKeyHandler.UP_ARROW;
            break;
         default:
            tgKeyNum = keyCode;
      }
      return tgKeyNum;
   }


   /**
    * Invoked when a key has been pressed.
    */
   public void keyPressed(KeyEvent ke)
   {
      int tgKeyNum = 0;
      char keyChar = ke.getKeyChar();
      if ( keyChar == KeyEvent.CHAR_UNDEFINED )
         tgKeyNum = mapToTGKeys( ke.getKeyCode() );
      else 
         tgKeyNum = keyChar;
      if ( tgKeyNum == 0 )
         return;
      for (int idx=0; idx < keyHandlers.length; idx++)
         if ( keyHandlers[idx] != null )
            keyHandlers[idx].tgKeyPressed( tgKeyNum );
   }


   /**
    * Invoked when a key has been released.
    */
   public void keyReleased(KeyEvent ke)
   { 
      int tgKeyNum = 0;
      char keyChar = ke.getKeyChar();
      if ( keyChar == KeyEvent.CHAR_UNDEFINED )
         tgKeyNum = mapToTGKeys( ke.getKeyCode() );
      else 
         tgKeyNum = keyChar;
      if ( tgKeyNum == 0 )
         return;
      for (int idx=0; idx < keyHandlers.length; idx++)
         if ( keyHandlers[idx] != null )
            keyHandlers[idx].tgKeyReleased( tgKeyNum );
   }


   /**
    * Invoked when a key has been typed.
    */
   public void keyTyped(KeyEvent ke)
   {
      char ch = ke.getKeyChar();
      for (int idx=0; idx < keyHandlers.length; idx++)
         if ( keyHandlers[idx] != null )
            keyHandlers[idx].tgKeyTyped( ch );
   }



   //
   // MouseListener interface methods
   // ------------- --------- -------

   /*
    * I intended to only support mouseClicked(), but too many clicks
    * of the mouse never got to the program.  Searching Sun's Java
    * Developer web site turned up an explanation: if the mouse is
    * moved at all between the press and the release, it is considered
    * a mouse-stroke instead of a mouse-click.  i was not able to find
    * any details on how one could adjust a threshold differentiating
    * the two, so the solution is to use mouseReleased() instead of
    * mouseClicked()
    *
    * Currently, org.bfoit.tg.TGCanvas only supports capturing and propagating a
    * left-button release (with no modifiers, e.g. [Shift] key down).
    * So, if a MouseEvent is received that isn't for org.bfoit.tg.TGCanvas, the
    * AWT tree is walked and if a MouseListener is found in a parent
    * component - the MouseEvent is given to it.
    */
   private void fwdMouseEvent( int id, MouseEvent me )
   {
      boolean popupTrigger = me.isPopupTrigger();
      long when = me.getWhen();
      int clickCount = me.getClickCount();
      int modifiers = me.getModifiers();
      int x = me.getX();
      int y = me.getY();
      Rectangle r = getBounds();
      x += r.x;
      y += r.y;
      Container parent = getParent();
      while ( parent != null )
      {
         Class c = parent.getClass();
         Class[] interfaces = c.getInterfaces();
         for ( int i=0; i < interfaces.length; i++)
         {
            String name = interfaces[i].getName();
            if ( name.equals("java.awt.event.MouseListener") )
            {
               MouseEvent nme = new MouseEvent(parent, id, when, modifiers,
                                               x, y, clickCount, popupTrigger);
               switch ( id )
               {
                 case MouseEvent.MOUSE_PRESSED:
                     ((MouseListener)parent).mousePressed( nme );
                     return;
                 case MouseEvent.MOUSE_RELEASED:
                     ((MouseListener)parent).mouseReleased( nme );
                     return;
                 case MouseEvent.MOUSE_CLICKED:
                     ((MouseListener)parent).mouseClicked( nme );
                     return;
                 case MouseEvent.MOUSE_ENTERED:
                     ((MouseListener)parent).mouseEntered( nme );
                     return;
                 case MouseEvent.MOUSE_EXITED:
                     ((MouseListener)parent).mouseExited( nme );
                     return;
                 case MouseEvent.MOUSE_MOVED:
                     ((MouseMotionListener)parent).mouseMoved( nme );
                     return;
                 case MouseEvent.MOUSE_DRAGGED:
                     ((MouseMotionListener)parent).mouseDragged( nme );
                     return;
                 default:
                     sysErr( "fwdMouseEvent: bad id" );
                     return;
               }
            }
         }
         r = parent.getBounds();
         x += r.x;
         y += r.y;
         parent = parent.getParent();
      }

   } // end fwdMouseEvent()


   /**
    * Invoked when a mouse button has been clicked (pressed and
    * released) on this Component.
    */
   public void mouseClicked(MouseEvent me)
   { fwdMouseEvent( MouseEvent.MOUSE_CLICKED, me ); }
   
   /**
    * Invoked when the mouse enters a Component.
    */
   public void mouseEntered(MouseEvent me)
   { fwdMouseEvent( MouseEvent.MOUSE_ENTERED, me ); }

   /**
    * Invoked when the mouse exits a Component.
    */
   public void mouseExited(MouseEvent me)
   { fwdMouseEvent( MouseEvent.MOUSE_EXITED, me ); }

   /**
    * Invoked when a mouse button has been pressed while on
    * a Component.
    */
   public void mousePressed(MouseEvent me)
   { fwdMouseEvent( MouseEvent.MOUSE_PRESSED, me ); }


   /**
    * Invoked when a mouse button has been released on org.bfoit.tg.TGCanvas.
    */
   public void mouseReleased(MouseEvent me)
   {
      int modifiersMask = me.getModifiers();
      if ( modifiersMask == InputEvent.BUTTON1_MASK )
      {
         mouseX = me.getX();
         mouseY = me.getY();
         for (int idx=0; idx < tgMouseHandlers.length; idx++)
            if ( tgMouseHandlers[idx] != null )
               tgMouseHandlers[idx].tgMouseClicked( mouseX - xCenter, -(mouseY - yCenter) );
         this.requestFocus();
      }
      else
         fwdMouseEvent( MouseEvent.MOUSE_RELEASED, me );
   }



   //
   // MouseMotionListener interface methods 
   // ------------------- --------- -------

   /**
    * Invoked when a mouse button is held down on org.bfoit.tg.TGCanvas and
    * the mouse is moved.
    */
   public void mouseDragged(MouseEvent me) { }


   /**
    * Invoked when the mouse is moved around on the org.bfoit.tg.TGCanvas
    * without any buttons in a depressed state.
    */
   public void mouseMoved(MouseEvent me)
   {
      if ( gotFocus )
      {
         mouseX = me.getX();
         mouseY = me.getY();
         for (int idx=0; idx < tgMouseHandlers.length; idx++)
            if ( tgMouseHandlers[idx] != null )
               tgMouseHandlers[idx].tgMouseMoved( mouseX - xCenter, -(mouseY - yCenter) );
      }
   }



   //
   // Support methods
   // ------- -------

   private void clearGraphicsImage()
   {
      if ( graphicsImage != null )
      {
         Graphics giGraphics = graphicsImage.getGraphics();
         giGraphics.setClip( 0, 0, GI_WIDTH-1, GI_HEIGHT-1 );
         giGraphics.setColor( backgroundColor );
         giGraphics.fillRect( 0, 0, GI_WIDTH-1, GI_HEIGHT-1 );
         if ( backgroundPicture != null )
         {
            int x = GI_WIDTH/2 - backgroundPicture.getWidth(this)/2;
            int y = GI_HEIGHT/2 - backgroundPicture.getHeight(this)/2;
            giGraphics.drawImage(backgroundPicture, x, y, this);
            backgroundPicture = null;
         }
         giGraphics.setColor( INIT_PEN_COLOR );
         giGraphics.dispose();
      }
   }


   /*
    * An attempt has been made to append another graphics operation to
    * the queue of outstanding ones and there is no room for it. Allocate
    * a new larger queue and transfer entries from old queue into it.
    */
   private void expandGraphicsOps()
   {
      TGGraphicsOp[] oldGraphicsOps = graphicsOps;
      graphicsOps = new TGGraphicsOp[ oldGraphicsOps.length + INCR_NUM_GRAFOPS ];
      System.arraycopy( oldGraphicsOps, 0, graphicsOps, 0, numGraphicsOps );
      System.out.println( "Expanded graphicsOps to " + graphicsOps.length );
   }


   private void initGraphicsImage()
   {
      graphicsImage = createImage( GI_WIDTH, GI_HEIGHT );
      clearGraphicsImage();
   }


   /*
    * Return a Logo color number given a java.awt.Color object.
    */
   private static int javaColorToLogoColor( Color color )
   {
      int rgbValue = color.getRGB();
      int alpha = rgbValue & 0xFF000000;
      if ( alpha == 0xFF000000 )
      {
         rgbValue &= 0xFFFFFF;
         for (int idx=0; idx < LOGO_COLORS.length; idx++)
            if ( (LOGO_COLORS[idx].getRGB() & 0xFFFFFF) == rgbValue  )
               return( idx );
      }
      return rgbValue;
   }


   /*
    * Apply all outstanding graphics operations to graphicsImage and
    * return a clipRect for area changed.
    */
   private Rectangle renderGraphics()
   {
      int giLeftX = GI_WIDTH;
      int giRightX = -1;
      int giUpperY = GI_HEIGHT;
      int giLowerY = -1;
      if ( graphicsImage == null )
      {
         initGraphicsImage();
         giLeftX = 0;
         giRightX = GI_WIDTH-1;
         giUpperY = 0;
         giLowerY = GI_HEIGHT-1;
      }
      synchronized ( graphicsOps )
      {
         for ( int opIdx=0; opIdx < numGraphicsOps; opIdx++ )
         {
            TGGraphicsOp op = graphicsOps[opIdx];
            graphicsOps[opIdx] = null;
            Rectangle clipRect = null;
            try { clipRect = op.doIt( graphicsImage ); }
            catch ( NullPointerException npe )
	    { sysErr("renderGraphics(): " + npe + " performing " + op); }
            if ( clipRect != null )
            {
               if ( clipRect.x < giLeftX )
                  giLeftX = clipRect.x;
               if ( clipRect.y < giUpperY )
                  giUpperY = clipRect.y;
               int coord = clipRect.x + clipRect.width - 1;
               if ( coord > giRightX )
                  giRightX = coord;
               coord = clipRect.y + clipRect.height - 1;
               if ( coord > giLowerY )
                  giLowerY = coord;
            }
         }
         numGraphicsOps = 0;
         graphicsOps.notifyAll();
      }
      int width = (giRightX + 1) - giLeftX;
      int widthInset = (GI_WIDTH - canvasWidth) / 2;
      int canvasLeftX = giLeftX - widthInset;
      if ( canvasLeftX < 0 )    // if negative, at least some of the
      {                         // painted pixels are to the left of
         width += canvasLeftX;  // the canvas, so adjust width
         canvasLeftX = 0;       // appropriately and reset left-most
      }                         // pixel number
      if ( width > canvasWidth )
         width = canvasWidth;
      int height = (giLowerY + 1) - giUpperY;
      int heightInset = (GI_HEIGHT - canvasHeight) / 2;
      int canvasUpperY = giUpperY - heightInset;
      if ( canvasUpperY < 0 )
      {
         height += canvasUpperY;
         canvasUpperY = 0;
      }
      if ( height > canvasHeight )
         height = canvasHeight;
      if ( height > 0 && width > 0 )
         return new Rectangle( canvasLeftX, canvasUpperY, width, height );
      return null;

   } // end renderGraphics()


   /*
    * print an error message to console tying it to this class
    */
   private void sysErr( String errTxt )
   { System.err.println( CLASS_NAME + "." + errTxt ); }



   //
   // Overridden Component methods
   // ---------- --------- -------

   /**
    * Paints this Component.
    * <p>
    * Something has occured that requires updating the graphics canvas.
    * As an example, TGGraphicsOps have been queued to be performed, or
    * a org.bfoit.tg.Sprite whose image is being displayed has moved or rotated, or
    * the AWT has decided we need to redraw at least some subset of our
    * pixels, e.g., partially covered stuff (by some other application
    * on the desktop) has moved/gone away, etc...
    */
   public void paint(Graphics g)
   {
      Rectangle rect = g.getClipBounds();
      int heightDiff = (GI_HEIGHT - canvasHeight) / 2;
      int widthDiff = (GI_WIDTH - canvasWidth) / 2;
      switch ( paintState )
      {
         case PAINT_REFRESH:
            if ( graphicsImage == null )
            {
               g.setColor( backgroundColor );
               g.fillRect( 0, 0, canvasWidth, canvasHeight );
            }
            else
            {
               if ( ! g.drawImage(graphicsImage, -widthDiff, -heightDiff, this) )
                  return;
            }
            paintState = PAINT_DRAW_GRAPHICS;
         case PAINT_DRAW_GRAPHICS:
            rect = renderGraphics();
            if ( rect != null )
            {
               g.setClip( rect );
               if ( ! g.drawImage(graphicsImage, -widthDiff, -heightDiff, this) )
                  return;
            }
            paintState = PAINT_ERASE_TURTLES;
            paintSpriteNum = 0;
         case PAINT_ERASE_TURTLES:
            while ( paintSpriteNum < spriteClipRect.length )
            {
               if ( (rect = spriteClipRect[paintSpriteNum]) != null )
               {
                  g.setClip( rect );
                  if ( ! g.drawImage(graphicsImage, -widthDiff, -heightDiff, this) )
                     return;
                  spriteClipRect[paintSpriteNum] = null;
               }
               paintSpriteNum++;
            }
            paintState = PAINT_DRAW_TURTLES;
            paintSpriteNum = 0;
         case PAINT_DRAW_TURTLES:
            while ( paintSpriteNum < sprites.length )
            {
               Sprite sprite = sprites[paintSpriteNum];
               if ( sprite != null )
               {
                  int spriteX = (int) Math.rint( sprite.xcor() + xCenter );
                  int spriteY = (int) Math.rint( yCenter - sprite.ycor() );
                  int imgSz = sprite.getImageSideSize();
                  int imgLeftX = spriteX - imgSz/2;
                  int imgTopY = spriteY - imgSz/2;
                  g.setClip( imgLeftX, imgTopY, imgSz, imgSz);
                  if ( ! g.drawImage (sprite.getImage(), imgLeftX, imgTopY, this) )
                     return;
                  spriteClipRect[paintSpriteNum] = new Rectangle(imgLeftX, imgTopY, imgSz, imgSz);
               }
               paintSpriteNum++;
            }
            paintState = PAINT_REFRESH;
      }

   } //end paint()


   /**
    * Moves and resizes this Component. The new location of the top-left
    * corner is specified by x and y. The new size is specified by width
    * and height.
    */
   public void setBounds( int x, int y, int width, int height )
   {
      super.setBounds( x, y, width, height );
      canvasWidth = width;
      canvasHeight = height;
      xCenter = width / 2;
      yCenter = height / 2;
      repaint();
   }


   /**
    * Resizes this Component to have the specified width and height.
    */
   public void setSize( int width, int height )
   {
      super.setSize( width, height );
      canvasWidth = width;
      canvasHeight = height;
      xCenter = width / 2;
      yCenter = height / 2;
      repaint();
   }


   /**
    * Update the displayed image of this Component.
    * <p>
    * Overridden in org.bfoit.tg.TGCanvas to eliminate Component.update()'s
    * invocation of Graphics.clear().
    */
   public void update(Graphics g) { paint(g); }



   //
   // Public methods
   // ------ -------


   /**
    * Append a graphics operation, a org.bfoit.tg.TGGraphicsOp object, to
    * the queue of outstanding operations.
    */
   public void addGraphOp( TGGraphicsOp grafOp )
   {
      synchronized (graphicsOps)
      {
         if ( numGraphicsOps == graphicsOps.length )
            expandGraphicsOps();
         graphicsOps[ numGraphicsOps++ ] = grafOp;
      }
   }


   /**
    * Add an object which implements org.bfoit.tg.TGKeyHandler to the list of
    * those that want a callback when a key is pressed while the
    * graphics canvas has focus. The object's keyPressed() method
    * is invoked.
    */
   public void addKeyHandler( TGKeyHandler kh )
   {
      for (int idx=0; idx < keyHandlers.length; idx++)
         if ( keyHandlers[idx] == kh )
            return;
      for (int idx=0; idx < keyHandlers.length; idx++)
         if ( keyHandlers[idx] == null )
         {
            keyHandlers[idx] = kh;
            return;
         }
      sysErr( "addKeyHandler: no space" );
   }


   /**
    * Add an object which implements org.bfoit.tg.TGMouseHandler to the list of
    * those that want a callback.  A callback is performed when the
    * graphics canvas has focus and 
    * <ul>
    * <li>
    * a mouse button is clicked (the object's tgMouseClicked() method is
    * invoked), or
    * </li>
    * <li>
    * the mouse is moved (the object's tgMouseMoved() method is invoked).
    * </li>
    * </ul>
    */
   public void addMouseHandler( TGMouseHandler mh )
   {
      for (int idx=0; idx < tgMouseHandlers.length; idx++)
         if ( tgMouseHandlers[idx] == mh )
            return;
      for (int idx=0; idx < tgMouseHandlers.length; idx++)
         if ( tgMouseHandlers[idx] == null )
         {
            tgMouseHandlers[idx] = mh;
            return;
         }
      sysErr( "addMouseHandler: no space" );
   }


   /**
    * Add a new org.bfoit.tg.Sprite, start displaying its image on the
    * graphics canvas.
    */
   public void addSprite( Sprite sprite )
   {
      //System.out.println( "org.bfoit.tg.TGCanvas.addSprite(): " + sprite );
      int openIdx = -1;
      for (int i=sprites.length-1; i >= 0; i--)
      {
         if ( sprites[i] != null )
         {
            if ( sprites[i] == sprite )
               return;
         }
         else
            openIdx = i;
      }
      if ( openIdx < 0 )
      {
         sysErr( "addSprite: no room!" );
         return;
      }
      sprites[openIdx] = sprite;
      repaint();
   }


   /**
    * Return the height of the graphics canvas.
    */
   public int canvasHeight()
   { return canvasHeight; }


   /**
    * Return the width of the graphics canvas.
    */
   public int canvasWidth()
   { return canvasWidth; }


   /**
    * Clean graphics off of the display.
    */
   public void clean()
   {
      synchronized ( graphicsOps )
      {
         while ( numGraphicsOps > 0 )
            graphicsOps[--numGraphicsOps] = null;
      }
      clearGraphicsImage();
      repaint();
   }


   /**
    * Return the background Logo color number.  Values zero through
    * fifteen (inclusive) are fixed Logo colors (black, blue, etc...)
    * other values are RGB numbers, with or without alpha values.
    */
   public int getBackgroundColor()
   { return javaColorToLogoColor(backgroundColor); }


   /**
    * Return an array of pixel RGB values.  A rectangular area of the
    * graphics canvas defined by a top-left corner (in TurtleSpace
    * coordinates) and its width and height.
    * <p>
    * Since all drawing operations are queued to be performed by the
    * paint() method, we must wait for all outstanding operations to
    * complete before we grab pixels.
    */
   public int[] getPixels( TGPoint topLeft, int width, int height )
   {
      synchronized ( graphicsOps )
      {
         while ( numGraphicsOps > 0 )
         {
            try { graphicsOps.wait(); }
            catch ( InterruptedException ie ) { }
         }
      }
      int backgroundRGB = backgroundColor.getRGB() & 0xffffff;
      int[] rgbArray = new int[ width * height ];
      for ( int idx=0; idx < rgbArray.length; idx++ )
         rgbArray[idx] = backgroundRGB;
      if ( graphicsImage == null )
         return rgbArray;

      int grabWidth = width;
      int imageLeftX = topLeft.imageX( GI_WIDTH );
      if ( imageLeftX < 0 )
      {
         grabWidth += imageLeftX;
         imageLeftX = 0;
      }
      if ( (imageLeftX + grabWidth) > GI_WIDTH )
         grabWidth -= (imageLeftX + grabWidth) - GI_WIDTH;
      int grabHeight = height;
      int imageTopY = topLeft.imageY( GI_HEIGHT );
      if ( imageTopY < 0 )
      {
         grabHeight += imageTopY;
         imageTopY = 0;
      }
      if ( (imageTopY + grabHeight) > GI_HEIGHT )
         grabHeight -= (imageTopY + grabHeight) - GI_HEIGHT;

      if ( grabWidth == width && grabHeight == height )
      {
         PixelGrabber pg = new PixelGrabber( graphicsImage,          // image to retrieve pixels from
                                             imageLeftX, imageTopY,  // top-left corner of rectangle
                                             grabWidth, grabHeight,  // width, height of rectangle
                                             rgbArray,               // destination array of RGB ints
                                             0,                      // destination offset into array
                                             grabWidth               // row width of destination image
                                           );
         try { pg.grabPixels(); }
         catch (InterruptedException e) { sysErr( "getPixels(): grabPixels interrupted" ); }
      } else {
         sysErr( "getPixels(): some desired pixels off image" );
      }
      for ( int idx=0; idx < rgbArray.length; idx++ )
      {
         rgbArray[idx] &= 0xffffff;
      }
      return rgbArray;

   } // end getPixels()


   /**
    * Load the provided array of pixels into the background.
    * <p>
    * *NOTE* a side-effect of changing the background color is that
    * all current graphics is cleared.
    */
   public void loadPicture( Image pictureImage )
   {
      backgroundPicture = pictureImage;
      clearGraphicsImage();
      repaint();
   }


   /**
    * Convert a Logo color to a Java Color object. If the alpha
    * component is zero it is replaced with 0xFF - assumed to be
    * an opaque color.
    */
   public static Color logoColorToJavaColor( int logoColor )
   {
      int alpha = (logoColor & 0xFF000000) >>> 24;
      logoColor &= 0xFFFFFF;
      if ( alpha == 0 || alpha == 0xFF )
      {
         if ( logoColor < LOGO_COLORS.length )
            return( LOGO_COLORS[logoColor] );
         return new Color( logoColor );
      }
      if ( logoColor < LOGO_COLORS.length )
         logoColor = (LOGO_COLORS[logoColor].getRGB()) & 0xFFFFFF;
      logoColor |= alpha << 24;
      return new Color( logoColor, true );
   }


   /**
    * Convert a Logo color to a Java RGB int with the alpha component
    * maintained.
    */
   public static int logoColorToRGB( int logoColor )
   {
      int alpha = logoColor & 0xFF000000;
      if ( alpha == 0 )
         alpha = 0xFF000000;
      logoColor &= 0xFFFFFF;
      if ( logoColor < LOGO_COLORS.length )
         logoColor = (LOGO_COLORS[logoColor].getRGB()) & 0xFFFFFF;
      return logoColor | alpha;
   }


   /**
    * Return the TurtleSpace x-coordinate of the mouse when it was
    * last clicked.
    * <p>
    * @see #mousey
    */
   public int mousex()
   { return mouseX - xCenter; }


   /**
    * Return the TurtleSpace y-coordinate of the mouse when it was
    * last clicked.
    * <p>
    * @see #mousex
    */
   public int mousey()
   { return -(mouseY - yCenter); }


   /**
    * Remove an object from org.bfoit.tg.TGCanvas' list of those wanting
    * their keyPressed() method invoked when org.bfoit.tg.TGCanvas
    * receives this Event.
    */
   public void removeKeyHandler( TGKeyHandler kh )
   {
      for (int idx=0; idx < keyHandlers.length; idx++)
         if ( keyHandlers[idx] == kh )
            keyHandlers[idx] = null;
   }


   /**
    * Remove an object from org.bfoit.tg.TGCanvas' list of those wanting
    * their mouseClicked() and mouseMoved() methods invoked
    * when we receive these Events.
    */
   public void removeMouseHandler( TGMouseHandler mh )
   {
      for (int idx=0; idx < tgMouseHandlers.length; idx++)
         if ( tgMouseHandlers[idx] == mh )
            tgMouseHandlers[idx] = null;
   }


   /**
    * Remove the specified org.bfoit.tg.Sprite from the list of sprites
    * whose images are displayed on the graphics canvas.
    */
   public void removeSprite( Sprite sprite )
   {
      //System.out.println( "org.bfoit.tg.TGCanvas.removeSprite(): " + sprite );
      for (int i=sprites.length-1; i >= 0; i--)
         if ( sprites[i] == sprite )
         {
            sprites[i] = null;
            return;
         }
      sysErr( "removeSprite: org.bfoit.tg.Sprite missing!" );
   }


   /**
    * Return a Logo pen color number given an ARGB or RGB value.
    */
   public static int rgbToLogoColor( int rgbValue )
   {
      int alpha = (rgbValue & 0xFF000000) >>> 24;
      if ( alpha == 0 || alpha == 0xFF )
      {
         rgbValue &= 0xFFFFFF;
         for (int idx=0; idx < LOGO_COLORS.length; idx++)
            if ( (LOGO_COLORS[idx].getRGB() & 0xFFFFFF) == rgbValue  )
               return( idx );
      }
      return rgbValue;
   }


   /**
    * Set the background color of the graphics canvas.
    * <p>
    * *NOTE* a side-effect of changing the background color is that
    * all current graphics is cleared.
    */
   public void setbg( int logoColor )
   {
      synchronized ( graphicsOps )
      {
         while ( numGraphicsOps > 0 )
            graphicsOps[--numGraphicsOps] = null;
      }
      backgroundColor = logoColorToJavaColor( logoColor );
      clearGraphicsImage();
      repaint();
   }


} // end class org.bfoit.tg.TGCanvas
