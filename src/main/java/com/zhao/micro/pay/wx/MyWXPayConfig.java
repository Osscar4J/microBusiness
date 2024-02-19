package com.zhao.micro.pay.wx;

import com.zhao.micro.constants.SysConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class MyWXPayConfig extends WXPayConfig {

    private byte[] certData;
    //    public static final String V3_SECRET = "";
    public static String API_SECRET = null;
    public static String MCH_ID = null;

    public static final String SignType_MD5 = "MD5";
    public static final String SignType_HMACSHA256 = "HMACSHA256";

    public MyWXPayConfig() throws Exception {
//        InputStream certStream = MyWXPayConfig.class.getClassLoader().getResourceAsStream("wxpaycert/apiclient_cert.p12");
        InputStream certStream = new FileInputStream(new File("wxpaycert/apiclient_cert.p12"));
        this.certData = new byte[certStream.available()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Value("${pay-secret}")
    public void setAPI_SECRET(String api_secret) {
        MyWXPayConfig.API_SECRET = api_secret;
    }

    @Value("${pay-mchid}")
    public void setMCH_ID(String mch_id) {
        MyWXPayConfig.MCH_ID = mch_id;
    }

    @Override
    String getAppID() {
        return SysConstants.GH_APPID;
    }

    @Override
    String getMchID() {
        return MCH_ID;
    }

    @Override
    String getKey() {
        return API_SECRET;
    }

    @Override
    InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }
}
