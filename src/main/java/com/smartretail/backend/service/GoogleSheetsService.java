package com.smartretail.backend.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GoogleSheetsService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    @Value("${google.sheets.enabled:false}")
    private boolean enabled;

    @Value("${google.sheets.spreadsheetId:}")
    private String spreadsheetId;

    @Value("${google.sheets.credentialsPath:}")
    private String credentialsPath;

    @Value("${google.sheets.applicationName:SmartRetail}")
    private String applicationName;

    private volatile Sheets sheets;
    private final Set<String> knownSheets = new HashSet<>();

    @PostConstruct
    public void init() {
        if (!enabled) {
            log.info("Google Sheets logging disabled (google.sheets.enabled=false).");
            return;
        }
        if (spreadsheetId == null || spreadsheetId.isBlank()) {
            log.warn("Google Sheets enabled but spreadsheetId is empty. Fallback to console logging.");
            enabled = false;
            return;
        }
        if (credentialsPath == null || credentialsPath.isBlank()) {
            log.warn("Google Sheets enabled but credentialsPath is empty. Fallback to console logging.");
            enabled = false;
            return;
        }
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleCredentials creds = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsPath))
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

            this.sheets = new Sheets.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(creds))
                    .setApplicationName(applicationName)
                    .build();

            try {
                var ss = this.sheets.spreadsheets().get(spreadsheetId).execute();
                if (ss.getSheets() != null) {
                    ss.getSheets().forEach(s -> {
                        if (s.getProperties() != null && s.getProperties().getTitle() != null) {
                            knownSheets.add(s.getProperties().getTitle());
                        }
                    });
                }
            } catch (Exception metaErr) {
                log.warn("Could not load spreadsheet metadata. Will append and create sheets lazily.", metaErr);
            }

            log.info("Google Sheets logging enabled. spreadsheetId={}", spreadsheetId);
        } catch (Exception e) {
            log.error("Failed to init Google Sheets client. Fallback to console logging.", e);
            enabled = false;
        }
    }

    private void ensureSheetExists(String sheetName) {
        if (sheetName == null || sheetName.isBlank()) return;
        if (knownSheets.contains(sheetName)) return;
        try {
            Request addSheet = new Request().setAddSheet(new AddSheetRequest().setProperties(
                    new com.google.api.services.sheets.v4.model.SheetProperties().setTitle(sheetName)
            ));
            BatchUpdateSpreadsheetRequest batch = new BatchUpdateSpreadsheetRequest().setRequests(List.of(addSheet));
            sheets.spreadsheets().batchUpdate(spreadsheetId, batch).execute();
            knownSheets.add(sheetName);
        } catch (Exception e) {
            // Nếu sheet đã tồn tại do race condition thì cũng ok
            knownSheets.add(sheetName);
        }
    }

    public void appendRow(String sheetName, List<Object> values) {
        if (!enabled || sheets == null) {
            log.info("Append to Google Sheet [{}] (fallback): {}", sheetName, values);
            return;
        }
        try {
            ensureSheetExists(sheetName);
            String range = (sheetName == null || sheetName.isBlank()) ? "Sheet1!A:Z" : (sheetName + "!A:Z");
            ValueRange body = new ValueRange().setValues(List.of(values));
            sheets.spreadsheets().values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();
        } catch (Exception e) {
            log.error("AppendRow failed for sheetName={}. Fallback to console logging. values={}", sheetName, values, e);
        }
    }
}

