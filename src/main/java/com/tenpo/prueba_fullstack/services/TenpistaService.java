package com.tenpo.prueba_fullstack.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.tenpo.prueba_fullstack.model.Tenpista;
import com.tenpo.prueba_fullstack.model.Transaction;
import com.tenpo.prueba_fullstack.repository.TenpistaRepository;
import com.tenpo.prueba_fullstack.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Slf4j
@Service
public class TenpistaService {
	
	@Autowired
	TenpistaRepository tenpistaRepository;
	
	@Transactional
	public Tenpista findOrCreateTenpistaByName(String name) {
		Tenpista tenpista;
		List<Tenpista> tenpistaList = tenpistaRepository.findByTenpistaName(name);
		if(tenpistaList.size() == 0) {
			tenpista = createTenpista(name);
		}else {
			tenpista = tenpistaList.get(0);
		}
		return tenpista;
	}
	
	@Transactional
	private Tenpista createTenpista(String name) {
		Tenpista tenpista = new Tenpista();
		tenpista.setTenpistaName(name);
		tenpistaRepository.save(tenpista);
		return tenpista;
	}

}
