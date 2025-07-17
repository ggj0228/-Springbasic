package com.beyond.basic.b2_bold.Author.domain;


import com.beyond.basic.b2_bold.Author.dto.AuthorListDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우 Entity를 반드시 붙여야하는 어노테이션
// JPA으 EntityManager에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성컨텍스트(엔터티의 현재상황)을 통해 db 데이터 관리.
@Entity
@Builder
public class Author {
    @Id //pk설정(바로 밑에 있는 Entity를)
    // identity: auto_increment
    // auto: id생성 전략을 jpa에게 자동 설정하도록 위임
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 칼람에 별다른 설저잉 없을 경우 default varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    // @Column(name = "pw") : 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING) // DB에 데이터가 들어갈 때 문자열로 저장 (default는 숫자로 0,1,2 .. 로 들어가짐)
    @Builder.Default // 빌더패턴에서 변수 초기화(디폴트값)시 Builder.Default어노테이션 필수
    private Role role = Role.USER;


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
//    public AuthorDetailDto detailFromEntity () {
//        return new AuthorDetailDto(this.id, this.name, this.password);
//    }
    public AuthorListDto listFromEntity () {
        return new AuthorListDto(this.id, this.name, this.password);
    }
}
