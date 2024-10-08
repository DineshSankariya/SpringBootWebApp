package com.demo.web.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    
    

    public Address() {
		super();
	}
    
    
	public Address(String street, String city, User user) {
		super();
		this.street = street;
		this.city = city;
		this.user = user;
	}


	public Address(Long id, String street, String city, User user) {
		super();
		this.id = id;
		this.street = street;
		this.city = city;
		this.user = user;
	}

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public void setUser(User user) {
		this.user = user;
	}
    
    

}

