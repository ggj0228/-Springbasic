package com.beyond.basic.b2_bold.Author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class AuthorListDto {
    private Long id;
    private String name;
    private String password;

    public AuthorListDto listfromEntity () {
        return new AuthorListDto(this.id, this.name, this.password);
    }
}
