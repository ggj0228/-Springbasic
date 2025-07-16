package com.beyond.basic.b2_bold.dto;


import com.beyond.basic.b2_bold.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class AuthorDetailDto {
    private Long id;
    private String name;
    private String password;
    // 1개의 entity로만 dto가 조립되는 것이 아니기에 dto계층에서 fromEntity를 설계
    // 2개 이상의 엔터티를 조합해서 dto를 만들어야 할 때가 있기에  fromEntity를 author에다가 종속시키는 것이 아닌
    // dto 계층에 두는 것이 낫다
    public static AuthorDetailDto fromEntity(Author author) {
        return new AuthorDetailDto(author.getId(), author.getName(), author.getEmail());
    }



}
