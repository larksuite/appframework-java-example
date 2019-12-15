/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty.handler;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.CardMessage;
import com.larksuite.appframework.sdk.client.message.Message;
import com.larksuite.appframework.sdk.client.message.TextMessage;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.client.message.card.Config;
import com.larksuite.appframework.sdk.client.message.card.Header;
import com.larksuite.appframework.sdk.client.message.card.element.Button;
import com.larksuite.appframework.sdk.client.message.card.module.Action;
import com.larksuite.appframework.sdk.client.message.card.module.Img;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.eventhandler.EventCallbackHandler;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.BaseMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.TextMessageEvent;

import java.io.InputStream;


public class MessageEventHandler implements EventCallbackHandler<BaseMessageEvent> {

    @Override
    public Object handler(InstanceContext c, BaseMessageEvent baseMessageEvent) {

        TextMessageEvent event = new TextMessageEvent();
        if (baseMessageEvent instanceof TextMessageEvent){
            event = (TextMessageEvent) baseMessageEvent;
        }else
            return null;

        Message msg = new TextMessage(event.getTextWithoutAtBot());

        if (event.getTextWithoutAtBot().trim().equals("card")) {

            try {
                String imageKey;
                try (InputStream is = Resources.getResource("card_banner.jpg").openStream()) {
                    if (c.getApp().getIsIsv()) {
                        imageKey = c.getImageKeyManager().registerImageIsv("card_banner", is, event.getTenantKey());
                    } else   {
                        imageKey = c.getImageKeyManager().registerImage("card_banner", is);
                    }
                }

                final Card card = new Card(new Config(true), new Header(new Text(Text.Mode.PLAIN_TEXT, "BtnTestCard")));
                card.setModules(Lists.newArrayList(
                        new Img(imageKey, new Text(Text.Mode.PLAIN_TEXT, "alt"), new Text(Text.Mode.PLAIN_TEXT, "title")),
                        new Action(Lists.newArrayList(new Button("TestCard.btn", new Text(Text.Mode.PLAIN_TEXT, "ok"))))
                ));
                msg = new CardMessage(card.toObjectForJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




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
