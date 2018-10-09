package com.earnest.video.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;


import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseVideoEntity implements Cloneable, Serializable {
    //
    protected String id;
    //标题
    protected String title;
    //获取时间
    protected Date collectTime;
    //播放时长
    protected String playInfo;
    //图片
    protected String image;
    //来源地
    protected Platform platform;
    //来源网址
    protected String fromUrl;
    //分类
    private Category category;
    //播放原地址
    protected String playValue;
    //
    protected String parseValue;


    public enum Category {
        MOVIE,
        ANIMATION;

        public static Category getCategory(String category) {
            if (StringUtils.equalsAnyIgnoreCase(category, "movie", "电影")) {
                return MOVIE;
            }
            if (StringUtils.equalsAnyIgnoreCase(category, "animation", "动漫")) {
                return ANIMATION;
            }
            throw new IllegalArgumentException("cannot find the category:" + category);
        }


    }


    @Override
    protected BaseVideoEntity clone() {
        try {
            return (BaseVideoEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("clone error:"+e.getMessage());
        }

    }
}
