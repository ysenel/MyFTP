package connection;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import controller.UserID;


public class SftpConnection {
	

	
	/******* OU ********/
	private UserID uid;
	
	private Boolean connected;
	private ChannelSftp channelSftp;
	private Session session;
	private Channel channel;
	
	
	public SftpConnection(){
		this.connected = false;
		this.channelSftp = null;
		this.session = null;
		this.channel = null;
		}
	
	
	public void setChannel(Channel channel){
		this.channel = channel;
	}
	
	public void setSession(Session session){
		this.session = session;
	}
	
	public void setChannelSftp(ChannelSftp channel){
		this.channelSftp = channel;
	}
	
	public void setConnected(Boolean connected){
		this.connected = connected;
	}
	
	public void connect(UserID uid)
	{
		this.connect(uid.getServer(), uid.getLogin(), uid.getPassword());
	}
	
	public void connect(String serverName, String userLogin, String userPassword){
		System.out.println(serverName + " " + userLogin + " " + userPassword);
		int port = 22;
		JSch jsch = new JSch();
		try {
			
			this.setSession(jsch.getSession(userLogin, serverName, port));
			this.session.setPassword(userPassword.getBytes(Charset.forName("ISO-8859-1")));
	        Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect();
	        
	        this.setChannel(session.openChannel("sftp"));
	        this.channel.connect();
	        this.setChannelSftp((ChannelSftp) channel);
	        this.setConnected(true);
	        
		} catch (JSchException e) {
			e.printStackTrace();
		}
		

		
	}
	
	public void download(String sourceFile, String destinationFile){
		try {
			this.channelSftp.get(sourceFile, destinationFile);
		} catch (SftpException e) {
			e.printStackTrace();
		}
		
	}
	
	public void upload(String sourceFile, String destionationFile){
		
	}
	
	public ArrayList<String> ls(){
		ArrayList<String> fileAndDirectory = new  ArrayList<String>();
		try {
			ArrayList<String> tmp = new  ArrayList<String>();
			Vector filelist = ((ChannelSftp) this.channel).ls(".");
	        for(int i=0; i<filelist.size();i++){
	            tmp.add(filelist.get(i).toString());
			  String[] tokens = tmp.get(i).trim().split(" ");
			  fileAndDirectory.add(tokens[tokens.length-1]);
	        }
	           
		} catch (SftpException e) {
			e.printStackTrace();
		}

		return fileAndDirectory;
		
	}
	
	public void cd(){
		
	}
	
	public void disconnect(){
        this.channelSftp.disconnect();
        this.channel.disconnect();
        this.session.disconnect();
	}
		

}
