package com.smartretail.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleSheetsAppendRequest {
    private String sheetName;
    private List<Object> values;
}

