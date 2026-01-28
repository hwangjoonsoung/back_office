package org.cric.back_office.work.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.ApiResponse;
import org.cric.back_office.work.dto.SiloCreateDto;
import org.cric.back_office.work.dto.SiloResponseDto;
import org.cric.back_office.work.dto.SiloUpdateDto;
import org.cric.back_office.work.service.SiloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/silo")
public class SiloRestController {

    private final SiloService siloService;

    /**
     * Silo 생성 API
     * POST /api/silo
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createSilo(@Valid @RequestBody SiloCreateDto dto) {
        Long siloId = siloService.createSilo(dto);
        ApiResponse<Long> response = new ApiResponse<>("ok", HttpStatus.CREATED.value(), siloId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Silo 단건 조회 API
     * GET /api/silo/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiloResponseDto>> findById(@PathVariable Long id) {
        SiloResponseDto siloResponseDto = siloService.findById(id);
        ApiResponse<SiloResponseDto> response = new ApiResponse<>("ok", HttpStatus.OK.value(), siloResponseDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Silo 전체 조회 API
     * GET /api/silo
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SiloResponseDto>>> findAll() {
        List<SiloResponseDto> siloList = siloService.findAll();
        ApiResponse<List<SiloResponseDto>> response = new ApiResponse<>("ok", HttpStatus.OK.value(), siloList);
        return ResponseEntity.ok(response);
    }

    /**
     * Silo 수정 API
     * PUT /api/silo/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateSilo(
            @PathVariable Long id,
            @Valid @RequestBody SiloUpdateDto dto) {
        siloService.updateSilo(id, dto);
        ApiResponse<Void> response = new ApiResponse<>("ok", HttpStatus.OK.value(), null);
        return ResponseEntity.ok(response);
    }

    /**
     * Silo 삭제 API (논리 삭제: enable = false)
     * DELETE /api/silo/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSilo(@PathVariable Long id) {
        siloService.deleteSilo(id);
        ApiResponse<Void> response = new ApiResponse<>("ok", HttpStatus.OK.value(), null);
        return ResponseEntity.ok(response);
    }



}
