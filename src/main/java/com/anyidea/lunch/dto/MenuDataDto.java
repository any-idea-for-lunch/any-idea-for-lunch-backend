package com.anyidea.lunch.dto;

import java.util.List;
import java.util.Map;

public record MenuDataDto(
        List<String> rootItems,
        Map<String, List<String>> subItemsMap
) {
}
