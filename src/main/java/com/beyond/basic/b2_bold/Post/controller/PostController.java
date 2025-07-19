package com.beyond.basic.b2_bold.Post.controller;


import com.beyond.basic.b2_bold.Common.CommonDto.CommonDto;
import com.beyond.basic.b2_bold.Post.dto.PostCreateDto;
import com.beyond.basic.b2_bold.Post.dto.PostDetailDto;
import com.beyond.basic.b2_bold.Post.dto.PostListDto;
import com.beyond.basic.b2_bold.Post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    // 페이지처리를 위한 데이타 요청 형식:  8080/post/list?page=0&size=20&sort=title,asc
    public ResponseEntity<?> postList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListDto> posts = this.postService.findAll(pageable);
        return new ResponseEntity<>(new CommonDto(posts, HttpStatus.OK.value(), "list"), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> postdetail(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);
        return new ResponseEntity<>(new CommonDto(dto, HttpStatus.OK.value(), "post id found"), HttpStatus.OK);
    }
}
