package com.springboot.scrum_tracker.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.scrum_tracker.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtil;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String jwtToken = null;
        String username = null;
        try {
        	if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
                username = jwtUtil.extractUsername(jwtToken);
                System.out.println("In JWT FIlter: "+ username);
            }
            
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            	System.out.println("in IFFF!!");
            	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            	if(jwtUtil.validateToken(jwtToken)) {
            		System.out.println("Validated!!!!");
            		UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
            		System.out.println(userDetails.getAuthorities());
            		auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            		SecurityContextHolder.getContext().setAuthentication(auth);
            		
            		SecurityContext sec=SecurityContextHolder.getContext();
            		System.out.println("Security Context "+ sec.getAuthentication().getAuthorities());
            	}
            }
//            response.addHeader("test","testabcdfef");
            filterChain.doFilter(request, response);
        }
        catch(ExpiredJwtException e) {
        	response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(e.getLocalizedMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return;
        }
        
		
	}

}
