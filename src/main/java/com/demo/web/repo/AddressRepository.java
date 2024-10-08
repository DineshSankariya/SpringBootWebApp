package com.demo.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.web.vo.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {

}
