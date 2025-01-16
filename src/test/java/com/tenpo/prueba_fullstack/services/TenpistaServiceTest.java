package com.tenpo.prueba_fullstack.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenpo.prueba_fullstack.model.Tenpista;
import com.tenpo.prueba_fullstack.repository.TenpistaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

class TenpistaServiceTest {

    @InjectMocks
    private TenpistaService tenpistaService;

    @Mock
    private TenpistaRepository tenpistaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findOrCreateTenpistaByName_ShouldReturnExistingTenpista() {
        String tenpistaName = "Existing Tenpista";
        Tenpista existingTenpista = Tenpista.builder()
                .tenpistaId(1L)
                .tenpistaName(tenpistaName)
                .build();

        when(tenpistaRepository.findByTenpistaName(tenpistaName))
                .thenReturn(List.of(existingTenpista));

        Tenpista result = tenpistaService.findOrCreateTenpistaByName(tenpistaName);

        assertNotNull(result);
        assertEquals(existingTenpista.getTenpistaId(), result.getTenpistaId());
        verify(tenpistaRepository, times(1)).findByTenpistaName(tenpistaName);
    }

    @Test
    void findOrCreateTenpistaByName_ShouldCreateNewTenpista_WhenNotFound() {
        String tenpistaName = "New Tenpista";

        when(tenpistaRepository.findByTenpistaName(tenpistaName))
                .thenReturn(Collections.emptyList());
        when(tenpistaRepository.save(any(Tenpista.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved Tenpista

        Tenpista result = tenpistaService.findOrCreateTenpistaByName(tenpistaName);

        assertNotNull(result);
        assertEquals(tenpistaName, result.getTenpistaName());
        verify(tenpistaRepository, times(1)).findByTenpistaName(tenpistaName);
        verify(tenpistaRepository, times(1)).save(any(Tenpista.class));
    }

}