package com.earnest.crawler.core;

import com.earnest.crawler.core.request.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Locale;

@AllArgsConstructor
@Getter
public class HtmlResponsePage {

    private final HttpRequest httpRequest;

    private final HttpEntity httpEntity;

    private final StatusLine statusLine;

    private final Locale locale;
    @Setter
    private CookieStore cookieStore;
    @Setter
    private Header[] headers;


    public HtmlResponsePage(HttpRequest httpRequest, HttpEntity httpEntity, StatusLine statusLine, Locale locale) {
        this(httpRequest, httpEntity, statusLine, locale, null, null);
    }

    public String entityAsString() throws IOException {
        return entityAsString(null);
    }

    public String entityAsString(String charset) throws IOException {
        return EntityUtils.toString(httpEntity, charset);
    }

    public byte[] entityAsByteArray() throws IOException {
        return EntityUtils.toByteArray(httpEntity);
    }

    public Document entityAsJsoupDocument(String charset) throws IOException {
        return Jsoup.parse(entityAsString(charset));
    }

}
