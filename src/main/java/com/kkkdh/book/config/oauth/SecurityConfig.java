package com.kkkdh.book.config.oauth;

import com.kkkdh.book.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests() // URL별 권한 관리의 시작점, antMatchers를 이용해 권한 관리 대상을 지정한다.
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2/**", "/profile").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated() // 설정 이외의 요청에 대한 설정
                .and()
                    .logout() // 로그아웃 관련 설정 시작점
                    .logoutSuccessUrl("/") // 로그아웃시 이동할 주소 설정
                .and()
                    .oauth2Login()// OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint() // 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                            .userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체를 등록
    }
}
