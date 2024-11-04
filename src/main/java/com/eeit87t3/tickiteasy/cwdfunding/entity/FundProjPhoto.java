package com.eeit87t3.tickiteasy.cwdfunding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author TingXD (chen19990627)
 */
@Entity
@Table( name = "FundingProjPhoto")
public class FundProjPhoto {

	@Id @Column(name = "photoID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column( name = "fileName")
	private String fileName;
	
	@ManyToOne
	@JoinColumn (name = "projectID")
	private FundProj fundProj;
}
