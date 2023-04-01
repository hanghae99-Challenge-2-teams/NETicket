package com.example.neticket.config;

import com.example.neticket.exception.ExceptionHandlerFilter;
import com.example.neticket.jwt.JwtAuthFilter;
import com.example.neticket.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        일단은 회원가입, 로그인, get으로 들어오는 상품조회만 허용하고 나머지 인증 받게 설정.
//        jwt필터와 필터예외처리필터 2개 생성
        http.authorizeRequests().antMatchers("/neticket/login").permitAll()
                .antMatchers("/neticket/signup").permitAll()
                .antMatchers("/neticket/events").permitAll()
                .antMatchers("/neticket/events/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthFilter.class);

//        cors 설정
//        http.cors();

//        http.formLogin().loginPage("/api/auth/login-page").permitAll();

        return http.build();
    }

//    CORS는 일단 주석 처리

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 프론트 로컬 서버 허용
////        config.addAllowedOrigin("http://localhost:3000");
//
//        // 특정 헤더를 클라이언트 측에서 사용할 수 있게 지정
//        // 만약 지정하지 않는다면, Authorization 헤더 내의 토큰 값을 사용할 수 없음
//        config.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);
//
//        // 본 요청에 허용할 HTTP method(예비 요청에 대한 응답 헤더에 추가됨)
//        config.addAllowedMethod("*");
//
//        // 본 요청에 허용할 HTTP header(예비 요청에 대한 응답 헤더에 추가됨)
//        config.addAllowedHeader("*");
//
//        // 기본적으로 브라우저에서 인증 관련 정보들을 요청 헤더에 담지 않음
//        // 이 설정을 통해서 브라우저에서 인증 관련 정보들을 요청 헤더에 담을 수 있도록 해줍니다.
//        config.setAllowCredentials(true);
//
//        // allowCredentials 를 true로 하였을 때,
//        // allowedOrigin의 값이 * (즉, 모두 허용)이 설정될 수 없도록 검증합니다.
//        config.validateAllowCredentials();
//
//        // 어떤 경로에 이 설정을 적용할 지 명시합니다. (여기서는 전체 경로)
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }


}
