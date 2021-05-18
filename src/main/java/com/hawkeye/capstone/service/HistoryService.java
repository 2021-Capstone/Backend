package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.GuestHistoryDto;
import com.hawkeye.capstone.dto.HostHistoryDto;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.repository.SessionRepository;
import com.hawkeye.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public History findOne(Long historyId){
        return historyRepository.findOne(historyId);
    }

    //수업 종료 후 히스토리 생성
    public Long createHistory(Long userId, Long sessionId, LocalDateTime createdAt,
                              int attendanceCount, int vibe, boolean attendance,
                              List<TimeLineLog> timeLineLogList, RollGraph roll,
                              YawGraph yaw){

        User findUser = userRepository.findOne(userId);
        Session findSession = sessionRepository.findOne(sessionId);

        History history = History.builder()
                .user(findUser)
                .session(findSession)
                .attendance(attendance)
                .attendanceCount(attendanceCount)
                .vibe(vibe)
                .timeLineLogList(timeLineLogList)
                .rollGraph(roll)
                .yawGraph(yaw)
                .build();

        historyRepository.save(history);

        return history.getId();
    }

    //세션의 게스트 히스토리 열람
    public GuestHistoryDto getGuestHistory(Long userId, Long sessionId){

        Session findSession = sessionRepository.findOne(sessionId);
        History findHistory = historyRepository.findByGuestInSession(userId, sessionId);

        return new GuestHistoryDto(findHistory.getId(), GroupRole.GUEST, findSession.getGroup().getName(), findSession.getStartTime(),
                findHistory.getAttendanceCount(), findHistory.getVibe(), findHistory.isAttendance(), findHistory.getTimeLineLogList(),
                findHistory.getRollGraph(), findHistory.getYawGraph());
    }

    //세션의 호스트 히스토리 열람
    public List<HostHistoryDto> getHostHistory(Long sessionId){

        Session findSession = sessionRepository.findOne(sessionId);
        List<History> findHistoryList = historyRepository.findAllInSession(sessionId);

        List<HostHistoryDto> hostHistoryDtoList = new ArrayList<>();

        for (History history : findHistoryList) {
            hostHistoryDtoList.add(new HostHistoryDto(
                    history.getUser().getName(), history.getUser().getEmail(),
                    history.getVibe(), history.getAbsenceTime(), history.isAttendance()));
        }

        return hostHistoryDtoList;
    }

}
