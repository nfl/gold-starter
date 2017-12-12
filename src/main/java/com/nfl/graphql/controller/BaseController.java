package com.nfl.graphql.controller;

import com.nfl.dm.shield.dynamic.domain.BaseKey;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

class BaseController {

    static final String SCHEMA_NAMESPACE = "GOLD_STARTER_SCHEMA_NAMESPACE";
    static final String INSTANCE_NAMESPACE = "GOLD_STARTER_INSTANDE_NAMESPACE";


    ResponseEntity<Object> buildSuccessResponse(Object data) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String ttl = "30";      // seconds
        List<String> cacheValues = new LinkedList<>();
        cacheValues.add("max-age=" + ttl);
        cacheValues.add("s-maxage=" + ttl);

        headers.put(HttpHeaders.CACHE_CONTROL, cacheValues);
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON.toString()));
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    Map<String, Object> buildVariablesMap(String dynamicDomainName) {
        Map<String, Object> retMap = new HashMap<>(89);
        retMap.put(BaseKey.DYNAMIC_TYPE_NAMESPACE, SCHEMA_NAMESPACE);
        retMap.put(BaseKey.DYNAMIC_INSTANCE_NAMESPACE, INSTANCE_NAMESPACE);
        retMap.put(BaseKey.DYNAMIC_TYPE_NAME, dynamicDomainName);
        return retMap;
    }

}
