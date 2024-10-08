package com.demo.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.web.vo.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
