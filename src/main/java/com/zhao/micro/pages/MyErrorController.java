package com.zhao.micro.pages;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        //获取statusCode:401,404,500
        model.addAttribute("code", request.getAttribute("javax.servlet.error.status_code"));
        return "errPage";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
