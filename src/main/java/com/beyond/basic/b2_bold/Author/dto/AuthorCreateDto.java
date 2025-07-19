package com.beyond.basic.b2_bold.Author.dto;

import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Author.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data // dto계층은 데이터의 안정성이 엔터티만큼 중요치 않으므로 setter도 일반적으로 추가

public class AuthorCreateDto {
    @NotEmpty(message = "이름을 입력을 해주세요")
    private String name;
    @NotEmpty(message = "이메일을 입력을 해주세요")
    private String email;
    @NotEmpty(message = "이름 입력을 해주세요")
    @Size(min = 8, message = "비밀번호 길이가 8자리 이상이여야합니다.")
    private String password;

    // 문자열로 값이 넘어오면 Role의 값으로 매핑됨
    private Role role = Role.USER;

    public Author authorToEntity () {
        return Author.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .build();
    }
}


