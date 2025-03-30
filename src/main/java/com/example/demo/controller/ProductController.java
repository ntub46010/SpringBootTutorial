package com.example.demo.controller;

import com.example.demo.model.BatchProduct;
import com.example.demo.param.BaseParameter;
import com.example.demo.model.Product;
import com.example.demo.validation.ValidationFailInfo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody Product request) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getProducts(@Valid @ModelAttribute BaseParameter param) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> createProducts(@Valid @RequestBody BatchProduct request) {
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ValidationFailInfo>> handleValidationFail(BindException ex) {
        var infoList = new ArrayList<ValidationFailInfo>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            var info = new ValidationFailInfo();
            info.setField(error.getField());
            info.setValue(error.getRejectedValue());
            info.setMessage(error.getDefaultMessage());

            infoList.add(info);
        });

        return ResponseEntity.badRequest().body(infoList);
    }
}
