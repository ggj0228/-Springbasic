package com.beyond.basic.b1_hello.Controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

// @Getter // 클래스 내의 모든 변수를 대상으로 getter생성
@Data // getter, setter, tostring매서드까지 모두 만들어주는 어노테이션
@AllArgsConstructor // 모든 매개변수가 있는 생성자
@NoArgsConstructor // 기본 생성자
// 기본생성자 & getter의 조합으로 parsing이 이뤄지므로 보통은 필수적 요소
public class Hello {
    private String name;
    private String email;
//    private MultipartFile photo;

}
