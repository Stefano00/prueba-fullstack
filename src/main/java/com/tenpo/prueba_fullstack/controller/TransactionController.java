package com.tenpo.prueba_fullstack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tenpo.prueba_fullstack.dto.TransactionDTO;
import com.tenpo.prueba_fullstack.model.Transaction;
import com.tenpo.prueba_fullstack.services.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Create a new transaction", description = "Adds a new transaction to the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestBody(description = "Transaction data to be created", required = true,
            		content = @Content(
            	            mediaType = "application/json",
            	            schema = @Schema(example = "{\n  \"transactionId\": null,\n  \"amount\": 10500,\n  \"giro\": \"string\",\n  \"transactionDate\": \"2025-01-16T03:15:06.139Z\",\n  \"tenpistaId\": null,\n  \"tenpistaName\": \"string\"\n}")
            	        ))
            @org.springframework.web.bind.annotation.RequestBody TransactionDTO dto
            ) {
        Transaction newTransaction = TransactionDTO.toEntity(dto);
        Transaction createdTransaction = transactionService.createTransaction(newTransaction);
        return new ResponseEntity<>(TransactionDTO.fromEntity(createdTransaction), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all transactions", description = "Fetches all transactions from the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of transactions",
                content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionDTO> dtoList = transactions.stream()
                .map(TransactionDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Get transaction by ID", description = "Fetches a specific transaction by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction found",
                content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @PathVariable("id") Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(TransactionDTO.fromEntity(transaction));
    }

    @Operation(summary = "Update a transaction", description = "Updates an existing transaction in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable("id") Long id,
            @RequestBody(description = "Updated transaction data", required = true)
            @org.springframework.web.bind.annotation.RequestBody TransactionDTO updatedDto) {
        Transaction entityToUpdate = TransactionDTO.toEntity(updatedDto);
        Transaction updatedEntity = transactionService.updateTransaction(id, entityToUpdate);
        return ResponseEntity.ok(TransactionDTO.fromEntity(updatedEntity));
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a specific transaction by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable("id") Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
