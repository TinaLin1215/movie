package cn.edu.scnu.service;

import cn.edu.scnu.entity.Movie;
import cn.edu.scnu.mapper.MovieMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RankService extends ServiceImpl<MovieMapper, Movie> {
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    public Map<String,Object> queryPage(String name, String actor,String rank, Integer pageNo, Integer pageSize) {
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        if(!"".equals(name) && name!=null)
            queryWrapper.like("name",name);
        if(!"".equals(actor) && actor!=null)
            queryWrapper.like("actor",actor);
        queryWrapper.between("id",1,100);
        int count = movieMapper.selectCount(queryWrapper).intValue();

        if("monthrank".equals(rank)){
            queryWrapper.orderByDesc("monthrank");
        } else if ("weekrank".equals(rank)) {
            queryWrapper.orderByDesc("weekrank");
        } else if ("hotrank".equals(rank)) {
            queryWrapper.orderByDesc("hotrank");
        } else{
            queryWrapper.orderByDesc("goodrank");
        }
        //构建分页对象（第一个参数是当前页数，第二个参数是每页条数
        Page<Movie> page= new Page<Movie>(pageNo,pageSize);
        Page<Movie> moviePage = movieMapper.selectPage(page,queryWrapper);
        Map<String,Object> map=new HashMap<>();
        map.put("count",count);
        map.put("ranklist",moviePage.getRecords());
        return map;
    }

}
