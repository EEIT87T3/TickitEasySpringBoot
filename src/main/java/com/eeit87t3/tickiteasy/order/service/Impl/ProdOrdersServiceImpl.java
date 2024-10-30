package com.eeit87t3.tickiteasy.order.service.Impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.admin.service.AdminService;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.service.TicketTypesProcessingService;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.order.entity.CheckoutPaymentRequestForm;
import com.eeit87t3.tickiteasy.order.entity.ConfirmData;
import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;
import com.eeit87t3.tickiteasy.order.entity.ProdOrders;
import com.eeit87t3.tickiteasy.order.entity.ProductForm;
import com.eeit87t3.tickiteasy.order.entity.ProductPackageForm;
import com.eeit87t3.tickiteasy.order.entity.RedirectUrls;
import com.eeit87t3.tickiteasy.order.repository.ProdOrdersRepository;
import com.eeit87t3.tickiteasy.order.service.ProdOrdersService;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.InvoiceObj;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;


@Service
@Transactional
public class ProdOrdersServiceImpl implements ProdOrdersService{
	
	@Autowired
	private ProdOrdersRepository por;

	@Override
	public ProdOrders saveOrder(ProdOrders prodOrders) {
		return por.save(prodOrders);
	}

	@Override
	public void deleteOrderById(Integer prodOrderId) {
		por.deleteById(prodOrderId);
	}

	@Override
	public ProdOrders updateOrder(ProdOrders prodOrders) {
		
		Optional<ProdOrders> byId = por.findById(prodOrders.getProdOrderID());
		
		if(byId.isPresent()) {
			ProdOrders prodOrders2 = byId.get();
			
			prodOrders2.setMember(prodOrders.getMember());
			prodOrders2.setOrderDate(prodOrders.getOrderDate());
			prodOrders2.setPayments(prodOrders.getPayments());
			prodOrders2.setPaymentInfo(prodOrders.getPaymentInfo());
			prodOrders2.setStatus(prodOrders.getStatus());
			prodOrders2.setTotalAmount(prodOrders.getTotalAmount());
			prodOrders2.setShippingStatus(prodOrders.getShippingStatus());
			prodOrders2.setShippingID(prodOrders.getShippingID());
			prodOrders2.setRecipientName(prodOrders.getRecipientName());
			prodOrders2.setAddress(prodOrders.getAddress());
			prodOrders2.setPhone(prodOrders.getPhone());
			
			
			return por.save(prodOrders2);
		}
		
		return null;
	}

	@Override
	public ProdOrders selectOrderById(Integer id) {
		
		Optional<ProdOrders> byId = por.findById(id);
		
		if(byId.isPresent()) {
			return byId.get();
		}
		
		return null;
	}

	@Override
	public List<ProdOrders> selectAllOrder() {
		return por.findAll();
	}
	
	public ProdOrders findLatestProdOrder() {
		return por.findLatestProdOrder();
	}
	
	public Page<ProdOrders> findAllPage(Integer id,Integer records){
        Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
        return por.findAll(pageable);
    }
	
	public Page<ProdOrders> findAllPage(Integer id){
        Pageable pageable = PageRequest.of(id-1,5, Sort.Direction.ASC,"prodOrderID");
        return por.findAll(pageable);
    }

	@Override
	public Page<ProdOrders> findByDate(String number,Integer id,Integer records) {
		Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
		return por.findByDate(number,pageable);
	}

	@Override
	public Page<ProdOrders> findByProdOrdersId(Integer number, Integer id,Integer records) {
		Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
		return por.findByProdOrdersId(number, pageable);
	}

	@Override
	public Page<ProdOrders> findByMemberId(Integer number, Integer id,Integer records) {
		Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
		return por.findByMemberId(number, pageable);
	}
	
	@Override
	public List<ProdOrders> findOrdersByMemberId(Integer number) {
		return por.findOrdersByMemberId(number);
	}
	
	@Autowired
	ProductService productService;
	@Autowired
	TicketTypesProcessingService ticketTypesProcessingService;
	@Autowired
	MemberService memberService;
	//串接綠界ECPay
	@Override
	public String ECPay(List<Map<String,Object>> ticketTypesCartToCheckoutJson,List<Map<String,Object>> checkoutItems,String totalAmount,String memberEmail) { 
		//lists內放productID、productName、productQuantity、productAmount
		StringBuilder itemNameBuilder  = new StringBuilder() ; //串接商品明細
		for(Map<String,Object> list : ticketTypesCartToCheckoutJson) {
			itemNameBuilder.append(list.get("eventName")).append("#"); //綠界明細需要	
		}
		for(Map<String,Object> list : checkoutItems) {
			itemNameBuilder.append(list.get("productName")).append("#"); //綠界明細需要	
		}
		if(itemNameBuilder.length() > 0) {
			itemNameBuilder.setLength(itemNameBuilder.length() - 1);
		}
		String itemname = itemNameBuilder.toString();
		
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo("Tickit" + System.currentTimeMillis()); //訂單編號均為唯一值，不可重複使用。 字數20
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String formatTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
		obj.setMerchantTradeDate(formatTime);  //格式為：yyyy/MM/dd HH:mm:ss
		obj.setTotalAmount(totalAmount); //交易金額 String類型
		obj.setTradeDesc("test Description"); //交易描述
		obj.setItemName(itemname); //商品名稱
		obj.setReturnURL("https://abf1-114-25-182-128.ngrok-free.app/TickitEasy/admin/order/ECPayReturn"); //為付款結果通知回傳網址，為特店server或主機的URL，用來接收綠界後端回傳的付款結果通知。
		obj.setNeedExtraPaidInfo("N"); //額外的付款資訊
		obj.setClientBackURL("http://localhost:8080/TickitEasy/user/clientSide/orderPaymentCompleted"); //消費者點選此按鈕後，會將頁面導回到此設定的網址
				
		String form = all.aioCheckOut(obj, null);
		
		ProdOrders prodOrders = new ProdOrders();//prodOrders 資料庫新增
		Member memberByEmail = memberService.findByEmail(memberEmail);
		prodOrders.setMember(memberByEmail);
		String orderDate = obj.getMerchantTradeDate().replace("/","-");
		prodOrders.setOrderDate(Timestamp.valueOf(orderDate));
		prodOrders.setStatus("未付款");
		prodOrders.setPaymentInfo(obj.getMerchantTradeNo());
		prodOrders.setTotalAmount(Integer.parseInt(obj.getTotalAmount()));
		ProdOrders prodOrderSave = por.save(prodOrders);
		
		List<ProdOrderDetails> listsProdOrderDetails = new ArrayList<>(); //prodOrderDetails 資料庫新增
		for(Map<String,Object> map : checkoutItems) {
			ProdOrderDetails prodOrderDetails = new ProdOrderDetails();
			Integer productID = (Integer)map.get("productID"); //產品ID		
			if (productID != null) {
				prodOrderDetails.setProductId(productID);
				ProductEntity productById = productService.findProductById(productID);
				Integer productQuantity = (Integer)map.get("productQuantity"); //產品數量
	
				prodOrderDetails.setProdOrder(prodOrderSave);
				prodOrderDetails.setPrice(productById.getPrice());
				prodOrderDetails.setQuantity(productQuantity);
			}			
			listsProdOrderDetails.add(prodOrderDetails);		
		}
		for(Map<String,Object> map : ticketTypesCartToCheckoutJson) {
			ProdOrderDetails prodOrderDetails = new ProdOrderDetails();
			Integer ticketTypeID = (Integer)map.get("ticketTypeID"); //票券ID	
			if (ticketTypeID != null) {
				prodOrderDetails.setTicketTypeId(ticketTypeID);
				TicketTypesEntity ticketById = ticketTypesProcessingService.findById(ticketTypeID);
				Integer ticketQuantity = (Integer) map.get("quantity");
				
				prodOrderDetails.setProdOrder(prodOrderSave);
				prodOrderDetails.setTicketPrice(ticketById.getPrice());
				prodOrderDetails.setTicketQuantity(ticketQuantity);
			}
			listsProdOrderDetails.add(prodOrderDetails);
		}
		
		prodOrderSave.setProdOrderDetailsBean(listsProdOrderDetails);
		por.save(prodOrderSave);
		
        return form;
	}
	//透過訂單號碼查詢
	public ProdOrders findBypaymentInfo(String paymentInfo) {
		return por.findBypaymentInfo(paymentInfo);
	}
	
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private AdminService adminService;
	//寄gmail信
	public void emailSend(Integer memberId,String merchantTradeNo,String tradeDate,String tradeAmt,String paymentType) throws MessagingException { //寄信
		Optional<Admin> adminById = adminService.getAdminById(memberId); //後續透過會員id找到會員信箱並寄出

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

	    helper.setFrom("eeit87t3@gmail.com");
	    helper.setTo("sososo8819@gmail.com");
	    helper.setSubject("Tickit訂單號碼: " + merchantTradeNo);

	    String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
        "<div style='background-color: #f8f8f8; padding: 20px; text-align: center;'>" +
        	"<div style='position: absolute; top: 10px; left: 20px; font-family: \"Brush Script MT\", cursive; font-size: 36px; color: #d3d3d3; opacity: 0.7;'>Tickit</div>" +
            "<h1 style='color: #333;'>[Tickit] 您的訂單" + merchantTradeNo + "已完成付款！</h1>" +
        "</div>" +
        "<div style='padding: 20px; background-color: white;'>" +
            "<p>王大明 您好：</p>" +
            "<p>感謝您在 Tickit 訂購商品</p>" +
            "<h2 style='color: #333; border-bottom: 1px solid #ccc; padding-bottom: 10px;'>訂單明細</h2>" +
            "<table style='width: 100%; border-collapse: collapse;'>" +
                "<tr>" +
                    "<td style='padding: 10px 0;'><strong>訂單編號：</strong></td>" +
                    "<td style='padding: 10px 0;'>#" + merchantTradeNo + "</td>" +
                "</tr>" +
                "<tr>" +
                    "<td style='padding: 10px 0;'><strong>訂購日期：</strong></td>" +
                    "<td style='padding: 10px 0;'>" + tradeDate + "</td>" +
                "</tr>" +
                "<tr>" +
                    "<td style='padding: 10px 0;'><strong>付款方式：</strong></td>" +
                    "<td style='padding: 10px 0;'>" + paymentType + "</td>" +
                "</tr>" +
            "</table>" +
            "<div style='margin-top: 20px; text-align: right;'>" +
                "<h3 style='color: #333;'>總計：NT$" + tradeAmt + "</h3>" +
            "</div>" +
        "</div>" +
    "</div>";

	    helper.setText(htmlContent, true);

		
		javaMailSender.send(mimeMessage);
	}
	
	public Integer chartjsFindStatusPaid(){
		List<ProdOrders> byStatusPaid = por.findByStatusPaid();
		Iterator<ProdOrders> iterator = byStatusPaid.iterator();
		Integer count = 0;
	 	String thisMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")); //轉化現在時間的月份為字串
		while (iterator.hasNext()) {
			ProdOrders next = iterator.next();
			String dbTime = new SimpleDateFormat("yyyy-MM").format(next.getOrderDate()); //轉化資料庫欄位的時間月份為字串
			if (dbTime.equals(thisMonth)) {//判斷日期是否為此年月
				count++;
			}
		}
		
		return count;
	}
	
	public Integer chartjsFindStatusNotPaid(){
		List<ProdOrders> byStatusPaid = por.findByStatusNotPaid();
		Iterator<ProdOrders> iterator = byStatusPaid.iterator();
		Integer count = 0;
	 	String thisMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")); //轉化現在時間的月份為字串
		while (iterator.hasNext()) {
			ProdOrders next = iterator.next();
			String dbTime = new SimpleDateFormat("yyyy-MM").format(next.getOrderDate()); //轉化資料庫欄位的時間月份為字串
			if (dbTime.equals(thisMonth)) {//判斷日期是否為此年月
				count++;
			}
		}
		
		
		return count;
	}
	
	//LinePay需要
	//Hmac 簽章 -- 透過javax.crypto.Mac類別提供訊息認證碼(Message Authentication Code, MAC)的演算法功能。
    public String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }
    //LinePay需要
  	//Hmac 簽章 -- 透過javax.crypto.Mac類別提供訊息認證碼(Message Authentication Code, MAC)的演算法功能。
    public String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }
	//LinePay調用
    @Override
    public String LinePay(List<Map<String,Object>> ticketTypesCartToCheckoutJson,List<Map<String,Object>> checkoutItems,String totalAmount,String memberEmail) throws Exception {
    	CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
    	
    	List lists = new ArrayList();
    	for(Map<String,Object> list : ticketTypesCartToCheckoutJson) {
    		ProductForm productForm = new ProductForm();
    		productForm.setId((String.valueOf(list.get("ticketTypeID"))));
    		productForm.setName((String)list.get("eventName"));
    		productForm.setImageUrl("");
    		productForm.setQuantity(new BigDecimal((Integer)list.get("quantity")));
    		productForm.setPrice(new BigDecimal((Integer)list.get("price")));
    		lists.add(productForm);
    	}
    	for(Map<String,Object> list : checkoutItems) {
    		ProductForm productForm = new ProductForm();
    		productForm.setId((String.valueOf(list.get("productID"))));
    		productForm.setName((String)list.get("productName"));
    		productForm.setImageUrl("");
    		productForm.setQuantity(new BigDecimal((Integer)list.get("productQuantity")));
    		productForm.setPrice(new BigDecimal((Integer)list.get("productPrice")));
    		lists.add(productForm);
    	}
        
        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id"); //Y Package list的唯一ID
        productPackageForm.setName("shop_name"); //N 店鋪名稱，選填
        productPackageForm.setAmount(new BigDecimal(totalAmount)); //Y 總金額 與form.setAmount()對應
        productPackageForm.setProducts(lists);

        form.setPackages(Arrays.asList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setAppPackageName(""); //N 在Android環境切換應用時所需的資訊，用於防止網路釣魚攻擊（phishing）
        redirectUrls.setConfirmUrlType("SERVER");
        redirectUrls.setConfirmUrl("https://abf1-114-25-182-128.ngrok-free.app/TickitEasy/admin/order/LinePayReturn"); //Y 設定支付完成後用戶跳轉的網址。
        redirectUrls.setCancelUrl("http://localhost:8080/TickitEasy"); //Y 設定支付取消後用戶跳轉的網址。
        form.setRedirectUrls(redirectUrls);
        
        form.setAmount(new BigDecimal(totalAmount)); //Y 設定付款的總金額，這裡是來自商品包裹的總價格。
        form.setCurrency("TWD"); //Y 貨幣
        form.setOrderId("Tickit" + System.currentTimeMillis()); //Y 設定商家的訂單編號，用於商家的管理和追蹤。

        ObjectMapper mapper = new ObjectMapper();
        try {
        	//Request API
        	String jsonBody = mapper.writeValueAsString(form);
        	String ChannelSecret = "f79c0cac4c3ab969f28b4b90abaf16f5";
        	String requestUri = "/v3/payments/request";
        	String nonce = UUID.randomUUID().toString();
			String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			// 構建 HTTP 請求
			HttpRequest request  = HttpRequest.newBuilder()
		            .uri(new URI("https://sandbox-api-pay.line.me/v3/payments/request"))
		            .header("Content-Type", "application/json")
		            .header("X-LINE-ChannelId", "2006474211") // 替換為你的 Channel ID
		            .header("X-LINE-Authorization-Nonce", nonce)
		            .header("X-LINE-Authorization", signature)
		            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
		            .build();
			// 發送請求並獲取回應
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());			
			 // 檢查回應
		    int statusCode = response.statusCode();
		    if (statusCode == 200) {
		    	
		    	ProdOrders prodOrders = new ProdOrders();
		    	prodOrders.setMember(memberService.findByEmail(memberEmail));
		    	prodOrders.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
		    	prodOrders.setStatus("未付款");
		    	prodOrders.setPayments("LinePay");
		    	prodOrders.setPaymentInfo(form.getOrderId());
		    	prodOrders.setTotalAmount(Integer.valueOf(totalAmount));
		    	por.save(prodOrders);//先儲存資料
		    	
		        // 如果成功，返回回應內容
		        return response.body();
		    } else {
		        throw new RuntimeException("HTTP Request Failed, Status Code: " + statusCode);
		    }
        
        } catch (JsonProcessingException e) {
			e.printStackTrace();
		} 
        
        return null;
    }
    
    //LinePay回傳
    @Override
    public String LinePayReturn(String targetUrl,String orderId, String transactionId) throws Exception {
    	ProdOrders bypaymentInfo = por.findBypaymentInfo(orderId);
    	ObjectMapper mapper = new ObjectMapper();
        
    	ConfirmData confirmData = new ConfirmData();
        confirmData.setAmount(new BigDecimal(bypaymentInfo.getTotalAmount()));
        confirmData.setCurrency("TWD");
    	
    	//Confirm API
        String jsonBody = mapper.writeValueAsString(confirmData);
    	String nonce = UUID.randomUUID().toString();
    	String ChannelSecret = "f79c0cac4c3ab969f28b4b90abaf16f5";
    	String requestUri = "/v3/payments/" + transactionId + "/confirm";
		String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(confirmData) + nonce);
		// 構建 HTTP 請求
		HttpRequest request  = HttpRequest.newBuilder()
	            .uri(new URI(targetUrl))
	            .header("Content-Type", "application/json")
	            .header("X-LINE-ChannelId", "2006474211") // 替換為你的 Channel ID
	            .header("X-LINE-Authorization-Nonce", nonce)
	            .header("X-LINE-Authorization", signature)
	            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
	            .build();
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		// 以下明日再處理 -------------------------------------------------------------------------------------------------------------
		JSONObject jsonObject = new JSONObject(response.body());
		
		String returnCode = jsonObject.getString("returnCode");
			    
	    // 取得 info 裡面的 transactionId 和 orderId
	    JSONObject info = jsonObject.getJSONObject("info");
	    
	    // 取得 payInfo 裡的第一筆資料
	    JSONArray payInfoArray = info.getJSONArray("payInfo");
	    
	    // 取得 packages 裡的第一筆資料
	    JSONArray packagesArray = info.getJSONArray("packages");
	    JSONObject packagesObject = packagesArray.getJSONObject(0);
	    JSONArray productsArray = packagesObject.getJSONArray("products");
	    
	    List<ProdOrderDetails> listsProdOrderDetails = bypaymentInfo.getProdOrderDetailsBean();//prodOrderDetails 資料庫新增
	    listsProdOrderDetails.clear();
	    for(int i = 0; i < productsArray.length(); i++) {
	    	ProdOrderDetails prodOrderDetails = new ProdOrderDetails();
	    	JSONObject product = productsArray.getJSONObject(i);
			String productId = product.getString("id");
			String productName = product.getString("name");
			int productQuantity = product.getInt("quantity");
			int productPrice = product.getInt("price");
			
			if(productName.contains("示範") || productName.contains("活動") || productName.contains("啟售") || productName.contains("票種")) {
				prodOrderDetails.setProdOrder(bypaymentInfo);
				prodOrderDetails.setTicketTypeId(Integer.parseInt(productId));
				prodOrderDetails.setTicketPrice(productPrice);
				prodOrderDetails.setTicketQuantity(productQuantity);
			}else {
				prodOrderDetails.setProdOrder(bypaymentInfo);
				prodOrderDetails.setProductId(Integer.parseInt(productId));
				prodOrderDetails.setPrice(productPrice);
				prodOrderDetails.setQuantity(productQuantity);
			}
			listsProdOrderDetails.add(prodOrderDetails);
	    }
	    if("0000".equals(returnCode)) {
			bypaymentInfo.setStatus("已付款");
			bypaymentInfo.setProdOrderDetailsBean(listsProdOrderDetails);
			
			por.save(bypaymentInfo);
			return "ok";
		}
	    return new RuntimeException("添加失敗, Status Code: " + response.statusCode()).toString();
    }
}
