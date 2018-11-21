package com.earnest.video.entity;

import lombok.Data;

/**
 * 剧集描述
 */
@Data
public class Episode extends Video {
    //短的剧集描述
    private String shortDescription;
    //集数
    private int number;
    //剧集描述
    private String description;
    //播放时长(以秒为单位)
    private int timeLength;
    //vId


    public Episode() {
        //剧集默认为单一
        this.single = true;
    }
}
