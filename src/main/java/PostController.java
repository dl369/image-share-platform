import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public PostController(PostRepository postRepository, UserRepository userRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @GetMapping("/{userId}")
    public List<Post> getUserPosts(@PathVariable Long userId) {
        return postRepository.findByUserId(userId);
    }

    @PostMapping("/{userId}/upload")
    public Post uploadImage(@PathVariable Long userId, @RequestParam("image") MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        String fileName = storageService.saveImage(file); 
        Post post = new Post(fileName, user);
        return postRepository.save(post);
    }

    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Resource file = storageService.loadImage(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(file);
    }
}
