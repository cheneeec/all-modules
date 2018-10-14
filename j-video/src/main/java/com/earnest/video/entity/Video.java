package com.earnest.video.entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.annotation.Id;


/**
 * 存储到Mongodb
 */
public class Video extends IQiYi {

    @Override
    @Id
    public String getId() {
        return super.getId();
    }

    @Override
    @Id
    public void setId(String id) {
        super.setId(id);
    }


    public static Video adapt(VideoEntity videoEntity) {

        if (videoEntity == null) {
            return null;
        }

        if (videoEntity instanceof Video) {
            return (Video) videoEntity;
        }

        return JSONObject.parseObject(JSONObject.toJSONString(videoEntity), Video.class);
    }


}
