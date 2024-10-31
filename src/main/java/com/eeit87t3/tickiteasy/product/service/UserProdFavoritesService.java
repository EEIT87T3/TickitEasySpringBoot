package com.eeit87t3.tickiteasy.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.product.entity.ProdFavoritesEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.repository.ProdFavoritesRepo;
import com.eeit87t3.tickiteasy.product.repository.ProductRepo;

import java.util.stream.Collectors;

@Service
public class UserProdFavoritesService {

	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ProdFavoritesRepo prodFavoritesRepo;
	
	public Map<String, Integer> getProductFavoriteCount() {
	    List<ProdFavoritesEntity> favorites = prodFavoritesRepo.findAll();
	    // 将商品按照名称分组，统计每个商品的收藏次数
	    return favorites.stream()
	            .collect(Collectors.groupingBy(
	                    favorite -> favorite.getProduct().getProductName(),  // 确保是字符串类型
	                    Collectors.summingInt(ProdFavoritesEntity::getFavoriteCount)
	            ));
	}

	
	
	// 處理收藏/取消收藏
    @Transactional
    public Map<String, String> toggleFavorite(Member member, Integer productID) {
        Map<String, String> result = new HashMap<>();
        try {
            // 先查找商品是否存在
            ProductEntity product = productRepo.findById(productID)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
                
            Optional<ProdFavoritesEntity> existingFavorite = 
                prodFavoritesRepo.findByMemberIDAndProductID(member.getMemberID(), productID);

            if (existingFavorite.isPresent()) {
                // 更新收藏狀態
                ProdFavoritesEntity favorite = existingFavorite.get();
                int newFavoriteCount = (favorite.getFavoriteCount() == 1) ? 0 : 1;
                prodFavoritesRepo.updateFavoriteCount(member.getMemberID(), productID, newFavoriteCount);
                result.put("message", newFavoriteCount == 1 ? "加入收藏" : "取消收藏");
            } else {
                // 新增收藏（使用完整的實體）
                ProdFavoritesEntity newFavorite = new ProdFavoritesEntity(member, product);
                prodFavoritesRepo.save(newFavorite);
                result.put("message", "已加入收藏");
            }
            
            return result;
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return result;
        }
    }

 // 處理補貨通知設定
    @Transactional
    public Map<String, String> setNotification(Member member, Integer productID) {
        Map<String, String> result = new HashMap<>();
        try {
            // 先查找商品是否存在
            ProductEntity product = productRepo.findById(productID)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
                
            Optional<ProdFavoritesEntity> existingFavorite = 
                prodFavoritesRepo.findByMemberIDAndProductID(member.getMemberID(), productID);

            if (existingFavorite.isPresent()) {
                // 更新通知狀態
                ProdFavoritesEntity favorite = existingFavorite.get();
                int newNotifyStatus = (favorite.getNotifyStatus() == 1) ? 0 : 1;
                prodFavoritesRepo.updateNotifyStatus(member.getMemberID(), productID, newNotifyStatus);
                result.put("message", newNotifyStatus == 1 ? "設定補貨通知" : "取消補貨通知");
            } else {
                // 新增通知設定（使用完整的實體，但預設 favoriteCount 為 0）
                ProdFavoritesEntity newFavorite = new ProdFavoritesEntity(member, product);
                newFavorite.setFavoriteCount(0); // 預設不收藏
                newFavorite.setNotifyStatus(1);  // 設定補貨通知
                prodFavoritesRepo.save(newFavorite);
                result.put("message", "已設定補貨通知");
            }
            
            return result;
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return result;
        }
    }

    // 查詢會員的所有收藏商品
    public List<ProductEntity> getMemberFavorites(Member member) {
        return prodFavoritesRepo.findProductsByMemberAndFavoriteCount(member.getMemberID(), 1);
    }
	
	
}
