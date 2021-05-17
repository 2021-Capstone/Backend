package com.hawkeye.capstone.api;

import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HistoryApiController {

    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

    //히스토리 저장

    //게스트 히스토리 열람

    //호스트 히스토리 열람
}
