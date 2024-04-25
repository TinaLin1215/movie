package cn.edu.scnu.mapper;

import cn.edu.scnu.entity.Movie;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional
public interface MovieMapper extends BaseMapper<Movie> {

}
