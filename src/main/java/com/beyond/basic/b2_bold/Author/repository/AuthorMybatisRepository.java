package com.beyond.basic.b2_bold.Author.repository;

import com.beyond.basic.b2_bold.Author.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//mybatis 레포지터리로 만들 때 필요한 어노테이션 -> 구현체는 resources 폴더 밑에 만듦
@Mapper
public interface AuthorMybatisRepository {

    void save(Author author);

    List<Author> findAll();

    Optional<Author> findById(Long id);

    Optional<Author> findByEmail(String email);
    void delete(Long id);
}
