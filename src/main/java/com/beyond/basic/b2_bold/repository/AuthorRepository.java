package com.beyond.basic.b2_bold.repository;

import com.beyond.basic.b2_bold.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// SpringDataJpa를 사용하기 위해서는 JpaRepository를 상속해야하고, 상속시에 Entity명과 pk타입을 지정해야함.
//JpaRepository를 상속함으로서 JpaRepository의 주요기능(각종 CRUD기능이 사전 구현) 상속

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // save, findAll, findById, 등은 사전에 구현
    // 그외에 다른 컬럼으로 조회할 떄는 findBy + 컬럼명 형식으로 선언만 하면 실행시점에 자동구현

    Optional<Author> findByEmail(String email);
}

