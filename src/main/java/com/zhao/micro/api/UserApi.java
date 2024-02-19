package com.zhao.micro.api;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.annotation.CommonPath;
import com.zhao.micro.annotation.CurrentUser;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.User;
import com.zhao.micro.entity.UserQrcode;
import com.zhao.micro.entity.WxQrCode;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.model.UserModel;
import com.zhao.micro.reqvo.UserReqVO;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.UserQrcodeService;
import com.zhao.micro.service.UserService;
import com.zhao.micro.service.WxQrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理
 */
@LoginRequired
@RestController
@RequestMapping("/api/user")
public class UserApi {

    @Autowired
    private UserService userService;
    @Autowired
    private UserQrcodeService userQrcodeService;
    @Autowired
    private WxQrCodeService wxQrCodeService;

    @GetMapping
    public BaseResponse page(UserReqVO reqVO, @CurrentUser User user) {
        if (SysConstants.Role.ADMIN != user.getRoleId()) {
            reqVO.setRecommendId(user.getId());
        } else if (reqVO.getRecommendId() == null) {
            reqVO.setRoleId(SysConstants.Role.PROXY);
        }
        if (!StringUtils.isEmpty(reqVO.getNickname())) {
            reqVO.setNickname("%" + reqVO.getNickname() + "%");
        }
        return BaseResponse.SUCCESS(userService.getPage(reqVO));
    }

    /**
     * 修改密码
     *
     * @param userModel
     * @param user
     * @return
     */
    @PostMapping("/resetPwd")
    public BaseResponse resetPwd(@RequestBody UserModel userModel, @CurrentUser User user) {
        userModel.setUserId(user.getId());
        return BaseResponse.SUCCESS(userService.resetPwd(userModel));
    }

    /**
     * 更新用户状态
     *
     * @param user
     * @return
     */
    @PutMapping("/status")
    public BaseResponse updateStatus(@RequestBody User user) {
        if (user.getId() == null || user.getStatus() == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", user.getId());
        wrapper.set("status", user.getStatus());
        return BaseResponse.SUCCESS(userService.update(wrapper));
    }

    @PostMapping
    public BaseResponse save(@RequestBody User user, @CurrentUser User currUser) {
        if (currUser.getRoleId() != SysConstants.Role.ADMIN) {
            if (user.getRecommendId() == null || !currUser.getId().toString().equals(user.getRecommendId().toString()))
                throw new BusinessException(ResponseStatus.NO_PERMISSION);
        }
        // 不能修改角色
        if (user.getId() != null)
            user.setRoleId(null);
        return BaseResponse.SUCCESS(userService.saveOrUpdate(user));
    }

    /**
     * 当前用户修改个人资料
     *
     * @param entity
     * @param user
     * @return
     */
    @PutMapping("/profile")
    public BaseResponse updateProfile(HttpServletRequest request, @RequestBody User entity, @CurrentUser User user) {
        entity.setId(user.getId());
        entity.setAccount(null);
        entity.setPasswd(null);
        entity.setRoleId(null);
        if (userService.saveOrUpdate(entity)) {
            userService.updateUserSession(request, user.getAccount());
            return BaseResponse.SUCCESS();
        }
        return BaseResponse.ERROR("error");
    }

    @HasRole({SysConstants.Role.ADMIN})
    @DeleteMapping("/{id}")
    public BaseResponse remove(@PathVariable Long id) {
        return BaseResponse.SUCCESS(userService.removeById(id));
    }

    /**
     * 员工业绩
     *
     * @param id   代理id，管理员可以查任意代理的，代理只能查自己的
     * @param user
     * @return
     */
    @GetMapping("/performance")
    public BaseResponse getPerformance(Long id, @CurrentUser User user) {
        if (user.getRoleId() != SysConstants.Role.ADMIN) {
            id = user.getId();
        }
        return BaseResponse.SUCCESS(userService.getPerformanceByProxy(id));
    }

    /**
     * 修改用户的活码
     *
     * @param entity
     * @param user
     * @return
     */
    @PostMapping("/qrcode")
    public BaseResponse addOrUpdateQrcode(@RequestBody UserQrcode entity, @CurrentUser User user) {
        entity.setUserId(user.getId());
        return BaseResponse.SUCCESS(userQrcodeService.saveOrUpdate(entity));
    }

    /**
     * 查询当前用户的微信二维码名片列表
     *
     * @param user
     * @return
     */
    @GetMapping("/qrcode")
    public BaseResponse<List<UserQrcode>> getQrcode(@CurrentUser User user) {
        return BaseResponse.SUCCESS(userQrcodeService.getByUserId(user.getId()));
    }

    /**
     * 查询用户的微信二维码列表
     *
     * @param id 活码id
     * @return
     */
    @GetMapping("/qrcode/{id}")
    public BaseResponse<List<WxQrCode>> getWxQrcode(@PathVariable long id) {
        return BaseResponse.SUCCESS(wxQrCodeService.getByQrcodeId(id));
    }

    /**
     * 生成当前用户的微信活码
     *
     * @param id 活码id
     * @return
     */
    @GetMapping("/generateQrcode")
    public BaseResponse<String> generateAliveQrcode(Long id, @CurrentUser User user) {
        return BaseResponse.SUCCESS(userService.generateAliveQrcode(id, user.getId()));
    }

    /**
     * 通过用户id创建该用户的活码链接
     *
     * @param id 用户id
     * @return
     */
    @CommonPath
    @GetMapping("/createQrcode")
    public BaseResponse<String> generateAliveQrcodeByUserId(Long id) {
        if (id == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        User user = userService.getById(id);
        if (user == null)
            throw new BusinessException(ResponseStatus.USER_NOT_FOUND);
        List<UserQrcode> qrcodes = userQrcodeService.getByUserId(id);
        if (qrcodes.isEmpty())
            throw new BusinessException(ResponseStatus.OBJECT_NOT_FOUND);
        return generateAliveQrcode(qrcodes.get(0).getId(), user);
    }

    /**
     * 查询代理的销售排行
     *
     * @param start 开始时间，格式：yyyy-MM-dd hh:mm:ss
     * @param end   结束时间，格式：同上
     * @param st    分页，开始条数
     * @param et    分页，要查询的数量
     * @return
     */
    @HasRole(SysConstants.Role.ADMIN)
    @GetMapping("/orderStatistics")
    public BaseResponse getOrderStatistics(Integer start, Integer end, String st, String et) {
        if (start == null)
            start = 0;
        if (end == null)
            end = 10;
        return BaseResponse.SUCCESS(userService.getOrderStatistics(start, end, st, et));
    }
}
