package com.springboot.scrum_tracker.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims=new HashMap<>();
		claims.put("role", userDetails.getAuthorities());
		System.out.println("ROLESSSS:: "+userDetails.getAuthorities());
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
	        System.out.println(authority);
	    }
		return Jwts.builder()
				.claims(claims)
//				.add(claims)
				.subject(userDetails.getUsername())
				.header().empty().add("typ","JWT")
				.and()
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000*60*60*24)) //2 mins expiration time
				.signWith(getKey())
				.compact();
	}
	
	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	public String extractUsername(String jwtToken) {
		return extractAllClaims(jwtToken).getSubject();
	}

	private Claims extractAllClaims(String jwtToken) {
		return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
	}

	public boolean validateToken(String jwtToken) {
		return !isTokenExpired(jwtToken);
	}

	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	private Date extractExpiration(String jwtToken) {
		 return extractAllClaims(jwtToken).getExpiration();
	}
}
