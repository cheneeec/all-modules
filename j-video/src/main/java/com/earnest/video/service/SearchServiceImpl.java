package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.exception.UnknownException;
import com.earnest.video.search.PlatformSearcher;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@AllArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {

    private final PlatformSearcher<? extends BaseVideoEntity> platformSearcher;


    @Override
    public Page<? extends BaseVideoEntity> search(String keyword, Pageable pageRequest) {

        try {
            return platformSearcher.search(keyword, pageRequest);
        } catch (IOException e) {
            throw new UnknownException(e);
        }

    }
}
