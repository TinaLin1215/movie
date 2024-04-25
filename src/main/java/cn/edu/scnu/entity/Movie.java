package cn.edu.scnu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("movie")
public class Movie {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String picture;
    private String detail;
    private Integer isvip;
    private String type;
    private String area;
    private String genre;
    private String actor;
    private Integer hotrank;
    private Integer weekrank;
    private Integer monthrank;
    private Integer goodrank;
    private Integer clickrate;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGenre() {
        return genre;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
