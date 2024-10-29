package com.eeit87t3.tickiteasy;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.order.entity.CheckoutPaymentRequestForm;
import com.eeit87t3.tickiteasy.order.entity.ConfirmData;
import com.eeit87t3.tickiteasy.order.entity.ProductForm;
import com.eeit87t3.tickiteasy.order.entity.ProductPackageForm;
import com.eeit87t3.tickiteasy.order.entity.RedirectUrls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.intThat;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Optional;
import java.util.UUID;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.AioCheckOutApplePay;
import ecpay.payment.integration.domain.AioCheckOutATM;
import ecpay.payment.integration.domain.AioCheckOutBARCODE;
import ecpay.payment.integration.domain.AioCheckOutCVS;
import ecpay.payment.integration.domain.AioCheckOutDevide;
import ecpay.payment.integration.domain.AioCheckOutOneTime;
import ecpay.payment.integration.domain.AioCheckOutPeriod;
import ecpay.payment.integration.domain.AioCheckOutWebATM;
import ecpay.payment.integration.domain.CreateServerOrderObj;
import ecpay.payment.integration.domain.DoActionObj;
import ecpay.payment.integration.domain.FundingReconDetailObj;
import ecpay.payment.integration.domain.InvoiceObj;
import ecpay.payment.integration.domain.QueryCreditCardPeriodInfoObj;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import ecpay.payment.integration.domain.QueryTradeObj;
import ecpay.payment.integration.domain.TradeNoAioObj;

@SpringBootTest
class TickitEasyApplicationTests {

	@Test
	void contextLoads() {				
		String targetUrl = "https://sandbox-api-pay.line.me/v3/payments/2024102802226907110/confirm";

		
	    RestTemplate restTemplate = new RestTemplate();
        // 使用 RestTemplate 發送 GET 請求並接收回應
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);

        // 處理回應
        String responseBody = response.getBody();
        System.out.println(responseBody);
	}
	
	@Test
	public void EcPay() {
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo("testCompany0004");
		obj.setMerchantTradeDate("2017/01/01 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		obj.setReturnURL("<http://211.23.128.214:5000>");
		obj.setNeedExtraPaidInfo("N");
		obj.setClientBackURL("<http://192.168.1.37:8080/>");
		String form = all.aioCheckOut(obj, null);
		System.out.println(form);
	}
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Test
	public void emailSend() { //寄信
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("eeit87t3@gmail.com");
		message.setTo("sososo8819@gmail.com");
		message.setSubject("主旨:Hello");
		message.setText("測試");
		
		javaMailSender.send(message);
	}
	
	//LinePay需要
	//Hmac 簽章 -- 透過javax.crypto.Mac類別提供訊息認證碼(Message Authentication Code, MAC)的演算法功能。
    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }
    //LinePay需要
  	//Hmac 簽章 -- 透過javax.crypto.Mac類別提供訊息認證碼(Message Authentication Code, MAC)的演算法功能。
    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }
    //LinePay test request api + confirm api
	@Test
	public void linepay() { //linepay測試
		
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();

		//設定每個商品的資訊
        ProductForm productForm = new ProductForm();
        productForm.setId("product_id"); //N 商家商品ID
        productForm.setName("product_name"); //Y 商品名稱
        productForm.setImageUrl(""); //N 商品圖示的URL
        productForm.setQuantity(new BigDecimal("10")); //Y 商品數量
        productForm.setPrice(new BigDecimal("10")); //Y 單一商品價格為10
        
        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id"); //Y Package list的唯一ID
        productPackageForm.setName("shop_name"); //N 店鋪名稱，選填
        productPackageForm.setAmount(productForm.getPrice().multiply(productForm.getQuantity())); //Y 總金額 與form.setAmount()對應
        productPackageForm.setProducts(Arrays.asList(productForm));

        form.setPackages(Arrays.asList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setAppPackageName(""); //N 在Android環境切換應用時所需的資訊，用於防止網路釣魚攻擊（phishing）
        redirectUrls.setConfirmUrl("https://www.google.com/"); //Y 設定支付完成後用戶跳轉的網址。
        redirectUrls.setCancelUrl("https://www.google.com/"); //Y 設定支付取消後用戶跳轉的網址。
        form.setRedirectUrls(redirectUrls);
        
        form.setAmount(productPackageForm.getAmount()); //Y 設定付款的總金額，這裡是來自商品包裹的總價格。
        form.setCurrency("TWD"); //Y 貨幣
        form.setOrderId("merchant_order_id"); //Y 設定商家的訂單編號，用於商家的管理和追蹤。

        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        
        //Confirm API
        ConfirmData confirmData = new ConfirmData();
        confirmData.setAmount(new BigDecimal("100"));
        confirmData.setCurrency("TWD");
        String confirmNonce = UUID.randomUUID().toString();
        String confirmUri = "/v3/payments/2024101902221679610/confirm";
        //Request API 、 Confirm API 共用部分
        ObjectMapper mapper = new ObjectMapper();
        String ChannelSecret = "f79c0cac4c3ab969f28b4b90abaf16f5";
        try {
        	//Request API
        	System.out.println("body => " + mapper.writeValueAsString(form));
			System.out.println("nonce => " + nonce);
			String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			System.out.println("signature => " + signature);
			//Confirm API
			System.out.println("bodyConfirm => " + mapper.writeValueAsString(confirmData));
			System.out.println("nonceConfrim => " + confirmNonce);
			String signatureConfirm = encrypt(ChannelSecret, ChannelSecret + confirmUri + mapper.writeValueAsString(confirmData) + confirmNonce);
			System.out.println("signatureConfirm => " + signatureConfirm);
        
        } catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	@Test
	public void testLinePay() throws JSONException {
		
		String body = "{\r\n"
				+ "    \"returnCode\": \"0000\",\r\n"
				+ "    \"returnMessage\": \"Success.\",\r\n"
				+ "    \"info\": {\r\n"
				+ "        \"transactionId\": 2024102802226934300,\r\n"
				+ "        \"orderId\": \"Tickit1730118061901\",\r\n"
				+ "        \"payInfo\": [\r\n"
				+ "            {\r\n"
				+ "                \"method\": \"CREDIT_CARD\",\r\n"
				+ "                \"amount\": 1098,\r\n"
				+ "                \"maskedCreditCardNumber\": \"************1111\"\r\n"
				+ "            }\r\n"
				+ "        ],\r\n"
				+ "        \"packages\": [\r\n"
				+ "            {\r\n"
				+ "                \"id\": \"package_id\",\r\n"
				+ "                \"amount\": 1098,\r\n"
				+ "                \"userFeeAmount\": 0,\r\n"
				+ "                \"products\": [\r\n"
				+ "                    {\r\n"
				+ "                        \"id\": \"9\",\r\n"
				+ "                        \"name\": \"示範輸入活動名稱 09\",\r\n"
				+ "                        \"quantity\": 2,\r\n"
				+ "                        \"price\": 299\r\n"
				+ "                    },\r\n"
				+ "                    {\r\n"
				+ "                        \"id\": \"3\",\r\n"
				+ "                        \"name\": \"藍色小精靈\",\r\n"
				+ "                        \"quantity\": 1,\r\n"
				+ "                        \"price\": 500\r\n"
				+ "                    }\r\n"
				+ "                ]\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "    }\r\n"
				+ "}";
		JSONObject jsonObject = new JSONObject(body);
		System.out.println(jsonObject.toString());
		
		
		String returnCode = jsonObject.getString("returnCode"); //0000
		System.out.println(returnCode);
	    String returnMessage = jsonObject.getString("returnMessage");//Success
	    System.out.println(returnMessage);
	    
	    // 取得 info 裡面的 transactionId 和 orderId
	    JSONObject info = jsonObject.getJSONObject("info");//{"transactionId":2024102802226934300,"orderId":"Tickit1730118061901","payInfo":[{"method":"CREDIT_CARD","amount":1098,"maskedCreditCardNumber":"************1111"}],"packages":[{"id":"package_id","amount":1098,"userFeeAmount":0,"products":[{"id":"9","name":"示範輸入活動名稱 09","quantity":2,"price":299},{"id":"3","name":"藍色小精靈","quantity":1,"price":500}]}]}
	    System.out.println(info.toString());
	    long transactionIdd = info.getLong("transactionId");//2024102802226934300
	    System.out.println(transactionIdd);
	    String orderIdd = info.getString("orderId");//Tickit1730118061901
	    System.out.println(orderIdd);
	    
	    // 取得 payInfo 裡的第一筆資料
	    JSONArray payInfoArray = info.getJSONArray("payInfo");
	    System.out.println(payInfoArray);
	    JSONObject payInfo = payInfoArray.getJSONObject(0);
	    System.out.println(payInfo);
	    String method = payInfo.getString("method");//CREDIT_CARD
	    System.out.println(method);
	    int amount = payInfo.getInt("amount");//1098
	    System.out.println(amount);
	    String maskedCreditCardNumber = payInfo.getString("maskedCreditCardNumber");//卡號
	    System.out.println(maskedCreditCardNumber);
	    // 取得 packages 裡的第一筆資料
	    JSONArray packagesArray = info.getJSONArray("packages"); //[{"id":"package_id","amount":1098,"userFeeAmount":0,"products":[{"id":"9","name":"示範輸入活動名稱 09","quantity":2,"price":299},{"id":"3","name":"藍色小精靈","quantity":1,"price":500}]}]
	    System.out.println(packagesArray);
	    JSONObject packageObject = packagesArray.getJSONObject(0); //{"id":"package_id","amount":1098,"userFeeAmount":0,"products":[{"id":"9","name":"示範輸入活動名稱 09","quantity":2,"price":299},{"id":"3","name":"藍色小精靈","quantity":1,"price":500}]}

	    
	    JSONArray products = packageObject.getJSONArray("products");
	    System.out.println(products);
	    for(int i=0; i<products.length(); i++) {
			JSONObject product = products.getJSONObject(i);
			System.out.println(product);
			String productId = product.getString("id");
			System.out.println(productId);
			String productName = product.getString("name");
			System.out.println(productName);
			int productQuantity = product.getInt("quantity");
			System.out.println(productQuantity);
			int productPrice = product.getInt("price");
			System.out.println(productPrice);
	    }
	    System.out.println(packageObject);
	    String packageId = packageObject.getString("id");//package_id
	    System.out.println(packageId);
	    int packageAmount = packageObject.getInt("amount");//1098
	    System.out.println(packageAmount);
	    int userFeeAmount = packageObject.getInt("userFeeAmount");//0
	    System.out.println(userFeeAmount);
	    
	    

	}
}
