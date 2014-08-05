package test;
import gui.ComputerFileBrowser;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.UIManager;



public class ComputerFileBrowserTest
{
	private static boolean useSystemLookAndFeel = true;
	
    private static void createAndShowGUI() 
    {
        if (useSystemLookAndFeel) 
        {
            try 
            {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e){
                System.err.println("Couldn't use system look and feel.");
            }
        }
        
        JFrame frame = new JFrame("TopLevelContainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new ComputerFileBrowser());

        frame.pack();
        frame.setVisible(true);
    }
    

    public static void main(String[] args) 
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() 
            {
                createAndShowGUI();
            }
        });
    }

}
