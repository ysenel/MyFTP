package controller;

import gui.LoginPanel;
import gui.MainPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import connection.SftpConnection;

public class MainPanelController implements ActionListener{
	
	private SftpConnection sftpConnection;
	private MainPanel mainPanel;
	
	public MainPanelController(SftpConnection connection, MainPanel panel){
		this.sftpConnection = connection;
		this.mainPanel = panel;
		}


	public void actionPerformed(ActionEvent e) {
		LoginPanel loginPanel = this.mainPanel.getLoginPanel();
		if (e.getSource() == loginPanel.getConnectionButton())
			this.sftpConnection.connect(loginPanel.getUserId());

		}

}
