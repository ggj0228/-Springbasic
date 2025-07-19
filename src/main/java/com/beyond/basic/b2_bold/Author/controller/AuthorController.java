package com.beyond.basic.b2_bold.Author.controller;

import com.beyond.basic.b2_bold.Author.dto.*;
import com.beyond.basic.b2_bold.Author.service.AuthorService;
import com.beyond.basic.b2_bold.Common.CommonDto.CommonDto;
import com.beyond.basic.b2_bold.Common.CommonDto.CommonErrorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequiredArgsConstructor
@RestController // controller + responsebody 조합
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;


    // 회원 가입
    @PostMapping("/create")
    // dto에 있는 validationn annotation과 controller에 @Valid 한쌍
    public ResponseEntity<String> save(@Valid @RequestBody AuthorCreateDto authorCreateDto) {
//       try {
//           authorService.save(authorCreateDto);
//           return  new ResponseEntity<>("ok", HttpStatus.CREATED);
//       } catch (IllegalArgumentException e) {
//           e.printStackTrace();
//           // 생성자 매개변수 body 부분의 객체와 header부에 상태코드
//           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//       }
//   }

        //contorllerAdvice가 없었으면 위와 갗이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    // 회원 목록 조회 :  /author/list
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return this.authorService.findAll();
    }

    // 회원 상세 조회 : (id로 조회) author/detail/1
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생시 500에러 + 스프링 포맷으로 에러 리턴

    @GetMapping ("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
       try {
           return new ResponseEntity<>(new CommonDto(this.authorService.findById(id), HttpStatus.OK.value(), "author is found"), HttpStatus.OK);

       }catch (NoSuchElementException e) {
           return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
       }
    }
    // 회원 비밀번호 수정 : author/udatepw   email, password -> json
    // get :조회, post : 등록, patch: 부분수정, put: 대체, delete: 삭제
    @PatchMapping("/updatepw")
    public void updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
       authorService.updatePassword(authorUpdatePwDto);
    }
    // 회원 탈퇴(삭제) : /author/delete/1
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
