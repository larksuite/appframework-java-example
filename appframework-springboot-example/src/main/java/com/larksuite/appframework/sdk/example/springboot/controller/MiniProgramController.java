/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.example.springboot.controller;

import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/miniprogram")
public class MiniProgramController {

    @Resource
    private LarkAppInstance echoRobotIsvLarkAppInstance;

    @GetMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest req, HttpServletResponse resp, @Param("code") String code) throws LarkClientException {

        try {
            System.out.println(code);
            echoRobotIsvLarkAppInstance.getMiniProgramAuthenticator().login(code, req, resp);
            return "ok";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.setStatus(403);
            return "login failed";
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Boolean logout(HttpServletRequest req, HttpServletResponse resp){
        return echoRobotIsvLarkAppInstance.getMiniProgramAuthenticator().logout(req, resp);
    }

    @RequestMapping("/auth")
    @ResponseBody
    public String auth(HttpServletRequest req, HttpServletResponse resp){

        SessionManager.SessionInfo session = echoRobotIsvLarkAppInstance.getMiniProgramAuthenticator().getSession(req);
        System.out.println(session);
        return null;
    }
}
