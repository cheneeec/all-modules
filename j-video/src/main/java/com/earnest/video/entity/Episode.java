package com.earnest.video.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 剧集描述
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString(callSuper = true)
public class Episode extends Video {
    //短的剧集描述
    private String shortDescription;
    //集数
    private int number;
    //剧集描述
    private String description;
    //播放时长(以秒为单位)
    private int duration;
    //vId


    public Episode() {
        //剧集默认为单一
        setSingle(true);
    }


}
