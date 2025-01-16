package com.tenpo.prueba_fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tenpo.prueba_fullstack.model.Tenpista;

@Repository
public interface TenpistaRepository extends JpaRepository<Tenpista, Long> {
     
	List<Tenpista> findByTenpistaName(String name);
}