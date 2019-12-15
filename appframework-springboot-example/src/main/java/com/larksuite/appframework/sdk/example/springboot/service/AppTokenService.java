/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.service;

import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AppTokenService {

    @Resource(name = "echoRobotInternalLarkAppInstance")
    private LarkAppInstance echoRobotInternalLarkAppInstance;

    public String getAppAccessToken(){

        TokenCenter tokenCenter = echoRobotInternalLarkAppInstance.getTokenCenter();
        String appAccessToken = tokenCenter.getAppAccessToken();
        return appAccessToken;
    }
}
