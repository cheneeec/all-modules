package com.earnest.video.entity;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
public abstract class BaseVideoEntity {
    //
    protected long id;
    //标题
    protected String title;
    //获取时间
    protected Date collectTime;
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
