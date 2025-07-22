package com.beyond.basic.b2_bold.Post.service;

import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Author.repository.AuthorRepository;
import com.beyond.basic.b2_bold.Post.domain.Post;
import com.beyond.basic.b2_bold.Post.dto.PostCreateDto;
import com.beyond.basic.b2_bold.Post.dto.PostDetailDto;
import com.beyond.basic.b2_bold.Post.dto.PostListDto;
import com.beyond.basic.b2_bold.Post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public void save(PostCreateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // claims의 subject : email
        System.out.println(email);
        // 해당 authorid가 실제 있는지 없는지 검증 필요.
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다"));
        this.postRepository.save(dto.toEntity(author));
    }

    public PostDetailDto findById(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("id isnt exist."));
        // entity간 관계성 설정을 하지 않았을 떄
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException(("authorId isnt exist")));
//        return PostDetailDto.fromEntity(post, author);
        // entity간 관계성 설정을 통해 Author객체를 쉽게 조회하는 경우
        return PostDetailDto.fromEntity(post);


    }
    public Page<PostListDto> findAll(Pageable pageable) {
        // postList를 조회할 떄 참조관계에 있는 author까지 조회하게 되므로, N(author쿼리) +1(post쿼리)문제 발생
        // jpa는 기본 방향성이 fetch lazy이므로 참조하는 시점에 쿼리를 내보내게 되어  Join문을 만들어주지 않고 N+1문제 발생
//        List<Post> postList = postRepository.findAll();
//        List<Post> postList = postRepository.findAllJoin();


        // 페이지처리 findAll호출
        Page<Post> postList = postRepository.findAllByDelYn(pageable, "N");
        return postList.map(a -> PostListDto.fromListEntity(a));

    }
}
