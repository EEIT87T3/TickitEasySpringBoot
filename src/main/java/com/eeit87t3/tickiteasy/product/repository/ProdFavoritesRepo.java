package com.eeit87t3.tickiteasy.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.product.entity.ProdFavoritesEntity;
import com.eeit87t3.tickiteasy.product.entity.ProdFavoritesEntity.ProdFavoriteID;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Liang123456123
 */
public interface ProdFavoritesRepo extends JpaRepository<ProdFavoritesEntity, ProdFavoriteID> {
	
	 // 查詢需要發送補貨通知的記錄
    List<ProdFavoritesEntity> findByProductProductIDAndNotifyStatus(Integer productID, Integer notifyStatus);
    
	// 查詢特定會員的特定商品收藏狀態(檢查會員是否已收藏此商品，以決定顯示「收藏」還是「取消收藏」按鈕)
    @Query("SELECT f FROM ProdFavoritesEntity f WHERE f.member.memberID = :memberID AND f.product.productID = :productID")
    Optional<ProdFavoritesEntity> findByMemberIDAndProductID(@Param("memberID") Integer memberID, @Param("productID") Integer productID);
    
    // 查詢會員的收藏商品(顯示會員的收藏商品列表頁面)
    @Query("SELECT f.product FROM ProdFavoritesEntity f WHERE f.member.memberID = :memberID AND f.favoriteCount = :favoriteCount")
    List<ProductEntity> findProductsByMemberAndFavoriteCount(@Param("memberID") Integer memberID, @Param("favoriteCount") Integer favoriteCount);
    
    
    // 更新收藏狀態
    @Modifying
    @Transactional
    @Query("UPDATE ProdFavoritesEntity f SET f.favoriteCount = :favoriteCount " +
           "WHERE f.member.memberID = :memberID AND f.product.productID = :productID")
    void updateFavoriteCount(@Param("memberID") Integer memberID, 
                           @Param("productID") Integer productID, 
                           @Param("favoriteCount") Integer favoriteCount);
    
    // 更新補貨通知狀態
    @Modifying
    @Transactional
    @Query("UPDATE ProdFavoritesEntity f SET f.notifyStatus = :notifyStatus " +
           "WHERE f.member.memberID = :memberID AND f.product.productID = :productID")
    void updateNotifyStatus(@Param("memberID") Integer memberID, 
                          @Param("productID") Integer productID, 
                          @Param("notifyStatus") Integer notifyStatus);


}
