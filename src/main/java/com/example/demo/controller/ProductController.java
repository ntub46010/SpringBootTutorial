package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.parameter.SortDirection;
import com.example.demo.model.ValidationFailInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Product Controller", description = "產品增刪改查相關的 API")
public class ProductController {
    private static final Map<String, Product> productMap = new HashMap<>();

    static {
        var p1 = Product.of("101", "Coke", 30);
        var p2 = Product.of("102", "Hamburger", 60);
        var p3 = Product.of("103", "Sandwich", 50);
        Stream.of(p1, p2, p3).forEach(p -> productMap.put(p.getId(), p));
    }

    @Operation(summary = "取得一個產品", description = "根據 id 取得指定產品資料。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功回傳產品資料"),
            @ApiResponse(responseCode = "404", description = "找不到該產品", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "產品 id", example = "101", required = true)
            @PathVariable("id") String id
    ) {
        var product =  productMap.get(id);
        return product == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(product);
    }

    @Operation(summary = "取得多個產品", description = "根據給予的參數（如排序）取得多筆產品資料。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功回傳產品資料")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @Parameter(description = "排序欄位", example = "price")
            @RequestParam(name = "sortField", required = false) String sortField,

            @Parameter(description = "排序方向")
            @RequestParam(name = "sortDirection", defaultValue = "ASC") SortDirection sortDirection
    ) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if ("name".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(p -> p.getName().toLowerCase());
        } else if ("price".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Product::getPrice);
        }

        if (sortDirection == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        var products = productMap.values().stream()
                .sorted(comparator)
                .toList();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "建立產品", description = "建立一筆產品資料。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功建立產品資料"),
            @ApiResponse(responseCode = "400", description = "產品資料不合理",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ValidationFailInfo.class)))),
            @ApiResponse(responseCode = "422", description = "已有相同 id 的產品資料", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody Product product) {
        if (!StringUtils.hasText(product.getId())) {
            return ResponseEntity.badRequest().build();
        }

        var isIdExisting = productMap.containsKey(product.getId());
        if (isIdExisting) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productMap.put(product.getId(), product);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "更新產品", description = "更新指定 id 的產品資料。Request body 中的 id 會被忽略。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功更新產品資料"),
            @ApiResponse(responseCode = "400", description = "產品資料不合理",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ValidationFailInfo.class)))),
            @ApiResponse(responseCode = "404", description = "找不到該產品", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @Parameter(description = "產品 id", example = "101", required = true)
            @PathVariable("id") String id,

            @Valid @RequestBody Product product
    ) {
        var isIdExisting = productMap.containsKey(id);
        if (!isIdExisting) {
            return ResponseEntity.notFound().build();
        }

        product.setId(id);
        productMap.put(product.getId(), product);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "刪除產品", description = "刪除指定 id 的產品資料。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功刪除產品資料", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "產品 id", example = "101", required = true)
            @PathVariable("id") String id
    ) {
        productMap.remove(id);
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
