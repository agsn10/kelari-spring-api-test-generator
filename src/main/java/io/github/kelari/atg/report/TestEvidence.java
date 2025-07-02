package io.github.kelari.atg.report;

import java.util.HashMap;
import java.util.Map;

public class TestEvidence {
    public String method;
    public String uri;
    public String requestBody;
    public Map<String, String> requestHeaders = new HashMap<>();
    public int responseStatus;
    public String responseBody;
    public Map<String, String> responseHeaders = new HashMap<>();
    public long durationMillis;
    public boolean success;
    public String errorMessage;
}
