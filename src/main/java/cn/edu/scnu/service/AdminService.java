package cn.edu.scnu.service;

import cn.edu.scnu.common.MD5Util;
import cn.edu.scnu.entity.Admin;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.mapper.AdminMapper;
import cn.edu.scnu.mapper.ViewerMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.View;
import java.util.UUID;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {
    @Autowired
    private AdminMapper adminMapper;
    public Admin findByUsername(String username) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return adminMapper.selectOne(queryWrapper);
    }

//    public Viewer findManagerByUsername(String username){
//        QueryWrapper<Viewer> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("username",username);
//        Viewer manager=viewerMapper.selectOne(queryWrapper);
//        if(manager.getType()!=2){
//            return null;
//        }else{
//            return manager;
//        }
//    }

//    public Viewer register(String username, String passw1) {
//        Viewer viewer=new Viewer(username, MD5Util.md5(passw1));
//        if(viewerMapper.insert(viewer)>0){
//            return viewer;
//        }else{
//            return null;
//        }
//    }
}
