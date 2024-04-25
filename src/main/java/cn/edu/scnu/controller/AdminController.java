package cn.edu.scnu.controller;


import cn.edu.scnu.common.MD5Util;
import cn.edu.scnu.entity.Admin;
import cn.edu.scnu.entity.Movie;
import cn.edu.scnu.entity.MyMovie;
import cn.edu.scnu.service.AdminService;
import cn.edu.scnu.service.MovieService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
public class AdminController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/toAdminLogin")
    public String toLogin(HttpSession session){
        if(session.getAttribute("adminLogin")!=null){
            return "redirect:/admin/movieIndex";
        }else{
            return "adminlogin";
        }
    }
    @RequestMapping("/admin/doAdminLogin")
    @ResponseBody
    public String doLogin(String username, String password, HttpSession session){
        Admin admin=adminService.findByUsername(username);
        if(admin!=null&&admin.getPassword().equals(MD5Util.md5(password))){
            session.setAttribute("adminLogin",admin);
            return "登录成功！";
        }else {
            return "登录失败！";
        }
    }

    @RequestMapping("/admin/logOut")
    public String logOut(HttpSession session){
        session.removeAttribute("adminLogin");
        return "redirect:/index";
    }

    @RequestMapping("/admin/movieIndex")
    public String movieindex(Model model,@RequestParam(name="pageNo",defaultValue = "1") Integer pageNo,
                             @RequestParam(name="pageSize",defaultValue = "4") Integer pageSize){
        Map<String,Object> map = movieService.queryPage2(pageNo,pageSize);
        Integer count = (Integer) map.get("count");
        List<Movie> movielist = (List<Movie>) map.get("movielist");
        int pageCount = (count % pageSize==0)?(count/pageSize):(count/pageSize+1);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("movies",map.get("movielist"));
        return "movieindex";
    }

//    @RequestMapping("/admin/movieIndex")
//    public String movieindex(Model model){
//        List<Movie> movielist=movieService.findAll();
//        model.addAttribute("movies",movielist);
//        return "movieindex";
//    }




    @RequestMapping("/admin/movieAdd")
    public String movieadd(Model model){
        List<String> areas=movieService.findarea();
        List<String> genres=movieService.findgenre();
        List<String> types=movieService.findtype();
        model.addAttribute("areas",areas);
        model.addAttribute("genres",genres);
        model.addAttribute("types",types);
        return "movieadd";
    }

    @RequestMapping("/admin/saveMovie")
    public String savemovie(MyMovie movie, Model model){
        String msg=movieService.savemovie(movie);
        model.addAttribute("msg",msg);
        return "redirect:/admin/movieIndex";
    }

    @RequestMapping("/admin/movieDelete")
    public String delete(String id){
        movieService.delete(id);
        return "redirect:/admin/movieIndex";
    }

    @RequestMapping("/admin/movieUpdate")
    public String update(String id,Model model){
        List<String> areas=movieService.findarea();
        List<String> genres=movieService.findgenre();
        List<String> types=movieService.findtype();
        model.addAttribute("areas",areas);
        model.addAttribute("genres",genres);
        model.addAttribute("types",types);
        Movie movie=movieService.findMovieById(id);
        model.addAttribute("movie",movie);
        return "movieupdate";
    }

    @RequestMapping("/admin/saveUpdate")
    public String saveUpdate(Movie movie){
        movieService.saveUpdate(movie);
        return "redirect:/admin/movieIndex";
    }

    @RequestMapping("/admin/dataOut")
    @ResponseBody
    public void dataout(HttpServletResponse response) throws IOException {
        //下载文件的信息头
        response.setHeader("Content-disposition","attachment;fileName=MovieSale.xlsx");
        Workbook workbook=movieService.dataout();
        workbook.write(response.getOutputStream());
    }

    @RequestMapping("/admin/graphdraw")
    public String draw(){
        return "draw";
    }


}

