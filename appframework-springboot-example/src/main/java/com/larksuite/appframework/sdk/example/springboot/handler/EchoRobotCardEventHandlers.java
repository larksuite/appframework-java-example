/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.handler;


import com.google.common.collect.Lists;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.client.message.card.CardActionUtils;
import com.larksuite.appframework.sdk.client.message.card.Config;
import com.larksuite.appframework.sdk.client.message.card.Header;
import com.larksuite.appframework.sdk.client.message.card.element.Button;
import com.larksuite.appframework.sdk.client.message.card.module.Action;
import com.larksuite.appframework.sdk.client.message.card.module.Div;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import com.larksuite.appframework.sdk.core.protocol.card.CardEvent;
import com.larksuite.appframework.spring.boot.annotation.CardAction;
import com.larksuite.appframework.spring.boot.annotation.Handler;
import com.larksuite.appframework.spring.boot.annotation.LarkEventHandlers;

@LarkEventHandlers(appName = "echoRobotInternal")
public class EchoRobotCardEventHandlers {

    @Handler
    public Card onCardEvent(CardEvent event, LarkClient larkClient) {
        if ("TestCard.dp".equals(CardActionUtils.getActionMethodName(event))) {

            System.out.println("card messageId: " + event.getOpenMessageId() + ", userId: " +event.getUserId());

            return onDatePicked(event);
        }
        return null;
    }

//    @CardAction(methodName = "TestCard.dp")
    public Card onDatePicked(CardEvent cardEvent) {

        final String date = cardEvent.getAction().getOption();

        Action action = new Action(Lists.newArrayList(
                new Button("TestCard.btn", new Text(Text.Mode.PLAIN_TEXT, "ok")))
        );

        final Card card = new Card(new Config(true), new Header(new Text(Text.Mode.PLAIN_TEXT, "BtnTestCard")));
        card.setModules(Lists.newArrayList(
                new Div(new Text(Text.Mode.PLAIN_TEXT, date), null, null),
                action));
        return card;
    }

    @CardAction(methodName = "TestCard.btn")
    public Card onActionMethod(CardEvent cardEvent, LarkClient larkClient) {
        System.out.println("btn clicked");
        return null;
    }

    @CardAction(methodName = "TestCard.of")
    public Card onOverflow(CardEvent event){
        String option = event.getAction().getOption();
        System.out.println("overflow select:" + option);
        return null;
    }

    @CardAction(methodName = "TestCard.menu")
    public Card onSelectMenu(CardEvent event){
        String option = event.getAction().getOption();
        System.out.println("selectMenu select:" + option);
        return null;
    }
}
