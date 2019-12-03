package com.ibm.fsdsmc.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ibm.fsdsmc.utils.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authToken = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
    if (authToken != null && authToken.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
      authToken = authToken.substring(JwtTokenUtil.TOKEN_PREFIX.length());
//      log.debug("JwtAuthenticationTokenFilter - authTokenHeader = {}", authToken);
    } else {
      authToken = request.getParameter("jwttoken");
//      log.debug("JwtAuthenticationTokenFilter - authTokenParams = {}" + authToken);

      if (authToken == null) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    try {
      String username = JwtTokenUtil.getUsername(authToken); // if token invalid, will get exception here
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//        log.debug("JwtAuthenticationTokenFilter: checking authentication for user = {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (JwtTokenUtil.validateToken(authToken, userDetails)) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "N/A",
                                                                                                       userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
//      log.debug("JwtAuthenticationTokenFilter:Exception");
//      log.error(e.getMessage(), e);
    }

    filterChain.doFilter(request, response);
  }

}
