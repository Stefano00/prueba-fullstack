package com.tenpo.prueba_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tenpo.prueba_fullstack.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
    long countByTenpistaTenpistaId(Long tenpistaId);
    
}