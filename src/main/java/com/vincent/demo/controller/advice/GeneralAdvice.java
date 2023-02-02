package com.vincent.demo.controller.advice;

import com.vincent.demo.controller.ProductController;
import com.vincent.demo.exception.OperateAbsentItemsException;
import com.vincent.demo.model.ExceptionResponse;
import com.vincent.demo.util.DateUtil;
import com.vincent.demo.util.ToSearchTextEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;

//@RestControllerAdvice(assignableTypes = {ProductController.class})
@RestControllerAdvice(basePackages = {"com.vincent.demo.controller"})
//@RestControllerAdvice(basePackageClasses = {ProductController.class})
public class GeneralAdvice {
    private final ToSearchTextEditor toSearchTextEditor = new ToSearchTextEditor();
    private final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    private final CustomDateEditor customDateEditor = new CustomDateEditor(DateUtil.sdf, true);

    @ExceptionHandler(OperateAbsentItemsException.class)
    public ResponseEntity<ExceptionResponse> handleOperateAbsentItem(OperateAbsentItemsException e) {
        Map<String, Object> info = Map.of("itemIds", e.getItemIds());
        var res = new ExceptionResponse();
        res.setInfo(info);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(res);
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
