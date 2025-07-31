package social;
import java.util.List;
import java.util.stream.Collectors;
public class PostRepository extends GenericRepository<Post, String> {
   public PostRepository() {
        super(Post.class);  // This fixes the error
    }
    
public List<Post> findByAuthor(String authorCode) {
        return findAll().stream()
                .filter(p -> p.getAuthor().getCode().equals(authorCode))
                .collect(Collectors.toList());
    }

    public List<Post> findByAuthors(List<String> authorCodes) {
        return findAll().stream()
                .filter(p -> authorCodes.contains(p.getAuthor().getCode()))
                .collect(Collectors.toList());
    }
}
