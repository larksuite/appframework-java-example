/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.jetty.servlet;

import com.google.common.io.CharStreams;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.example.jetty.Global;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LarkNotifyServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path;
        {
            String requestUri = req.getRequestURI();
            String basePath = req.getServletPath();
            path = requestUri.substring(basePath.length());
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
        }

        String[] parts = path.split("/");

        if (parts.length != 2) {
            return;
        }

        String type = parts[0];
        String appShortName = parts[1];

        LarkAppInstance ins = Global.getAppInstance(appShortName);

        if (ins == null) {
            return;
        }


        final String requestString = CharStreams.toString(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));

        System.out.println("LarkNotifyServlet requestString: " + requestString);

        String respData = null;
        if (type.equals("event")) {
            respData = ins.receiveLarkNotify(requestString);
        }

        if (type.equals("card")) {
            respData = ins.receiveCardNotify(requestString, req);
        }

        if (respData != null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(respData);
        }

    }
}
