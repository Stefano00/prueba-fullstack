package com.tenpo.prueba_fullstack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenpista")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenpista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nm_tenpista_id")
    private Long tenpistaId;

    @Column(name = "va_name", nullable = false, length = 100)
    private String tenpistaName;
}
