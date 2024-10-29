package com.example.order.api;

import com.example.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class ProductProcessingFailureController {

    private final OrderService orderService;

    @GetMapping("/disability-situation/case1")
    public ResponseEntity<String> case1() {
        return orderService.case1();
    }

    @GetMapping("/disability-situation/case2")
    public ResponseEntity<String> case2() {
        return orderService.case2();
    }

    @GetMapping("/disability-situation/case3")
    public ResponseEntity<String> case3() {
        return orderService.case3();
    }
}
