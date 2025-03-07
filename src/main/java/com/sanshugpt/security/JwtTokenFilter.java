package com.sanshugpt.security;

import java.io.IOException;


import cn.hutool.json.JSONUtil;
import com.sanshugpt.module.common.utils.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.sanshugpt.exception.CustomException;
import org.springframework.web.filter.OncePerRequestFilter;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
public class JwtTokenFilter extends OncePerRequestFilter {

  private com.sanshugpt.security.JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(com.sanshugpt.security.JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(httpServletRequest);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      //this is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
      httpServletResponse.setStatus(HttpStatus.OK.value()); // 设置 HTTP 状态码为 200
      httpServletResponse.setContentType("application/json; charset=UTF-8");
      httpServletResponse.getWriter().write(JSONUtil.toJsonStr(new Result<String>().error(-1,ex.getMessage())));
      httpServletResponse.getWriter().flush();
      httpServletResponse.getWriter().close();

      return;
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

}
