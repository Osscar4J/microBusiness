package com.zhao.micro.task;

import com.zhao.micro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * 定时任务
 *
 * @author Administrator
 */
@Component
public class PeriodTask {

    @Autowired
    private OrderService orderService;

    /**
     * 运行周期，表达式对应的是：秒、分、时、天、月、?
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void oneM() {
        // 查询3分钟内未支付的订单的状态
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 3);
        orderService.autoQueryOrderStatus(calendar.getTime());
    }

}
