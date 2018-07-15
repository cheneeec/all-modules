package com.earnest.security.core.validate.image;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码的描述类
 */
@Getter
@Setter
public class ImageVerificationCode {
    private BufferedImage bufferedImage;
    private String code;
    private LocalDateTime expireTime;

    public ImageVerificationCode(BufferedImage bufferedImage, String code, int expireIn) {
        this.bufferedImage = bufferedImage;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
}
