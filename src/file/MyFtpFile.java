package file;

import java.nio.file.Files;
import java.nio.file.Path;


public class MyFtpFile implements Comparable<MyFtpFile>
{
	private Path path;
	private boolean isDir;
	private boolean isRoot;
	
	public MyFtpFile(Path p)
	{
		path = p.toAbsolutePath().normalize();
		isDir = Files.isDirectory(p);
		isRoot = path.getNameCount() == 0 ? true : false;
	}
	
	public boolean isDir() { return isDir; }
	public boolean isRoot() { return isRoot; }
	
	public String getDisplayName()
	{
		if(isRoot)
			return path.toString();
		return path.getFileName().toString();
	}
	
	public void print()
	{
		if(isDir)
			System.out.println("+" + getDisplayName());
		else if(isRoot)
			System.out.println("> " + getDisplayName());
		else
			System.out.println(getDisplayName());
	}

	public void prettyPrint()
	{
		
	}

	@Override
	public int compareTo(MyFtpFile f) 
	{
		int is_bool_this = isDir == true ? 1 : 0;
		int is_bool_f = f.isDir == true ? 1 : 0;
		
		int res = is_bool_f - is_bool_this;
		
		String filename = this.getDisplayName();
		
		if(res == 0)
			return filename.toLowerCase().compareTo(f.getDisplayName().toLowerCase());
		
		return res;
	}
	
	public Path getPath()
	{
		return path;
	}
	
	public String toString()
	{
		return getDisplayName();
	}
}
