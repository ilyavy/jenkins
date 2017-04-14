package hudson.scm;

public class CommitAuthor {
    private String name;
    
    public CommitAuthor(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
