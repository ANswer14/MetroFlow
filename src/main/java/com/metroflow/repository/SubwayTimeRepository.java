package com.metroflow.repository;

import com.metroflow.model.dto.SubwayTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayTimeRepository extends JpaRepository<SubwayTime, Long> {
}
