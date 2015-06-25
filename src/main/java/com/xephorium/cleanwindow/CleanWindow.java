/*
  Chris Cruzen                           Personal Project
  "CleanWindow.java"                           06.04.2014

  CleanWindow

    This app is my first attempt to build a professional,
  fully-interactive, minimalistic GUI from the ground up
  in Java. Clean_Window is designed to support all of the
  expected functionality of a modern application without
  sacrificing absolute visual simplicity.

  (Development notes at file end.)

*/
package com.xephorium.cleanwindow;

import com.xephorium.cleanwindow.external.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;


public class CleanWindow
{
    /*--- Fields ---*/

    ComponentMover mover;
    MouseAdapter[] adapterBorder;
    MouseAdapter[] adapterControl;

    JFrame    window;
    JPanel[]  panelBorder;
    JPanel[]  panelBuffer;
    JPanel[]  panelControl;
    JPanel    panelNorth;
    JLabel    labelNorth;
    Color     colorNorthBkgnd;
    Color     colorNorthText;
    Color     colorCenter;
    Color     colorControl[];
    Image     imageTitleBar[];
    ImageIcon icon;
    String    winTitle;
    int       winTitleSize;
    int[]     winDim;
    int[]     winDimMin;
    int[]     winLoc;
    int       buttonDimCtrl;
    boolean   bordered;
    boolean   winLocCustom;


    /*--- Constructor ---*/

    public CleanWindow()
    {
        // Declare Variables
        adapterBorder    = new MouseAdapter[5];
        adapterControl   = new MouseAdapter[3];
        panelBuffer      = new JPanel[2];
        panelControl     = new JPanel[4];
        panelNorth       = new JPanel();
        panelBorder      = new BorderPanel[5];
        buttonDimCtrl    = 26;

        // Defaults
        colorNorthBkgnd  = new Color(74,74,74);
        colorNorthText   = new Color(255,255,255);
        colorCenter      = new Color(150,150,150);
        colorControl     = new Color[]{colorNorthBkgnd,  // Default
                           new Color(100,100,100),       // Highlight
                           new Color(60,60,60)};         // Click
        winTitle         = "Xephorium";
        winTitleSize     = 12;
        winDim           = new int[]{600,400};
        winDimMin        = new int[]{300,200};
        winLoc           = new int[]{100,100};
        winLocCustom     = false;
        bordered         = true;

        // Import Files
        import_files();
    }


    /*---  Public Methods ---*/

    // Public Set Colors    (window combo, {title})
    // Public Set Title     (text, {size})
    // Public Set Bordered  (t/f, {size})
    // Public Set Shadowed  (t/f)
    // Public Set Min Size  (x, y)
    // Public Set Location  (x, y)

    // Public Add           (JPanel)
    // {
    //
    // }

    public void set_size(int x, int y)
    {
        winDim[0] = x;
        winDim[1] = y;
    }

    // Show Window
    public void display_window(boolean s)
    {
        window.setVisible(s);
    }

    // Construct Window
    public void create_window()
    {
        // Determine Mouse Behavior
        define_adapterBorders();
        define_adapterControls();

        // Assemble Borders
        for(int x=0; x<5; x++)
        {
            if(bordered)
                panelBorder[x] = new BorderPanel(x, colorCenter, colorNorthBkgnd, "sml");
            else
                panelBorder[x] = new BorderPanel(x, colorCenter);
            panelBorder[x].addMouseMotionListener(adapterBorder[x]);
        }

        // Assemble Control Panels - Mix, Max, Close
        for(int x=0; x<3; x++)
        {
            panelControl[x] = new JPanel();
            panelControl[x].setBackground(colorControl[0]);
            panelControl[x].add(new JLabel(new ImageIcon(imageTitleBar[x])));
            panelControl[x].addMouseListener(adapterControl[x]);
        }

        // Assemble Left Buffer & Control Panel Container
        for(int x=0; x<2; x++)
        {
            panelBuffer[x] = new JPanel();
            panelBuffer[x].setBackground(colorNorthBkgnd);
            panelBuffer[x].setLayout(new GridBagLayout());
        }

        // Add Blank Panels & Control Panels to Above
        for(int x=0; x<3; x++)
        {
            JPanel tempHandle = new JPanel();
            tempHandle.setBackground(colorNorthBkgnd);
            tempHandle.add(new JLabel(new ImageIcon(imageTitleBar[3])));
            panelBuffer[0].add(tempHandle,      new GridBagConstraints(x,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            panelBuffer[1].add(panelControl[x], new GridBagConstraints(x,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        }

        // Assemble Conditional Border Buffers
        if(bordered)
        {
            JPanel tempHandle = new BorderPanel(BorderPanel.NORTHPAD, colorNorthBkgnd);
            panelBuffer[0].add(tempHandle, new GridBagConstraints(3,0,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            panelBuffer[1].add(tempHandle, new GridBagConstraints(3,0,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        }

        // Assemble Title Bar
        labelNorth = new JLabel(winTitle);
        labelNorth.setFont(new Font("Arial", Font.PLAIN, winTitleSize));
        labelNorth.setForeground(colorNorthText);
        panelNorth.setLayout(new GridBagLayout());
        panelNorth.setBackground(colorNorthBkgnd);
        panelNorth.add(panelBuffer[0], new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));
        panelNorth.add(labelNorth,     new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));
        panelNorth.add(panelBuffer[1], new GridBagConstraints(2,0,1,1,0,1,GridBagConstraints.EAST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));

        // Assemble Program
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setUndecorated(true);
        window.setLayout(new GridBagLayout());
        window.getContentPane().setBackground(colorCenter);
        window.setSize(winDim[0],winDim[1]);
        window.setTitle(winTitle);
        window.setIconImage(icon.getImage());
        window.add(panelBorder[0], new GridBagConstraints(0,1,1,1,0,1,GridBagConstraints.WEST,     GridBagConstraints.VERTICAL,  new Insets(0,0,0,0),0,0));
        window.add(panelBorder[1], new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,      new Insets(0,0,0,0),0,0));
        window.add(panelBorder[2], new GridBagConstraints(1,2,1,1,1,0,GridBagConstraints.SOUTH,    GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
        window.add(panelBorder[3], new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,      new Insets(0,0,0,0),0,0));
        window.add(panelBorder[4], new GridBagConstraints(2,1,1,1,0,1,GridBagConstraints.EAST,     GridBagConstraints.VERTICAL,  new Insets(0,0,0,0),0,0));
        window.add(panelNorth,     new GridBagConstraints(0,0,3,1,1,0,GridBagConstraints.NORTH,    GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
        // Add Central Panel Content

        // Register Moving Components
        mover = new ComponentMover(JFrame.class, panelNorth);
        mover.setChangeCursor(false);

        // Set Location
        if(winLocCustom)
            window.setLocation(winLoc[0],winLoc[1]);
        else
        {
            int[] screen_dim = new int[2];
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            screen_dim[0] = gd.getDisplayMode().getWidth();
            screen_dim[1] = gd.getDisplayMode().getHeight();
            window.setLocation(((screen_dim[0]/2)-(winDim[0]/2)),((screen_dim[1]/2)-(winDim[1]/2)));
        }
    }

    // Deconstruct Window (NOT ENDING THREAD)
    public void destroy_window()
    {
        WindowEvent close = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
        window.dispose();
        System.exit(0);
    }


    /*---  Private Methods ---*/

    // Import Program Files
    private void import_files()
    {
        String path = "";
        try
        {
            path = new File(".").getCanonicalPath();
        }
        catch (IOException e)
        {
            System.out.println("Error determining program directory (Clean_Window).");
        }

        try
        {
            // Needs IDE Refactoring
            imageTitleBar = new Image[]{ImageIO.read(new File("src\\main\\resources\\images\\Minimize (Custom).png")),
                    ImageIO.read(new File("src\\main\\resources\\images\\Maximize (Custom).png")),
                    ImageIO.read(new File("src\\main\\resources\\images\\Close (Custom).png")),
                    ImageIO.read(new File("src\\main\\resources\\images\\Buffer (Custom).png"))};
        }
        catch (IOException e)
        {
            System.out.println("Error importing title bar images.");
        }

        try
        {
            icon = new ImageIcon("C:\\Earth Icon.png");
        }
        catch (NullPointerException e)
        {
            System.out.println("Error importing icon image.");
        }
    }

    // Define Border Mouse Behavior
    private void define_adapterBorders()
    {
        // Resize West Wall
        adapterBorder[0] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() - e.getX() > winDimMin[0])
                {
                    window.setSize(window.getWidth()-e.getX(),window.getHeight());
                    window.setLocation(init_pos_x+e.getX(),init_pos_y);
                }
            }
        };

        // Resize Southwest Corner
        adapterBorder[1] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() - e.getX() > winDimMin[0])
                {
                    window.setSize(window.getWidth()-e.getX(),window.getHeight());
                    window.setLocation(init_pos_x+e.getX(),init_pos_y);
                }
                if(window.getHeight() + e.getY() > winDimMin[1])
                    window.setSize(window.getWidth(),window.getHeight()+e.getY());
            }
        };

        // Resize South Wall
        adapterBorder[2] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getHeight() + e.getY() > winDimMin[1])
                    window.setSize(window.getWidth(),window.getHeight()+e.getY());
            }
        };

        // Resize Southeast Corner
        adapterBorder[3] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() + e.getX() > winDimMin[0])
                    window.setSize(window.getWidth()+e.getX(),window.getHeight());
                if(window.getHeight() + e.getY() > winDimMin[1])
                    window.setSize(window.getWidth(),window.getHeight()+e.getY());
            }
        };

        // Resize East Wall
        adapterBorder[4] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() + e.getX() > winDimMin[0])
                    window.setSize(window.getWidth()+e.getX(),window.getHeight());
            }
        };
    }

    // Define Control Mouse Behavior
    private void define_adapterControls()
    {
        // Minimize Button
        adapterControl[0] = new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e){
                panelControl[0].setBackground(colorControl[1]);}

            public void mouseExited(MouseEvent e){
                panelControl[0].setBackground(colorControl[0]);}

            public void mousePressed(MouseEvent e){
                panelControl[0].setBackground(colorControl[2]);}

            public void mouseReleased(MouseEvent e)
            {
                // If On Button, Minimize.
                if(e.getX() < buttonDimCtrl && e.getX() >= 0 && e.getY() < buttonDimCtrl && e.getY() >= 0)
                {
                    panelControl[0].setBackground(colorControl[1]);
                    window.setExtendedState(JFrame.ICONIFIED);
                }
                else
                    panelControl[0].setBackground(colorControl[0]);

            }
        };

        // Maximize Button
        adapterControl[1] = new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e){
                panelControl[1].setBackground(colorControl[1]);}

            public void mouseExited(MouseEvent e){
                panelControl[1].setBackground(colorControl[0]);}

            public void mousePressed(MouseEvent e){
                panelControl[1].setBackground(colorControl[2]);}

            public void mouseReleased(MouseEvent e)
            {
                // If On Button, Change.
                if(e.getX() < buttonDimCtrl && e.getX() >= 0 && e.getY() < buttonDimCtrl && e.getY() >= 0)
                {
                    if(window.getExtendedState() == JFrame.MAXIMIZED_BOTH)
                        window.setExtendedState(JFrame.NORMAL);
                    else
                        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    panelControl[1].setBackground(colorControl[1]);
                }
                else
                    panelControl[1].setBackground(colorControl[0]);
            }
        };

        // Close Button
        adapterControl[2] = new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e){
                panelControl[2].setBackground(colorControl[1]);}

            public void mouseExited(MouseEvent e){
                panelControl[2].setBackground(colorControl[0]);}

            public void mousePressed(MouseEvent e){
                panelControl[2].setBackground(colorControl[2]);}

            public void mouseReleased(MouseEvent e)
            {
                // If On Button, Close.
                if(e.getX() < buttonDimCtrl && e.getX() >= 0 && e.getY() < buttonDimCtrl && e.getY() >= 0)
                {
                    panelControl[2].setBackground(colorControl[1]);
                    WindowEvent close = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
                    window.dispose();
                    System.exit(0);
                }
                else
                    panelControl[2].setBackground(colorControl[0]);
            }
        };
    }

}


/*
  ///////////////////////////////////////////////////////
  //                                                   //
  //              CleanWindow - Dev. Notes             //
  //                                                   //
  ///////////////////////////////////////////////////////


  > GUI Design Mechanics

    panelBuffer[0]
    ,--------------------------------.
    |,-------.,-------.,-------.,---.|
    ||   3   ||   3   ||   3   || ? ||
    ||_______||_______||_______||___||
    '--------------------------------'
    (3=Blank Panel, ?=conditional buffer)

    panelBuffer[1]
    ,--------------------------------.
    |,-------.,-------.,-------.,---.|
    ||   0   ||   1   ||   2   || ? ||
    ||_______||_______||_______||___||
    '--------------------------------'
    (0-3=panelControl, ?=conditional buffer)


    ,------------------------------------------------.
    |,--------------.                ,--------------.|
    ||panelBuffer[0]|   panelNorth   |panelBuffer[1]||
    ||______________|                |______________||
    '------------------------------------------------'
    \                                              /
	  ____________________________________________
	 |,------------------------------------------.|
	 ||                panelNorth                ||
	 ||__________________________________________||
	 |--------------------------------------------|
	 | |                                        | |
	 | |                                        | |
	 | |            (Program Panel)             | |
	 | |                                        | |
	 | |                                        | |
	 |-|----------------------------------------|-|
	 '--------------------------------------------'

    - Layout: GridManager (http://goo.gl/jwk83C)
    - Northwest, North, and Northeast panels dedicated to
      title bar. Color variable.
    - West and East panels control horizontal resize. Color
      variable.
    - South panel controls vertical resize.
    - Southwest and Southeast panels control diagonal
      resize. Color variable.


  > Graphically Variable Elements

     Title Bar:       Control Bar:      Borders:
       Text Field       Bkgnd Color       Bkgnd Color
       Bkgnd Color      Hghlt Color
       Text Color       Clkd Color
                        Fgnd Color


  > External Code Credit
    - Rob Camick's "ComponentMover" Class


  > Lessons in Java
    1.) GridBagLayout assumes its grid is only as large
        as it needs to be to render the coded regions.
        (ie. Add a panel that spans 1x3 cells; grid will
        be 1x3 cells.)
    2.) Undecorated JFrames maximize to the size of the
        entire window (covering the task bar).


	> Revision History
    v1.0: JWindow Class with Panel for Title Bar.
    v1.2: Added ComponentMover Support (Clean Dragging).
    v1.3: Redesigned JWindow to allow for Border Variable.
    v1.4: Changed from JWindow to JFrame Class (Taskbar Support).
    v1.5: Code Cleanup #1.
    v1.6: Added Resize Functionality.
    v1.7: Added Application Icon Field.
    v1.8: Added Control Bar/Dynamic Title Buffer.
    v1.9: Reduced Border Size/Added Variable. Cleaned Up Code.
    v2.0: Major Refactor (IntelliJ, GitHub, Design Patterns)

  > Current Task(s)
    - Add Windows Positioning Shortcuts
    - Fullscreen Variable
    - Font Size Variable
    - Fix "Minimize -> Maximize" Bug
    - Window Shadow
    - New Graphic Elemets: Drop-Down, Transparency,
                           Lines & Gradients, Mouse
                           position response


*/