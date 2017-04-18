package hudson.scm.filesystem_scm;

import static org.junit.Assert.*;
import org.junit.*;

import hudson.scm.filesystem_scm.FolderDiff;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.nio.file.attribute.*;
import java.nio.file.*;

public class FolderDiffTest2 {

	@Test
	public void testGetRelative1() throws IOException {	
		String x = FolderDiff.getRelativeName("c:\\tmp\\abc\\qq\\qq.java", "c:\\tmp");
		assertEquals(x, "abc\\qq\\qq.java");
	}
	
	@Test
	public void testGetRelative2() throws IOException {
		String x = FolderDiff.getRelativeName("c:\\tmp\\abc\\qq\\qq.java", "c:\\");
		assertEquals(x, "tmp\\abc\\qq\\qq.java");
	}
	
	@Test(expected=IOException.class)
	public void testGetRelative3() throws IOException {
		String x = FolderDiff.getRelativeName("c:\\tmp\\abc\\qq\\qq.java", "c:\\tm");
	}
	
	@Test(expected=IOException.class)
	public void testGetRelative4() throws IOException {
		String x = FolderDiff.getRelativeName("c:\\tmp\\abc\\qq\\qq.java", "c:\\def");
	}
		
	@Test(expected=IOException.class)
	public void testGetRelative5() throws IOException {
		String x = FolderDiff.getRelativeName("c:\\tmp\\abc\\qq\\qq.java", "c:\\tmp//");
	}
	
	
	@Test
	public void test() throws Exception {
	    Path path = Paths.get(
	            "C:/Java/workspace/tcs-ha1/src/main/java/main/App.java");
        FileOwnerAttributeView ownerAttributeView = 
                Files.getFileAttributeView(path, FileOwnerAttributeView.class);
        UserPrincipal owner = ownerAttributeView.getOwner();
        System.out.println("owner: " + owner.getName());
	}
}
