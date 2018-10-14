package com.earnest.video.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper = true)
public class IQiYi extends VideoEntity {

    //爱奇艺获取集数时，需要此参数。
    private String albumId;

    public IQiYi() {
        this.platform = Platform.IQIYI;
    }

}
