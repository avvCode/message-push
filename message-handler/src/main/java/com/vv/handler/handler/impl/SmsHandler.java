package com.vv.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Throwables;
import com.vv.common.constant.CommonConstant;
import com.vv.common.domain.TaskInfo;
import com.vv.common.dto.account.sms.SmsAccount;
import com.vv.common.dto.model.SmsContentModel;
import com.vv.common.enums.ChannelType;
import com.vv.handler.handler.BaseHandler;
import com.vv.handler.handler.Handler;
import com.vv.handler.handler.MessageTypeSmsConfig;
import com.vv.handler.handler.SmsParam;
import com.vv.support.domain.MessageTemplate;
import com.vv.support.domain.SmsRecord;
import com.vv.support.mapper.SmsRecordMapper;
import com.vv.support.service.ConfigService;
import com.vv.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 短信发送处理
 *
 * @author vv
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler implements Handler {

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private ConfigService config;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 流量自动分配策略
     */
    private static final Integer AUTO_FLOW_RULE = 0;

    private static final String FLOW_KEY = "msgTypeSmsConfig";
    private static final String FLOW_KEY_PREFIX = "message_type_";

    @Override
    public boolean handler(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .build();
        try {
            /**
             * 1、动态配置做流量负载
             * 2、发送短信
             */
            MessageTypeSmsConfig[] messageTypeSmsConfigs = loadBalance(getMessageTypeSmsConfig(taskInfo));
            for (MessageTypeSmsConfig messageTypeSmsConfig : messageTypeSmsConfigs) {
//                smsParam.setScriptName(messageTypeSmsConfig.getScriptName());
//                smsParam.setSendAccountId(messageTypeSmsConfig.getSendAccount());
//                List<SmsRecord> recordList = applicationContext.getBean(messageTypeSmsConfig.getScriptName(), SmsScript.class).send(smsParam);
//                if (CollUtil.isNotEmpty(recordList)) {
//                    smsRecordMapper.saveAll(recordList);
//
//                    return true;
//                }
                //TODO 保存短信记录
            }
        } catch (Exception e) {
            log.error("SmsHandler#handler fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }

    /**
     * 流量负载
     * 根据配置的权重优先走某个账号，并取出一个备份的
     *
     * @param messageTypeSmsConfigs
     */
    private MessageTypeSmsConfig[] loadBalance(List<MessageTypeSmsConfig> messageTypeSmsConfigs) {

        int total = 0;
        for (MessageTypeSmsConfig channelConfig : messageTypeSmsConfigs) {
            total += channelConfig.getWeights();
        }

        // 生成一个随机数[1,total]，看落到哪个区间
        Random random = new Random();
        int index = random.nextInt(total) + 1;

        MessageTypeSmsConfig supplier = null;
        MessageTypeSmsConfig supplierBack = null;
        for (int i = 0; i < messageTypeSmsConfigs.size(); ++i) {
            if (index <= messageTypeSmsConfigs.get(i).getWeights()) {
                supplier = messageTypeSmsConfigs.get(i);

                // 取下一个供应商
                int j = (i + 1) % messageTypeSmsConfigs.size();
                if (i == j) {
                    return new MessageTypeSmsConfig[]{supplier};
                }
                supplierBack = messageTypeSmsConfigs.get(j);
                return new MessageTypeSmsConfig[]{supplier, supplierBack};
            }
            index -= messageTypeSmsConfigs.get(i).getWeights();
        }
        return null;
    }

    /**
     * 如模板指定具体的明确账号，则优先发其账号，否则走到流量配置
     * <p>
     * 流量配置每种类型都会有其下发渠道账号的配置(流量占比也会配置里面)
     * <p>
     * 样例：
     * key：msgTypeSmsConfig
     * value：[{"message_type_10":[{"weights":80,"scriptName":"TencentSmsScript"},{"weights":20,"scriptName":"YunPianSmsScript"}]},{"message_type_20":[{"weights":20,"scriptName":"YunPianSmsScript"}]},{"message_type_30":[{"weights":20,"scriptName":"TencentSmsScript"}]},{"message_type_40":[{"weights":20,"scriptName":"TencentSmsScript"}]}]
     * 通知类短信有两个发送渠道 TencentSmsScript 占80%流量，YunPianSmsScript占20%流量
     * 营销类短信只有一个发送渠道 YunPianSmsScript
     * 验证码短信只有一个发送渠道 TencentSmsScript
     *
     * @param taskInfo
     * @return
     */
    private List<MessageTypeSmsConfig> getMessageTypeSmsConfig(TaskInfo taskInfo) {

        /**
         * 如果模板指定了账号，则优先使用具体的账号进行发送
         */
        if (!taskInfo.getSendAccount().equals(AUTO_FLOW_RULE)) {
            SmsAccount account = accountUtils.getChannelAccountById(taskInfo.getSendAccount(), SmsAccount.class);
            return Arrays.asList(MessageTypeSmsConfig.builder().sendAccount(taskInfo.getSendAccount()).scriptName(account.getScriptName()).weights(100).build());
        }

        /**
         * 读取流量配置
         */
        String property = config.getProperty(FLOW_KEY, CommonConstant.EMPTY_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(property);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray array = jsonArray.getJSONObject(i).getJSONArray(FLOW_KEY_PREFIX + taskInfo.getMsgType());
            if (CollUtil.isNotEmpty(array)) {
                return JSON.parseArray(JSON.toJSONString(array), MessageTypeSmsConfig.class);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 如果有输入链接，则把链接拼在文案后
     * <p>
     * PS: 这里可以考虑将链接 转 短链
     * PS: 如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + StrUtil.SPACE + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
