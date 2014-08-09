package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;

import javax.swing.JPanel;

import connection.SftpConnection;
import controller.MainPanelController;
import controller.MainPanelController;

public class MainPanel extends JPanel
{
	private int window_height = 600;
	private int window_width = 800;
	
	private ComputerFileBrowser localFileBrowser;
	private LoginPanel loginPanel;
	private MainPanelController mainPanelController;
	
	public MainPanel(SftpConnection sftpConnection)
	{
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(window_width, window_height));
		
		localFileBrowser = new ComputerFileBrowser();
		loginPanel = new LoginPanel();
		
		
		this.add(loginPanel, BorderLayout.PAGE_START);
		this.add(localFileBrowser, BorderLayout.CENTER);
		this.mainPanelController = new MainPanelController(sftpConnection, this);
		loginPanel.getConnectionButton().addActionListener(mainPanelController);
		
		
	}
	
	public LoginPanel getLoginPanel(){
		return this.loginPanel;
	}
	
	public Path getLocalFileSelected()
	{
		return localFileBrowser.getCurrentPath();
	}

}
