package com.example.service;

import java.util.Map;

public interface UploadConfigService extends SystemConfigService {

	Map<String, Object> getUploadConfig();
}
