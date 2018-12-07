package com.earnest.web.autoconfigure;

import com.earnest.crawler.proxy.HttpProxySupplier;
import com.earnest.video.parser.AwkVideoAddressParser;
import com.earnest.video.parser.IQiYiVideoAddressParser;
import com.earnest.video.parser.VideoAddressParser;
import com.earnest.video.parser.VideoAddressParserManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
public class PlayAddressParserAutoConfiguration {

    @Bean
    public VideoAddressParser awkVideoAddressParser(HttpClient httpClient,
                                                    ResponseHandler<String> responseHandler,
                                                    @Autowired(required = false) HttpProxySupplier httpProxySupplier) {
        AwkVideoAddressParser awkVideoAddressParser = new AwkVideoAddressParser(httpClient, responseHandler);
        awkVideoAddressParser.setHttpProxySupplier(httpProxySupplier);
        return awkVideoAddressParser;
    }


    @Bean
    public VideoAddressParser iQiYiVideoAddressParser(HttpClient httpClient,
                                                      ResponseHandler<String> responseHandler,
                                                      @Autowired(required = false) HttpProxySupplier httpProxySupplier) {
        IQiYiVideoAddressParser iQiYiVideoAddressParser = new IQiYiVideoAddressParser(httpClient, responseHandler);
        iQiYiVideoAddressParser.setHttpProxySupplier(httpProxySupplier);
        return iQiYiVideoAddressParser;
    }


    @Bean
    public VideoAddressParser videoAddressParser( @Autowired(required = false) List<VideoAddressParser> videoAddressParsers) {
        VideoAddressParserManager videoAddressParserManager = new VideoAddressParserManager();


        //add other
        Optional.ofNullable(videoAddressParsers).stream()
                .flatMap(Collection::stream)
                .forEach(videoAddressParserManager::addWork);


        return videoAddressParserManager;
    }
}
