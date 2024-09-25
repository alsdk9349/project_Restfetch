package com.example.backend.global.util;


import com.example.backend.domain.member.dto.request.CustomUserInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration_time}")
    private long accessTokenExpTime;

    @Value("${jwt.refresh_expiration_time}")
    private long refreshTokenExpTime;

    private Key key;

    @PostConstruct // 빈이 생성되고 의존관계 주입이 완료 된 후 실행되는 `초기화 콜백`을 적용하는 어노테이션
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(CustomUserInfoDto member){
        return createToken(member, accessTokenExpTime);
    }

//    public String changeAccessToken(CustomUserInfoDto member) {
//        return createToken(member, accessTokenExpTime);
//    }


    public String createRefreshToken(CustomUserInfoDto member) { return createToken(member, refreshTokenExpTime); }

    private String createToken(CustomUserInfoDto member, long expireTime){

        Claims claims = Jwts.claims();

        log.info("[JwtUtil-createToken] email : {}", member.getEmail());

        claims.put("memberId", member.getMemberId());
        claims.put("email", member.getEmail());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant())) // 토큰 발행 시간
                .setExpiration(Date.from(tokenValidity.toInstant())) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘으로 토큰 암호화
                .compact(); // 토큰을 문자열 형태로 반환
    }


    public Long getMemberId(String token){
        return parseClaims(token).get("memberId", Long.class);
    }
    public String getMemberEmail(String token){
        return parseClaims(token).get("email", String.class);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty");
        }
        return false;
    }

    public Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch(ExpiredJwtException e){
            return e.getClaims();
        }
    }

}
