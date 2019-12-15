/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty.token;

import com.larksuite.appframework.sdk.AppConfiguration;
import com.larksuite.appframework.sdk.AppEventListener;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.LarkAppInstanceFactory;
import com.larksuite.appframework.sdk.example.jetty.handler.AppCardEventHandler;
import com.larksuite.appframework.sdk.example.jetty.handler.MessageEventHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetAppAccessToken {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try (InputStream is = ClassLoader.getSystemResourceAsStream("apps.properties")) {
            properties.load(is);
        }

        AppConfiguration appConfiguration = AppConfiguration.loadFromProperties("echoRobotIsv", properties);

        AppEventListener listener = LarkAppInstanceFactory
                .createAppEventListener()
                .onMessageEvent(new MessageEventHandler())
                .onCardEvent(new AppCardEventHandler())
                ;

        LarkAppInstance larkAppInstance =  LarkAppInstanceFactory
                .builder(appConfiguration)
                .feishu()
                .registerAppEventCallbackListener(listener)
                .create();
        System.out.println("get app_access_token:" + larkAppInstance.getTokenCenter().getAppAccessToken());
    }
}
