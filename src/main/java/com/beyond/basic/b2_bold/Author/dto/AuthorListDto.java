package com.beyond.basic.b2_bold.Author.dto;

import com.beyond.basic.b2_bold.Author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorListDto {
    private Long id;
    private String name;
    private String password;

    public static AuthorListDto listfromEntity (Author author) {
        return AuthorListDto.builder()
                .id(author.getId())
                .name(author.getName())
                .password(author.getPassword())
                .build();
    }
}
