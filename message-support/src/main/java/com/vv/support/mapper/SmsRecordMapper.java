package com.vv.support.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vv.support.domain.SmsRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author vv
 */
@Mapper
public interface SmsRecordMapper extends BaseMapper<SmsRecord> {

    void saveAll(List<SmsRecord> recordList);
}




