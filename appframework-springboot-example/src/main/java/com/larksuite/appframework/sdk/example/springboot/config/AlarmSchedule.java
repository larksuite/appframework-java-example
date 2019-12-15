/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.config;

import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.TextMessage;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AlarmSchedule {

    @Autowired
    private LarkClient echoRobotInternalLarkClient;

    @Resource(name = "echoRobotInternalLarkClient")
    private LarkClient c;

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void alarmPerMinute() {
        try {
            LarkClient.GroupListResult groupListResult = echoRobotInternalLarkClient.fetchGroupList(10, "");

            groupListResult.getGroups().forEach(g -> {
                try {
                    LarkClient.GroupInfoResult groupInfoResult = echoRobotInternalLarkClient.fetchGroupInfo(g.getChatId());

                    echoRobotInternalLarkClient.sendChatMessage(
                            MessageDestinations.chatId(groupInfoResult.getChatId()),
                            new TextMessage("Hello, I'm Echo Robot, try say to me."));
                } catch (LarkClientException e) {
                    e.printStackTrace();
                }
            });

        } catch (LarkClientException e) {
            e.printStackTrace();
        }
    }
}
