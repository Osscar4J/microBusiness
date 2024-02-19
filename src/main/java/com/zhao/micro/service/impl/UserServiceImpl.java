package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Address;
import com.zhao.micro.entity.Area;
import com.zhao.micro.entity.Domain;
import com.zhao.micro.entity.User;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.UserMapper;
import com.zhao.micro.model.ShareCacheModel;
import com.zhao.micro.model.UserModel;
import com.zhao.micro.reqvo.UserReqVO;
import com.zhao.micro.service.*;
import com.zhao.micro.utils.DateStyle;
import com.zhao.micro.utils.DateUtils;
import com.zhao.micro.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends MyBaseService<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private DomainService domainService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AreaService areaService;
    private char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    @Override
    public User login(UserReqVO reqVO) {
        if (StringUtils.isEmpty(reqVO.getPasswd()) || StringUtils.isEmpty(reqVO.getAccount()))
            throw new BusinessException(ResponseStatus.USER_ACCOUNT_OR_PWD_WRONG);
        String password = MD5Utils.Bit32(reqVO.getPasswd());
        User user = userMapper.selectByUsernamePasswd(reqVO.getAccount(), password);
        if (user == null)
            throw new BusinessException(ResponseStatus.USER_ACCOUNT_OR_PWD_WRONG);
        if (user.getStatus() == SysConstants.UserStatus.FREEZON)
            throw new BusinessException(ResponseStatus.USER_FREEZED);
        return user;
    }

    @Override
    public boolean register(User user) {
        if (StringUtils.isEmpty(user.getEmail()))
            throw new BusinessException(ResponseStatus.USER_EMAIL_CANT_EMPTY);
        if (StringUtils.isEmpty(user.getPasswd()))
            throw new BusinessException(ResponseStatus.USER_PASSWD_NOT_EMPTY);
        // 默认角色为微商
        if (user.getRoleId() == null || user.getRoleId() == SysConstants.Role.ADMIN)
            user.setRoleId(SysConstants.Role.MICRO_BUSINESS);
        String account = user.getAccount();
        if (account == null)
            account = user.getEmail();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("account", account);
        wrapper.ne("status", SysConstants.DELETED);
        // 生成昵称
        if (user.getEmail() != null) {
            if (user.getRoleId() == SysConstants.Role.MICRO_BUSINESS) {
                user.setNickname("C" + user.getEmail().substring(0, user.getEmail().indexOf("@")));
            } else if (user.getRoleId() == SysConstants.Role.PROXY) {
                user.setNickname("P" + user.getEmail().substring(0, user.getEmail().indexOf("@")));
            }
        }
        if (user.getRecommendId() == null)
            user.setRecommendId(-1L);
        synchronized (this) {
            if (this.getOne(wrapper) != null)
                throw new BusinessException(ResponseStatus.USER_ACCOUNT_HAS_ECIST);
            user.setPasswd(MD5Utils.Bit32(user.getPasswd()));
            user.setAccount(account);
            if (this.save(user) && user.getRecommendId().longValue() != -1L) {
                userMapper.addOneProxyCount(user.getRecommendId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(User entity) {
        if (entity.getId() == null)
            return this.register(entity);
        return this.updateById(entity);
    }

    @Override
    public boolean updateById(User entity) {
        if (!StringUtils.isEmpty(entity.getPasswd())) {
            entity.setPasswd(MD5Utils.Bit32(entity.getPasswd()));
        }
        return super.updateById(entity);
    }

    @Override
    public boolean resetPwd(UserModel userModel) {
        if (userModel.getNewPwd() == null || userModel.getOldPwd() == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        User user = this.getById(userModel.getUserId());
        if (!user.getPasswd().equalsIgnoreCase(MD5Utils.Bit32(userModel.getOldPwd())))
            throw new BusinessException(ResponseStatus.USER_PASSWORD_INCORRECT);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", user.getId());
        wrapper.set("passwd", MD5Utils.Bit32(userModel.getNewPwd()));
        return this.update(wrapper);
    }

    @Override
    public List<User> getPerformanceByProxy(Long proxyId) {
        Calendar calendar = Calendar.getInstance();
        String today = DateUtils.DateToString(calendar.getTime(), DateStyle.YYYY_MM_DD);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        String yestoday = DateUtils.DateToString(calendar.getTime(), DateStyle.YYYY_MM_DD);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        // 获取本月的开始日期
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return userMapper.selectPerformance(
                proxyId,
                today,
                yestoday,
                DateUtils.getBeginDayOfLastWeek(),
                DateUtils.getEndDayOfLastWeek(),
                DateUtils.getBeginDayOfMonth(),
                new Date());
    }

    @Override
    public void updateUserSession(HttpServletRequest request, String account) {
        User user = userMapper.selectByUsernamePasswd(account, null);
        if (user != null)
            request.getSession().setAttribute(SysConstants.USER_IN_SESSION, user);
    }

    @Override
    public String generateAliveQrcode(Long qrcodeId, Long userId) {
        return generateShareQrcode(SysConstants.ShareCacheModelType.USER_ALIVE_QRCODE, qrcodeId, userId);
    }

    @Override
    public String generateShareQrcode(int type, Long value, Long userId) {
        if (value == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        StringBuilder re = new StringBuilder();
        // 随机域名
        int dType = -1;
        switch (type) {
            case SysConstants.ShareCacheModelType.USER_ALIVE_QRCODE: // 微信活码地址, 7天有效
            case SysConstants.ShareCacheModelType.USER_WX_QRCODE: // 微信活码跳转链接, 2小时有效
                dType = SysConstants.DomainType.USER_QRCODE;
                break;
            case SysConstants.ShareCacheModelType.PRODUCT_POPULARIZE: // 产品推广码, 7天有效
            case SysConstants.ShareCacheModelType.PRODUCT_DETAIL: // 产品详情链接, 2小时有效
                dType = SysConstants.DomainType.PRODUCT;
                break;
        }
        Domain domain = domainService.getRandom(dType);
        if (domain != null) {
            re.append("http://");
            re.append(domain.getName());
        }
        re.append("/");
        re.append(chars[(int) (Math.floor(Math.random() * chars.length))]);
        re.append(chars[(int) (Math.floor(Math.random() * chars.length))]);
        re.append("/");
        re.append(generateNewRandomCacheKey(type, value, userId));
        return re.toString();
    }

    @Override
    public String generateNewRandomCacheKey(int type, Long value, Long userId) {
        if (value == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        StringBuilder sb;
        ShareCacheModel cacheModel;
        synchronized (this){
            do {
                sb = new StringBuilder();
                int max = 3 + (int) Math.floor(Math.random() * 8);
                for (int i = 0; i < max; i++) {
                    sb.append(chars[(int) (Math.floor(Math.random() * chars.length))]);
                }
                cacheModel = (ShareCacheModel) cacheService.get(sb.toString());
            } while (cacheModel != null);
            cacheModel = new ShareCacheModel();
            cacheModel.setValue(value);
            cacheModel.setType(type);
            int howlong = 60;
            switch (type) {
                case SysConstants.ShareCacheModelType.USER_ALIVE_QRCODE: // 微信活码地址, 7天有效
                    howlong = SysConstants.RedisTime.USER_ALIVE_CODE;
                    break;
                case SysConstants.ShareCacheModelType.USER_WX_QRCODE: // 微信活码跳转链接, 2小时有效
                    howlong = SysConstants.RedisTime.USER_ALIVE_CODE_URL;
                    break;
                case SysConstants.ShareCacheModelType.PRODUCT_POPULARIZE: // 产品推广码, 7天有效
                    howlong = SysConstants.RedisTime.PRODUCT_POPULARIZE;
                    break;
                case SysConstants.ShareCacheModelType.PRODUCT_DETAIL: // 产品详情链接, 2小时有效
                    howlong = SysConstants.RedisTime.PRODUCT_DETAIL;
                    break;
            }
            cacheModel.setUserId(userId);
            cacheService.put(sb.toString(), cacheModel, howlong);
        }
        return sb.toString();
    }

    @Override
    public String generateProdPopularizeQrcode(Long productId, Long userId) {
        return generateShareQrcode(SysConstants.ShareCacheModelType.PRODUCT_POPULARIZE, productId, userId);
    }

    @Override
    public User getCustomerByPhone(String phone, String name, Long areaId, String addr) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone", phone);
        wrapper.ne("status", SysConstants.DELETED);
        synchronized (this) {
            User user = userMapper.selectByPhoneWithAddr(phone);
            if (user == null) {
                user = new User();
                user.setNickname(name);
                user.setPhone(phone);
                user.setRoleId(SysConstants.Role.CUSTOMER);
                this.save(user);
            } else if (!StringUtils.isEmpty(name) && !name.equalsIgnoreCase(user.getNickname())) {
                user.setNickname(name);
                this.updateById(user);
            }
            wrapper = new QueryWrapper();
            wrapper.eq("user_id", user.getId());
            Address address = addressService.getOne(wrapper, false);
            if (address == null) {
                address = new Address();
                address.setUserId(user.getId());
            }
            address.setAreaId(areaId);
            address.setReceiver(name);

            String[] areas = new String[3];
            Area area = areaService.getById(areaId);
            if (area != null) {
                areas[2] = area.getName();
                area = areaService.getById(area.getParentId());
                if (area != null) {
                    areas[1] = area.getName();
                    area = areaService.getById(area.getParentId());
                    if (area != null) {
                        areas[0] = area.getName();
                    }
                }
            }
            address.setAddr(addr);
            addr = "";
            for (String adr : areas) {
                if (adr != null)
                    addr += adr;
            }
            address.setAddr(addr + address.getAddr());
            addressService.saveOrUpdate(address);
            user.setAddress(address);

            return user;
        }
    }

    @Override
    public List<User> getOrderStatistics(int start, int end, String st, String et) {
        return userMapper.selectOrderStatistics(start, end, st, et);
    }

    @Override
    public boolean removeById(Serializable id) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", id);
        wrapper.set("status", SysConstants.DELETED);
        return this.update(wrapper);
    }
}
