package com.tenpo.prueba_fullstack.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenpo.prueba_fullstack.model.Tenpista;
import com.tenpo.prueba_fullstack.model.Transaction;
import com.tenpo.prueba_fullstack.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TenpistaService tenpistaService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Tenpista tenpista = Tenpista.builder()
                .tenpistaId(1L)
                .tenpistaName("Test Tenpista")
                .build();

        transaction = Transaction.builder()
                .transactionId(1L)
                .amount(1000)
                .giro("Test Giro")
                .transactionDate(LocalDateTime.now())
                .tenpista(tenpista)
                .build();
    }

    @Test
    void createTransaction_ShouldSaveTransaction() {
        when(tenpistaService.findOrCreateTenpistaByName(transaction.getTenpista().getTenpistaName()))
                .thenReturn(transaction.getTenpista());
        when(transactionRepository.countByTenpistaTenpistaId(transaction.getTenpista().getTenpistaId()))
                .thenReturn(0L);
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        Transaction result = transactionService.createTransaction(transaction);

        assertNotNull(result);
        assertEquals(transaction.getTransactionId(), result.getTransactionId());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void createTransaction_ShouldThrowException_WhenAmountIsNegative() {
        transaction.setAmount(-1000);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.createTransaction(transaction));

        assertEquals("El monto de la transacción no puede ser negativo.", exception.getMessage());
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions_WhenDataExists() {
        // Preparar datos simulados
        Transaction transaction1 = Transaction.builder()
                .transactionId(1L)
                .amount(1000)
                .giro("Giro 1")
                .transactionDate(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .transactionId(2L)
                .amount(2000)
                .giro("Giro 2")
                .transactionDate(LocalDateTime.now())
                .build();
        
        when(transactionRepository.findAll()).thenReturn(List.of(transaction1, transaction2));

        List<Transaction> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size(), "El tamaño de la lista no coincide con los datos simulados");
        assertEquals(transaction1.getTransactionId(), result.get(0).getTransactionId());
        assertEquals(transaction2.getTransactionId(), result.get(1).getTransactionId());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getAllTransactions_ShouldReturnEmptyList_WhenNoDataExists() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Transaction> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "La lista debería estar vacía");

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        when(transactionRepository.findById(transaction.getTransactionId()))
                .thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById(transaction.getTransactionId());

        assertNotNull(result);
        assertEquals(transaction.getTransactionId(), result.getTransactionId());
    }

    @Test
    void getTransactionById_ShouldReturnNull_WhenNotFound() {
        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.empty());

        Transaction result = transactionService.getTransactionById(transaction.getTransactionId());

        assertNull(result);
    }

    @Test
    void deleteTransaction_ShouldDeleteTransaction() {
        when(transactionRepository.existsById(transaction.getTransactionId())).thenReturn(true);

        transactionService.deleteTransaction(transaction.getTransactionId());

        verify(transactionRepository, times(1)).deleteById(transaction.getTransactionId());
    }

    @Test
    void deleteTransaction_ShouldThrowException_WhenTransactionDoesNotExist() {
        when(transactionRepository.existsById(transaction.getTransactionId())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.deleteTransaction(transaction.getTransactionId()));

        assertEquals("No existe una transacción con el ID: " + transaction.getTransactionId(), exception.getMessage());
    }

    @Test
    void updateTransaction_ShouldUpdateTransaction() {
        Transaction updatedTransaction = Transaction.builder()
                .transactionId(transaction.getTransactionId())
                .amount(2000)
                .giro("Updated Giro")
                .transactionDate(LocalDateTime.now())
                .build();

        when(transactionRepository.findById(transaction.getTransactionId()))
                .thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransaction(transaction.getTransactionId(), updatedTransaction);

        assertNotNull(result);
        assertEquals(updatedTransaction.getAmount(), result.getAmount());
        assertEquals(updatedTransaction.getGiro(), result.getGiro());
    }

    @Test
    void updateTransaction_ShouldThrowException_WhenTransactionDoesNotExist() {
        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.updateTransaction(transaction.getTransactionId(), transaction));

        assertEquals("No existe una transacción con el ID: " + transaction.getTransactionId(), exception.getMessage());
    }
    
    @Test
    void createTransaction_ShouldThrowException_WhenAmountIsNegative1() {
        Transaction transaction = Transaction.builder()
                .amount(-100) // Monto negativo
                .transactionDate(LocalDateTime.now())
                .tenpista(Tenpista.builder().tenpistaName("Valid Tenpista").build())
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertEquals("El monto de la transacción no puede ser negativo.", exception.getMessage());
    }
    

    @Test
    void createTransaction_ShouldThrowException_WhenTenpistaIsNull() {
        Transaction transaction = Transaction.builder()
                .amount(100)
                .transactionDate(LocalDateTime.now())
                .tenpista(null) // Tenpista nulo
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertEquals("La transacción debe estar asociada a un Tenpista válido.", exception.getMessage());
    }
    
    @Test
    void createTransaction_ShouldThrowException_WhenTenpistaNameIsNull() {
        Transaction transaction = Transaction.builder()
                .amount(100)
                .transactionDate(LocalDateTime.now())
                .tenpista(Tenpista.builder().tenpistaName(null).build()) // Nombre de Tenpista nulo
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertEquals("La transacción debe estar asociada a un Tenpista válido.", exception.getMessage());
    }
    
    @Test
    void createTransaction_ShouldThrowException_WhenMaxTransactionsReached() {
        Transaction transaction = Transaction.builder()
                .amount(100)
                .transactionDate(LocalDateTime.now())
                .tenpista(Tenpista.builder().tenpistaName("Valid Tenpista").build())
                .build();

        Tenpista tenpista = Tenpista.builder()
                .tenpistaId(1L)
                .tenpistaName("Valid Tenpista")
                .build();

        when(tenpistaService.findOrCreateTenpistaByName("Valid Tenpista")).thenReturn(tenpista);
        when(transactionRepository.countByTenpistaTenpistaId(1L)).thenReturn(100L); // Máximo alcanzado

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertEquals("Este Tenpista ya alcanzó el máximo de 100 transacciones.", exception.getMessage());
    }
    
    @Test
    void updateTransaction_ShouldThrowException_WhenAmountIsNegative() {
        Transaction existingTransaction = Transaction.builder()
                .transactionId(1L)
                .amount(1000)
                .giro("Giro existente")
                .build();

        Transaction newData = Transaction.builder()
                .amount(-500) // Monto negativo
                .giro("Nuevo giro")
                .build();

        when(transactionRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTransaction));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.updateTransaction(1L, newData);
        });

        assertEquals("El monto de la transacción no puede ser negativo.", exception.getMessage());

        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }


}