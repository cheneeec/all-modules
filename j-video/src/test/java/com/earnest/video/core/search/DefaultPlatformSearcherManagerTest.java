package com.earnest.video.core.search;

import com.earnest.video.VideoApplication;
import com.earnest.video.entity.VideoEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest(classes = VideoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class DefaultPlatformSearcherManagerTest {
    @Autowired
    DefaultPlatformSearcherManager platformSearcherManager;

    @Test
    public void search() throws IOException {
        Page<VideoEntity> entities = platformSearcherManager.search("海贼王", new PageRequest(0, 50));
        Assert.assertNotNull(entities.getContent());
    }

    @Test
    public void search1() {
    }
}