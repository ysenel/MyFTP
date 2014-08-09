package controller;

public class UserID
{
	private String password, login, server;
	
	public UserID(String log, String pass, String server)
	{
		this.password = pass;
		this.login = log;
		this.server = server;
	}
	
	public String getPassword() {
		return password;
	}

	public String getLogin() {
		return login;
	}

	public String getServer() {
		return server;
	}


}
