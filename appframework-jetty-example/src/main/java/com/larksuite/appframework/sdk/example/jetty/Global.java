/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty;

import com.larksuite.appframework.sdk.LarkAppInstance;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class Global {

    static Map<String, LarkAppInstance> appInstanceMap = new HashMap<>();

    public static void addAppInstance(LarkAppInstance ins) {

        appInstanceMap.put(ins.getAppShortName(), ins);
    }

    public static LarkAppInstance getAppInstance(String appShortName) {
        return appInstanceMap.get(appShortName);
    }

    public static JedisPool JEDIS_POOL = new JedisPool();
}
