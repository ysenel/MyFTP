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


public class Connection {
	
	private String server;
	private String pass;
	private String login;
	private Boolean connected;
	private ChannelSftp channelSftp;
	private Session session;
	private Channel channel;
	
	public Connection(String server, String login, String pass){
		this.connected = false;
		this.login = login;
		this.pass = pass;
		this.server = server;
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
	
	public void connect(){
		int port = 22;
		JSch jsch = new JSch();
		try {
			
			this.setSession(jsch.getSession(this.login, this.server, port));
			this.session.setPassword(pass.getBytes(Charset.forName("ISO-8859-1")));
	        Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect();
	        
	        this.setChannel(session.openChannel("sftp"));
	        this.channel.connect();
	        this.setChannelSftp((ChannelSftp) channel);
	        this.setConnected(true);
	        
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	public void download(String sourceFile, String destinationFile){
		try {
			this.channelSftp.get(sourceFile, destinationFile);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void upload(String sourceFile, String destionationFile){
		
	}
	
	public ArrayList<String> ls(){
		ArrayList<String> fileAndDirectory = new  ArrayList();
		try {
			ArrayList<String> tmp = new  ArrayList();
			Vector filelist = ((ChannelSftp) this.channel).ls(".");
	        for(int i=0; i<filelist.size();i++){
	            tmp.add(filelist.get(i).toString());
			  String[] tokens = tmp.get(i).trim().split(" ");
			  fileAndDirectory.add(tokens[tokens.length-1]);
	        }
	           
		} catch (SftpException e) {
			// TODO Auto-generated catch block
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
