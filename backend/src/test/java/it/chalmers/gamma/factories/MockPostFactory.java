package it.chalmers.gamma.factories;

import it.chalmers.gamma.db.entity.Text;
import it.chalmers.gamma.domain.dto.post.PostDTO;
import it.chalmers.gamma.service.PostService;
import it.chalmers.gamma.utils.GenerationUtils;
import java.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockPostFactory {

    @Autowired
    private PostService postService;

    public PostDTO generatePost() {
        return generatePost(GenerationUtils.generateText(), GenerationUtils.generateEmail());
    }
    public PostDTO generatePost(Text name, String email) {
        return new PostDTO(
                UUID.randomUUID(),
                name,
                email
        );
    }

    public PostDTO savePost(PostDTO post) {
        return this.postService.addPost(post.getPostName());
    }

}
