package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.HistoryDto;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.service.HistoryService;
import com.hawkeye.capstone.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryApiController {

    private final HistoryRepository historyRepository;
    private final HistoryService historyService;
    private final SessionService sessionService;

//    //히스토리 저장
//    @PostMapping("/api/history/createHistory")
//    @Transactional
//    public CreateHistoryResponse createHistory(@RequestBody CreateHistoryRequest request){
//
//        Long historyId = historyService.createOrUpdateHistory(request.userId, request.sessionId,
//                request.attendanceCount, request.attitude, request.vibe, request.attendance, request.timeLineLogList,
//                request.roll, request.yaw
//        );
//
//        //그룹이 수업 중이면 endSession = false
//        if(sessionService.isOnAir(request.sessionId))
//            return new CreateHistoryResponse(historyId, false);
//        else
//            return new CreateHistoryResponse(historyId, true);
//    }
    //히스토리 저장
    @PostMapping("/api/history/createHistory")
    @Transactional
    public CreateHistoryResponse createHistory(@RequestBody CreateOrUpdateHistoryRequest request){

        Long historyId = historyService.createRequest(request.userId, request.sessionId, request.pitch,
                request.yaw, request.absence
        );

        //그룹이 수업 중이면 endSession = false
        if(sessionService.isOnAir(request.sessionId))
            return new CreateHistoryResponse(historyId, false);
        else
            return new CreateHistoryResponse(historyId, true);
    }

    //수업 전체 분위기
    @GetMapping("/api/history/getVibe/{sessionId}")
    @Transactional
    public GetVibeResponse getVibe(@PathVariable Long sessionId){

        Session findSession = sessionService.findOne(sessionId);
        List<History> historyList = findSession.getHistoryList();

        return new GetVibeResponse(historyList.get(0).getVibe());
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
        private boolean endSession;
    }

    @Data
    static class CreateOrUpdateHistoryRequest{

        private Long userId;
        private Long sessionId;
        private int pitch;
        private int yaw;
        private boolean absence;
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
        private PitchGraph pitch;
        private YawGraph yaw;

    }

    @Data
    @AllArgsConstructor
    static class GetVibeResponse{
        private int vibe;
    }
}
