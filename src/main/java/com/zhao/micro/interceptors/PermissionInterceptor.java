package com.zhao.micro.interceptors;

import com.zhao.micro.annotation.AnyRole;
import com.zhao.micro.annotation.CommonPath;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (HandlerMethod.class == handler.getClass()) {
            HandlerMethod method = (HandlerMethod) handler;

            if (method.hasMethodAnnotation(CommonPath.class))
                return true;

            //类注解权限验证
            if (!classPermissionHandler(method.getBeanType(), request, response))
                return false;

            // 方法注解权限验证
            if (!methodPermissionHandler(method, request, response))
                return false;
        }
        return true;
    }

    /**
     * 验证 类级别 的权限
     *
     * @param c
     * @param request
     * @param response
     * @return
     */
    private boolean classPermissionHandler(Class<?> c, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (c.getAnnotation(LoginRequired.class) != null
                || c.getAnnotation(HasRole.class) != null
                || c.getAnnotation(AnyRole.class) != null) {
            if (request.getSession().getAttribute(SysConstants.USER_IN_SESSION) == null) {
                response.sendRedirect("/manager/login");
                return false;
            }
            User user = (User) request.getSession().getAttribute(SysConstants.USER_IN_SESSION);
            if (c.getAnnotation(HasRole.class) != null) {
                HasRole annotation = c.getAnnotation(HasRole.class);
                long[] roleIds = annotation.value();
                for (long roleId : roleIds) {
                    if (roleId != user.getRoleId())
                        return false;
                }
            }
            if (c.getAnnotation(AnyRole.class) != null) {
                AnyRole annotation = c.getAnnotation(AnyRole.class);
                long[] roleIds = annotation.value();
                for (long roleId : roleIds) {
                    if (roleId == user.getRoleId())
                        return true;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 验证 方法级别 的权限
     *
     * @param method
     * @param request
     * @param response
     * @return
     */
    private boolean methodPermissionHandler(HandlerMethod method, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (method.hasMethodAnnotation(LoginRequired.class)
                || method.hasMethodAnnotation(HasRole.class)
                || method.hasMethodAnnotation(AnyRole.class)) {
            if (request.getSession().getAttribute(SysConstants.USER_IN_SESSION) == null) {
                response.sendRedirect("/manager/login");
                return false;
            }
            User user = (User) request.getSession().getAttribute(SysConstants.USER_IN_SESSION);
            if (method.hasMethodAnnotation(HasRole.class)) {
                HasRole annotation = method.getMethodAnnotation(HasRole.class);
                long[] roleIds = annotation.value();
                for (long roleId : roleIds) {
                    if (roleId != user.getRoleId())
                        return false;
                }
            }
            if (method.hasMethodAnnotation(AnyRole.class)) {
                AnyRole annotation = method.getMethodAnnotation(AnyRole.class);
                long[] roleIds = annotation.value();
                for (long roleId : roleIds) {
                    if (roleId == user.getRoleId())
                        return true;
                }
                return false;
            }
        }
        return true;
    }
}
