package com.sunlands.api;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截入口
 *
 * @author chengweijie
 */
public class ApiGatewayServlet extends HttpServlet {

    private ApiHandler apiHandler;

    private ApiSign myApiSign;

    public ApiGatewayServlet() {
    }

    public ApiGatewayServlet(ApiSign myApiSign) {
        Assert.notNull(myApiSign, "ApiSign不可为null");
        this.myApiSign = myApiSign;
    }

    @Override
    public void init() throws ServletException {
        Assert.notNull(myApiSign, "ApiSign不可为null");
        super.init();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.apiHandler = new ApiHandler(myApiSign);
        apiHandler.initApiStore(context);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        Assert.isTrue(method.equals("POST"), "仅支持POST请求，请检查请求格式");
        apiHandler.handle(req, resp);
    }
}
