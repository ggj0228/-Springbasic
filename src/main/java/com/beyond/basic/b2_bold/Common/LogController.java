package com.beyond.basic.b2_bold.Common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// logback 객체 만드는 방법2.
@Slf4j
@RestController
public class LogController {

    // 로그백 객체 만드는 방법1.
//    private static final Logger logger = LoggerFactory.getLogger(LogController.class);
    @GetMapping("/log/test")
    public String logTest() {
        // 기존의 system print는 스프링에서는 잘 사용되지 않음.
        // 이유 1) 성능이 떨어짐 2) 로그분류작업 불가

        System.out.println("hello world");

//        // 가장 많이 사용되느 로그 라이브러리: logback
//        // 로그레벨(프로젝트차원에 설정): trace < debug < info < error (스프링에 아무런 설정을 안 했을 시 info레벨 이상만 출력됨)
//        logger.trace("teace 로그입니다.");
//        logger.debug("debug 로그입니다.");
//        logger.info("info 로그입니다.");
//        logger.error("error 로그입니다.");

        // Slf4j 어노테이션을 선언시, log라는 변수로 logback 객체 사용가능
        log.info("Slf4j 테스트");

        try {
            log.info("에러테스트 시작");
            throw new RuntimeException("에러 테스트");
        } catch (RuntimeException e) {
            // +보다 ,로 두 메시지를 이어서 출력하는 것이 성능에 더 좋음.
            log.error("에러메시지 : ", e);
            // e.printStackTrace(); // 성능 문제로 logback을 사용하는 것이 좋음.

        }

        return "ok";
    }
}
