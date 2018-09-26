package com.earnest.video.entity;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 存储到Jdbc的类
 */
@Entity
public class Video extends IQiYi {

    @Override
    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }

    @Override
    @Id
    public void setId(Long id) {
        super.setId(id);
    }




    public static Video adapt(BaseVideoEntity videoEntity) {

        if (videoEntity instanceof Video) {
            return (Video) videoEntity;
        }

        return JSONObject.parseObject(JSONObject.toJSONString(videoEntity), Video.class);
    }


    //===================Setter、Getter====================================
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public Date getCollectTime() {
        return super.getCollectTime();
    }

    @Override
    public String getPlayInfo() {
        return super.getPlayInfo();
    }

    @Override
    public String getImage() {
        return super.getImage();
    }

    @Override
    public Platform getPlatform() {
        return super.getPlatform();
    }

    @Override
    public String getFromUrl() {
        return super.getFromUrl();
    }

    @Override
    public Category getCategory() {
        return super.getCategory();
    }

    @Override
    public String getPlayValue() {
        return super.getPlayValue();
    }

    @Override
    public String getParseValue() {
        return super.getParseValue();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void setCollectTime(Date collectTime) {
        super.setCollectTime(collectTime);
    }

    @Override
    public void setPlayInfo(String playInfo) {
        super.setPlayInfo(playInfo);
    }

    @Override
    public void setImage(String image) {
        super.setImage(image);
    }

    @Override
    public void setPlatform(Platform platform) {
        super.setPlatform(platform);
    }

    @Override
    public void setFromUrl(String fromUrl) {
        super.setFromUrl(fromUrl);
    }

    @Override
    public void setCategory(Category category) {
        super.setCategory(category);
    }

    @Override
    public void setPlayValue(String playValue) {
        super.setPlayValue(playValue);
    }

    @Override
    public void setParseValue(String parseValue) {
        super.setParseValue(parseValue);
    }

    @Override
    public void setAlbumId(String albumId) {
        super.setAlbumId(albumId);
    }

    @Override
    public String getAlbumId() {
        return super.getAlbumId();
    }
}
