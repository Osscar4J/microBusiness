package com.zhao.micro.service;

import com.zhao.micro.reqvo.OrderReqVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PoiService {

    /**
     * 导出订单详细信息
     *
     * @param reqVO
     * @return
     * @throws IOException
     */
    void exportOrder(HttpServletResponse response, OrderReqVO reqVO) throws IOException;
}
