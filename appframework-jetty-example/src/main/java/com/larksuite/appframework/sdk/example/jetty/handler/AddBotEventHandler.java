/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty.handler;

import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.Message;
import com.larksuite.appframework.sdk.client.message.TextMessage;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.eventhandler.EventCallbackHandler;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddBotEvent;
import com.larksuite.appframework.sdk.exception.LarkClientException;

public class AddBotEventHandler implements EventCallbackHandler<AddBotEvent> {
    @Override
    public Object handler(InstanceContext c, AddBotEvent event) {

        Message msg = new TextMessage("Hello, I'm Echo Robot, try say to me.");

        try {
            if (c.getApp().getIsIsv()) {
                c.getLarkClient().sendChatMessageIsv(
                        MessageDestinations.chatId(event.getOpenChatId()),
                        msg,
                        event.getTenantKey());
            } else {
                c.getLarkClient().sendChatMessage(
                        MessageDestinations.chatId(event.getOpenChatId()),
                        msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
