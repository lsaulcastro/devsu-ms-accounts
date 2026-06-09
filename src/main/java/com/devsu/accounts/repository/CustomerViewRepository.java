package com.devsu.accounts.repository;

import com.devsu.accounts.domain.model.CustomerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerViewRepository extends JpaRepository<CustomerView, String> {
}