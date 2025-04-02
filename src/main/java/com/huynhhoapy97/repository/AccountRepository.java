package com.huynhhoapy97.repository;

import com.huynhhoapy97.model.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountDTO, Integer> {
}
