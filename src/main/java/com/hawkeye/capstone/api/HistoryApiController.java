package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.RollGraph;
import com.hawkeye.capstone.domain.TimeLineLog;
import com.hawkeye.capstone.domain.YawGraph;
import com.hawkeye.capstone.dto.GuestHistoryDto;
import com.hawkeye.capstone.dto.HostHistoryDto;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.service.HistoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryApiController {

    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

    //히스토리 저장
    @PostMapping("/api/history/createHistory")
    public CreateHistoryResponse createHistory(@RequestBody CreateHistoryRequest request){

        Long historyId = historyService.createHistory(request.userId, request.SessionId, request.createdAt,
                request.attendanceCount, request.vibe, request.attendance, request.timeLineLogList,
                request.roll, request.yaw
        );

        return new CreateHistoryResponse(historyId);
    }

    //게스트 히스토리 열람
    @GetMapping("/api/history/getGuestHistory")
    public GuestHistoryDto getGuestHistory(@RequestParam Long userId, @RequestParam Long sessionId){

        return historyService.getGuestHistory(userId, sessionId);
    }

    //호스트 히스토리 열람
    @GetMapping("api/history/getGuestHistory")
    public List<HostHistoryDto> getHostHistoryDto(@RequestParam Long sessionId){

        return historyService.getHostHistory(sessionId);
    }

    @Data
    @AllArgsConstructor
    static class CreateHistoryResponse{
        private Long id;
    }

    @Data
    static class CreateHistoryRequest{

        private Long userId;
        private Long SessionId;
        private LocalDateTime createdAt;
        private int attendanceCount;
        private int vibe;
        private boolean attendance;
        private List<TimeLineLog> timeLineLogList;
        private RollGraph roll;
        private YawGraph yaw;

    }
}
