package com.zhao.micro.utils;

import com.zhao.micro.exception.BusinessException;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtils {
    private static File file = null;
    private static Logger logger = Logger.getLogger(ImageUtils.class);

    static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();
    static private byte[] codes = new byte[256];

    static {
        for (int i = 0; i < 256; i++)
            codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++)
            codes[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++)
            codes[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++)
            codes[i] = (byte) (52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    /**
     * 读取图像的二进制流
     *
     * @param infile
     * @return
     */
    public static FileInputStream getByteImage(String infile) {
        FileInputStream inputImage = null;
        file = new File(infile);
        try {
            inputImage = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new BusinessException("文件不存在");
        }
        return inputImage;
    }

    public static void readImg(InputStream inputStream, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, len);// 写
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new BusinessException("文件不存在");
        } catch (IOException e) {
            logger.error(e);
            throw new BusinessException("服务器异常");
        }
    }

    public static void downLoadImg(String imgPath, String savePath) {
        // 测试地址，"http://m.1more.com/image/pic1_ok.jpg";
        BufferedReader reader = null;
        try {
            URL url = new URL(imgPath);
            URLConnection conn = url.openConnection();
            ImageUtils.readImg(conn.getInputStream(), savePath);
        } catch (Exception e) {
            logger.error(e);
            throw new BusinessException("文件不存在");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e2) {
                    logger.error(e2);
                }
            }
        }
    }

    /**
     * 本地图片文件转base64编码
     *
     * @param file 图片
     * @return
     */
    public static String imageFile2Base64(File file) {
        FileInputStream is;
        try {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            is = new FileInputStream(file);
            byte[] by = new byte[1024];
            int len;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            return encode(data.toByteArray());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public static String imageToBase64ByOnline(String imageUrl) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            URL url = new URL(imageUrl);
            byte[] by = new byte[1024];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            is = conn.getInputStream();
            int len;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                throw new BusinessException(e.getMessage());
            }
        }
//		BASE64Encoder encoder = new BASE64Encoder();
        return encode(data.toByteArray());
    }

    static public String encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;
            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return String.valueOf(out);
    }

    /**
     * 裁剪图片
     *
     * @param img 图片文件
     * @param x   起始位置x
     * @param y   起始位置y
     * @param w   裁剪的宽度
     * @param h   裁剪的高度
     * @return
     */
    public static BufferedImage cutImage(File img, int x, int y, int w, int h) {
        try {
            BufferedImage image = ImageIO.read(img);
            return image.getSubimage(x, y, w, h);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64字符串转byte数组
     *
     * @param base64Str 要转换的base64字符串
     * @return
     */
    public static byte[] base64ToByte(String base64Str) {
        if (base64Str == null) // 图像数据为空
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] b;
        try {
            b = decoder.decodeBuffer(base64Str);
        } catch (IOException e) {
            return null;
        }
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        return b;
    }
}
