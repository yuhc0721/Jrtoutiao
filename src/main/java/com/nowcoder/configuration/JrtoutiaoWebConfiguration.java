package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Component
public class JrtoutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    //将拦截器注册到MVC上
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        //只在访问setting及以后的页面菜会触发
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg*"); //两个拦截器，第一个看用户是谁，第二个看是否有权限。
        super.addInterceptors(registry);
    }
}
