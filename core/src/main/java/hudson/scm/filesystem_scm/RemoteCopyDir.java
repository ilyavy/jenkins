package hudson.scm.filesystem_scm;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jenkinsci.remoting.RoleChecker;

import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;

public class RemoteCopyDir implements FileCallable<Boolean> {

	private static final long serialVersionUID = 1L; 

	private String sourceDir;
	
	public RemoteCopyDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}
	
	public Boolean invoke(File workspace, VirtualChannel channel) throws IOException {
		FileUtils.copyDirectory(new File(sourceDir), workspace);
		return Boolean.TRUE;
	}

    @Override
    public void checkRoles(RoleChecker checker) throws SecurityException {
        
    }
}
