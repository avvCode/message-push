package com.vv.handler.deduplication.service;


import com.vv.handler.deduplication.DeduplicationParam;

/**
 * @author vv
 */
public interface DeduplicationService {

    /**
     * 去重
     *
     * @param param
     */
    void deduplication(DeduplicationParam param);
}
