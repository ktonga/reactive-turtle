package org.bfoit.tg;

/**
 * Class with parameters which are needed by multiple TG classes.
 * <p>
 * I'm doing this to try to isolate parts of TG, e.g., provide
 * the turtle graphics stuff for anyone programming in Java.
 * <p>
 * @author Guy Haas
 */


class Params
{

   // Global Symbolic Constants
   // ------ -------- ---------

   /**
    * Name of App/Applet/Application.
    */
   public static final String APP_NAME = "TG";


   /**
    * Name of file containing TG's HELP information database.
    */
   public static final String HELP_FILENAME = "TGHelp.txt";


   /**
    * Maximum number of turtles than can be created.
    */
   public static final int MAX_TURTLES = 64;


   /**
    * TG' version number.
    */
   public static final String VERSION = ".9.37";

}
