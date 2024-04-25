package cn.edu.scnu.service.security;

import cn.edu.scnu.entity.JwtUser;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
public class JwtUserService implements UserDetailsService {

    @Autowired
    ViewerService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Viewer user = this.userService.findViewerByUsername(username);
        if (user != null) {
            JwtUser jwtUser = new JwtUser(user);
            return jwtUser;
        } else {
            try {
               throw new ValidationException("该用户不存在");
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
