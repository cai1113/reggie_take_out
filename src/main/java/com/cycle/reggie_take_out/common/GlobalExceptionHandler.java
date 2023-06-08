package com.cycle.reggie_take_out.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        if (e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            String msg = split[9] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException e) {
        log.error("自定义异常信息 ex={}", e.getMessage(), e);
        return R.error(e.getMessage());
    }
}
