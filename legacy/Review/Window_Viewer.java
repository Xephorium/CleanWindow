/*
  Chris Cruzen                           04/29/2014

  This program gives a basic example of each of the
  types of windows I know how to create in Java. To
  run, see "Window_Runner.java".

  Additional Notes:
    http://www.java2s.com/Tutorial/Java/0240__Swing/DifferencebetweenJFrameandJWindow.htm

  Included:
    - JFrame  (Standard Application Window)
    - JWindow (No Title Bar/Action Buttons)

*/

import javax.swing.JFrame;
import javax.swing.JWindow;

public class Window_Viewer
{
  //---- Variable Declarations ----\\
  JFrame  window_1;
  JWindow window_2;
  int dim_x = 300;
  int dim_y = 300;

  //--------- Constructor ---------\\
  public Window_Viewer()
  {
    // Instantiate Windows
    window_1 = new JFrame("JFrame Example");
    window_2 = new JWindow();

    // Set Window Dimensions
    window_1.setSize(dim_x,dim_y);
    window_2.setSize(dim_x,dim_y);

    // Set Window Locations
    window_1.setLocation(100,100);
    window_2.setLocation(500,100);

    // Set Windows Visible
    display_Windows();

  }


  //----------- Methods -----------\\
  private void display_Windows()
  {
    window_1.setVisible(true);
    window_2.setVisible(true);
  }
}
