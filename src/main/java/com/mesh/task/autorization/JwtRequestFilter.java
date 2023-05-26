package com.mesh.task.autorization;

import com.mesh.task.service.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import java.io.*;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserDetailsImpl userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                userId = jwtUtil.getUserIdFromToken(token);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserById(userId);
            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
