package com.example.ProjekatIsa.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.ProjekatIsa.DTO.AviocompanyDTO;
import com.example.ProjekatIsa.DTO.HotelDTO;

@Entity
@Table(name = "hotel")
public class Hotel implements Serializable{

	private static final long serialVersionUID = 225;

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id", nullable = false, updatable = false)
    private Long id;

	@Column(name = "name", nullable = false, columnDefinition="VARCHAR(40)")
    private String name;

	@Column(name = "adress", nullable = false, columnDefinition="VARCHAR(100)")
    private String address;
	
	@Column(name = "description", nullable = false, columnDefinition="VARCHAR(50)")
    private String description;
	
	@Column(name = "average_rating", nullable = true)
	private Double average_rating;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "hotel_rooms", joinColumns = @JoinColumn(name = "hotel_id"), inverseJoinColumns = @JoinColumn(name = "room_id"))   
	private Set<Room> rooms;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "hotel_additional_service", joinColumns = @JoinColumn(name = "hotel_id"), inverseJoinColumns = @JoinColumn(name = "additional_service_id"))   
	private Set<AdditionalServiceForHotel> additional_services;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "hotel_rating_hotel", joinColumns = @JoinColumn(name = "hotel_id"), inverseJoinColumns = @JoinColumn(name = "ratingHotel_id"))
    private Set<RatingHotel> hotel_ratings;
    
	
	public Set<AdditionalServiceForHotel> getAdditional_services() {
		return additional_services;
	}

	public void setAdditional_services(Set<AdditionalServiceForHotel> additional_services) {
		this.additional_services = additional_services;
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}
	
	public Set<RatingHotel> getHotel_ratings() {
		return hotel_ratings;
	}

	public void setHotel_ratings(Set<RatingHotel> hotel_ratings) {
		this.hotel_ratings = hotel_ratings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAdress(String adress) {
		this.address = adress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getAverage_rating() {
		return average_rating;
	}

	public void setAverage_rating(Double average_rating) {
		this.average_rating = average_rating;
	}

	public Hotel() {
		super();
	}

	public Hotel(Long id, String name, String address, String description, Double average_rating) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.average_rating = average_rating;
	}
	public Hotel(HotelDTO h) {
		 setId(h.getId());
	     setName(h.getName());
	     setDescription(h.getDescription());
	     setAdress(h.getAddress());
		
	}

	
}
