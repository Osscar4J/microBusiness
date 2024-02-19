package com.zhao.micro.service;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public interface QrCodeService {

    /**
     * 生成二维码图片base64编码格式字符串
     *
     * @param content 内容
     * @param width   图片宽
     * @param height  图片高
     * @return
     */
    public String createQrCode(String content, int width, int height);

    /**
     * 解码
     *
     * @param filepath 图片路径
     * @return
     */
    public String decode(String filepath);

    /**
     * 解码
     *
     * @param is 文件流
     * @return
     */
    public String decode(InputStream is);

    public String decode(BufferedImage bufferedImage);
}
