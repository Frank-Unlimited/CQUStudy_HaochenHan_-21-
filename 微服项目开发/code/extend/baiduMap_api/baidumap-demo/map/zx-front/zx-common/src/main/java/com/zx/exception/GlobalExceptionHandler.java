package com.zx.exception;

import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * 全局异常处理器
 * @author wgl
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public Result businessException(CustomException e)
    {
        if (e.getCode()!=null)
        {
            return Result.build(null, e.getCode(), e.getMessage());
        }
        return Result.build(null, 500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public Result handleException(RuntimeException e)
    {
        log.error(e.getMessage(), e);
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Result validatedBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.build(null, ResultCodeEnum.VALIDATECODE_ERROR);
    }

}
