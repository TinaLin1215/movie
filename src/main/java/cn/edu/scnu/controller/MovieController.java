package cn.edu.scnu.controller;

import cn.edu.scnu.entity.Movie;
import cn.edu.scnu.entity.Viewer;
import cn.edu.scnu.service.MovieService;
import cn.edu.scnu.service.ViewerService;
//import net.sf.jsqlparser.Model;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import java.io.IOException;
import java.nio.file.Files;

import static com.baomidou.mybatisplus.extension.toolkit.Db.updateById;

@RequestMapping("/movie")
@Controller
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private ResourceLoader resourceLoader;
    @GetMapping
    public String getMovieDetail(int id, Authentication authentication, Model model) throws IOException {
        Movie movie = movieService.getMovieDetail(id, authentication);
        Resource file = resourceLoader.getResource("classpath:/static/picture/" + movie.getPicture());
        int sales = movie.getClickrate() + 1;
        movie.setClickrate(sales);
        updateById(movie);
        byte[] imageBytes = Files.readAllBytes(file.getFile().toPath());
        String base64Image = Base64Utils.encodeToString(imageBytes);
        movie.setPicture(base64Image);
        model.addAttribute("movie", movie);
        return "moviedetail";
    }

}
