package com.eeit87t3.tickiteasy.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.product.entity.ProdFavoritesEntity;
import com.eeit87t3.tickiteasy.product.entity.ProdFavoritesEntity.ProdFavoriteID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import java.util.Optional;

public interface ProdFavoritesRepo extends JpaRepository<ProdFavoritesEntity, ProdFavoriteID> {
	
	// 查詢特定會員的特定商品收藏狀態
    @Query("SELECT f FROM ProdFavoritesEntity f WHERE f.member.memberID = :memberID AND f.product.productID = :productID")
    Optional<ProdFavoritesEntity> findByMemberIDAndProductID(@Param("memberID") Integer memberID, @Param("productID") Integer productID);
    
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
