/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.handler;

import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.*;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.FileMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.TextMessageEvent;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.spring.boot.annotation.Handler;
import com.larksuite.appframework.spring.boot.annotation.LarkEventHandlers;

@LarkEventHandlers(appName = "echoRobotInternal")
public class EchoRobotCallbackEventHandlers {

    @Handler
    public Object onRobotAdd(AddBotEvent event, LarkClient larkClient, InstanceContext instanceContext) {

        try {
            String tenantKey = event.getTenantKey();

            //get appAccessToken
            String appAccessToken = instanceContext.getTokenCenter().getAppAccessToken();

            //get tenantAccessToken
            String tenantAccessToken = instanceContext.getTokenCenter().getTenantAccessToken(tenantKey);

            larkClient.sendChatMessage(
                    MessageDestinations.chatId(event.getOpenChatId()),
                    new TextMessage("Hello, I'm Echo Robot, try say to me."));
        } catch (LarkClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Handler
    public Object onMessageEvent(TextMessageEvent event, InstanceContext ic) {

        String msg = event.getTextWithoutAtBot();
        LarkClient larkClient = ic.getLarkClient();
        String openChatId = event.getOpenChatId();
        String openId = event.getOpenId();

        LarkAppInstance larkAppInstance = ic.getLarkAppInstance();

        try {
            if (msg.trim().equals("upload_img")){
                //upload image
                GetEchoRobotMessage.uploadImage(larkClient);
            }else if (msg.trim().startsWith("batch")){
                //send batch message
                GetEchoRobotMessage.batchSendMessage(larkClient, openId);
            }else {

                //send message
                answerMsg(larkClient, openChatId, msg);
            }
        } catch (LarkClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void answerMsg(LarkClient larkClient, String openChatId, String msg) throws LarkClientException {

        if (msg.trim().equals("interact_card")) {
            //send interactive card message
            GetEchoRobotMessage.sendInteractionCardMessage(larkClient, openChatId);
        }else if (msg.trim().equals("image")){
            //send picture message
            GetEchoRobotMessage.sendImageMessage(larkClient, openChatId);
        }else if (msg.trim().equals("post")){
            //send rich text message
            GetEchoRobotMessage.sendPostMessage(larkClient, openChatId);
        }else if (msg.trim().equals("share")){
            //send group sharing message
            GetEchoRobotMessage.sendShareGroupMessage(larkClient, openChatId);
        }else if (msg.trim().equals("card")){
            //send card message
            GetEchoRobotMessage.sendCardMessage(larkClient, openChatId);
        }else {
            //send text message
            GetEchoRobotMessage.sendTextMessage(larkClient, openChatId, msg);
        }
    }

    @Handler
    public void onFileMessage(FileMessageEvent event, LarkClient larkClient) throws LarkClientException {
        String key = event.getFileKey();
        System.out.println(key);
        GetEchoRobotMessage.sendTextMessage(larkClient, event.getOpenChatId(), key);
    }
}