package com.earnest.video.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public abstract class VideoUtils {
    //播放时长正则
    private static final Pattern playTimePattern = Pattern.compile("^\\d+:\\d+(:\\d+)?$");

    /**
     * 判断是否为播放时长。为空或者空字符串时，返回<code>true</code>。
     * <pre>
     *     null   　=>true
     *     ""     　=>true
     *     01:32:00=>true
     *  　 02:32   =>true
     *     02      =>false
     * </pre>
     *
     * @param playInfo
     * @return
     */
    public static boolean isPlayTime(String playInfo) {
        if (StringUtils.isBlank(playInfo)) {
            return true;
        }
        return playTimePattern.matcher(playInfo).find();
    }

}
