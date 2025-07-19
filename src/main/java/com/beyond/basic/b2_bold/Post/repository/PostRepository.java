package com.beyond.basic.b2_bold.Post.repository;

import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // select * from post where author_id = ? and title = ?;
    // List<Post> findByauthorIdAndTitleOrderBy(Long author, String title);
    // select * from post where author_id = ? and title = ? order by createdTime desc;
    // List<Post> findByauthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title);

    // 변수명은 author지만, authoorid로도 조회가 가능하다
//    List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthor(Author author);

    // jpql을 사용한 일반 inner join
    // jpql은 기본적으로 lazy로딩을 지원하므로 innerjoin으로 filterling은 하되 post객체만 조회 -> N+1문제 여전함
    // 순수 raw쿼리 : select p.* from post p inner join author a on a.id=p.auhtor_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

    // jpql을 사용한 fetch inner join
    // join시 post객체 뿐만 아니라 author객체까지 조회 -> N+1문제 해결
    // 순수 raw쿼리 : select * from post p inner join author a on a.id=p.auhtor_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();



    // paging처리 & delYn 적용
    // import org.springframework.data.domain.Pageable;를 import
    // Page객체 안에 List<Post> 포함, 전체페이지수 등의 정보 포함.
    // Pageable 객체 안에는 페이지size, 페이지번호, 정렬기준 등이 포함.
    Page<Post> findAllByDelYn(Pageable pageable, String delYn);


}
