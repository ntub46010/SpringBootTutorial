package com.vincent.demo.controller.advice;

import com.vincent.demo.controller.ProductController;
import com.vincent.demo.controller.UserController;
import com.vincent.demo.exception.OperateAbsentItemsException;
import com.vincent.demo.model.BusinessExceptionType;
import com.vincent.demo.model.ExceptionResponse;
import com.vincent.demo.util.CommonUtil;
import com.vincent.demo.util.ToSearchTextEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {ProductController.class, UserController.class})
public class GeneralAdvice {
    private final CustomDateEditor customDateEditor = new CustomDateEditor(CommonUtil.sdf, true);
    private final ToSearchTextEditor toSearchTextEditor = new ToSearchTextEditor();

    @ExceptionHandler(OperateAbsentItemsException.class)
    public ResponseEntity<ExceptionResponse> handleOperateAbsentItem(OperateAbsentItemsException e) {
        Map<String, Object> info = Map.of("itemIds", e.getItemIds());
        var res = new ExceptionResponse();
        res.setType(BusinessExceptionType.OPERATE_ABSENT_ITEM);
        res.setInfo(info);

        return ResponseEntity.unprocessableEntity().body(res);
    }

    @InitBinder({"createdFrom", "createdTo"})
    public void bindDate(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, customDateEditor);
    }

    @InitBinder({"name", "email"})
    public void bindSearchText(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, toSearchTextEditor);
    }
}
