package com.earnest.video.entity;



import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class IQiYi extends BaseVideoEntity {

    //爱奇艺获取集数时，需要此参数。
//    private String albumId;

    public IQiYi() {
        this.origin = "爱奇艺";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseVideoEntity that = (BaseVideoEntity) o;

        return new EqualsBuilder()
                .append(title, that.title)
                .append(fromUrl, that.fromUrl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(title)
                .append(fromUrl)
                .toHashCode();
    }

}
