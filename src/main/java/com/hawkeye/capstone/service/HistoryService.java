package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.GroupRole;
import com.hawkeye.capstone.domain.History;
import com.hawkeye.capstone.domain.Session;
import com.hawkeye.capstone.dto.GuestHistoryDto;
import com.hawkeye.capstone.dto.HostHistoryDto;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final SessionRepository sessionRepository;

    public History findOne(Long historyId){
        return historyRepository.findOne(historyId);
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
