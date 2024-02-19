package com.zhao.micro.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.oss.AliOSSUploader;
import com.zhao.micro.respvo.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;

@LoginRequired
@RestController
@RequestMapping("/api/file")
public class FileApi {

    private JSONObject ueConfig = null;

    @RequestMapping
    public Object ueHandler(HttpServletRequest request, String action, String path) throws IOException {

        switch (action) {
            case "config":
                if (ueConfig == null) {
                    ueConfig = new JSONObject();
                    ueConfig.put("imageActionName", "uploadimage");
                    ueConfig.put("imageMaxSize", 2048000);
                    ueConfig.put("imageInsertAlign", "none");
                    JSONArray allowFiles = new JSONArray();
                    allowFiles.add(".png");
                    allowFiles.add(".jpg");
                    allowFiles.add(".jpeg");
                    allowFiles.add(".gif");
                    allowFiles.add(".bmp");
                    ueConfig.put("imageAllowFiles", allowFiles);
                    ueConfig.put("imageUrlPrefix", "https://yangyangdeyi.oss-cn-shenzhen.aliyuncs.com");
                    ueConfig.put("imagePathFormat", "/product/{yyyy}{mm}{dd}{time}{rand:6}");
                    ueConfig.put("videoActionName", "uploadimage");
                    ueConfig.put("videoFieldName", "file");
                    ueConfig.put("videoPathFormat", "/product/{yyyy}{mm}{dd}{time}{rand:6}");
                    ueConfig.put("videoUrlPrefix", "https://yangyangdeyi.oss-cn-shenzhen.aliyuncs.com");
                    ueConfig.put("videoMaxSize", 409600000);
                    allowFiles = new JSONArray();
                    allowFiles.add(".flv");
                    allowFiles.add(".swf");
                    allowFiles.add(".mkv");
                    allowFiles.add(".avi");
                    allowFiles.add(".rm");
                    allowFiles.add(".rmvb");
                    allowFiles.add(".mpeg");
                    allowFiles.add(".mpg");
                    allowFiles.add(".ogg");
                    allowFiles.add(".ogv");
                    allowFiles.add(".mov");
                    allowFiles.add(".wmv");
                    allowFiles.add(".mp4");
                    allowFiles.add(".webm");
                    allowFiles.add(".mp3");
                    allowFiles.add(".wav");
                    allowFiles.add(".mid");
                    ueConfig.put("videoAllowFiles", allowFiles);
                }
                return ueConfig;
            case "uploadimage": // 上传文件
                if (path == null)
                    path = "product/";
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile file = multipartRequest.getFile(multipartRequest.getFileNames().next());
                JSONObject re = new JSONObject();
                re.put("state", "SUCCESS");
                re.put("code", "success");
                JSONObject infoMap = new JSONObject();
                Calendar c = Calendar.getInstance();
                String fileName = "" + c.get(Calendar.YEAR)
                        + (c.get(Calendar.MONTH) + 1)
                        + c.get(Calendar.DAY_OF_MONTH)
                        + c.get(Calendar.HOUR_OF_DAY)
                        + c.get(Calendar.MINUTE)
                        + c.get(Calendar.SECOND)
                        + c.get(Calendar.MILLISECOND);
                String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                re.put("url", "/" + path + fileName + fileSuffix);
                re.put("fullUrl", "https://yangyangdeyi.oss-cn-shenzhen.aliyuncs.com/" + path + fileName + fileSuffix);
                infoMap.put("type", fileSuffix);
                infoMap.put("original", file.getOriginalFilename());
                infoMap.put("size", file.getSize());
                infoMap.put("title", file.getOriginalFilename());
                re.put("infoMap", infoMap);
                AliOSSUploader.upload(file.getInputStream(), path + fileName + fileSuffix, true);
                return re;
        }
        return BaseResponse.SUCCESS();
    }
}
