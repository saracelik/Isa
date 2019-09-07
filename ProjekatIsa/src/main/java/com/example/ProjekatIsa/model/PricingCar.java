package com.example.ProjekatIsa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pricingcar")
public class PricingCar implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricing_id", nullable = false, updatable = false)
    private Long id;
	
	@Column(name = "price", nullable = false)
    private Double price;
	
	@Column(name = "date_from", nullable = false)
    private Double dateFrom;
	
	@Column(name = "date_to", nullable = false)
    private Double dateTo;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="car_id")
	private Car car;
	
	

	public PricingCar() {
		
	}

	public PricingCar(Long id, Double price, Double dateFrom, Double dateTo, Car car) {
		super();
		this.id = id;
		this.price = price;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.car = car;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Double dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Double getDateTo() {
		return dateTo;
	}

	public void setDateTo(Double dateTo) {
		this.dateTo = dateTo;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	

}