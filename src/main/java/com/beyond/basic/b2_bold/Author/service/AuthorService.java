package com.beyond.basic.b2_bold.Author.service;

import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Author.dto.*;
//import com.beyond.basic.b2_bold.repository.AuthorJdbcRepository;
//import com.beyond.basic.b2_bold.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_bold.Author.repository.AuthorRepository;
import com.beyond.basic.b2_bold.Post.domain.Post;
import com.beyond.basic.b2_bold.Post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service // Component 로도 대체 가능(트랜잭션 처리가 없는 경우)
// 아래 Transactional 어노테이션을 사용하면 Spring에서 메서드 단위로 트랜잭션처리(commit)를 하고,  만약 예외(unchecked) 발생시 자동 롤백처리 지원
// 트랜잭션의 시작/커밋/롤백 자동 처리
@Transactional
public class AuthorService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
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
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;


    // 객체조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto, MultipartFile profileImage) {
        //이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("email already exists");
        }

        // Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
       // toEntity패턴을 통해 Author 객체 조립을 공통화
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encodedPassword);
        this.authorRepository.save(author);

        // image명 설정
        String fileName = "user-"+author.getId()+ "-profileImage"+profileImage.getOriginalFilename();
        // 저장 객체 구성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(profileImage.getContentType()) // imag, jpeg, video/mp4
                .build();

        // 이미지를 업로드(byte형태로)
        // checked에러 나오는데 service 계층에서 롤백되야하니 try catch해야함
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes((profileImage.getBytes())));
        } catch (Exception e) {
            // checked를 unchecked로 바꿔서  전체 내용이 rollback되도록 예외처리
            throw new IllegalArgumentException("이미지 업로드 실패");
        }

        // 이미지 url 추출
        String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
        author.updateImageUrl(imgUrl);

//        // cascading 테스트: 회원이 생성될 떄, 곧바로 "가입인사"글을 생성하는 상황
//        // 방법 2가지
//        // 방법 1. 직접 POST객체 생성 . 저장
//        Post post = Post.builder()
//                .title("안녕하세요")
//                .contents(authorCreateDto.getName() + "입니다. 방가방가")
//                // author 객체가 db에 save되는 순간 엔터티매니저와 영속성컨텍스트에 의해 author 객체에도 id값 생성
//                .author(author)
//                .delYn("N")
//                .build();
//        //postRepository.save(post);
//        // 방법 2. cascade 옵션 활용
//        author.getPostList().add(post);
//        this.authorRepository.save(author);
    }
    // 로그인 서비스

    public Author doLogin(AuthorLoginDto dto) {
        //Author author = this.authorRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new EntityNotFoundException("wrong email"));
        Optional<Author> optionalAuthor = this.authorRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if(!optionalAuthor.isPresent()) {
            check = false;
        } else {
            // 비밀번호 일치여부 검증: matches함수를 통해서 암호되지 않은 값을 다시 암호화하여 db의 password를 검증
            if(!passwordEncoder.matches(dto.getPassword(), optionalAuthor.get().getPassword())){
                check = false;
            }
        }
        if(!check) {
            throw new IllegalArgumentException("email or password is wrong.");
        }
        return optionalAuthor.get();
    }


    // 트랜잭션이 필요없는 경우, 아래와 같이 명시적으로 제외 (ex. 조회만 있는 경우)
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
        return authorRepository.findAll().stream().map(a -> AuthorListDto.listfromEntity(a)).toList();
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("id not found"));
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        // 연관관계없이 직접 조회해서 count값 찾는 경우
//        List<Post> postList = postRepository.findByAuthor(author);
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        // OneToMany연관관계 설정 후, 직접 조회해서 count값 찾는 경우
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }

    // myPage
    @Transactional(readOnly = true)
    public AuthorDetailDto myInfo () {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("id not found"));
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        // 연관관계없이 직접 조회해서 count값 찾는 경우
//        List<Post> postList = postRepository.findByAuthor(author);
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        // OneToMany연관관계 설정 후, 직접 조회해서 count값 찾는 경우
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
