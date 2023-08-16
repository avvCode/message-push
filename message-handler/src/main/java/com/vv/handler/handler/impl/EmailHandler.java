package com.vv.handler.handler.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.sun.mail.util.MailSSLSocketFactory;
import com.vv.common.domain.TaskInfo;
import com.vv.common.dto.model.EmailContentModel;
import com.vv.common.enums.ChannelType;
import com.vv.handler.handler.BaseHandler;
import com.vv.handler.handler.Handler;
import com.vv.support.domain.ChannelAccount;
import com.vv.support.domain.MessageTemplate;
import com.vv.support.utils.AccountUtils;
import com.vv.support.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * 邮箱发送处理器
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler implements Handler{

    @Autowired
    private AccountUtils accountUtils;


    @Value("${message.push.business.upload.crowd.path}")
    private String dataPath;

    public EmailHandler(){
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account= getAccountConfig(taskInfo.getSendAccount());
        try {
            File file = StrUtil.isNotBlank(emailContentModel.getUrl()) ?
                   FileUtils.getRemoteUrl2File(dataPath, emailContentModel.getUrl()) : null;
            String result = Objects.isNull(file) ?
                    MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true) :
                    MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true, file);
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    /**
     * 获取账号信息和配置
     *
     * @return
     */
    private MailAccount getAccountConfig(Long sendAccount) {
        MailAccount account = accountUtils.getChannelAccountById(sendAccount, MailAccount.class);
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(account.isAuth())
                    .setStarttlsEnable(account.isStarttlsEnable())
                    .setSslEnable(account.isSslEnable())
                    .setCustomProperty("mail.smtp.ssl.socketFactory", sf);
            account.setTimeout(25000)
                    .setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
