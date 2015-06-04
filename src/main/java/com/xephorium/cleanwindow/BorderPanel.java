/*
  Chris Cruzen                           06/04/2015

  This class defines the appearance and behavior of
  each border panel used in the Clean_Window class.
  Basic Border_Panel structure is composed of two
  layers:

             .
          .#####.
       .###########.
     <     ##########>    Layer 2 - Bkgnd Stencil
       .   ---#####.                (If bordered)
        - .-----. -
     <------ . ------>    Layer 1 - Border Color
       .-----------.                (Bkgnd if unbordered)
          .-----.
             .

  By painting partially transparent images over the
  border color specified by Clean_Window, each
  Border_Panel appears to have an edge of specified
  width while maintaining a seven pixel click-area 
  for program resize.

  Parameters:
      (int brdr_type, Color clr_bkgnd,
      {Color clr_brdr, String brdr_size})

      brdr_type  // EAST/SOUTHEAST/SOUTH/SOUTHWEST/WEST
      clr_bkgnd  // Color of background
      clr_brdr   // Color of border
      brdr_size  // sml/med/lrg
   

*/
package com.xephorium.cleanwindow;

import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

public class BorderPanel extends JPanel
{
    /////////////////////////////
    //--- Field Declaration ---//
    /////////////////////////////

    public static final int EAST      = 0;
    public static final int SOUTHEAST = 1;
    public static final int SOUTH     = 2;
    public static final int SOUTHWEST = 3;
    public static final int WEST      = 4;
    public static final int SPECIAL   = 5;
    private static final int[] CURSOR_TYPE = new int[]{Cursor.E_RESIZE_CURSOR,
            Cursor.NE_RESIZE_CURSOR,
            Cursor.N_RESIZE_CURSOR,
            Cursor.NW_RESIZE_CURSOR,
            Cursor.E_RESIZE_CURSOR,};
    private int     brdr_type;
    public  Color   clr_bkgnd;
    public  Color   clr_brdr;
    private String  brdr_size;
    private String  path;
    private Image   brdr_img;
    private JLabel  brdr_lbl;


    /////////////////////////////
    /////--- Constructors ---////
    /////////////////////////////

    // Unbordered Clean_Window
    public BorderPanel(int t, Color bk)
    {
        // Set Variables
        brdr_type = t;
        clr_bkgnd = bk;

        // Create Panel
        if(brdr_type != SPECIAL)
        {
            setCursor(Cursor.getPredefinedCursor(CURSOR_TYPE[brdr_type]));
            setBackground(clr_bkgnd);
        }
        else
        {
            // Get Program Directory
            get_program_directory();

            // Import Border Image
            brdr_size = "sml";
            import_border_image();

            // Create Panel
            setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            setBackground(clr_bkgnd);
            brdr_lbl = new JLabel(new ImageIcon(brdr_img));
            add(brdr_lbl);
        }
    }

    // Bordered Clean_Window
    public BorderPanel(int t, Color bk, Color bdr, String s)
    {
        // Set Variables
        brdr_type = t;
        clr_bkgnd = bk;
        clr_brdr  = bdr;
        brdr_size = s;

        // Get Program Directory
        get_program_directory();

        // Import Border Image
        import_border_image();

        // Create Panel
        setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        setCursor(Cursor.getPredefinedCursor(CURSOR_TYPE[brdr_type]));
        setBackground(clr_brdr);
        brdr_lbl = new JLabel(new ImageIcon(brdr_img));
        add(brdr_lbl);
    }

    /////////////////////////////
    ///////--- Methods ---///////
    /////////////////////////////

    private void get_program_directory()
    {
        try
        {
            path = new File(".").getCanonicalPath();
        }
        catch (IOException e)
        {
            System.out.println("Error determining program directory (Border_Panel).");
        }
    }

    private void import_border_image()
    {
        try
        {
            brdr_img = ImageIO.read(new File(path+"\\final_images\\border_"+brdr_type+"_"+brdr_size+"_gray.png"));
        }
        catch (IOException e)
        {
            System.out.println("Error importing Border_Panel image - type "+brdr_type+", size "+brdr_size+".\n");
        }
    }

}