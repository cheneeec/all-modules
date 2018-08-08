package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.StringResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;


import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FilePipeline implements Pipeline {

    private String parentPath;

    @Override
    public void pipe(StringResponseResult result) {

        FileWriter writer;

        try {
            writer = new FileWriter(parentPath + "/" + System.currentTimeMillis());
            FileCopyUtils.copy(result.getContent(), writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
