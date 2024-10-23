package com.eeit87t3.tickiteasy.order.service.Impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.order.entity.CheckoutPaymentRequestForm;
import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;
import com.eeit87t3.tickiteasy.order.entity.ProdOrders;
import com.eeit87t3.tickiteasy.order.entity.ProductForm;
import com.eeit87t3.tickiteasy.order.entity.ProductPackageForm;
import com.eeit87t3.tickiteasy.order.entity.RedirectUrls;
import com.eeit87t3.tickiteasy.order.repository.ProdOrdersRepository;
import com.eeit87t3.tickiteasy.order.service.ProdOrdersService;
import com.eeit87t3.tickiteasy.product.entity.CartItem;
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
			prodOrders2.setPaymenInfo(prodOrders.getPaymenInfo());
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
	
	@Autowired
	ProductService productService;
	//串接綠界ECPay
	@Override
	public String ECPay(List<Map<String,Object>> lists,String totalAmount) { 
		//lists內放productID、productName、productQuantity、productAmount
		StringBuilder itemNameBuilder  = new StringBuilder() ; //串接商品明細
		for(Map<String,Object> list : lists) {
			itemNameBuilder.append(list.get("productName")).append("#"); //綠界需要	
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
		obj.setReturnURL("https://9fe3-36-227-50-63.ngrok-free.app/TickitEasy/order/ECPayReturn"); //為付款結果通知回傳網址，為特店server或主機的URL，用來接收綠界後端回傳的付款結果通知。
		obj.setNeedExtraPaidInfo("N"); //額外的付款資訊
		obj.setClientBackURL("http://localhost:8080/TickitEasy/user/product"); //消費者點選此按鈕後，會將頁面導回到此設定的網址
				
		String form = all.aioCheckOut(obj, null);
		
		
		ProdOrders prodOrders = new ProdOrders();//prodOrders 資料庫新增
		Member member = new Member();
		member.setMemberID(5);
		prodOrders.setMember(member);
		String orderDate = obj.getMerchantTradeDate().replace("/","-");
		prodOrders.setOrderDate(Timestamp.valueOf(orderDate));
		prodOrders.setStatus("未付款");
		prodOrders.setPaymenInfo(obj.getMerchantTradeNo());
		prodOrders.setTotalAmount(Integer.parseInt(obj.getTotalAmount()));
		ProdOrders prodOrderSave = por.save(prodOrders);
		
		List<ProdOrderDetails> listsProdOrderDetails = new ArrayList<>(); //prodOrderDetails 資料庫新增
		for(Map<String,Object> list : lists) {
			ProdOrderDetails prodOrderDetails = new ProdOrderDetails();
			Integer productID = (Integer)list.get("productID"); //產品ID
			ProductEntity productById = productService.findProductById(productID);
			Integer prodproductQuantityuct = (Integer)list.get("productQuantity"); //產品數量
			
			prodOrderDetails.setProdOrder(prodOrderSave);
			prodOrderDetails.setProductId(productID);
			prodOrderDetails.setPrice(productById.getPrice());
			prodOrderDetails.setQuantity(prodproductQuantityuct);
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
		
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("eeit87t3@gmail.com");
//		message.setTo("sososo8819@gmail.com");
//		message.setSubject("Tickit訂單號碼: " + merchantTradeNo);
//		message.setText( 
//				+ "訂單確認\r\n"
//				+ "親愛的 [客戶名稱]，\r\n"
//				+ "感謝您在我們的商店購物。您的訂單已經成功付款並確認。以下是您的訂單詳情：\r\n"
//				+ "\r\n"
//				+ "訂單編號：" + merchantTradeNo + "\r\n"
//				+ "訂購日期：" + tradeDate + "\r\n"
//				+ "付款金額：NT" + tradeAmt + "\r\n"
//				+ "付款方式：" + paymentType + "\r\n"
//				+ "\r\n"
//				+ "訂單內容：\r\n"
//				+ "\r\n"
//				+ "商品A x 1\r\n"
//				+ "商品B x 2\r\n"
//				+ "\r\n"
//				+ "預計出貨日期：2023年10月27日\r\n"
//				+ "如果您有任何問題或需要進一步的協助，請隨時聯繫我們的客戶服務團隊，並提供您的訂單編號 " + merchantTradeNo + "\r\n"
//				+ "再次感謝您的購買！\r\n"
//				+ "[您的商店名稱]\r\n"
//				+ "客戶服務團隊";
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
            "<div style='background-color: #f8f8f8; padding: 15px; margin-top: 20px;'>" +
                "<table style='width: 100%;'>" +
                    "<tr>" +
                        "<td style='width: 80px;'>" +
                            "<img src='product-image-url' alt='商品圖片' style='width: 80px; height: 80px; object-fit: cover;'>" +
                        "</td>" +
                        "<td style='padding-left: 15px;'>" +
                            "<h3 style='margin: 0; color: #333;'>醜比頭</h3>" +
                            "<p style='margin: 5px 0;'>單價：NT$549 數量：1</p>" +
                            "<p style='margin: 5px 0;'><strong>小計：NT$549</strong></p>" +
                        "</td>" +
                    "</tr>" +
                "</table>" +
            "</div>" +
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
    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }
    //LinePay需要
  	//Hmac 簽章 -- 透過javax.crypto.Mac類別提供訊息認證碼(Message Authentication Code, MAC)的演算法功能。
    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }
	//LinePay主要
    public static String LinePay() {
    	 CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();


         form.setAmount(new BigDecimal("100"));
         form.setCurrency("TWD");
         form.setOrderId("merchant_order_id");

         ProductPackageForm productPackageForm = new ProductPackageForm();
         productPackageForm.setId("package_id");
         productPackageForm.setName("shop_name");
         productPackageForm.setAmount(new BigDecimal("100"));

         ProductForm productForm = new ProductForm();
         productForm.setId("product_id");
         productForm.setName("product_name");
         productForm.setImageUrl("");
         productForm.setQuantity(new BigDecimal("10"));
         productForm.setPrice(new BigDecimal("10"));
         productPackageForm.setProducts(Arrays.asList(productForm));

         form.setPackages(Arrays.asList(productPackageForm));
         RedirectUrls redirectUrls = new RedirectUrls();
         redirectUrls.setAppPackageName("");
         redirectUrls.setConfirmUrl("");
         form.setRedirectUrls(redirectUrls);
         
         ObjectMapper mapper = new ObjectMapper();
         String ChannelSecret = "f79c0cac4c3ab969f28b4b90abaf16f5";
         String requestUri = "/v3/payments/request";
         String nonce = UUID.randomUUID().toString();
         try {
			String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return null;
    }
}
