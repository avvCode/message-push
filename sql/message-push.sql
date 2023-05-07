create table channel_account
(
    id bigint auto_increment
        primary key,
    name varchar(100) default '' not null comment '账号名称',
    send_channel tinyint default 0 not null comment '消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
    account_config varchar(1024) default '' not null comment '账号配置',
    creator varchar(128) default 'vv' not null comment '拥有者',
    created int default 0 not null comment '创建时间',
    updated int default 0 not null comment '更新时间',
    is_deleted tinyint default 0 not null comment '是否删除：0.不删除 1.删除'
)
    comment '渠道账号信息' collate=utf8mb4_unicode_ci;

create index idx_send_channel
    on channel_account (send_channel);

create table message_template
(
    id bigint auto_increment
        primary key,
    name varchar(100) default '' not null comment '标题',
    msg_status tinyint default 0 not null comment '当前消息状态：10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败',
    id_type tinyint default 0 not null comment '消息的发送ID类型：10. userId 20.did 30.手机号 40.openId 50.email 60.企业微信userId',
    send_channel int default 0 not null comment '消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
    msg_type tinyint default 0 not null comment '10.通知类消息 20.营销类消息 30.验证码类消息',
    `template_type`    tinyint(4) NOT NULL DEFAULT '0' COMMENT '10.运营类 20.技术类接口调用',
    msg_content varchar(4096) default '' not null comment '消息内容 占位符用{$var}表示',
    send_account int default 0 not null comment '发送账号 一个渠道下可存在多个账号',
    creator varchar(45) default '' not null comment '创建者',
    updater varchar(45) default '' not null comment '更新者',
    is_deleted tinyint default 0 not null comment '是否删除：0.不删除 1.删除',
    created int default 0 not null comment '创建时间',
    updated int default 0 not null comment '更新时间'
)
    comment '消息模板信息' collate=utf8mb4_unicode_ci;

create index idx_channel
    on message_template (send_channel);

create table sms_record
(
    id bigint auto_increment
        primary key,
    message_template_id bigint default 0 not null comment '消息模板ID',
    phone bigint default 0 not null comment '手机号',
    supplier_id tinyint default 0 not null comment '发送短信渠道商的ID',
    supplier_name varchar(40) default '' not null comment '发送短信渠道商的名称',
    msg_content varchar(600) default '' not null comment '短信发送的内容',
    series_id varchar(100) default '' not null comment '下发批次的ID',
    charging_num tinyint default 0 not null comment '计费条数',
    report_content varchar(50) default '' not null comment '回执内容',
    status tinyint default 0 not null comment '短信状态： 10.发送 20.成功 30.失败',
    send_date int default 0 not null comment '发送日期：20211112',
    created int default 0 not null comment '创建时间',
    updated int default 0 not null comment '更新时间'
)
    comment '短信记录信息' collate=utf8mb4_unicode_ci;

create index idx_send_date
    on sms_record (send_date);

