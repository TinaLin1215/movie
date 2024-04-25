package cn.edu.scnu.service;

import cn.edu.scnu.common.JwtUtils;
import cn.edu.scnu.common.MD5Util;
import cn.edu.scnu.common.SysResult;
import cn.edu.scnu.entity.JwtUser;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.mapper.ViewerMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Service
public class ViewerService extends ServiceImpl<ViewerMapper, Viewer> {


    @Autowired
    private ViewerMapper viewerMapper;

    public Viewer findViewerByUsername(String username) {
        QueryWrapper<Viewer> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return viewerMapper.selectOne(queryWrapper);
    }

    public String register(Viewer viewer) {
        if (findViewerByUsername(viewer.getUsername()) != null) {
            return "该用户名已被注册！请重新输入！";
        }
        if(viewerMapper.insert(viewer)>0){
            return "注册成功！";
        }else{
            return "注册失败！";
        }
    }

}
