package com.earnest.video.entity;

import lombok.Data;


import java.util.Date;

@Data
public abstract class BaseVideoEntity {
    //
    protected long id;
    //标题
    protected String title;
    //获取时间
    protected Date collectTime;
    //播放时长
    protected String playInfo;
    //图片
    protected String image;
    //来源地
    protected String origin;
    //来源网址
    protected String fromUrl;
    //分类
    private String category;
    //播放原地址
    protected String playValue;
    //
    protected String parseValue;


}
