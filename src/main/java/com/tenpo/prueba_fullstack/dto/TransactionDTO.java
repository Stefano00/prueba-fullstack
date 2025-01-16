package com.tenpo.prueba_fullstack.dto;

import java.time.LocalDateTime;

import com.tenpo.prueba_fullstack.model.Transaction;
import com.tenpo.prueba_fullstack.model.Tenpista;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    
    private Long transactionId;
    private Integer amount;
    private String giro;
    private LocalDateTime transactionDate;

    private Long tenpistaId;
    private String tenpistaName;

    public static TransactionDTO fromEntity(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        Tenpista tenpista = transaction.getTenpista();

        return TransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .giro(transaction.getGiro())
                .transactionDate(transaction.getTransactionDate())
                .tenpistaId(tenpista != null ? tenpista.getTenpistaId() : null)
                .tenpistaName(tenpista != null ? tenpista.getTenpistaName() : null)
                .build();
    }


    public static Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        return Transaction.builder()
                .transactionId(dto.getTransactionId())
                .amount(dto.getAmount())
                .giro(dto.getGiro())
                .transactionDate(dto.getTransactionDate())
                .tenpista(
                    Tenpista.builder()
                            .tenpistaId(dto.getTenpistaId())
                            .tenpistaName(dto.getTenpistaName())
                            .build()
                )
                .build();
    
}
}
    