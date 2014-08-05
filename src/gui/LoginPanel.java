package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends JPanel implements ActionListener
{
	private JTextField login;
	private JTextField server_address;
	private JTextField password;
	
	private JButton connect;
	
	public LoginPanel()
	{
		this.setLayout(new FlowLayout());
		//this.setPreferredSize(new Dimension(400, 100));
		
		server_address = new JTextField(15);
		server_address.setText("server_address");
		this.add(server_address);
		
		login = new JTextField(15);
		login.setText("Login");
		this.add(login);
		
		password = new JTextField(15);
		password.setText("password");
		this.add(password);
		
		connect = new JButton("connect");
		this.add(connect);
		
		connect.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object event_source = event.getSource();
		
		if(event_source == connect)
		{
			System.out.println("address: " + server_address.getText());
			System.out.println("login: " + login.getText());
			System.out.println("pass: " + password.getText());
			
		}
	}
	
	
}
