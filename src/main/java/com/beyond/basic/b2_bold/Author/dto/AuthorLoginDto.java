package com.beyond.basic.b2_bold.Author.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthorLoginDto {
    @NotEmpty(message = "이메일을 입력을 해주세요")
    private String email;
    @NotEmpty(message = "이름 입력을 해주세요")
    @Size(min = 8, message = "비밀번호 길이가 8자리 이상이여야합니다.")
    private String password;


}
