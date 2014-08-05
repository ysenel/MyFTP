package file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryFilter implements DirectoryStream.Filter<Path>
{
	
	@Override
    public boolean accept(Path path) throws IOException 
    {
        return (path.getFileName().toString().startsWith(".")) ? false : true;
		//return true;
    }
}