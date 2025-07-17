package com.beyond.basic.b2_bold.Post.domain;

import com.beyond.basic.b2_bold.Author.domain.Author;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;

    // fk설정 시 ManyToOne(OnetoOne etc)은 필수
    // ManyToOne에서는 default fetch EAGER(즉시로딩) : author객체를 사용하지 않아도 author테이블로 쿼리발생함
    // 그래서, 일반적으로 fetch LAZY(지연로딩) 설정 : author객체를 사용하지 않는 한, author객체로 쿼리발생X
    @ManyToOne(fetch = FetchType.LAZY) // 연결 관계 표시
    @JoinColumn(name = "author_id") // fk관계성 설정
    private Author author;
}
