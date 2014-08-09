package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.UserID;



public class LoginPanel extends JPanel
{
	private JTextField login;
	private JTextField serverAddress;
	private JPasswordField password;
	
	private JButton connect;
	
	public LoginPanel()
	{
		this.setLayout(new FlowLayout());
		//this.setPreferredSize(new Dimension(400, 100));
		
		serverAddress = new JTextField(15);
		serverAddress.setText("server_address");
		this.add(serverAddress);
		
		login = new JTextField(15);
		login.setText("Login");
		this.add(login);
		
		password = new JPasswordField(15);
		password.setText("password");
		
		this.add(password);
		
		connect = new JButton("connect");
		this.add(connect);
		
	}
	

	
	public JButton getConnectionButton(){
		return this.connect;
	}
	
	public UserID getUserId()
	{
		return new UserID(login.getText(), password.getText(),
				serverAddress.getText());
	}
	
	/*public String getServerName(){
		return this.serverAddress.getText();
	}
	
	public String getLogin(){
		return this.login.getText();
	}
	
	public String getPassword(){
		return this.password.getText();
	}*/

	

	
	
}
