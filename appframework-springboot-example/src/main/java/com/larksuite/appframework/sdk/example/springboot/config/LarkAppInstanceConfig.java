/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.config;

import com.larksuite.appframework.sdk.client.AbstractSessionManager;
import com.larksuite.appframework.sdk.client.ImageKeyStorage;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.utils.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class LarkAppInstanceConfig {

    @Bean
    public SessionManager sessionManager(StringRedisTemplate redisTemplate) {
        final String sessionRedisPrefix = "larkApp:session:";
        return new AbstractSessionManager() {

            @Override
            protected String loadSessionData(String sessionId) {
                return redisTemplate.opsForValue().get(sessionRedisPrefix + sessionId);
            }

            @Override
            protected void persistSessionData(String sessionId, String sessionData, int validPeriod) {
                redisTemplate.opsForValue().set(sessionRedisPrefix + sessionId, sessionData, validPeriod, TimeUnit.SECONDS);
            }
        };
    }

    @Bean
    public ImageKeyStorage imageKeyStorage(StringRedisTemplate redisTemplate) {
        final String imageKeyFormat = "larkApp:img:%s:%s";

        return new ImageKeyStorage() {

            @Override
            public void persistImageInfo(String appId, String appName, String imageName, ImageInfo imageInfo) {
                redisTemplate.opsForValue().set(
                        toKey(appName, imageName),
                        JsonUtil.toJsonString(imageInfo)
                );
            }

            @Override
            public ImageInfo loadImageInfo(String appId, String appName, String imageName) {
                String s = redisTemplate.opsForValue().get(toKey(appName, imageName));
                if (s == null) {
                    return null;
                }
                try {
                    return JsonUtil.toJavaObject(s, ImageInfo.class);
                } catch (IOException e) {
                    return null;
                }
            }

            private String toKey(String appName, String imageName) {
                return String.format(imageKeyFormat, appName, imageName);
            }
        };
    }
}
