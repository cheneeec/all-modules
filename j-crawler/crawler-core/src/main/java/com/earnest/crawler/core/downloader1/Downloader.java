package com.earnest.crawler.core.downloader1;

import java.io.Closeable;
import java.io.IOException;

public interface Downloader<R, T> extends Closeable {
    T download(R r) throws IOException;
}
