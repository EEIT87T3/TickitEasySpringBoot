package com.eeit87t3.tickiteasy.product.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.eeit87t3.tickiteasy.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * @author Liang123456123
 */
@Entity
@Table(name = "prodFavorites")
public class ProdFavoritesEntity {
    
    // 定義嵌入式複合主鍵類
    @Embeddable
    public static class ProdFavoriteID implements Serializable {
        private static final long serialVersionUID = 1L;
        
        @Column(name = "memberID")
        private Integer memberID;
        
        @Column(name = "productID")
        private Integer productID;
        
        public ProdFavoriteID() {}
        
        public ProdFavoriteID(Integer memberID, Integer productID) {
            this.memberID = memberID;
            this.productID = productID;
        }
        
        public Integer getMemberID() {
            return memberID;
        }
        
        public void setMemberID(Integer memberID) {
            this.memberID = memberID;
        }
        
        public Integer getProductID() {
            return productID;
        }
        
        public void setProductID(Integer productID) {
            this.productID = productID;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProdFavoriteID that = (ProdFavoriteID) o;
            if (!memberID.equals(that.memberID)) return false;
            return productID.equals(that.productID);
        }
        
        @Override
        public int hashCode() {
            int result = memberID.hashCode();
            result = 31 * result + productID.hashCode();
            return result;
        }
    }
    
    @EmbeddedId
    private ProdFavoriteID ID;
    
    @ManyToOne
    @MapsId("memberID")
    @JoinColumn(name = "memberID")
    private Member member;
    
    @ManyToOne
    @MapsId("productID")
    @JoinColumn(name = "productID")
    private ProductEntity product;
    
    @Column(name = "dateAdded")
    @CreationTimestamp
    private LocalDateTime dateAdded;
    
    @Column(name = "favoriteCount", nullable = false)
    private Integer favoriteCount = 1;
    
    @Column(name = "notifyStatus", nullable = false)
    private Integer notifyStatus = 0;
    
    // 無參建構子
    public ProdFavoritesEntity() {}
    
    // 建構子
    public ProdFavoritesEntity(Member member, ProductEntity product) {
        this.ID = new ProdFavoriteID(member.getMemberID(), product.getProductID());
        this.member = member;
        this.product = product;
    }
    
    public ProdFavoriteID getID() {
        return ID;
    }
    
    public void setID(ProdFavoriteID ID) {
        this.ID = ID;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
    }
    
    public ProductEntity getProduct() {
        return product;
    }
    
    public void setProduct(ProductEntity product) {
        this.product = product;
    }
    
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public Integer getFavoriteCount() {
        return favoriteCount;
    }
    
    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
    
    public Integer getNotifyStatus() {
        return notifyStatus;
    }
    
    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }
}