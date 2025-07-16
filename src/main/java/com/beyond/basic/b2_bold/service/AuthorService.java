package com.beyond.basic.b2_bold.service;

import com.beyond.basic.b2_bold.domain.Author;
import com.beyond.basic.b2_bold.dto.AuthorCreateDto;
import com.beyond.basic.b2_bold.dto.AuthorDetailDto;
import com.beyond.basic.b2_bold.dto.AuthorListDto;
import com.beyond.basic.b2_bold.dto.AuthorUpdatePwDto;
//import com.beyond.basic.b2_bold.repository.AuthorJdbcRepository;
//import com.beyond.basic.b2_bold.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_bold.repository.AuthorJpaRepository;
import com.beyond.basic.b2_bold.repository.AuthorMybatisRepository;
import com.beyond.basic.b2_bold.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service // Component 로도 대체 가능(트랜잭션 처리가 없는 경우)
// 아래 Transactional 어노테이션을 사용하면 Spring에서 메서드 단위로 트랜잭션 처리를 하고,  만약 예외(unchecked) 발생시 자동 롤백처리 지원
@Transactional
public class AuthorService {

    // 의존성 주입(DI)방법1. Autowired 어노테이션 사용 -> 필드 주입
//    @Autowired
//    private AuthorRepositoryInterface authorRepository;

//    // 의존성 주입(DI)방법2. 생성자 주입(가장 많이 쓰는 방식)
//    // 장점1) final을 통해 상수로 사용가능(안정성 향상, 장점2) 다형성 구현가능 장점3) 순환참조방지(컴파일 타임에 check)
//    private final AuthorMemoryRepository authorRepository;
//    // 싱글톤 객체로 만들어지는 시점에 spring에서 authorRepository 객체를 매개변수로 주입
//    @Autowired // 생성자가 하나밖에 없는 경우 Autowired 생략 가능
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }

    // 의존성 주입(DI)방법3. RequiredArgs 어노테이션 사용 -> 반드시 초기화 되어야하는 필드(final 등)을 대상으로 생성자를 자동생성
    // 다형성 설계는 불가능
    private final AuthorRepository authorRepository;

    // 객체조립은 서비스 담당
    public void save(@RequestBody AuthorCreateDto authorCreateDto) {
        //이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("email already exists");
        }
        // Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
       // toEntity패턴을 통해 Author 객체 조립을 공통화

        Author author = authorCreateDto.authorToEntity();
        this.authorRepository.save(author);
    }
    // 트랜잭션이 피료없는 경우, 아래와 같이 명시적으로 제외 (ex. 조회만 있는 경우)
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
        return authorRepository.findAll().stream().map(a -> a.listFromEntity()).toList();
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("id not found"));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }


    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("email not found"));
        // dirty checking: 객체를 수정한 후 별도의 update쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 변경사항 자동DB반영
        author.updatePassword(authorUpdatePwDto.getPassword());
    }

    public void delete(Long id) {
    Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("id not found"));
    authorRepository.delete(author);

    }
}
