package com.tragent.pressing.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="expense")
public class MaterialPurchase {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false)
	private int quantity;
	
	@Column(nullable=false)
	private Date purchasedDate = new Date();
	
	@Column(nullable=false)
	private Date depreciationDate = new Date();
		
	@ManyToOne(optional=false)
	@JoinColumn(name="cleaning_material_id", referencedColumnName = "id")
	private CleaningMaterial cleaningMaterial;

	public MaterialPurchase() {
		super();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getPurchasedDate() {
		return this.purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public Date getDepreciationDate() {
		return this.depreciationDate;
	}

	public void setDepreciationDate(Date depreciationDate) {
		this.depreciationDate = depreciationDate;
	}

	public CleaningMaterial getCleaningMaterial() {
		return this.cleaningMaterial;
	}

	public void setCleaningMaterial(CleaningMaterial cleaningMaterial) {
		this.cleaningMaterial = cleaningMaterial;
	}
	
}
