package com.beyond.basic.b2_bold.Common;


import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Author.domain.Role;
import com.beyond.basic.b2_bold.Author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner를 구현함으로써 해당 컴포넌트가 스프링빈으로 등록되는 시점에 run메서드 자동실행.
@Component
@RequiredArgsConstructor

public class InitialDataloader implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(authorRepository.findByEmail("admin@naver.com").isPresent()) {
            return;
        }
        Author author = Author.builder()
                .name("우영짱짱맨")
                .email("admin@naver.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("12341234"))
                .build();
        authorRepository.save(author);
    }

}
