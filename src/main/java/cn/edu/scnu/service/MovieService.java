package cn.edu.scnu.service;


import cn.edu.scnu.entity.Movie;
import cn.edu.scnu.entity.MyMovie;
import cn.edu.scnu.mapper.MovieMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

@Service
public class MovieService extends ServiceImpl<MovieMapper,Movie> {
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    public List<String> findtype(){
        List<String> listType=new ArrayList<>();
        QueryWrapper<Movie> queryWrapper= new QueryWrapper<>();
        queryWrapper.select("distinct type");
        List<Movie> movies=movieMapper.selectList(queryWrapper);
        for(Movie movie:movies){
            listType.add(movie.getType());
        }
        return listType;
    }
    public List<String> findarea(){
        List<String> listArea=new ArrayList<>();
        QueryWrapper<Movie> queryWrapper= new QueryWrapper<>();
        queryWrapper.select("distinct area");
        List<Movie> movies=movieMapper.selectList(queryWrapper);
        for(Movie movie:movies){
            listArea.add(movie.getArea());
        }
        return listArea;
    }
    public List<String> findgenre(){
        List<String> listGenre=new ArrayList<>();
        QueryWrapper<Movie> queryWrapper= new QueryWrapper<>();
        queryWrapper.select("distinct genre");
        List<Movie> movies=movieMapper.selectList(queryWrapper);
        for(Movie movie:movies){
            listGenre.add(movie.getGenre());
        }
        return listGenre;
    }

    @Transactional
    public Map<String,Object> queryPage(String type,String area,String genre,Integer pageNo, Integer pageSize) {
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        if(!"".equals(type) && type!=null)
            queryWrapper.eq("type",type);
        if(!"".equals(area) && area!=null)
            queryWrapper.eq("area",area);
        if(!"".equals(genre) && genre!=null)
            queryWrapper.eq("genre",genre);
        queryWrapper.between("id",1,544655);
        int count = movieMapper.selectCount(queryWrapper).intValue();
        queryWrapper.orderByDesc("goodrank");
        Page<Movie> page= new Page<Movie>(pageNo,pageSize);
        Page<Movie> moviePage = movieMapper.selectPage(page,queryWrapper);
        Map<String,Object> map=new HashMap<>();
        map.put("count",count);
        map.put("movielist",moviePage.getRecords());
        return map;
    }

    public Map<String, Object> querymovie() {
        Set<Integer> randomIds = generateRandomIds();
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", randomIds);
        List<Movie> movies = movieMapper.selectList(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie : movies) {
            movieList.add(movie);
        }
        map.put("movielist", movieList);
        return map;
    }


    private Set<Integer> generateRandomIds() {
        Set<Integer> ids = new HashSet<>();
        Random random = new Random();
        while (ids.size() < 4) {
            int randomId = random.nextInt(43) + 1;
            ids.add(randomId);
        }
        return ids;
    }
    @Transactional
    public Map<String,Object> queryPage2(Integer pageNo, Integer pageSize) {
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("id",1,544655);
        int count = movieMapper.selectCount(queryWrapper).intValue();
        Page<Movie> page= new Page<>(pageNo, pageSize);
        Page<Movie> moviePage = movieMapper.selectPage(page,queryWrapper);
        Map<String,Object> map=new HashMap<>();
        map.put("count",count);
        map.put("movielist",moviePage.getRecords());
        return map;
    }


    public Movie getMovieDetail(int id,Authentication authentication) {
        Movie movie = getById(id);
        if (movie.getIsvip() == 1 && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("VIP"))) {
            throw new AccessDeniedException("vipOnly");
        }
        return movie;
    }



    public String uploadfile(MultipartFile file, String dir){
        // 1.判断后缀是否合法
        // 获取图名称，后缀名称
        // 截取后缀substring split (".png" ".jgp")
        String originName = file.getOriginalFilename();
        String extName = originName.substring(originName.lastIndexOf("."));
        if (!(extName.equalsIgnoreCase(".jpg") || extName.equalsIgnoreCase(".png")
                || extName.equalsIgnoreCase(".gif")||extName.equalsIgnoreCase(".webp"))) {// 图片后缀不合法
            return "图片后缀不合法!";
        }
        // 判断木马(java的类判断是否是图片属性，也可以引入第三方jar包判断)
        try {
            BufferedImage bufImage = ImageIO.read(file.getInputStream());
            bufImage.getHeight();
            bufImage.getWidth();
        } catch (Exception e) {
            return "该文件不是图片！";
        }
        File _file = new File(dir, originName);
        if (!_file.exists()) {
            _file.mkdirs();
        }
        // 上传文件
        try {
            file.transferTo(_file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

//    public static int getUUID() {
//        UUID uuid = UUID.randomUUID();
//        String id = uuid.toString();
//        //去掉随机ID的短横线
//        id = id.replace("-", "");
//        //将随机ID换成数字
//        int num = id.hashCode();
//        //去绝对值
//        num = num < 0 ? -num : num;
//        return num;
//    }

    public String savemovie(MyMovie mymovie) {
        Movie movie=new Movie();
//        movie.setId(getUUID());
        movie.setId(mymovie.getId());
        movie.setName(mymovie.getName());
        movie.setDetail(mymovie.getDetail());
        movie.setIsvip(mymovie.getIsvip());
        movie.setType(mymovie.getType());
        movie.setArea(mymovie.getArea());
        movie.setGenre(mymovie.getGenre());
        movie.setActor(mymovie.getActor());
        movie.setHotrank(mymovie.getHotrank());
        movie.setWeekrank(mymovie.getWeekrank());
        movie.setMonthrank(mymovie.getMonthrank());
        movie.setGoodrank(mymovie.getGoodrank());
        movie.setClickrate(mymovie.getClickrate());


        // 2.生成多级路径
        String imgurl = "";
        //   /a/2/e/a/2/3/b/e/f
        for (int i = 0; i < 8; i++) {
            imgurl += "/"+Integer.toHexString(new Random().nextInt(16));
        }
        //获取resources文件夹路径
        String realpath = System.getProperty("user.dir")+"/src/main/resources";
        String dir = realpath + "/static/picture" + imgurl+"/";
//        System.out.println(dir);
        //处理picture
        MultipartFile picture = mymovie.getPicture();
        String message ="";
        if(!"".equals(picture.getOriginalFilename())){
            message = uploadfile(picture,dir);
            if(!"".equals(message)){
                return message;
            }else{
                movie.setPicture(imgurl+"/"+picture.getOriginalFilename());
            }}

        movieMapper.insert(movie);
        return "商品添加成功";
    }


    public void delete(String id) {
        movieMapper.deleteById(id);
    }

    public Movie findMovieById(String id) {
        return movieMapper.selectById(id);
    }

    public void saveUpdate(Movie movie) {
        movieMapper.updateById(movie);
    }

    public List<Movie> findAll() {return movieMapper.selectList(null); }

    public Workbook dataout() {
        //创建workbook(2007版本以上)
        Workbook workbook=new XSSFWorkbook();
        //创建表sheet
        Sheet sheet=workbook.createSheet("电影销售榜单");
        //创建行row
        Row row=sheet.createRow(0);
        //创建单元格cell
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("name");
        row.createCell(2).setCellValue("sale");
        row.createCell(3).setCellValue("time");
        //查询数据库movie表，将movies信息放入列表
        List<Movie> movies=movieMapper.selectList(null);
        //遍历列表
        for(int i=0;i<movies.size();i++){
            Movie movie=movies.get(i);
            //创建行row
            Row r=sheet.createRow(i+1);
            //创建单元格cell
            r.createCell(0).setCellValue(movie.getId());
            r.createCell(1).setCellValue(movie.getName());
            r.createCell(2).setCellValue(movie.getClickrate());
            r.createCell(3).setCellValue(new Date().toLocaleString());
        }
        return workbook;
    }


}
