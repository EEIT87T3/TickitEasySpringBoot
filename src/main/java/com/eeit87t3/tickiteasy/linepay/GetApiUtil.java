package com.eeit87t3.tickiteasy.linepay;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetApiUtil {
	
	public static JsonNode sendGet(String channelId, String nonce, String signature, String url) {
	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("X-LINE-ChannelId", channelId);
	        headers.set("X-LINE-Authorization-Nonce", nonce);
	        headers.set("X-LINE-Authorization", signature);

	        HttpEntity<String> entity = new HttpEntity<>(headers);

	        ResponseEntity<String> response = restTemplate.exchange(
	            url,
	            HttpMethod.GET,
	            entity,
	            String.class
	        );

	        ObjectMapper objectMapper = new ObjectMapper();
	        return objectMapper.readTree(response.getBody());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}
