package com.ssafy.project.util;

import static io.jsonwebtoken.Jwts.SIG.RS256;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component // 객체
@Slf4j // 로깅
@RequiredArgsConstructor
public class JWTUtil {


    @Value("${jwt.private.key.path}") // application.properties의 값을 읽어오는 Annotation
    private String privateKeyPath;
    private PrivateKey privateKey;

    @Value("${jwt.public.key.path}")
    private String publicKeyPath;
    private PublicKey publicKey;

    @Value("${jwt.access-token.expiration}")
    private long jwtAccessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long jwtRefreshTokenExpiration;

    private final UserDetailsService userDetailsService;

    /**
     * Key 파일 읽어 실제 데이터 추출.
     *
     * @param file Key 파일
     * @return BASE64 디코딩된 Key 데이터.
     * @throws IOException
     */
    private byte[] readKeyBytes(ClassPathResource keyFile) throws IOException {

        String keyContent = new String(keyFile.getInputStream().readAllBytes());

        // PEM 형식의 헤더와 푸터 제거
        keyContent = keyContent.replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        // Base64 디코딩
        return java.util.Base64.getDecoder().decode(keyContent);
    }

    // 생성자가 호출된 후에 호출이 되도록 명시하는 Annotation.
    @PostConstruct // Post: 다음 (후), Construct: 생성--> 생성자
    public void init() throws Exception {

        // RSA 형식 지정
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Private Key Load (PKCS#8 형식)
        byte[] privateKeyBytes = readKeyBytes(new ClassPathResource(privateKeyPath));
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey = keyFactory.generatePrivate(privateKeySpec);

        // Public Key Load (X.509 형식)
        byte[] publicKeyBytes = readKeyBytes(new ClassPathResource(publicKeyPath));
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = keyFactory.generatePublic(publicKeySpec);

    }

    /**
     * Token Claims 추출.
     *
     * @param token JWT token
     * @return all claims
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    /**
     * 사용자 이름 추출
     *
     * @param token JWT token
     * @return 사용자 이름
     */
    public String extractId(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * 만료 일자 추출
     *
     * @param token JWT token
     * @return 만료 일자
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * 만료 여부
     *
     * @param token JWT token
     * @return 만료 여부
     */
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 토큰 검증 (서명 및 만료시간만 검증)
     *
     * @param token JWT token
     * @throws JwtException 토큰이 유효하지 않을 경우 발생하는 예외
     */
    public void validateToken(String token) {
        // 토큰 검증 - 실패 시 관련 예외가 발생함

        Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token);

    }

    public String generateAccessToken(Long id, String email, String name, String role) {

        String jwtId = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("name", name);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtAccessTokenExpiration))
                .id(jwtId)
                .signWith(privateKey, RS256)
                .compact();
    }

    public String generateRefreshToken(Long id, String email) {

        String jwtId = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("email", email);

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(id))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtRefreshTokenExpiration))
                .id(jwtId)
                .signWith(privateKey, RS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(extractId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
    // Authorization 헤더에서 accessToken 추출
    public String resolveToken(String authorization) {
    	if(authorization != null && authorization.startsWith("Bearer "))
    		return authorization.substring(7);
    	return null;
    }

}