package com.vv.web.controller;

import cn.hutool.core.util.StrUtil;
import com.vv.support.domain.MessageTemplate;
import com.vv.web.annotation.MessagePushResponseResult;
import com.vv.web.service.MessageTemplateService;
import com.vv.web.utils.Convert4Amis;
import com.vv.web.vo.MessageTemplateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息模板相关
 */
@Api("消息模板管理")
@RestController
@MessagePushResponseResult
@RequestMapping("/messageTemplate")
public class MessageTemplateController {
    @Resource
    private MessageTemplateService messageTemplateService;

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public MessageTemplate saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        messageTemplateService.saveOrUpdate(messageTemplate);
        return messageTemplate;
    }

    /**
     * 列表数据
     */
    @GetMapping("/list")
    @ApiOperation("/列表页")
    public List<MessageTemplate> queryList(@Validated MessageTemplateParam messageTemplateParam) {

        return messageTemplateService.list();
    }

    /**
     * 根据Id查找
     */
    @GetMapping("query/{id}")
    @ApiOperation("/根据Id查找")
    public Map<String, Object> queryById(@PathVariable("id") Long id) {
        return Convert4Amis.flatSingleMap(messageTemplateService.getById(id));
    }


    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public void deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            messageTemplateService.removeBatchByIds(idList);
        }
    }
}
