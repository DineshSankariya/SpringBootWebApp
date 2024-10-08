package com.demo.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.web.vo.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {

}
