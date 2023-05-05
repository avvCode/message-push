package com.vv.web.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.web.domain.SmsRecord;
import com.vv.web.service.SmsRecordService;
import org.springframework.stereotype.Service;
import com.vv.web.mapper.SmsRecordMapper;

/**
 * @author vv
 */
@Service
public class SmsRecordServiceImpl extends ServiceImpl<SmsRecordMapper,SmsRecord>
    implements SmsRecordService {

}




