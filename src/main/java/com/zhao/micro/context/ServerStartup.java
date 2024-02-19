package com.zhao.micro.context;

import com.zhao.micro.service.DomainService;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.PayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ServerStartup implements ApplicationRunner {

    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private DomainService domainService;
    @Lazy
    @Autowired
    @Qualifier("xorpayService")
    private PayService payService;

    @Override
    public void run(ApplicationArguments args) {
        if (logger.isInfoEnabled())
            logger.info("======================= 初始化数据 =======================");
        domainService.initCache();
        payService.initAppidSecret();
    }

}
