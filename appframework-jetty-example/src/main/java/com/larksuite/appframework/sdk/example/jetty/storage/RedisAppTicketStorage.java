/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty.storage;

import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;
import com.larksuite.appframework.sdk.example.jetty.Global;
import redis.clients.jedis.Jedis;

public class RedisAppTicketStorage implements AppTicketStorage {
    @Override
    public void updateAppTicket(String appShortName, String appId, String appTicket) {
        try (Jedis jedis = Global.JEDIS_POOL.getResource()) {
            jedis.set(redisKey(appShortName), appTicket);
        }
    }

    @Override
    public String loadAppTicket(String appShortName, String appId) {
        try (Jedis jedis = Global.JEDIS_POOL.getResource()) {
            return jedis.get(redisKey(appShortName));
        }
    }

    private String redisKey(String appShortName) {
        return "larkApp:appTicket:" + appShortName;
    }
}
