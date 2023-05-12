package com.vv.service.api.impl.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.vv.common.constant.CommonConstant;
import com.vv.common.domain.TaskInfo;
import com.vv.common.dto.model.ContentModel;
import com.vv.common.enums.ChannelType;
import com.vv.common.enums.ResponseCodeEnums;
import com.vv.common.vo.BasicResult;
import com.vv.service.api.domain.MessageParam;
import com.vv.service.api.enums.BusinessCode;
import com.vv.service.api.impl.domain.SendTaskModel;
import com.vv.support.domain.MessageTemplate;
import com.vv.support.mapper.MessageTemplateMapper;
import com.vv.support.pipeline.BusinessProcess;
import com.vv.support.pipeline.ProcessContext;
import com.vv.support.utils.ContentHolderUtils;
import com.vv.support.utils.TaskInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 参数拼装
 */
@Service
@Slf4j
public class AssembleAction implements BusinessProcess<SendTaskModel> {
    private static final String LINK_NAME = "url";

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();

        try {
            //查数据库有无该消息模板
            MessageTemplate messageTemplate = messageTemplateMapper.selectById(messageTemplateId);
            if (Objects.isNull(messageTemplate) || messageTemplate.getIsDeleted().equals(CommonConstant.TRUE)) {
                context.setNeedBreak(true)
                        .setResponse(BasicResult.fail(ResponseCodeEnums.TEMPLATE_NOT_FOUND));
                return;
            }
            //判断当前请求是撤回请求还是发送请求
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                //发送
                //组装消息信息
                List<TaskInfo> taskInfos = assembleTaskInfo(sendTaskModel, messageTemplate);
                sendTaskModel.setTaskInfo(taskInfos);
            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
                //撤回
                sendTaskModel.setMessageTemplate(messageTemplate);
            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResult.fail(ResponseCodeEnums.SERVICE_ERROR));
            log.error("assemble task fail! templateId:{}, e:{}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }

    }

    /**
     * 组装 TaskInfo 任务消息
     *
     * @param sendTaskModel
     * @param messageTemplate
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {

            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
//                    .shieldType(messageTemplate.getShieldType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();

            taskInfoList.add(taskInfo);
        }

        return taskInfoList;

    }


    /**
     * 获取 contentModel，替换模板msgContent中占位符信息
     */
    private static ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {

        // 得到真正的ContentModel 类型
        Integer sendChannel = messageTemplate.getSendChannel();

        Class<? extends ContentModel> contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);

        // 得到模板的 msgContent 和 入参
        Map<String, String> variables = messageParam.getVariables();

        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());

        // 通过反射 组装出 contentModel
        Field[] fields = ReflectUtil.getFields(contentModelClass);

        ContentModel contentModel = ReflectUtil.newInstance(contentModelClass);

        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());
            //将模板参数替换成真实值
            if (StrUtil.isNotBlank(originValue)) {

                String resultValue = ContentHolderUtils.replacePlaceHolder(originValue, variables);

                Object resultObj = JSONUtil.isJsonObj(resultValue)
                        ?
                        JSONUtil.toBean(resultValue, field.getType())
                        :
                        resultValue;

                ReflectUtil.setFieldValue(contentModel, field, resultObj);
            }
        }
        // 如果 url 字段存在，则在url拼接对应的埋点参数
        String url = (String) ReflectUtil.getFieldValue(contentModel, LINK_NAME);

        if (StrUtil.isNotBlank(url)) {

            String resultUrl = TaskInfoUtils.generateUrl(url, messageTemplate.getId(), messageTemplate.getTemplateType());

            ReflectUtil.setFieldValue(contentModel, LINK_NAME, resultUrl);
        }

        return contentModel;
    }
}
