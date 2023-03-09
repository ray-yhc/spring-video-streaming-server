package com.ray.hlsstreamingserver.utils;


import com.ray.hlsstreamingserver.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Service @Slf4j
public class JwtService {

    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    /**
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(Long userIdx){
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        log.info("Secret key: " + secretString);

        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000* Secret.JWT_EXPIRE_TIME_SEC)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Header에서 X-ACCESS-TOKEN 으로 JWT 추출
     * @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /**
     * JWT에서 userIdx 추출
     * @return int
     * @throws ResponseStatusException
     */
    public Long getUserIdx() {
        //1. http 헤더에서 JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT를 입력해주세요."); // todo: 예외 코드, 내용 수정하기
        }

        return parseUserIdx(accessToken);
    }

    public Long parseUserIdx(String accessToken) {
        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT입니다."); // todo: 예외 코드, 내용 수정하기
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx",Long.class);  // jwt 에서 userIdx를 추출합니다.
    }

}