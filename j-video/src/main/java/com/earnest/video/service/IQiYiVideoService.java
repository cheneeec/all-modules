package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.IQiYi;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class IQiYiVideoService implements VideoService<IQiYi> {

    private static final Map<Long, IQiYi> iQiYiMap = new LinkedHashMap<>();

    @Override
    public void save(List<IQiYi> iQiYis) {
        iQiYiMap.putAll(
                iQiYis.stream().collect(Collectors.toMap(BaseVideoEntity::getId, i -> i))
        );
    }

    @Override
    public void save(IQiYi iQiYi) {
        Assert.notNull(iQiYi, "the argument:[iQiYi] is null");
        iQiYiMap.put(iQiYi.getId(), iQiYi);
    }

    @Override
    public Page<IQiYi> findAll(Pageable pageRequest) {
        Assert.notNull(pageRequest, "pageRequest is null");
        Collection<IQiYi> values = iQiYiMap.values();

        return new PageImpl<>(
                //获得内容
                values.stream().skip(pageRequest.getPageSize() * pageRequest.getPageNumber())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, iQiYiMap.size());

    }


    @Override
    public List<IQiYi> findAll() {
        return new ArrayList<>(iQiYiMap.values());
    }

    @Override
    public IQiYi get(Long id) {
        Assert.notNull(id, "the id is required");
        return iQiYiMap.get(id);
    }
}
