package org.cric.back_office.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 1. String을 특정 객체(DTO)로 변환
    public static <T> T stringToObject(String jsonString, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 에러: " + e.getMessage());
        }
    }

    // 2. String을 Map으로 변환 (구조를 모를 때 유용)
    public static Map<String, Object> stringToMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON을 Map으로 변환 실패", e);
        }
    }

    // 3. String을 JsonNode로 변환 (특정 필드만 추출할 때 유용)
    public static JsonNode stringToJsonNode(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 트리 생성 실패", e);
        }
    }
}