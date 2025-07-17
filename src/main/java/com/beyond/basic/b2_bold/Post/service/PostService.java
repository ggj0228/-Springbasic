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
        // 해당 authorid가 실제 있는지 없는지 검증 필요.
        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다"));
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
    public List<PostListDto> findAll() {
        return this.postRepository.findAll().stream().map(a -> PostListDto.fromListEntity(a)).toList();
    }
}
