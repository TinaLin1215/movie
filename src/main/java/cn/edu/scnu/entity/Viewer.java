package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("viewer")
public class Viewer implements Serializable {
    private static final long serialVersionUID=2L;
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String nickname;
    private String headPic;
    private String sex;
    private String phone;
    private String email;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Viewer() {
    }

    public Viewer(String username,String nickname,String password) {
        this.username=username;
        this.nickname=nickname;
        this.password=password;
    }
}
