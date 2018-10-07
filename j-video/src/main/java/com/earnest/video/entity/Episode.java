package com.earnest.video.entity;

import lombok.Data;

/**
 * 剧集描述
 */
@Data
public class Episode  extends  BaseVideoEntity{
    //短的剧集描述
    private String shortDescription;
    //集数
    private int number;
    //剧集描述
    private String description;
    //播放时长
    private int timeLength;
    //vId
    private String vId;
}
