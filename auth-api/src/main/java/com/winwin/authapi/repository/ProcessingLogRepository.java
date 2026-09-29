package com.winwin.authapi.repository;

import com.winwin.authapi.entity.ProcessingLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, UUID> {
}
