package com.beyond.basic.b2_bold.Post.controller;


import com.beyond.basic.b2_bold.Author.dto.CommonDto;
import com.beyond.basic.b2_bold.Post.domain.Post;
import com.beyond.basic.b2_bold.Post.dto.PostCreateDto;
import com.beyond.basic.b2_bold.Post.dto.PostDetailDto;
import com.beyond.basic.b2_bold.Post.dto.PostListDto;
import com.beyond.basic.b2_bold.Post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostCreateDto dto) {
        this.postService.save(dto);
        return new ResponseEntity<>(new CommonDto("ok", HttpStatus.CREATED.value(), "post is created"), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> postList() {
        List<PostListDto> posts = this.postService.findAll();
        return new ResponseEntity<>(new CommonDto(posts, HttpStatus.OK.value(), "list"), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> postdetail(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);
        return new ResponseEntity<>(new CommonDto(dto, HttpStatus.OK.value(), "post id found"), HttpStatus.OK);
    }
}
