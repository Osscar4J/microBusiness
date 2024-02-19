package com.zhao.micro.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SysConstants {
    public static final String USER_IN_SESSION = "user_session";
    public static final int DELETED = -1;
    public static final String SESSION_OPENID = "openid";

    public static final String REDIS_PAY_APPID = "redis-pay-appid";
    public static final String REDIS_PAY_APPSECRET = "redis-pay-appsecret";

    public static String GH_TOKEN = null;
    public static String GH_APPID = null;
    public static String GH_APP_SECRET = null;

    @Value("${gh-token}")
    public void setTOKEN(String token) {
        SysConstants.GH_TOKEN = token;
    }

    @Value("${gh-appid}")
    public void setAPPID(String appid) {
        SysConstants.GH_APPID = appid;
    }

    @Value("${gh-appsecret}")
    public void setAPP_SECRET(String app_secret) {
        SysConstants.GH_APP_SECRET = app_secret;
    }

    /**
     * 产品状态
     */
    public class ProductStatus {
        /**
         * 1：上架
         */
        public final static int SELLING = 1;
        /**
         * 0：下架
         */
        public final static int DOWN = 0;
    }

    /**
     * 支付方式
     */
    public class PayStyle {
        /**
         * 1、微信支付
         */
        public final static int WX_PAY = 1;
        /**
         * 2、支付宝支付
         */
        public final static int ALI_PAY = 2;
    }

    /**
     * 微信支付交易类型
     */
    public class WXPayTradeType {
        /**
         * Native支付，生成支付二维码
         */
        public final static String NATIVE = "NATIVE";
        /**
         * JSAPI支付（或小程序支付）
         */
        public final static String JSAPI = "jsapi";
        /**
         * app支付
         */
        public final static String APP = "APP";
        /**
         * H5支付
         */
        public final static String H5 = "MWEB";
        /**
         * 付款码支付，付款码支付有单独的支付接口，所以接口不需要上传，该字段在对账单中会出现
         */
        public final static String MICROPAY = "MICROPAY";
    }

    public class ShareCacheModelType {
        /**
         * 微信活码
         */
        public static final int USER_ALIVE_QRCODE = 1;
        /**
         * 微信二维码地址
         */
        public static final int USER_WX_QRCODE = 2;
        /**
         * 产品推广
         */
        public static final int PRODUCT_POPULARIZE = 3;
        /**
         * 产品详情
         */
        public static final int PRODUCT_DETAIL = 4;
    }

    public class RedisTime {
        /**
         * 用户活码缓存时长, 7天
         */
        public static final int USER_ALIVE_CODE = 604800;
        /**
         * 用户微信名片链接, 2小时
         */
        public static final int USER_ALIVE_CODE_URL = 7200;
        /**
         * 产品推广码, 7天
         */
        public static final int PRODUCT_POPULARIZE = 604800;
        /**
         * 产品详情链接
         */
        public static final int PRODUCT_DETAIL = 7200;
    }

    /**
     * cookie名称
     */
    public class Cookies {
        /**
         * 用户账号
         */
        public static final String ACCOUNT = "account";
        /**
         * 用户密码
         */
        public static final String PASSWORD = "password";
    }

    public class Role {
        /**
         * 1：系统管理员
         */
        public static final long ADMIN = 1;
        /**
         * 2：代理商
         */
        public static final long PROXY = 2;
        /**
         * 3：微商
         */
        public static final long MICRO_BUSINESS = 3;
        /**
         * 4、客户
         */
        public static final long CUSTOMER = 4;
    }

    /**
     * 用户账号状态
     */
    public class UserStatus {
        /**
         * 2：被冻结
         */
        public static final int FREEZON = 2;
        /**
         * 3：登录异常
         */
        public static final int LOGIN_UNNORMAL = 3;
    }

    /**
     * 域名状态
     */
    public class DomainStatus {
        /**
         * 1：正常
         */
        public static final int NORMAL = 1;
        /**
         * 2：暂停使用
         */
        public static final int PAUSED = 2;
    }

    /**
     * 订单状态
     */
    public class OrderStatus {
        /**
         * 0: 未支付
         */
        public final static int NOT_PAY = 0;
        /**
         * 1: 已支付
         */
        public final static int PAYED = 1;
        /**
         * 2: 正在退款
         */
        public final static int REFUNDING = 2;
        /**
         * 3: 已退款
         */
        public final static int REFUNDED = 3;
        /**
         * 4: 订单已取消
         */
        public final static int CANCELED = 4;
        /**
         * 5: 支付失败
         */
        public final static int PAY_FAIL = 5;
        /**
         * 6：已过期
         */
        public final static int OUTOFDATE = 6;
    }

    /**
     * 域名类型
     */
    public class DomainType {
        /**
         * 1：产品域名
         */
        public final static int PRODUCT = 1;
        /**
         * 2：用户活码域名
         */
        public final static int USER_QRCODE = 2;
    }
}
