package com.zhao.micro.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class AliOSSUploader {

    /**
     * 以流形式上传文件到OSS
     *
     * @param inputream 文件流
     * @param filepath  文件名，包含路径（以bucket为根目录）
     */
    public static void upload(InputStream inputream, String filepath) {
        upload(inputream, filepath, false);
    }

    /**
     * 以流形式上传文件到OSS
     *
     * @param inputream 文件流
     * @param filepath  文件名，包含路径（以bucket为根目录）
     */
    public static void upload(InputStream inputream, String filepath, boolean publicRead) {
        OSSClient ossClient = new OSSClient(AliOSSConfig.END_POINT, AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
        ossClient.putObject(AliOSSConfig.BUCKET, filepath, inputream);
//		if (publicRead)
//			ossClient.setObjectAcl(AliOSSConfig.BUCKET, filepath, CannedAccessControlList.PublicRead);
        ossClient.shutdown();
    }

    /**
     * 以字节上传文件到OSS
     *
     * @param content  字节数组
     * @param filepath 文件名，包含路径（以bucket为根目录）
     */
    public static void upload(byte[] content, String filepath) {
        upload(content, filepath, false);
    }

    /**
     * 以字节上传文件到OSS
     *
     * @param content    字节数组
     * @param filepath   文件名，包含路径（以bucket为根目录）
     * @param publicRead 是否设置公共读
     */
    public static void upload(byte[] content, String filepath, boolean publicRead) {
        OSSClient ossClient = new OSSClient(AliOSSConfig.END_POINT, AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
        ossClient.putObject(AliOSSConfig.BUCKET, filepath, new ByteArrayInputStream(content));
        if (publicRead)
            ossClient.setObjectAcl(AliOSSConfig.BUCKET, filepath, CannedAccessControlList.PublicRead);
        ossClient.shutdown();
    }

    /**
     * 上传文件到OSS
     *
     * @param file     文件
     * @param filepath 文件名，包含路径（以bucket为根目录）
     */
    public static void upload(File file, String filepath) {
        upload(file, filepath, false);
    }

    /**
     * 上传文件到OSS
     *
     * @param file       文件
     * @param filepath   文件名，包含路径（以bucket为根目录）
     * @param publicRead 是否设置公共读
     */
    public static void upload(File file, String filepath, boolean publicRead) {
        OSSClient ossClient = new OSSClient(AliOSSConfig.END_POINT, AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
        ossClient.putObject(AliOSSConfig.BUCKET, filepath, file);
        if (publicRead)
            ossClient.setObjectAcl(AliOSSConfig.BUCKET, filepath, CannedAccessControlList.PublicRead);
        ossClient.shutdown();
    }

}
