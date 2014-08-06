package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;

import javax.swing.JPanel;

public class MainPanel extends JPanel
{
	private int window_height = 600;
	private int window_width = 800;
	
	private ComputerFileBrowser localFileBrowser;
	private LoginPanel loginPanel;
	
	public MainPanel()
	{
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(window_width, window_height));
		
		localFileBrowser = new ComputerFileBrowser();
		loginPanel = new LoginPanel();
		
		this.add(loginPanel, BorderLayout.PAGE_START);
		this.add(localFileBrowser, BorderLayout.CENTER);
		
	}
	
	public Path getLocalFileSelected()
	{
		return localFileBrowser.getCurrentPath();
	}

}
