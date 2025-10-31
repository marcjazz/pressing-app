package com.tragent.pressing.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="item")
public class Item {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String name;
	
	@Column(nullable=true)
	private String description;
	
	@Column(nullable=false)
	private Double cost;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="category_id", referencedColumnName = "id")
	private Category category;
	
	@OneToMany(mappedBy="item", cascade = CascadeType.ALL)
	private List<Transaction> customerItems;
	
	public Item() {
		super();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
