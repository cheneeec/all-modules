package com.earnest.video.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;


import java.io.Serializable;
import java.util.Date;

@Data
public abstract class VideoEntity implements Cloneable, Serializable {
    //
    protected String id;
    //标题
    protected String title;
    //視頻信息
    protected String videoInfo;
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
    //解析後的值
    protected String parseValue;
    //單一視頻（true：只有一集）
    protected Boolean single;

    public enum Category {
        MOVIE,  //电影
        ANIMATION, //动漫
        ORIGINAL; //原创

        public static Category getCategory(String category) {
            return getCategory(category, true);
        }

        public static Category getCategory(String category, boolean ignoredNotFound) {
            if (StringUtils.equalsAnyIgnoreCase(category, "movie", "电影")) {
                return MOVIE;
            }
            if (StringUtils.equalsAnyIgnoreCase(category, "animation", "动漫")) {
                return ANIMATION;
            }
            if (StringUtils.equalsAnyIgnoreCase(category, "original", "原创")) {
                return ORIGINAL;
            }
            if (!ignoredNotFound) {
                throw new IllegalArgumentException("cannot find the category:" + category);
            }
            return null;

        }

    }


    @Override
    protected VideoEntity clone() {
        try {
            return (VideoEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("clone error:" + e.getMessage());
        }

    }
}
