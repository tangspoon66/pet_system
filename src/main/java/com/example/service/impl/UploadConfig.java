package com.example.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.SystemConfigDao;
import com.example.entity.SystemConfig;
import com.example.service.AbstractUploadConfig;


@Service
public class UploadConfig extends AbstractUploadConfig {


    @Autowired
    private SystemConfigDao systemConfigDao;

    //上传配置
    private Map<String, Object> uploadConfig;

    @Override
    public Map<String, Object> getUploadConfig() {
        if (uploadConfig != null) {
            return uploadConfig;
        }

        uploadConfig = new HashMap<>();
        SystemConfig systemConfig = systemConfigDao.selectByKey("upload_type");
        List<SystemConfig> list = systemConfigDao.selectByPid(new Integer(systemConfig.getValue()));
        uploadConfig.put(systemConfig.getKey(), systemConfig.getValue());
        list.forEach(systemConfig2 -> {
            uploadConfig.put(systemConfig2.getKey(), systemConfig2.getValue());
        });

        return uploadConfig;
    }


}
