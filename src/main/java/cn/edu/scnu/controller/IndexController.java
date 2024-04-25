package cn.edu.scnu.controller;

import cn.edu.scnu.common.MD5Util;
import cn.edu.scnu.entity.Movie;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.service.MovieService;
import cn.edu.scnu.service.RankService;
import cn.edu.scnu.service.ViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private MovieService  movieService;
    @Autowired
    private RankService rankService;

    @Autowired
    private ViewerService viewerService;

    @RequestMapping("/index")
    public String index(Model model){

        Map<String,Object> map = movieService.querymovie();
        List<Movie> flowerlist = (List<Movie>) map.get("movielist");
//        model.addAttribute("name",name);
        model.addAttribute("movielist",map.get("movielist"));

        return "index";
    }

    @GetMapping("/index/login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/index/toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/index/doLoginNew")
    @ResponseBody
    public String doLogin(String username, String password, HttpSession session){
        Viewer viewer=viewerService.findViewerByUsername(username);
        if(viewer!=null){
            session.setAttribute("viewerLogin",viewer);
            return "登录成功！";
        }else if(password==null){
            return "密码不能为空！";
        }else
        {
            return "登录失败！";
        }
    }
//    @RequestMapping("/index/doLogin")
//    @ResponseBody
//    public String doLogin(String username, String password, HttpSession session){
//        Viewer viewer=viewerService.findViewerByUsername(username);
//        if(viewer!=null){
//            session.setAttribute("viewerLogin",viewer);
//            return "登录成功！";
//        }else{
//            return "登录失败！";
//        }
//    }
//    @RequestMapping("/index/doRegister")
//    @ResponseBody
//    public String doRegister(String username, String passw1, HttpSession session){
//        Viewer viewer=viewerService.findViewerByUsername(username);
//        if(viewer==null){
//            Viewer new_viewer=viewerService.register(username,passw1);
//            session.setAttribute("viewerLogin",new_viewer);
//            return "注册成功！";
//        }
//        return "该用户名已被注册！请重新输入！";
//    }
@GetMapping("/index/relogin")
public String reLogin(HttpServletRequest request){
    request.getSession().removeAttribute("loginState");
    return "login";
}
    @RequestMapping("/index/logOut")
    public String logOut(HttpSession session){
        session.removeAttribute("viewerLogin");
        return "redirect:/index";
    }
    @RequestMapping("/index/showMovie")
    public  String showMovie(@RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                             @RequestParam(name="pageSize",defaultValue = "8") Integer pageSize,
                             String type,
                             String area,
                             String genre,
                             Model model){
        System.out.println(type+","+area+","+genre);
        Map<String,Object> map = movieService.queryPage(type,area,genre,pageNo,pageSize);
        Integer count = (Integer) map.get("count");
        List<Movie> flowerlist = (List<Movie>) map.get("movielist");
        int pageCount = (count % pageSize==0)?(count/pageSize):(count/pageSize+1);
//        model.addAttribute("name",name);
        model.addAttribute("type",type);
        model.addAttribute("area",area);
        model.addAttribute("genre",genre);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("movielist",map.get("movielist"));
        model.addAttribute("types",movieService.findtype());
        model.addAttribute("areas",movieService.findarea());
        model.addAttribute("genres",movieService.findgenre());
        return "showmovie";
    }

    @RequestMapping("/index/applyvip")
    public String applyvip(){
        return "applyvip";
    }
    @RequestMapping("/index/account")
    public String acount(){
        return "account";
    }
    @RequestMapping("/index/showRank")
    public String showRank(@RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                           @RequestParam(name="pageSize",defaultValue = "4") Integer pageSize,
                           String name,
                           String actor,
                           String rank,
                           Model model){
        Map<String,Object> map = rankService.queryPage(name,actor,rank,pageNo,pageSize);
        Integer count = (Integer) map.get("count");
        int pageCount = (count % pageSize==0)?(count/pageSize):(count/pageSize+1);
        model.addAttribute("name",name);
        model.addAttribute("actor",actor);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("ranklist",map.get("ranklist"));

        return "showrank";
    }




}
