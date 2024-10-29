package com.eeit87t3.tickiteasy.cwdfunding.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;


import com.eeit87t3.tickiteasy.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "fundingProjFollow")
public class FundProjFollow {

	@Embeddable
	public static class FundProjFollowPK implements Serializable {
        private static final long serialVersionUID = 1L;

		@Column (name = "memberID")
		private Integer memberID;
		
		@Column (name = "projectID")
		private Integer projectID;
		
		 @Override
		 public boolean equals(Object o) {
		  if (this == o) return true;
		  if (!(o instanceof FundProjFollowPK fundProjFollowPK)) return false;
		        return (memberID == fundProjFollowPK.memberID) && (projectID == fundProjFollowPK.projectID);
		 }

		 @Override
		 public int hashCode() {
		  return Objects.hash(memberID, projectID);
		 }

		
		public Integer getMemberID() {
			return memberID;
		}

		public FundProjFollowPK() {
		}

		public void setMemberID(Integer memberID) {
			this.memberID = memberID;
		}

		public Integer getProjectID() {
			return projectID;
		}

		public void setProjectID(Integer projectID) {
			this.projectID = projectID;
		}		

	} 
	
	@EmbeddedId
	private FundProjFollowPK fundProjFollowPK;
	
	/*
	 * 因PK中已有memberID, projectID，因此需設置(insertable = false, updatable =
	 * false)避免在FundProjFollow重複寫入這兩個欄位 因為這些欄位由嵌入式複合主鍵負責。
	 * 
	 */    
	@ManyToOne
	@JoinColumn (name = "memberID", referencedColumnName = "memberID", insertable = false, updatable = false)
	private Member member;
    
    @ManyToOne
	@JoinColumn (name = "projectID", referencedColumnName = "projectID", insertable = false, updatable = false)
	private FundProj fundProj;   
	
	@Column(name = "followDate")
	private Timestamp followDate;

	public FundProjFollowPK getFundProjFollowPK() {
		return fundProjFollowPK;
	}

	public void setFundProjFollowPK(FundProjFollowPK fundProjFollowPK) {
		this.fundProjFollowPK = fundProjFollowPK;
	}

	public Timestamp getFollowDate() {
		return followDate;
	}

	public void setFollowDate(Timestamp followDate) {
		this.followDate = followDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public FundProj getFundProj() {
		return fundProj;
	}

	public void setFundProj(FundProj fundProj) {
		this.fundProj = fundProj;
	}
	
	
}
