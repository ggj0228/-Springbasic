package com.beyond.basic.b2_bold.Common;

import com.beyond.basic.b2_bold.Common.CommonDto.JwtAuthorizationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
// PreAuthorize 어노테이션을 사용하기 위한 설정
@EnableMethodSecurity
public class SecurityConfig {

    // 내가 만든 객체는 Component, 외부 라이브러리를 활용한 객체는 Bean + Configuration
    // Bean은 메서드 위에 붙여 Return되는 객체를 싱글톤객체로 생성. Component는 클래스 위에 붙여 클래스 자체를 싱글톤 객체로 설정
    // filter 계층에서 filter로직을 커스텀.

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationHandler jwtAuthenticationHandler;
    private final JwtAuthorizationHandler jwtAuthorizationHandler;
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                // cors: 특정도메인에 대한 허용정책, postman은 cors정책에 적용x
                .cors( c -> c.configurationSource(corsConfiguration()))
                // csrf( 보안공격 중 하나로써 타사이트의 쿠키값을 꺼내서 탈취하는 공격) 비활성화
                // 세션기반 로그인(mvc, ssr)에서는 csrf별도 설정하는 것이 일반적이고
                // 토큰기반 로그인(rest api서버, csr)에서는 csrf설정하지 않는 것이 일반적
                .csrf(AbstractHttpConfigurer::disable)
                // http basic은 email/pw를 인코딩하여 인증하는 방식으로 간단한 인증의경우에만 사용.
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션로그인 방식 비활성화 (세션은 상태가 full임 -> 서버가 가지고 있음)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 이 단계에서 token을 검증하고,  token 검증을 통해 Autentication 객체를 생성하겠다
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e
                        -> e.authenticationEntryPoint(jwtAuthenticationHandler)     // 401에러의 경우(토큰의 잘못)
                        .accessDeniedHandler(jwtAuthorizationHandler)              // 403에러의 경우(NO! 권한)

                )
                // 예외 api 정책 설정
                // authenticated() : 예외를 제외한 모든 요청에 대해서 Authentication객체가 생성되기를 요구
                .authorizeHttpRequests(a -> a.requestMatchers("/author/create", "/author/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }

    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP(get, post 등) 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더요소(Authorization 등) 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url패턴에 대해 cors설정 적용
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
