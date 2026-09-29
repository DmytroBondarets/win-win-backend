package com.winwin.dataapi.controller;

import com.winwin.dataapi.dto.TransformRequest;
import com.winwin.dataapi.dto.TransformResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransformController {

    @PostMapping("/api/transform")
    public TransformResponse transform(@Valid @RequestBody TransformRequest request) {
        String transformed = new StringBuilder(request.text()).reverse().toString().toUpperCase();
        return new TransformResponse(transformed);
    }
}
