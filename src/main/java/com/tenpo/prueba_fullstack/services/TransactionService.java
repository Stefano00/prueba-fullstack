package com.tenpo.prueba_fullstack.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import com.tenpo.prueba_fullstack.model.Transaction;
import com.tenpo.prueba_fullstack.model.Tenpista;
import com.tenpo.prueba_fullstack.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private TenpistaService tenpistaService;


    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("El monto de la transacción no puede ser negativo.");
        }

        transaction.setTransactionDate(LocalDateTime.now());
        
    
        log.info("Transaction {}", transaction);
        if (transaction.getTenpista() == null || transaction.getTenpista().getTenpistaName() == null) {
            throw new IllegalArgumentException("La transacción debe estar asociada a un Tenpista válido.");
        }
        
        Tenpista tenpista = tenpistaService.findOrCreateTenpistaByName(transaction.getTenpista().getTenpistaName());
        
        long currentCount = transactionRepository.countByTenpistaTenpistaId(tenpista.getTenpistaId());
        if (currentCount >= 100) {
            throw new IllegalArgumentException("Este Tenpista ya alcanzó el máximo de 100 transacciones.");
        }
        transaction.setTenpista(tenpista);

        return transactionRepository.save(transaction);
    }


    public List<Transaction> getAllTransactions() {
    	log.info("Consultando por todas las transacciones en la DB");
        return transactionRepository.findAll();
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new IllegalArgumentException("No existe una transacción con el ID: " + transactionId);
        }
        transactionRepository.deleteById(transactionId);
    }
    
    public Transaction getTransactionById(Long transactionId) {
    	return transactionRepository.findById(transactionId).orElse(null);
    }

    @Transactional
    public Transaction updateTransaction(Long transactionId, Transaction newData) {
        Transaction existing = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("No existe una transacción con el ID: " + transactionId));
        
        if (newData.getAmount() < 0) {
            throw new IllegalArgumentException("El monto de la transacción no puede ser negativo.");
        }

        existing.setAmount(newData.getAmount());
        existing.setGiro(newData.getGiro());
        
        return transactionRepository.save(existing);
    }
}
