package com.eeit87t3.tickiteasy.cwdfunding.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
	
	
}
