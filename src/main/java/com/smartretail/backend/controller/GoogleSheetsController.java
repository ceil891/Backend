package com.smartretail.backend.controller;

import com.smartretail.backend.dto.GoogleSheetsAppendRequest;
import com.smartretail.backend.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sheets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GoogleSheetsController {

    private final GoogleSheetsService googleSheetsService;

    @PostMapping("/append")
    public ResponseEntity<Map<String, Object>> append(@RequestBody GoogleSheetsAppendRequest req) {
        googleSheetsService.appendRow(req.getSheetName(), req.getValues());
        return ResponseEntity.ok(Map.of("success", true));
    }
}

