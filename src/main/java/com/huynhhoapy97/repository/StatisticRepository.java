package com.huynhhoapy97.repository;

import com.huynhhoapy97.model.StatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<StatisticDTO, Integer> {
}
