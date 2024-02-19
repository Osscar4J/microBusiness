package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Order;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.OrderMapper;
import com.zhao.micro.reqvo.OrderReqVO;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.PoiService;
import com.zhao.micro.utils.DateStyle;
import com.zhao.micro.utils.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class PoiServiceImpl implements PoiService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;

    private int defaultCellHeight = 20;

    private HSSFFont createFont(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        return font;
    }

    private void autosetCellBorder(HSSFCellStyle cellStyle) {
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); //左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); //上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); //右边框
    }

    private void createHeaderRow(String[] titles, HSSFSheet sheet, HSSFWorkbook workbook) {
        HSSFRow headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(defaultCellHeight);
        HSSFCell cell;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        autosetCellBorder(cellStyle);

        cellStyle.setFont(createFont(workbook));

        for (int i = 0; i < titles.length; i++) {
            cell = headerRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(cellStyle);
        }
    }

    private void createDownloadResponse(HttpServletResponse response, byte[] bytes, String fileName) throws UnsupportedEncodingException {
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
        response.addHeader("Content-Length", "" + bytes.length);
        response.addHeader("Content-Type", "application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new BusinessException(500, e.getLocalizedMessage());
        }
    }

    @Override
    public void exportOrder(HttpServletResponse response, OrderReqVO reqVO) throws IOException {
        reqVO.setStatus(SysConstants.OrderStatus.PAYED);
        reqVO.setNeedPage(0);
        IPage<Order> page = orderService.getPage(reqVO);

        if (page.getRecords().isEmpty())
            throw new BusinessException(ResponseStatus.ORDER_EMPTY);

        HSSFWorkbook workbook = new HSSFWorkbook();
        String[] titles = new String[]{"订单号", "名称", "商品", "金额", "数量", "状态", "创建时间", "支付时间", "收件人", "手机号码", "地址", "销售员工", "销售代理"};
        HSSFSheet sheet = workbook.createSheet("订单明细");
        sheet.setColumnWidth(0, 5600);
        sheet.setColumnWidth(1, 6800);
        sheet.setColumnWidth(2, 3800);
        sheet.setColumnWidth(3, 3800);
        sheet.setColumnWidth(4, 3800);
        sheet.setColumnWidth(5, 3800);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 10000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);

        createHeaderRow(titles, sheet, workbook);

        HSSFRow row;
        HSSFCell cell;
        Order order;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        autosetCellBorder(cellStyle);

        HSSFCellStyle leftCellStyle = workbook.createCellStyle();
        leftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        autosetCellBorder(leftCellStyle);

        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        for (int i = 1; i <= page.getRecords().size(); i++) {
            order = page.getRecords().get(i - 1);
            row = sheet.createRow(i);
            // 订单号
            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(order.getOrderNo());
            // 名称
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(order.getName() + "（" + order.getProdProperty() + "）");
            // 商品名称
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            if (order.getProduct() != null) {
                cell.setCellValue(order.getProduct().getName());
            } else {
                cell.setCellValue("");
            }
            // 金额
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(order.getTotalFee() / 100D);
            // 数量
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(order.getProdCount());
            // 状态
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("已支付");
            // 创建时间
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(DateUtils.DateToString(order.getCreateTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
            // 支付时间
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(DateUtils.DateToString(order.getPayTime(), DateStyle.YYYY_MM_DD_HH_MM_SS));
            // 客户姓名
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle);
            if (order.getUser() != null) {
                cell.setCellValue(order.getUser().getNickname());
            } else {
                cell.setCellValue("");
            }
            // 客户手机号
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle);
            if (order.getUser() != null) {
                if (order.getUser().getPhone() != null) {
                    cell.setCellValue(order.getUser().getPhone());
                } else {
                    cell.setCellValue(order.getUser().getNickname());
                }
            } else {
                cell.setCellValue("");
            }
            // 地址
            cell = row.createCell(10);
            cell.setCellStyle(leftCellStyle);
            if (order.getUser() != null && order.getUser().getAddress() != null) {
                cell.setCellValue(order.getUser().getAddress().getAddr());
            } else {
                cell.setCellValue("");
            }
            // 销售员工
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle);
            if (order.getSeller() != null) {
                cell.setCellValue(order.getSeller().getNickname());
            }
            // 销售代理
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle);
            if (order.getProxy() != null) {
                cell.setCellValue(order.getProxy().getNickname());
            }
        }

        String fileName = "";
        if (!StringUtils.isEmpty(reqVO.getSt()))
            fileName += reqVO.getSt();
        if (!StringUtils.isEmpty(reqVO.getEt()))
            fileName += "-" + reqVO.getEt();
        fileName += "订单明细";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        createDownloadResponse(response, baos.toByteArray(), fileName);
    }
}
