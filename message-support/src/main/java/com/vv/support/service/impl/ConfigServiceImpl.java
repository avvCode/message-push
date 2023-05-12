package com.vv.support.service.impl;

import cn.hutool.setting.dialect.Props;
import com.vv.support.service.ConfigService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ConfigServiceImpl implements ConfigService {
    /**
     * 本地配置
     */
    private static final String PROPERTIES_PATH = "local.properties";
    private Props props = new Props(PROPERTIES_PATH, StandardCharsets.UTF_8);
    @Override
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
