package file;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;



public class FileBrowser
{
	
	private DirectoryFilter filter;
	
	public FileBrowser()
	{
		filter = new DirectoryFilter();
	}
	
	public ArrayList<MyFtpFile> getRoots()
	{
		//ArrayList<Path> roots = new ArrayList<Path>();
		ArrayList<MyFtpFile> roots = new ArrayList<MyFtpFile>();
		Iterable<Path> roots_it = FileSystems.getDefault().getRootDirectories();
		
		for(Path root : roots_it)
			roots.add(new MyFtpFile(root));
		
		return roots;
			
	}
	
	
	/*public MyFtpFile scanForFile(Path containing_path, Path path)
	{
		MyFtpFile file = null;
		ArrayList<MyFtpFile> files = scanDirectory(containing_path);
		
	}*/
	
	public ArrayList<MyFtpFile> scanDirectory(Path path)
	{
		if(!Files.isDirectory(path))
		{
			System.out.println("Path not a directory.");
			return null;
		}
		
		ArrayList<MyFtpFile> files = new ArrayList<MyFtpFile>();

		try (DirectoryStream<Path> stream = 
				Files.newDirectoryStream(path, filter)) 
	    {
	        for (Path p : stream) 
	        	files.add(new MyFtpFile(p));
	        
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
		
		return files;
	}

}
