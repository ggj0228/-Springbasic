package com.beyond.basic.b1_hello.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;
    private String email;
    private List<Score> scores;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    // 내부 클래스에서 어노테이션을 다시 선언해줘야 함
    private static class Score {
        private String subject;
        private int point;
    }
}
