/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty;

import com.larksuite.appframework.sdk.AppConfiguration;
import com.larksuite.appframework.sdk.AppEventListener;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.LarkAppInstanceFactory;
import com.larksuite.appframework.sdk.example.jetty.handler.AddBotEventHandler;
import com.larksuite.appframework.sdk.example.jetty.handler.AppCardEventHandler;
import com.larksuite.appframework.sdk.example.jetty.handler.MessageEventHandler;
import com.larksuite.appframework.sdk.example.jetty.servlet.LarkNotifyServlet;
import com.larksuite.appframework.sdk.example.jetty.storage.RedisAppTicketStorage;
import com.larksuite.appframework.sdk.example.jetty.storage.RedisImageKeyStorage;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class Launcher {

    public static void main(String[] args) throws Exception {

        RedisImageKeyStorage imageKeyStorage = new RedisImageKeyStorage();

        Global.addAppInstance(initBotApp1(imageKeyStorage));
        Global.addAppInstance(initBotApp2(imageKeyStorage));

        Server server = new Server(7070);
        ServletContextHandler handler = new ServletContextHandler(server, "/");

        handler.addServlet(LarkNotifyServlet.class, "/notify/*");

        server.start();
    }


    private static LarkAppInstance initBotApp1(RedisImageKeyStorage imageKeyStorage) throws Exception {


        Properties properties = new Properties();
        try (InputStream is = ClassLoader.getSystemResourceAsStream("apps.properties")) {
            properties.load(is);
        }

        AppConfiguration appConfiguration = AppConfiguration.loadFromProperties("echoRobotIsv", properties);

        AppEventListener listener = LarkAppInstanceFactory
                .createAppEventListener()
                .onMessageEvent(new MessageEventHandler())
                .onAddBotEvent(new AddBotEventHandler())
                .onCardEvent(new AppCardEventHandler())
                ;

        LarkAppInstanceFactory.LarkAppInstanceBuilder builder = LarkAppInstanceFactory
                .builder(appConfiguration)
                .feishu()
                .appTicketStorage(new RedisAppTicketStorage())
                .imageKeyStorage(imageKeyStorage)
                .registerAppEventCallbackListener(listener)
                ;

        return builder.create();

    }


    private static LarkAppInstance initBotApp2(RedisImageKeyStorage imageKeyStorage) throws Exception {
        Properties properties = new Properties();
        try (InputStream is = ClassLoader.getSystemResourceAsStream("apps.properties")) {
            properties.load(is);
        }

        AppConfiguration appConfiguration = AppConfiguration.loadFromProperties("echoRobotInternal", properties);

        AppEventListener listener = LarkAppInstanceFactory
                .createAppEventListener()
                .onMessageEvent(new MessageEventHandler())
                .onCardEvent(new AppCardEventHandler())
                ;

        return LarkAppInstanceFactory
                .builder(appConfiguration)
                .feishu()
                .imageKeyStorage(imageKeyStorage)
                .registerAppEventCallbackListener(listener)
                .create();
    }
}
