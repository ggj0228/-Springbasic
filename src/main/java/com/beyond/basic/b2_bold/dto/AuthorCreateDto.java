package com.beyond.basic.b2_bold.dto;

import com.beyond.basic.b2_bold.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Data // dto계층은 데이터의 안정성이 엔터티만큼 중요치 않으므로 setter도 일반적으로 추가

public class AuthorCreateDto {
    private String name;
    private String email;
    private String password;


    public Author authorToEntity () {
        return new Author(this.name, this.email, this.password);
    }
}


