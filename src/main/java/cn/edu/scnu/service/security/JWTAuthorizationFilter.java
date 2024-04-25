package cn.edu.scnu.service.security;

import cn.edu.scnu.common.JwtUtils;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * 登录成功之后走此类进行  鉴定 权限
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String tokenHeader = request.getHeader(JwtUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行
        if (tokenHeader == null || !tokenHeader.startsWith(JwtUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    // 从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) throws Exception {

        String token = tokenHeader.replace(JwtUtils.TOKEN_PREFIX, "");

        // 检测token是否过期 如果过期会自动抛出错误
        JwtUtils.isExpiration(token);
        String username = JwtUtils.getUsername(token);
        String role = JwtUtils.getUserRole(token);
        String nickname = JwtUtils.getNickname(token);
        String sex = JwtUtils.getSex(token);
        String phone = JwtUtils.getPhone(token);
        String email = JwtUtils.getUserEmail(token);
        if (username != null) {
            return new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        }
        return null;
    }
}

