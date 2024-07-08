package it.capstone.prestigecarboutique.security;


import it.capstone.prestigecarboutique.entity.User;
import it.capstone.prestigecarboutique.exception.NotFoundException;
import it.capstone.prestigecarboutique.exception.UnauthorizedException;
import it.capstone.prestigecarboutique.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;
    @Autowired
    UserService userService;

    private static final List<String> EXCLUDE_URLS = List.of("/auth/**", "/api/prestigecarboutique/allauto");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        response.setHeader("Access-Control-Allow-Origin", "https://prestige-car-boutique-website-eb1v.vercel.app");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Error in authorization, token is null or Bearer is null");
        }else{
            String token = authHeader.substring(7);

            jwtTool.verifyToken(token);

            int userIdInsideToken = jwtTool.getIdFromToken(token);

            Optional<User> userOptional = userService.getUserById(userIdInsideToken);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                Authentication authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                throw new NotFoundException("User with id: "+userIdInsideToken+" ,not found");
            }

            filterChain.doFilter(request,response);
        }

    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return EXCLUDE_URLS.stream().anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
    }

}

