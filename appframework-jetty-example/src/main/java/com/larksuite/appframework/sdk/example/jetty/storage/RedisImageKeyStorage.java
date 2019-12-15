/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty.storage;

import com.larksuite.appframework.sdk.client.ImageKeyStorage;
import com.larksuite.appframework.sdk.example.jetty.Global;
import com.larksuite.appframework.sdk.utils.JsonUtil;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class RedisImageKeyStorage implements ImageKeyStorage {

    final String imageKeyFormat = "larkApp:img:%s:%s";

    @Override
    public void persistImageInfo(String appId, String appName, String imageName, ImageInfo imageInfo) {

        try (Jedis jedis = Global.JEDIS_POOL.getResource()) {
            jedis.set(toKey(appName, imageName), JsonUtil.toJsonString(imageInfo));
        }
    }

    @Override
    public ImageInfo loadImageInfo(String appId, String appName, String imageName) {

        try (Jedis jedis = Global.JEDIS_POOL.getResource()) {
            String s = jedis.get(toKey(appName, imageName));

            if (s == null) {
                return null;
            }

            try {
                return JsonUtil.toJavaObject(s, ImageInfo.class);
            } catch (IOException e) {
                return null;
            }
        }
    }

    private String toKey(String appName, String imageName) {
        return String.format(imageKeyFormat, appName, imageName);
    }
}
