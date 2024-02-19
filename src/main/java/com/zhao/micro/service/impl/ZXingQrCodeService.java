package com.zhao.micro.service.impl;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.service.QrCodeService;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class ZXingQrCodeService implements QrCodeService {

    @Override
    public String createQrCode(String content, int width, int height) {
        if (StringUtils.isEmpty(content))
            throw new BusinessException("二维码内容不能为空");
        if (content.length() > 150)
            throw new BusinessException("内容过长");
        if (width < 20)
            width = 20;
        if (height < 20)
            height = 20;
        ServletOutputStream stream = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Map<EncodeHintType, Comparable> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, "png", os);
            return "data:image/png;base64," + Base64.encode(os.toByteArray());
        } catch (Exception e) {
            throw new BusinessException(e.getLocalizedMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    throw new BusinessException(e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public String decode(String filepath) {
        if (StringUtils.isEmpty(filepath))
            throw new BusinessException("非法参数");
        BufferedImage image;
        if (filepath.startsWith("http://") || filepath.startsWith("https://")) {
            try {
                image = ImageIO.read(new URL(filepath));
            } catch (IOException e) {
                throw new BusinessException("图片地址有误");
            }
        } else {
            throw new BusinessException("图片地址有误");
        }
        if (image == null)
            throw new BusinessException("找不到图片");
        return decode(image);
    }

    @Override
    public String decode(InputStream is) {
        try {
            return decode(ImageIO.read(is));
        } catch (IOException e) {
            throw new BusinessException(e.getLocalizedMessage());
        }
    }

    @Override
    public String decode(BufferedImage bufferedImage) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> decodeHints = new HashMap<>();
            decodeHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(bitmap, decodeHints);
            return result.getText();
        } catch (Exception e) {
            throw new BusinessException("未识别到二维码");
        }
    }
}
