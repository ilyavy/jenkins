package hudson.scm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;


@RunWith(MockitoJUnitRunner.class)
public class SCMTest {
    
    private final static String TEST_AUTHOR_NAME = "TestAuthor";
    
    private static CommitAuthor commitAuthor =
            new CommitAuthor(TEST_AUTHOR_NAME);
    
    private static boolean CAN_DISTINCT_AUTHORS = false;
    
    @Mock
    private Jenkins j;
    
    
    @InjectMocks
    private SCMForTests scm;
    
    
    @Mock
    private Job<?,?> project;
    
    
    @Mock
    private TaskListener listener;
    
    
    @Mock
    SCMRevisionState baseline;
    
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    
    @Test
    public void isCommitAuthorAllowedToBuildTest() {
        List<String> unAuthors = new ArrayList<String>();
        when(j.getUnathorizedAuthors()).thenReturn(unAuthors);
        assertEquals(true, scm.isCommitAuthorAllowedToBuild());
        
        unAuthors.add(TEST_AUTHOR_NAME);
        assertEquals(false, scm.isCommitAuthorAllowedToBuild());
    }
    
    
    @Test
    public void compareRemoteRevisionAuthorWithTest() throws Exception {
        // Init
        PollingResult res;
        List<String> unAuthors = new ArrayList<String>();
        unAuthors.add(TEST_AUTHOR_NAME);
        when(j.getUnathorizedAuthors()).thenReturn(unAuthors);
        
        // The system cannot distinct authors of commits
        CAN_DISTINCT_AUTHORS = false;
        res = scm.compareRemoteRevisionAuthorWith(
                project, null, null, listener, baseline);
        assertEquals(PollingResult.SIGNIFICANT, res);
        
        // The system can distinct authors and the author is unathorized
        CAN_DISTINCT_AUTHORS = true;
        res = scm.compareRemoteRevisionAuthorWith(
                project, null, null, listener, baseline);
        assertEquals(PollingResult.UNAUTHORIZED_USER, res);
        
        // The system can distinct authors and the author is athorized
        unAuthors.remove(0);
        res = scm.compareRemoteRevisionAuthorWith(
                project, null, null, listener, baseline);
        assertEquals(PollingResult.SIGNIFICANT, res);
        
        // The system can distinct authors, but author == null
        commitAuthor = null;
        res = scm.compareRemoteRevisionAuthorWith(
                project, null, null, listener, baseline);
        assertEquals(PollingResult.SIGNIFICANT, res);
    }
 
   
    
    /**
     * Test implementation of SCM (a stub)
     */
    public static class SCMForTests extends SCM {
        
        @Override
        public CommitAuthor getCommitAuthor() {
            return commitAuthor;
        }
        
        
        @Override
        public PollingResult compareRemoteRevisionWith(@Nonnull Job<?,?> project, @Nullable Launcher launcher, @Nullable FilePath workspace, @Nonnull TaskListener listener, @Nonnull SCMRevisionState baseline) throws IOException, InterruptedException {
            return PollingResult.SIGNIFICANT;
        }
        
        
        @Override
        public boolean canDistinctAuthors() {
            return CAN_DISTINCT_AUTHORS;
        }
        
        
        @Override
        public ChangeLogParser createChangeLogParser() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
