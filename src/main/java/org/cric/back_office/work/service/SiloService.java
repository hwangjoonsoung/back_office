package org.cric.back_office.work.service;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.work.dto.SiloCreateDto;
import org.cric.back_office.work.dto.SiloResponseDto;
import org.cric.back_office.work.dto.SiloUpdateDto;
import org.cric.back_office.work.entity.Silo;
import org.cric.back_office.work.repository.SiloJpaRepository;
import org.cric.back_office.work.repository.SiloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SiloService {

    private final SiloJpaRepository siloJpaRepository;
    private final SiloRepository siloRepository;

    /**
     * Silo 생성
     */
    @Transactional
    public Long createSilo(SiloCreateDto dto) {
        Silo silo = Silo.createSilo(dto);
        Silo savedSilo = siloJpaRepository.save(silo);
        return savedSilo.getId();
    }

    /**
     * Silo 단건 조회
     */
    public SiloResponseDto findById(Long id) {
        Silo silo = siloJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silo not found with id: " + id));
        return new SiloResponseDto(silo);
    }

    /**
     * Silo 전체 조회 (삭제되지 않은 것만)
     */
    public List<SiloResponseDto> findAll() {
        return siloJpaRepository.findAllByIsDeleted(false).stream().map(SiloResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Silo 수정
     */
    @Transactional
    public void updateSilo(Long id, SiloUpdateDto dto) {
        Silo silo = siloJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silo not found with id: " + id));
        silo.updateSilo(dto);
    }

    /**
     * Silo 삭제 (논리 삭제: isDeleted = true)
     */
    @Transactional
    public void deleteSilo(Long id) {
        Silo silo = siloJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silo not found with id: " + id));
        silo.delete();
    }

}
