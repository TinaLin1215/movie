package cn.edu.scnu.entity;

import org.springframework.web.multipart.MultipartFile;

public class MyMovie {
    private Integer id;
    private String name;
    private MultipartFile picture;
    private String detail;
    private Integer isvip;
    private String type;
    private String area;
    private String genre;
    private String actor;
    private int hotrank;
    private int weekrank;
    private int monthrank;
    private int goodrank;
    private int clickrate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getIsvip() {
        return isvip;
    }

    public void setIsvip(Integer isvip) {
        this.isvip = isvip;
    }

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

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getHotrank() {
        return hotrank;
    }

    public void setHotrank(int hotrank) {
        this.hotrank = hotrank;
    }

    public int getWeekrank() {
        return weekrank;
    }

    public void setWeekrank(int weekrank) {
        this.weekrank = weekrank;
    }

    public int getMonthrank() {
        return monthrank;
    }

    public void setMonthrank(int monthrank) {
        this.monthrank = monthrank;
    }

    public int getGoodrank() {
        return goodrank;
    }

    public void setGoodrank(int goodrank) {
        this.goodrank = goodrank;
    }

    public int getClickrate() {
        return clickrate;
    }

    public void setClickrate(int clickrate) {
        this.clickrate = clickrate;
    }
}
