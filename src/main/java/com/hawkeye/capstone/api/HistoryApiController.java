package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.RollGraph;
import com.hawkeye.capstone.domain.TimeLineLog;
import com.hawkeye.capstone.domain.YawGraph;
import com.hawkeye.capstone.dto.GuestHistoryDto;
import com.hawkeye.capstone.dto.HistoryDto;
import com.hawkeye.capstone.dto.HostHistoryDto;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.service.HistoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryApiController {

    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

    //히스토리 저장
    @PostMapping("/api/history/createHistory")
    public CreateHistoryResponse createHistory(@RequestBody CreateHistoryRequest request){

        Long historyId = historyService.createHistory(request.userId, request.sessionId,
                request.attendanceCount, request.attitude, request.vibe, request.attendance, request.timeLineLogList,
                request.roll, request.yaw
        );

        return new CreateHistoryResponse(historyId);
    }

    //히스토리 한 번에 전송
    @GetMapping("/api/history/getHistory")
    public List<HistoryDto> getHistory(@RequestParam Long userId){

        List<HistoryDto> historyDtoList = historyService.getHistory(userId);
        //정렬
        historyDtoList.sort(new Comparator<HistoryDto>() {
            @Override
            public int compare(HistoryDto o1, HistoryDto o2) {
                if(o1.getHistoryId() == o2.getHistoryId())
                    return 0;
                else if(o2.getHistoryId() > o1.getHistoryId())
                    return 1;
                else
                    return -1;
            }
        });
        return historyDtoList;
    }

//    //게스트 히스토리 열람
//    @GetMapping("/api/history/getGuestHistory")
//    public GuestHistoryDto getGuestHistory(@RequestParam Long userId, @RequestParam Long sessionId){
//
//        return historyService.getGuestHistory(userId, sessionId);
//    }
//
//    //호스트 히스토리 열람
//    @GetMapping("api/history/getGuestHistory")
//    public List<HostHistoryDto> getHostHistoryDto(@RequestParam Long sessionId){
//
//        List<HostHistoryDto> hostHistoryDtoList = historyService.getHostHistory(sessionId);
//
//        hostHistoryDtoList.sort(new Comparator<HostHistoryDto>() {
//            @Override
//            public int compare(HostHistoryDto o1, HostHistoryDto o2) {
//                if(o1.getId() == o2.getId())
//                    return 0;
//                else if(o2.getId() > o1.getId())
//                    return 1;
//                else
//                    return -1;
//            }
//        });
//
//        return hostHistoryDtoList;
//    }

    @Data
    @AllArgsConstructor
    static class CreateHistoryResponse{
        private Long id;
    }

    @Data
    static class CreateHistoryRequest{

        private Long userId;
        private Long sessionId;
        private int attendanceCount;
        private int attitude;
        private int vibe;
        private boolean attendance;
        private List<TimeLineLog> timeLineLogList;
        private RollGraph roll;
        private YawGraph yaw;



    }
}
