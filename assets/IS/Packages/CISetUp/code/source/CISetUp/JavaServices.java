package CISetUp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
// --- <<IS-END-IMPORTS>> ---

public final class JavaServices

{
	// ---( internal utility methods )---

	final static JavaServices _instance = new JavaServices();

	static JavaServices _newInstance() { return new JavaServices(); }

	static JavaServices _cast(Object o) { return (JavaServices)o; }

	// ---( server methods )---




	public static final void copyDirectory (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(copyDirectory)>> ---
		// @sigtype java 3.5
		// [i] field:0:required localGitHubRepoLocation
		// [i] field:0:required sagCodebaseLocation
		// [i] field:0:required packageName
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	localGitHubRepoLocation = IDataUtil.getString( pipelineCursor, "localGitHubRepoLocation" );
			String	sagCodebaseLocation = IDataUtil.getString( pipelineCursor, "sagCodebaseLocation" );
			String	packageName = IDataUtil.getString( pipelineCursor, "packageName" );
		pipelineCursor.destroy();	
		
		//Forming Fully Qualified Path using Package Name
			localGitHubRepoLocation=localGitHubRepoLocation.concat("\\").concat(packageName);
			sagCodebaseLocation=sagCodebaseLocation.concat("\\").concat(packageName);
			
		//Business Logic
		Path sourceDir = Paths.get(localGitHubRepoLocation);
		Path targetDir = Paths.get(sagCodebaseLocation);
		 
		try {
			Files.walkFileTree(sourceDir, new CopyDir(sourceDir, targetDir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	public static class CopyDir extends SimpleFileVisitor<Path> {
	private Path sourceDir;
	private Path targetDir;
	 
	public CopyDir(Path sourceDir, Path targetDir) {
	    this.sourceDir = sourceDir;
	    this.targetDir = targetDir;
	}
	 
	public FileVisitResult visitFile(Path file,BasicFileAttributes attributes) {
	 
	    try {
	        Path targetFile = targetDir.resolve(sourceDir.relativize(file));
	        Files.copy(file, targetFile);
	    } catch (IOException ex) {
	        System.err.println(ex);
	    }
	 
	    return FileVisitResult.CONTINUE;
	}
	 
	public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes attributes) {
	    try {
	        Path newDir = targetDir.resolve(sourceDir.relativize(dir));
	        Files.createDirectory(newDir);
	    } catch (IOException ex) {
	        System.err.println(ex);
	    }
	 
	    return FileVisitResult.CONTINUE;
	}
	}
	// --- <<IS-END-SHARED>> ---
}

