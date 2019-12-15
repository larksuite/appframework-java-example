/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.handler;

import com.google.common.collect.Lists;
import com.larksuite.appframework.sdk.client.BatchMessageDestination;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.*;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.client.message.card.Config;
import com.larksuite.appframework.sdk.client.message.card.Header;
import com.larksuite.appframework.sdk.client.message.card.TemplateColor;
import com.larksuite.appframework.sdk.client.message.card.element.*;
import com.larksuite.appframework.sdk.client.message.card.module.*;
import com.larksuite.appframework.sdk.client.message.card.objects.Confirm;
import com.larksuite.appframework.sdk.client.message.card.objects.Field;
import com.larksuite.appframework.sdk.client.message.card.objects.Option;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import com.larksuite.appframework.sdk.client.message.post.PostBuilder;
import com.larksuite.appframework.sdk.exception.LarkClientException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetEchoRobotMessage {

    /**
     * send interactive card message
     * @param larkClient
     * @param openChatId
     * @throws LarkClientException
     */
    public static void sendInteractionCardMessage(LarkClient larkClient, String openChatId) throws LarkClientException {
        DatePicker datePicker = new DatePicker("TestCard.dp")
                .setPlaceholder(new Text(Text.Mode.PLAIN_TEXT, "Please choose a date"));
        Button button = new Button("TestCard.btn", new Text(Text.Mode.PLAIN_TEXT, "ok"));

        Action action = new Action(Lists.newArrayList(datePicker, button));

        Card card = new Card(new Config(true), new Header(new Text(Text.Mode.PLAIN_TEXT, "BtnTestCard"), TemplateColor.RED));
        card.setModules(Lists.newArrayList(
                action
        ));
        CardMessage cardMessage =  new CardMessage(card.toObjectForJson());
        cardMessage.setUpdateMulti(true);

        larkClient.sendChatMessage(
                MessageDestinations.chatId(openChatId),
                cardMessage);
    }

    /**
     * send text message
     * @param larkClient
     * @param openChatId
     * @param msg
     * @throws LarkClientException
     */
    public static void sendTextMessage(LarkClient larkClient, String openChatId, String msg) throws LarkClientException {
        TextMessage textMessage = new TextMessage(msg);
        larkClient.sendChatMessage(
                MessageDestinations.chatId(openChatId),
                textMessage);
    }

    /**
     * send picture message
     * @param larkClient
     * @param openChatId
     * @throws LarkClientException
     */
    public static void sendImageMessage(LarkClient larkClient, String openChatId) throws LarkClientException {
        InputStream inputStream = getImageStream("https://s0.pstatp.com/ee/lark-open/web/static/apply.226f11cb.png");
        LarkClient.UploadImageResult result = larkClient.uploadImage("lark", inputStream);
        String imageKey = result.getImageKey();
        ImageMessage imageMessage = new ImageMessage(imageKey);

        larkClient.sendChatMessage(
                MessageDestinations.chatId(openChatId),
                imageMessage);
    }

    /**
     * send rich text message
     * @param larkClient
     * @param openChatId
     * @throws LarkClientException
     */
    public static void sendPostMessage(LarkClient larkClient, String openChatId) throws LarkClientException {
        PostBuilder.Post post = PostBuilder.newPost();

        InputStream inputStream = getImageStream("https://sf1-ttcdn-tos.pstatp.com/obj/website-img/11ec939007a63d679f4b37ae497cdf94_oYduFid8mI.png");
        LarkClient.UploadImageResult result = larkClient.uploadImage("lark", inputStream);
        String imageKey = result.getImageKey();

        //ZH-cn
        PostBuilder.Language cnLanguage = post.createZhCnLanguage("这是一个标题");
        PostBuilder.Line line1 = cnLanguage.createLine();
        line1.createTextTag("first line", true)
                .creatATag("link:", "https://www.feishu.cn");

        PostBuilder.Line line2 = cnLanguage.createLine();
        line2.createTextTag("second line", Boolean.TRUE)
                .createTextTag("text:", Boolean.TRUE);

        PostBuilder.Line line3 = cnLanguage.createLine();
        line3.createImgTag(imageKey, 300, 300);

        //EN-us
        PostBuilder.Language enUsLanguage = post.createEnUsLanguage("this is a title");
        PostBuilder.Line line4 = enUsLanguage.createLine();
        line4.creatATag("link:", "https://www.feishu.cn")
                .createImgTag(imageKey, 200, 100);

        PostMessage postMessage = new PostMessage(post.toContent());

        larkClient.sendChatMessage(
                MessageDestinations.chatId(openChatId),
                postMessage);
    }

    /**
     * send group sharing message
     * @param larkClient
     * @param openChatId
     * @throws LarkClientException
     */
    public static void sendShareGroupMessage(LarkClient larkClient, String openChatId) throws LarkClientException {
        ShareGroupMessage groupMessage = new ShareGroupMessage(openChatId);

        larkClient.sendChatMessage(
                MessageDestinations.chatId(openChatId),
                groupMessage);
    }

    /**
     * send card message
     * @param larkClient
     * @param openChatId
     * @throws LarkClientException
     */
    public static void sendCardMessage(LarkClient larkClient, String openChatId) throws LarkClientException {
        //config
        Config config = new Config(Boolean.FALSE);

        //header
        Header header = new Header(new Text(Text.Mode.PLAIN_TEXT, "this is a card"), TemplateColor.GREY);

        Card card = new Card(config, header);

        //div
        Field field1 = new Field(new Text(Text.Mode.PLAIN_TEXT, "Content module（div）"), Boolean.FALSE);
        Field field2 = new Field(new Text(Text.Mode.LARK_MD, "**function:**\nNew function", 0), Boolean.FALSE);

        InputStream inputStream = getImageStream("https://s0.pstatp.com/ee/lark-open/web/static/apply.226f11cb.png");
        LarkClient.UploadImageResult result = larkClient.uploadImage("lark", inputStream);
        String imageKey = result.getImageKey();
        Element extra = new Image(imageKey, new Text(Text.Mode.PLAIN_TEXT, "alt image"));

        Div div = new Div(new Text(Text.Mode.PLAIN_TEXT, "Content module"),
                Lists.newArrayList(field1, field2),
                extra);

        //hr
        Hr hr = new Hr();

        //img
        Img img = new Img(imageKey, new Text(Text.Mode.PLAIN_TEXT, "alt image"),
                new Text(Text.Mode.PLAIN_TEXT, "alt image title"));

        //Action - Button
        Button button = new Button("TestCard.btn", new Text(Text.Mode.PLAIN_TEXT, "action button"));

        //Action - DatePicker
        DatePicker datePicker = new DatePicker("TestCard.dp").setPlaceholder(
                new Text(Text.Mode.PLAIN_TEXT, "please select date")
        );

        //Action - Overflow
        Option option1 = new Option("option1", new Text(Text.Mode.PLAIN_TEXT, "option1"));
        Option option2 = new Option("option2", new Text(Text.Mode.PLAIN_TEXT, "option2"), "https://open.feishu.cn");
        Option option3 = new Option("option3", new Text(Text.Mode.PLAIN_TEXT, "option3"), "https://open.feishu.cn");
        Overflow overflow = new Overflow("TestCard.of", Lists.newArrayList(option1, option2, option3));
        overflow.setConfirm(new Confirm(new Text(Text.Mode.PLAIN_TEXT, "Confirm"),
                new Text(Text.Mode.PLAIN_TEXT, "confirm your selection")));

        //Action - SelectMenu
        SelectMenu staticMenu = new SelectMenu.Static("TestCard.menu");
        staticMenu.setConfirm(new Confirm(new Text(Text.Mode.PLAIN_TEXT, "Confirm"),
                new Text(Text.Mode.PLAIN_TEXT, "confirm your selection")))
                .setOptions(Lists.newArrayList(option1, option2, option3))
                .setPlaceholder(new Text(Text.Mode.PLAIN_TEXT, "select a menu"));

        SelectMenu personMenu = new SelectMenu.Person("TestCard.menu");
        personMenu.setPlaceholder(new Text(Text.Mode.PLAIN_TEXT, "select a person"));

        //Action
        Action action = new Action(Lists.newArrayList(button, datePicker, overflow, staticMenu, personMenu));

        //note
        Element noteImage = new Image(imageKey, new Text(Text.Mode.PLAIN_TEXT, "note image"));
        Note note = new Note(Lists.newArrayList(new Text(Text.Mode.PLAIN_TEXT, "note text"),
                noteImage));

        card.setModules(Lists.newArrayList(div, hr, img, hr, action, note));
        CardMessage cardMessage = new CardMessage(card.toObjectForJson());

        larkClient.sendChatMessage(
                MessageDestinations.chatId(openChatId),
                cardMessage);
    }

    /**
     * send batch message
     * @param larkClient
     * @param openId
     * @throws LarkClientException
     */
    public static void batchSendMessage(LarkClient larkClient, String openId) throws LarkClientException {

        BatchMessageDestination batchMessageDestination = new BatchMessageDestination();
        //multiple openIds can be set
        batchMessageDestination.setOpenIds(Lists.newArrayList(openId));
        LarkClient.BatchSendChatMessageResult result = larkClient.batchSendChatMessage(
                batchMessageDestination,
                new TextMessage("send batch msg"));
        System.out.println("msg result:" + result);
    }

    /**
     * upload image
     * @param larkClient
     * @throws LarkClientException
     */
    public static void uploadImage(LarkClient larkClient) throws LarkClientException {
        InputStream inputStream = getImageStream("https://s0.pstatp.com/ee/lark-open/web/static/apply.226f11cb.png");
        LarkClient.UploadImageResult result = larkClient.uploadImage("lark", inputStream);

        System.out.println("upload image result:" + result);
    }

    /**
     * get the input stream of network picture
     * @param url
     * @return
     */
    public static InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
