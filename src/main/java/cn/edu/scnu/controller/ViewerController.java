package cn.edu.scnu.controller;

import cn.edu.scnu.common.MD5Util;
import cn.edu.scnu.common.SysResult;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RequestMapping("/user")
@RestController
public class ViewerController {
    @Autowired
    ViewerService viewerService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public String save(String username, String nickname, String passw1, HttpSession session) {
        Viewer viewer = new Viewer(username,nickname,bCryptPasswordEncoder.encode(passw1));
        // 默认角色为普通用户
        viewer.setRole("VIEWER");
//        session.setAttribute("viewerLogin",viewer);
        return viewerService.register(viewer);
    }

    @PostMapping("/upgradeToVIP")
    public ResponseEntity<String> upgradeToVIP(@RequestBody HashMap<String, String> requestBody, HttpSession session) {
//        String username = requestBody.get("username");
//        System.out.println(username);
        Viewer viewer = (Viewer)session.getAttribute("viewerLogin");

//        Viewer viewer = viewerService.findViewerByUsername(username);
        if (viewer != null) {
            // Update the role to "VIP"
            viewer.setRole("VIP");

//            updateById(viewer);
            viewerService.updateById(viewer);

            session.setAttribute("viewerLogin",viewer);
            return ResponseEntity.ok("User upgraded to VIP successfully.");
        } else {
            return ResponseEntity.status(-1).body("开通失败");
        }
    }

}
