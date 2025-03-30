import jakarta.persistence.*;

@Entity
public class Post {
    private Long id;
    private String filename;
    
    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    public Post(String filename, User user) {
        this.filename = filename;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
}
