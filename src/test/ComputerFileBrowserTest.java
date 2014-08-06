package test;
import gui.MainPanel;

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
        
        JFrame frame = new JFrame("MyFTP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new MainPanel());

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
