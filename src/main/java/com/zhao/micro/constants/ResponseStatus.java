package com.zhao.micro.constants;

public enum ResponseStatus {
    /**
     * 操作成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 400：操作失败
     */
    FAILURE(400, "操作失败"),
    /**
     * 401：登陆已失效
     */
    UNAUTHORIZED(401, "登陆已失效"),
    /**
     * 402:非法参数
     */
    INVALIDE_PARAMS(402, "非法参数"),
    /**
     * 403:无权限操作
     */
    NO_PERMISSION(403, "无权限操作"),
    /**
     * 404:对象不存在
     */
    OBJECT_NOT_FOUND(404, "对象不存在"),
    /**
     * 500：服务异常
     */
    SERVER_ERROR(500, "服务异常"),

    /**
     * 1001: 账号或密码错误
     */
    USER_ACCOUNT_OR_PWD_WRONG(1001, "账号或密码错误")
    /**1002：账号不存在*/
    , USER_ACCOUNT_NOT_EXIST(1002, "账号不存在")
    /**1003：手机号不能为空*/
    , USER_PHONE_NOT_EMPTY(1003, "手机号不能为空")
    /**1004：密码不能为空*/
    , USER_PASSWD_NOT_EMPTY(1004, "密码不能为空")
    /**1005：该手机号已被注册*/
    , USER_PHONE_REGISTERED(1005, "该手机号已被注册")
    /**1006：该账号已被冻结请联系管理员*/
    , USER_FREEZED(1006, "该账号已被冻结请联系管理员")
    /**1007：账号异常请联系管理员*/
    , USER_UNORMAL(1007, "账号异常请联系管理员")
    /**1008：密码错误*/
    , USER_PASSWORD_INCORRECT(1008, "密码错误")
    /**1009：请输入正确的手机号*/
    , USER_PHONE_ERROR(1009, "请输入正确的手机号")
    /**1010：验证码已过期*/
    , USER_SMSCODE_EXPIRED(1010, "验证码已过期")
    /**1011：验证码错误*/
    , USER_SMSCODE_INCORRECT(1011, "验证码错误")
    /**1012：尚未完成实名认证*/
    , USER_NOT_CERTIFICATED(1012, "尚未完成实名认证")
    /**1013：用户不存在*/
    , USER_NOT_FOUND(1013, "用户不存在")
    /**1014：未选择照片*/
    , USER_HASNO_IMAGE(1014, "未选择照片")
    /**1015：该账号已注册*/
    , USER_ACCOUNT_HAS_ECIST(1015, "该账号已注册")
    /**1016：用户不在线*/
    , USER_OFFLINE(1016, "用户不在线")
    /**1017: 密码长度最多6位*/
    , USER_INVALID_PASSWORD_LENGTH(1017, "密码长度最多6位")
    /**1018：无效的openid*/
    , USER_INVALID_OPENID(1018, "无效的openid")
    /**1019: 用户积分不足*/
    , USER_CREDIT_NOT_ENOUGH(1019, "用户积分不足")
    /**1020：邮箱不能为空*/
    , USER_EMAIL_CANT_EMPTY(1020, "邮箱不能为空")

    /**1101：设备不存在*/
    , DEVICE_NOT_FOUND(1101, "设备不存在")
    /**1107：对方正在通话中*/
    , DEVICE_MANAGER_IS_TALKING(1107, "对方正在通话中")
    /**1108：密码已失效*/
    , DEVICE_PASSWORD_TIMEOUT(1108, "密码已失效")
    /**1109：重复的硬件编码*/
    , DEVICE_DUPLICATED_HARDWARECODE(1109, "重复的硬件编码")
    /**1110：设备不在线*/
    , DEVICE_OFFLINE(1110, "设备不在线")
    /**1112：通话已结束*/
    , DEVICE_TALKING_FINISHED(1112, "通话已结束")
    /**1113: 设备名称不能为空*/
    , DEVICE_NAME_EMPTY(1113, "设备名称不能为空")
    /**1114：未知类型的设备*/
    , DEVICE_UNKNOWN_TYPE(1114, "未知类型的设备")

    /**1201: 柜子已锁定，请重新选择*/
    , BOX_LOCKED(1201, "柜子已锁定，请重新选择")
    /**1202: 密码不能重复*/
    , BOX_PASSWORD_DUPLICATED(1202, "密码不能重复")
    /**1203: 已被他人选中*/
    , BOX_SELECTED(1203, "已被他人选中")
    /**1204: 柜子非空闲状态*/
    , BOX_NOT_IDLE(1204, "柜子非空闲状态")

    /**1301：未检测到人脸*/
    , FACE_NOT_DETECTED(1301, "未检测到人脸")
    /**1302：缺少人脸照片*/
    , FACE_IMAGE_NOT_FOUND(1302, "缺少人脸照片")
    /**1303：人脸检测引擎初始化失败*/
    , FACE_ENGINE_INIT_ERROR(1303, "人脸检测引擎初始化失败")
    /**1304：人脸特征值提取失败*/
    , FACE_EXTRACT_ERROR(1304, "人脸特征值提取失败")
    /**1305：图片宽度或高度不支持*/
    , FACE_IMAGE_INVALID_SIZE(1305, "图片宽度或高度不支持")
    /**1306：请拍摄正面照片*/
    , FACE_ANGLE_INCORRECT(1306, "请拍摄正面照片")
    /**1307：照片异常*/
    , FACE_IMAGE_ERROR(1307, "照片异常")

    /**1601：文件不存在*/
    , FILE_NOT_EXSIT(1601, "文件不存在")

    /**1701：已失效的价格*/
    , PRODUCT_PRICE_INVALID(1701, "已失效的价格")
    /**1702: 无效的商品*/
    , PRODUCT_INVALID(1702, "无效的商品")
    /**1703: 购买次数受限*/
    , PRODUCT_BUY_LIMITED(1703, "购买次数受限")
    /**1704：库存不足*/
    , PRODUCT_REMAIN_EMPTY(1704, "库存不足")

    /**1901：未选择所辖小区*/
    , PROPERTYSTAFF_NO_VILLAGE(1901, "未选择所辖小区")

    /**2001：该组已关联小区*/
    , VILLAGE_GROUP_HASVILLAGE(2001, "该组已关联小区")

    /**2101：无效的文件*/
    , FILE_INVALIDATE(2101, "无效的文件")

    /**3001：已存在该邮箱*/
    , MAIL_EXIST(3001, "已存在该邮箱")

    /**4001：该手机号已报过名*/
    , MEETING_USER_EXIST(4001, "该手机号已报过名")

    /**5001：无效的订单*/
    , ORDER_INVALID(5001, "无效的订单")
    /**5002: 查不到订单信息*/
    , ORDER_EMPTY(5002, "查不到订单信息");

    private final int code;
    private final String msg;

    private ResponseStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
