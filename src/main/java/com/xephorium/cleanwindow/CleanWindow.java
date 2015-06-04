/*
  ///////////////////////////////////////////////////////
  //                                                   //
  //                   CLEAN WINDOW                    //
  //                                                   //
  ///////////////////////////////////////////////////////

  Christopher Cruzen                           06/04/2014

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
    /////////////////////////////
    //--- Field Declaration ---//
    /////////////////////////////

    ComponentMover mover;
    MouseAdapter[] border_adapter;
    MouseAdapter[] control_adapter;

    JFrame    window;
    JPanel[]  panel_border;
    JPanel[]  panel_buffer;
    JPanel[]  panel_control;
    JPanel    panel_north;
    JLabel    label_north;
    Color     color_north_bkgd;
    Color     color_north_text;
    Color     color_center;
    Color     color_control[];
    Image     image_title_bar[];
    ImageIcon icon;
    String    win_title;
    int[]     win_dim;
    int[]     win_min;
    int[]     win_loc;
    int       button_ctl_dim;
    int       win_title_size;
    boolean   show_border;
    boolean   custom_location;



    /////////////////////////////
    /////--- Constructor ---/////
    /////////////////////////////

    public CleanWindow()
    {
        // Declare Variables
        border_adapter    = new MouseAdapter[5];
        control_adapter   = new MouseAdapter[3];
        panel_buffer      = new JPanel[2];
        panel_control     = new JPanel[4];
        panel_north       = new JPanel();
        panel_border      = new BorderPanel[5];
        button_ctl_dim    = 26;

        // Defaults
        color_north_bkgd  = new Color(74,74,74);
        color_north_text  = new Color(255,255,255);
        color_center      = new Color(150,150,150);
        color_control     = new Color[]{color_north_bkgd,        // Default
                new Color(100,100,100),  // Highlight
                new Color(60,60,60)};    // Click
        win_title         = "Xephorium";
        win_title_size    = 12;
        win_dim           = new int[]{600,400};
        win_min           = new int[]{300,200};
        win_loc           = new int[]{100,100};
        custom_location   = false;
        show_border       = false;

        // Import Files
        import_files();
    }



    /////////////////////////////
    ///---  Public Methods ---///
    /////////////////////////////

    // Public Set Colors    (window combo, {title})
    // Public Set Title     (text, {size})
    // Public Set Bordered  (t/f, {size})
    // Public Set Shadowed  (t/f)
    // Public Set Min Size  (x, y)
    // Public Set Location  (x, y)
    // Public Add           (JPanel)

    public void set_size(int x, int y)
    {
        win_dim[0] = x;
        win_dim[1] = y;
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
        define_border_adapters();
        define_control_adapters();

        // Assemble Borders
        for(int x=0; x<5; x++)
        {
            if(show_border)
                panel_border[x] = new BorderPanel(x, color_center, color_north_bkgd, "sml");
            else
                panel_border[x] = new BorderPanel(x, color_center);
            panel_border[x].addMouseMotionListener(border_adapter[x]);
        }

        // Assemble Control Panels - Mix, Max, Close
        for(int x=0; x<3; x++)
        {
            panel_control[x] = new JPanel();
            panel_control[x].setBackground(color_control[0]);
            panel_control[x].add(new JLabel(new ImageIcon(image_title_bar[x])));
            panel_control[x].addMouseListener(control_adapter[x]);
        }

        // Assemble Left Buffer & Control Panel Container
        for(int x=0; x<2; x++)
        {
            panel_buffer[x] = new JPanel();
            panel_buffer[x].setBackground(color_north_bkgd);
            panel_buffer[x].setLayout(new GridBagLayout());
        }

        // Add Blank Panels & Control Panels to Above
        for(int x=0; x<3; x++)
        {
            JPanel temp_handle = new JPanel();
            temp_handle.setBackground(color_north_bkgd);
            temp_handle.add(new JLabel(new ImageIcon(image_title_bar[3])));
            panel_buffer[0].add(temp_handle,      new GridBagConstraints(x,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            panel_buffer[1].add(panel_control[x], new GridBagConstraints(x,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        }

        // Assemble Conditional Border Buffers
        if(show_border)
        {
            JPanel temp_handle = new BorderPanel(BorderPanel.SPECIAL, color_north_bkgd);
            panel_buffer[0].add(temp_handle, new GridBagConstraints(3,0,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
            panel_buffer[1].add(temp_handle, new GridBagConstraints(3,0,1,1,0,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
        }

        // Assemble Title Bar
        label_north = new JLabel(win_title);
        label_north.setFont(new Font("Arial", Font.PLAIN, win_title_size));
        label_north.setForeground(color_north_text);
        panel_north.setLayout(new GridBagLayout());
        panel_north.setBackground(color_north_bkgd);
        panel_north.add(panel_buffer[0], new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));
        panel_north.add(label_north,     new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));
        panel_north.add(panel_buffer[1], new GridBagConstraints(2,0,1,1,0,1,GridBagConstraints.EAST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0));

        // Assemble Program
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setUndecorated(true);
        window.setLayout(new GridBagLayout());
        window.getContentPane().setBackground(color_center);
        window.setSize(win_dim[0],win_dim[1]);
        window.setTitle(win_title);
        window.setIconImage(icon.getImage());
        window.add(panel_border[0], new GridBagConstraints(0,1,1,1,0,1,GridBagConstraints.WEST,     GridBagConstraints.VERTICAL,  new Insets(0,0,0,0),0,0));
        window.add(panel_border[1], new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,      new Insets(0,0,0,0),0,0));
        window.add(panel_border[2], new GridBagConstraints(1,2,1,1,1,0,GridBagConstraints.SOUTH,    GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));
        window.add(panel_border[3], new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,      new Insets(0,0,0,0),0,0));
        window.add(panel_border[4], new GridBagConstraints(2,1,1,1,0,1,GridBagConstraints.EAST,     GridBagConstraints.VERTICAL,  new Insets(0,0,0,0),0,0));
        window.add(panel_north,     new GridBagConstraints(0,0,3,1,1,0,GridBagConstraints.NORTH,    GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0));

        // Register Moving Components
        mover = new ComponentMover(JFrame.class, panel_north);
        mover.setChangeCursor(false);

        // Set Location
        if(custom_location)
            window.setLocation(win_loc[0],win_loc[1]);
        else
        {
            int[] screen_dim = new int[2];
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            screen_dim[0] = gd.getDisplayMode().getWidth();
            screen_dim[1] = gd.getDisplayMode().getHeight();
            window.setLocation(((screen_dim[0]/2)-(win_dim[0]/2)),((screen_dim[1]/2)-(win_dim[1]/2)));
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



    ////////////////////////////
    //---  Private Methods ---//
    ////////////////////////////

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
            image_title_bar = new Image[]{ImageIO.read(new File("src\\main\\resources\\images\\Minimize (Custom).png")),
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
    private void define_border_adapters()
    {
        // Resize West Wall
        border_adapter[0] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() - e.getX() > win_min[0])
                {
                    window.setSize(window.getWidth()-e.getX(),window.getHeight());
                    window.setLocation(init_pos_x+e.getX(),init_pos_y);
                }
            }
        };

        // Resize Southwest Corner
        border_adapter[1] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() - e.getX() > win_min[0])
                {
                    window.setSize(window.getWidth()-e.getX(),window.getHeight());
                    window.setLocation(init_pos_x+e.getX(),init_pos_y);
                }
                if(window.getHeight() + e.getY() > win_min[1])
                    window.setSize(window.getWidth(),window.getHeight()+e.getY());
            }
        };

        // Resize South Wall
        border_adapter[2] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getHeight() + e.getY() > win_min[1])
                    window.setSize(window.getWidth(),window.getHeight()+e.getY());
            }
        };

        // Resize Southeast Corner
        border_adapter[3] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() + e.getX() > win_min[0])
                    window.setSize(window.getWidth()+e.getX(),window.getHeight());
                if(window.getHeight() + e.getY() > win_min[1])
                    window.setSize(window.getWidth(),window.getHeight()+e.getY());
            }
        };

        // Resize East Wall
        border_adapter[4] = new MouseAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                int init_pos_x = (new Point(window.getLocation()).x);
                int init_pos_y = (new Point(window.getLocation()).y);
                if(window.getWidth() + e.getX() > win_min[0])
                    window.setSize(window.getWidth()+e.getX(),window.getHeight());
            }
        };
    }

    // Define Control Mouse Behavior
    private void define_control_adapters()
    {
        // Minimize Button
        control_adapter[0] = new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e){
                panel_control[0].setBackground(color_control[1]);}

            public void mouseExited(MouseEvent e){
                panel_control[0].setBackground(color_control[0]);}

            public void mousePressed(MouseEvent e){
                panel_control[0].setBackground(color_control[2]);}

            public void mouseReleased(MouseEvent e)
            {
                // If On Button, Minimize.
                if(e.getX() < button_ctl_dim && e.getX() >= 0 && e.getY() < button_ctl_dim && e.getY() >= 0)
                {
                    panel_control[0].setBackground(color_control[1]);
                    window.setExtendedState(JFrame.ICONIFIED);
                }
                else
                    panel_control[0].setBackground(color_control[0]);

            }
        };

        // Maximize Button
        control_adapter[1] = new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e){
                panel_control[1].setBackground(color_control[1]);}

            public void mouseExited(MouseEvent e){
                panel_control[1].setBackground(color_control[0]);}

            public void mousePressed(MouseEvent e){
                panel_control[1].setBackground(color_control[2]);}

            public void mouseReleased(MouseEvent e)
            {
                // If On Button, Change.
                if(e.getX() < button_ctl_dim && e.getX() >= 0 && e.getY() < button_ctl_dim && e.getY() >= 0)
                {
                    if(window.getExtendedState() == JFrame.MAXIMIZED_BOTH)
                        window.setExtendedState(JFrame.NORMAL);
                    else
                        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    panel_control[1].setBackground(color_control[1]);
                }
                else
                    panel_control[1].setBackground(color_control[0]);
            }
        };

        // Close Button
        control_adapter[2] = new MouseAdapter()
        {
            public void mouseEntered(MouseEvent e){
                panel_control[2].setBackground(color_control[1]);}

            public void mouseExited(MouseEvent e){
                panel_control[2].setBackground(color_control[0]);}

            public void mousePressed(MouseEvent e){
                panel_control[2].setBackground(color_control[2]);}

            public void mouseReleased(MouseEvent e)
            {
                // If On Button, Close.
                if(e.getX() < button_ctl_dim && e.getX() >= 0 && e.getY() < button_ctl_dim && e.getY() >= 0)
                {
                    panel_control[2].setBackground(color_control[1]);
                    WindowEvent close = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
                    window.dispose();
                    System.exit(0);
                }
                else
                    panel_control[2].setBackground(color_control[0]);
            }
        };
    }


}


/*
  ///////////////////////////////////////////////////////
  //                                                   //
  //             CLEAN WINDOW - Dev. Notes             //
  //                                                   //
  ///////////////////////////////////////////////////////


  > GUI Design Mechanics

						  ,---------------------------.
						  |-|-----------------------|-|
						  | |                       | |
						  | |                       | |
						  | |    (Program Panel)    | |
						  | |                       | |
						  | |                       | |
						  |-|-----------------------|-|
						  '---------------------------'

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
        entire window (including the task bar). :D


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