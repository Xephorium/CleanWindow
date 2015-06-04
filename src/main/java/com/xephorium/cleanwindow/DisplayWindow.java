/*
  Chris Cruzen                            06/04/2014

  This program creates an object of the Clean_Window
  class and demonstrates proper parameter/method 
  usage.

*/
package com.xephorium.cleanwindow;

public class DisplayWindow
{
    public static void main(String args[])
    {
        CleanWindow example = new CleanWindow();

        //example.set_size(600,400);

        example.create_window();
        example.display_window(true);


    }
}