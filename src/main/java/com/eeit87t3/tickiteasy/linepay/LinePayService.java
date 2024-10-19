package com.eeit87t3.tickiteasy.linepay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LinePayService {

	@Value("${line.pay.channel.id}")
	private  String CHANNEL_ID;

	@Value("${line.pay.channel.secret}")
	private  String CHANNEL_SECRET;

	@Value("${line.pay.api.url}")
	private  String API_URL;	
	
	private ObjectMapper objectMapper = new ObjectMapper();
    /*
     * Hmac 簽章
     */
	public static String encrypt(final String keys, final String data) {
		return toBase64String(
				HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
	}
    /*
     * Base64
     */
	public static String toBase64String(byte[] bytes) {
		byte[] byteArray = Base64.encodeBase64(bytes);
		return new String(byteArray);
	}
	
	 public JsonNode requestAPI(Map<String, Object> form) {

			 String formString = null;
			try {
				formString = objectMapper.writeValueAsString(form);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // 產生 requestApi requestHeaders 所需的Uri、隨機數、HmacBase64簽章
	        String requestUri = "/v3/payments/request";
	        String nonce = UUID.randomUUID().toString();
	        String signature;
			signature = encrypt(CHANNEL_SECRET, CHANNEL_SECRET + requestUri + formString + nonce);
	        String httpsUrl = "https://sandbox-api-pay.line.me/v3/payments/request";
	        JsonNode json = null;

				json = PostApiUtil.sendPost(CHANNEL_ID, nonce, signature, httpsUrl, formString);
	        
	        return json;
	        
//	        if ("0000".equals(json.get("returnCode").asText())) {
//	        	// 請求成功，導向至付款頁面
//	        	String paymentUrl = json.get("info").get("paymentUrl").get("web").asText();
//	        	return paymentUrl;
//	        }else {
//	        	return failUrl;
//	        }	        
	        
	 }
	 
	 public Map<String, Object> formMakerTest() {
		 Map<String, Object> form = new HashMap<>();
		 form.put("amount", 100);
		 form.put("currency", "TWD");
		 form.put("orderId", "EXAMPLE_ORDER_20230422_1000001");

		  // Create the packages list
		    List<Map<String, Object>> packages = new ArrayList<>();

		    // Create the first package
		    Map<String, Object> package1 = new HashMap<>();
		    package1.put("id", "1");
		    package1.put("amount", 100);

		    // Create the products list inside the package
		    List<Map<String, Object>> products = new ArrayList<>();

		    // Create the first product
		    Map<String, Object> product1 = new HashMap<>();
		    product1.put("id", "PEN-B-001");
		    product1.put("name", "Pen Brown");
		    product1.put("quantity", 2);
		    product1.put("price", 50);

		    // Add the product to the products list
		    products.add(product1);

		    // Add the products list to the package
		    package1.put("products", products);

		    // Add the package to the packages list
		    packages.add(package1);

		    // Add the packages list to the form
		    form.put("packages", packages);

		    // Add the redirect URLs
		    Map<String, String> redirectUrls = new HashMap<>();
		    redirectUrls.put("confirmUrl", "http://localhost:8080/TickitEasy/test/linepay/requestOK");
		    redirectUrls.put("cancelUrl", "http://localhost:8080/TickitEasy/test/linepay/requestNO");
		    form.put("redirectUrls", redirectUrls);

		    return form;	
		 
	 }
	 
	 public Map<String, Object> formMaker(Map<String, Object> form){
		 	Map<String, Object> fullForm = new HashMap<String, Object>();
		 	fullForm = null;
		 
		    // Add the redirect URLs
		    Map<String, String> redirectUrls = new HashMap<>();
		    redirectUrls.put("confirmUrl", "http://localhost:8080/TickitEasy/test/linepay/requestOK");
		    redirectUrls.put("cancelUrl", "http://localhost:8080/TickitEasy/test/linepay/requestNO");
		    form.put("redirectUrls", redirectUrls);
		    
		    fullForm = form;
		    
		    return fullForm;
		 
	 }

	 public JsonNode confirmAPI(String transactionId) {
			 // 先將request body所需的資料用Map儲存，再將Map轉成Json格式的字串
			 Map<String, Object> formMap = new HashMap<>();
			 formMap.put("amount", 100);
			 formMap.put("currency", "TWD");
			 
			 String formString = null;
			 try {
			 	formString = objectMapper.writeValueAsString(formMap);
			 } catch (JsonProcessingException e) {
				e.printStackTrace();
			 }
			 
	//       產生 confirmApi requestHeaders 所需的Uri、隨機數、HmacBase64簽章
	         String requestUri = "/v3/payments/" + transactionId + "/confirm";
	         String nonce = UUID.randomUUID().toString();
			 String signature = encrypt(CHANNEL_SECRET, CHANNEL_SECRET + requestUri + formString + nonce);
	//       Post confirmApi 資訊
	         String httpsUrl = "https://sandbox-api-pay.line.me/v3/payments/" + transactionId + "/confirm";
	         
            JsonNode json = PostApiUtil.sendPost(CHANNEL_ID, nonce, signature, httpsUrl, formString); //將Line Confirm API後回傳的response body接起
            
            return json;
//	        if ("0000".equals(json.get("returnCode").asText())) {
//	        	//請款成功，交易完成，應把此訂單寫進資料庫中，呼叫與資料庫互動的方法
//	        	return null;
//	        }else {
//	        	return null;
//	        }	  

	 }
}
