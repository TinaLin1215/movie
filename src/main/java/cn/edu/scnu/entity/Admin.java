package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("admin")
public class Admin implements Serializable {
    private static final long serialVersionUID=2L;
    private String username;
    private String password;


    public Admin(String username, String password) {
        this.username=username;
        this.password=password;
    }
}
