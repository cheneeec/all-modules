package com.earnest.video.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Video implements Cloneable, Serializable {
    //
    private String id;
    //标题
    private String title;
    //視頻信息
    private String videoInfo;
    //获取时间
    private Date collectTime;
    //播放时长
    private String playInfo;
    //图片
    private String image;
    //来源地
    private Platform platform;
    //来源网址
    private String fromUrl;
    //分类
    private Category category;
    //播放原地址
    private String rawValue;
    //解析後的值
    private List<PlayAddress> parseValue;
    //單一視頻（true：只有一集）
    private Boolean single;
    //视频的属性　兼容平台差异性
    private Map<String, ?> properties;

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
    public Video clone() {
        try {
            return (Video) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("clone error:" + e.getMessage());
        }

    }

    @Data
    public static class PlayAddress {
        private String quality;//画质
        private String codecs;//码率
        private String url;//播放地址
        private int width;
        private int height;
        private String type;
        private String script;//脚本字符串
    }
}
