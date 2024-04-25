package cn.edu.scnu.service.security;

import cn.edu.scnu.entity.JwtUser;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpSession;
import java.util.Set;

@EnableWebSecurity
// 只有加了@EnableGlobalMethodSecurity(prePostEnabled=true) 那么在上面使用的 @PreAuthorize(“hasAuthority(‘admin’)”)才会生效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUserService jwtUserService;

    @Autowired
    private ViewerService userService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 通过重写configure(),去数据库查询用户是否存在
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserService).passwordEncoder(bCryptPasswordEncoder());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers("/js/**", "/css/**", "/image/**","/picture/**").permitAll()
                //用户注册接口和执行用户注册接口允许访问
                .antMatchers("/index/relogin","/index/toRegister", "/index","/user/register","/admin/toAdminLogin","/admin/dataOut","/admin/graphdraw" ,"/admin/movieAdd","/admin/saveMovie","/admin/movieDelete","/admin/movieUpdate","/admin/saveUpdate","/admin/doAdminLogin")
                .permitAll()
                //用户访问其他URL资源都必须认证后访问，即登陆后访问
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler())
                .and()
                .formLogin()
                .loginPage("/index/login")
                .loginProcessingUrl("/doLogin")
                .permitAll()
                .successForwardUrl("/index")
//                成功登录后跳转到首页
                .successHandler((request, response, authentication) -> {
                    JwtUser user = (JwtUser) authentication.getPrincipal();
                    Viewer newViewer = userService.findViewerByUsername(user.getUsername());
                    HttpSession session = request.getSession();
                    session.setAttribute("viewerLogin", newViewer);
                    //进行页面跳转
                    response.setContentType("application/json;charset=utf-8");

                    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

                    if(roles.contains("VIP")){
                        session.setAttribute("vip", 1);
                    }else{
                        session.setAttribute("vip", 0);
                    }
                    if(roles.contains("ADMIN")){

                        response.sendRedirect("/admin/movieIndex");
                    }else{

                        response.sendRedirect("/index");
                    }

                })
                .failureHandler((request, response, authentication)->{
                    request.setAttribute("loginState", "fail");
                    request.getSession().setAttribute("loginState", "fail");
                    response.sendRedirect("/login");
                })
                .and()
                .logout()
                .logoutUrl("/index/logOut")
                .logoutSuccessUrl("/index") // 设置注销成功后重定向的URL
                .invalidateHttpSession(true) // 确保注销时使Session无效
                .deleteCookies("JSESSIONID") // 删除JSESSIONID cookie
                .and()
                .csrf()
                .disable();


    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


}

