package com.app.userapi.service.dto;

import lombok.Data;
import java.util.Map;

@Data
public class EndpointDescription {
    public final String method;
    public final String url;
    public final Map<String, String> getParams;
    public final Map<String, String> data;
}
