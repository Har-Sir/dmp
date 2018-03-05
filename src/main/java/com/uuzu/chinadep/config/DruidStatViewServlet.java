package com.uuzu.chinadep.config;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by lixing on 2017/4/26.
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid*//*", initParams = {
        @WebInitParam(name = "loginUsername", value = "agg-lixing"),
        @WebInitParam(name = "loginPassword", value = "admin")
})
public class DruidStatViewServlet extends StatViewServlet {
}
