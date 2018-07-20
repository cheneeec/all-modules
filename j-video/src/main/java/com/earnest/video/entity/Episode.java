package com.earnest.video.entity;

import lombok.Data;

/**
 * 剧集描述
 */
@Data
public class Episode {
    //id 不知道有没有用
    private String id;
    //标题 如：航海王 第201集
    private String title;
    //图片 如：http://pic3.qiyipic.com/image/20150803/a9/1b/v_109343453_m_601.jpg
    private String image;
    //播放地址 如：http://www.iqiyi.com/v_19rrok9n74.html
    private String playUrl;
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
