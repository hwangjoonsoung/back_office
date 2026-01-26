package org.cric.back_office.work.service;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.work.repository.SiloJpaRepository;
import org.cric.back_office.work.repository.SiloRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class SiloService {

    private final SiloJpaRepository siloJpaRepository;
    private final SiloRepository siloRepository;

}
