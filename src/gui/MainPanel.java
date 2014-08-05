package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public class MainPanel extends JPanel
{
	private int window_height = 600;
	private int window_width = 400;
	
	private ComputerFileBrowser localFileBrowser;
	private LoginPanel loginPanel;
	
	public MainPanel()
	{
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(800, 600));
		
		localFileBrowser = new ComputerFileBrowser();
		loginPanel = new LoginPanel();
		
		this.add(loginPanel, BorderLayout.PAGE_START);
		this.add(localFileBrowser, BorderLayout.CENTER);
		
	}

}
