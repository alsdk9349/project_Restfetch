package com.example.backend.global.security;

import com.example.backend.domain.member.dto.request.CustomUserInfoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {


    private final CustomUserInfoDto member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<String> memberTypes = new ArrayList<>();

        return memberTypes.stream()
                .map(SimpleGrantedAuthority::new) // GrantedAuthority의 구현체로 문자열을 객체로 반환
                .collect(Collectors.toList()); // 변환된 객체를 리스트로 반환
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(member.getMemberId());
    }

    public String getEmail() {return String.valueOf(member.getEmail());}


    public long getMemberId() {return member.getMemberId();}

    @Override
    public boolean isAccountNonExpired() { // 계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠겼는지 여부, 계정이 잠겨있는 경우 인증을 방지하는데 사용
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정 활성화, 비활성화 여부
        return true;
    }
}
