package social;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
 @Id
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "author_code")
    private Person author;
    
    private String text;
    
    private long timestamp;

    public Post() {};

    public Post(Person author, String text) {
        this.id = UUID.randomUUID().toString().replaceAll("-", ""); // Alphanumeric ID
        this.author = author;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public Person getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
