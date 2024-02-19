package com.zhao.micro.utils;

import okhttp3.*;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static OkHttpClient httpClient;
    private static OkHttpClient httpsClient;
    private static OkHttpClient client;
    private static int READ_TIMEOUT = 120;
    private static int CONNECT_TIMEOUT = 180;
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    /**
     * 上传文件
     *
     * @param url      接口地址
     * @param filePath 本地文件地址
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
    public static String upload(String url, String filePath, String fileName) throws Exception {
        return upload(url, new File(filePath), fileName);
    }

    /**
     * 上传文件
     *
     * @param url      接口地址
     * @param file     文件
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
    public static String upload(String url, File file, String fileName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    /**
     * GET请求
     *
     * @param url      地址
     * @param callback 回调，如果是同步的请求就传null
     * @return
     */
    public static String get(String url, Callback callback) {
        String result = null;

        if (httpClient == null)
            initHttpClient();

        client = httpClient;
        if (url.startsWith("https:")) {
            if (httpsClient == null)
                initHttpsClient();
            client = httpsClient;
        }

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        if (callback != null) { // 异步请求
            call.enqueue(callback);
        } else { // 同步请求
            try {
                Response resp = call.execute();
                if (resp != null)
                    result = resp.body().string();
            } catch (IOException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    /**
     * 同步GET请求
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * POST请求
     *
     * @param url         地址
     * @param requestBody POST参数
     * @param callback    回调，如果是同步的请求就传null
     */
    public static String post(String url, String requestBody, Callback callback) {
        String result = null;

        if (httpClient == null)
            initHttpClient();

        client = httpClient;
        if (url.startsWith("https:")) {
            if (httpsClient == null)
                initHttpsClient();
            client = httpsClient;
        }

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        if (callback != null) {
            client.newCall(request).enqueue(callback);
        } else {
            try {
                Response resp = client.newCall(request).execute();
                if (resp != null)
                    result = resp.body().string();
            } catch (IOException e) {
                logger.error(e);
            }
        }

        return result;
    }

    /**
     * 同步POST
     */
    public static String post(String url, String requestBody) {
        return post(url, requestBody, null);
    }

    private synchronized static void initHttpsClient() {
        if (httpsClient == null) {
            try {
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();//设置读取超时时间;//设置连接超时时间;
                builder.sslSocketFactory(sslSocketFactory);

                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                httpsClient = builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized static void initHttpClient() {
        if (httpClient == null)
            httpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .build();
    }

    /**
     * @param url    请求的地址
     * @param params 封装的参数
     * @return
     */
    public static String postByOrigin(String url, Map<String, String> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        System.out.println("send_url:" + url);
        System.out.println("send_data:" + sb.toString());
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            //一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    public static String getByOrigin(String url) throws Exception {
        URL urlobj = new URL(url);
        URLConnection conn = urlobj.openConnection();
        InputStream in = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        in.close();
        return sb.toString();
    }

}
