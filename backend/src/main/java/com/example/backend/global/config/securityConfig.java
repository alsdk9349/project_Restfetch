package com.example.backend.global.config;

import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.member.service.MemberRedisService;
import com.example.backend.global.filter.JwtAuthFilter;
import com.example.backend.global.security.CustomUserDetailsService;
import com.example.backend.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class securityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberRedisService memberRedisService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("security");

        http.csrf((csrf) -> csrf.disable());        // CSRF 비활성화 -> JWT 쓰기 때문에
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // 세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 해주거나 사용하지 않음
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        // FormLogin, BasicHttp 비활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil, memberRedisService), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
        );

        // 권한 규칙 : login, signup 경로는 직접 service에서 인증처리 나머지는 여기서 인증
        // -> main경로도 추가해야하나?
        http.authorizeHttpRequests((authorizeRequests) -> {
            authorizeRequests.requestMatchers("/user/signup/**").permitAll() // 회원가입 api
                    .requestMatchers("/user/login/**").permitAll() // 로그인 api
                    .requestMatchers("/user/emailCheck/**").permitAll()
                    .requestMatchers("/user/emails/**").permitAll() // 이메일 중복 검사
                    .requestMatchers("/**").permitAll()
                    .requestMatchers("/observer/report").permitAll()
                    .requestMatchers("/error/**").authenticated()
                    .anyRequest().authenticated(); // 위의 것 외에는 인증 없이 접근 불가
        });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
