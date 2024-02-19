package com.zhao.micro.context;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ServerShutdown {

    @PreDestroy
    public void destory() {

    }
}
