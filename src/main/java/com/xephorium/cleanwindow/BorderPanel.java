/*
  Chris Cruzen                       Personal Project
  "BorderPanel.java"                       06.04.2014

  Clean Window

    This class defines the appearance and behavior of
  each border panel used in the Clean_Window class.

  (Development notes at file end.)
*/
package com.xephorium.cleanwindow;

import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

public class BorderPanel extends JPanel
{
    /*--- Fields ---*/

    public static final int EAST      = 0;
    public static final int SOUTHEAST = 1;
    public static final int SOUTH     = 2;
    public static final int SOUTHWEST = 3;
    public static final int WEST      = 4;
    public static final int NORTHPAD  = 5;
    private static final int[] CURSORTYPE = new int[]{Cursor.E_RESIZE_CURSOR,
                                                      Cursor.NE_RESIZE_CURSOR,
                                                      Cursor.N_RESIZE_CURSOR,
                                                      Cursor.NW_RESIZE_CURSOR,
                                                      Cursor.E_RESIZE_CURSOR,};

    private int     borderType;
    public  Color   colorBkgnd;
    public  Color   colorBrdr;
    private String  borderSize;
    private String  path;
    private Image   borderImage;
    private JLabel  borderLabel;


    /*--- Constructors ---*/

    // Unbordered Clean_Window
    public BorderPanel(int t, Color bk)
    {
        // Set Variables
        borderType = t;
        colorBkgnd = bk;

        // Create Panel
        if(borderType != NORTHPAD)
        {
            setCursor(Cursor.getPredefinedCursor(CURSORTYPE[borderType]));
            setBackground(colorBkgnd);
        }
        else
        {
            // Get Program Directory
            getProgramDirectory();

            // Import Border Image
            borderSize = "sml";
            importBorderImage();

            // Create Panel
            setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            setBackground(colorBkgnd);
            borderLabel = new JLabel(new ImageIcon(borderImage));
            add(borderLabel);
        }
    }

    // Bordered Clean_Window
    public BorderPanel(int t, Color bk, Color bdr, String s)
    {
        // Set Variables
        borderType = t;
        colorBkgnd = bk;
        colorBrdr  = bdr;
        borderSize = s;

        // Get Program Directory
        getProgramDirectory();

        // Import Border Image
        importBorderImage();

        // Create Panel
        setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        setCursor(Cursor.getPredefinedCursor(CURSORTYPE[borderType]));
        setBackground(colorBrdr);
        borderLabel = new JLabel(new ImageIcon(borderImage));
        add(borderLabel);
    }


    /*--- Methods ---*/

    private void getProgramDirectory()
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

    private void importBorderImage()
    {
        try
        {
            //borderImage = ImageIO.read(new File(path+"\\final_images\\border_"+borderType+"_"+borderSize+"_gray.png"));
            borderImage = ImageIO.read(new File("src\\main\\resources\\images\\border_"+borderType+"_"+borderSize+"_gray.png"));
        }
        catch (IOException e)
        {
            System.out.println("Error importing Border_Panel image - type "+borderType+", size "+borderSize+".\n");
        }
    }

}

/*
  ///////////////////////////////////////////////////////
  //                                                   //
  //              BorderPanel - Dev. Notes             //
  //                                                   //
  ///////////////////////////////////////////////////////

  Basic BorderPanel structure is composed of two layers:

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
      (int borderType, Color colorBkgnd,
      {Color colorBrdr, String borderSize})
      borderType  // EAST/SOUTHEAST/SOUTH/SOUTHWEST/WEST
      colorBkgnd  // Color of background
      colorBrdr   // Color of border
      borderSize  // sml/med/lrg

  Border Numbering:

  		    ,-------------------------------.
		    |(5)|                       |(5)|
		    |---|-----------------------|---|
	        |   |                       |   |
		    | 0 |     Program Panel     | 4 |
		    |   |                       |   |
		    |---|-----------------------|---|
		    | 1 |           2           | 3 |
		    '-------------------------------'

*/