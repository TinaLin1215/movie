package cn.edu.scnu.mapper;

import cn.edu.scnu.entity.Admin;
import cn.edu.scnu.entity.Viewer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional
public interface AdminMapper extends BaseMapper<Admin> {

}
