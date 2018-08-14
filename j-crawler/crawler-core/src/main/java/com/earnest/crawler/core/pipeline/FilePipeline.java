package com.earnest.crawler.core.pipeline;

import com.alibaba.fastjson.util.IOUtils;
import com.earnest.crawler.core.StringResponseResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.util.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class FilePipeline implements Pipeline {

    private final String rootPath;

    private final String charset;

    @Setter
    @Getter
    private FileNameGenerator fileNameGenerator =UriPathFileNameGenerator.INSTANCE;


    /**
     * @param rootPath 需要保存文件的目录。
     * @param charset  需要保存文件的字符集。默认为<code>UTF-8</code>。
     */
    public FilePipeline(String rootPath, String charset) {
        this.rootPath = rootPath;
        this.charset = charset;
    }

    public FilePipeline(String rootPath) {
        this(rootPath, Charset.defaultCharset().displayName());
    }

    @Override
    public void pipe(StringResponseResult result) {
        Assert.state(fileNameGenerator != null, "fileNameGenerator has been not set yet");
        final String fileNameSuffix = fileNameGenerator.generate(result);
        final String fileName = rootPath + "/" + fileNameSuffix;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
            StreamUtils.copy(result.getContent(), Charset.forName(charset), outputStream);
        } catch (IOException e) {
            log.error("An error occurred while copying file:[{}] to {}", result.getHttpRequest().getURI().toString(), fileName);
            e.printStackTrace();
        } finally {
            IOUtils.close(outputStream);
        }

    }

}
