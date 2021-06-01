package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.History;
import com.hawkeye.capstone.domain.Session;
import com.hawkeye.capstone.domain.TimeLineLog;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final GroupService groupService;
    private final HistoryRepository historyRepository;

    public Session findOne(Long id) {
        return sessionRepository.findOne(id);
    }

    @Transactional
    public Long createSession(Long groupId) {

        Group group = groupService.findOne(groupId);
        Session session = new Session();
        session.setStartTime(LocalDateTime.now());
        session.setGroup(group);
        group.setOnAir(true);
        sessionRepository.save(session);

        return session.getId();
    }

    @Transactional
    public Long endSession(Long sessionId) {

        Session session = sessionRepository.findOne(sessionId);
        LocalDateTime endTime = LocalDateTime.now();
        session.setEndTime(endTime);
        //변경 감지
        Group group = groupService.findOne(session.getGroup().getId());
        group.setOnAir(false);
        //수업이 끝나도 자리 비움 상태인 히스토리 로그 수정
        List<History> findHistoryList = historyRepository.findAllGuestsInSession(sessionId);
        for (History history : findHistoryList) {
            //히스토리 중 마지막 로그
            TimeLineLog findTimeLineLog = history.getTimeLineLogList().get(history.getTimeLineLogList().size() - 1);
            if(findTimeLineLog.isEnd() == false){
                Duration between = Duration.between(session.getStartTime(), endTime);

                int hour = (int)between.getSeconds() / 3600;
                int minute = (int)(between.getSeconds() - 3600 * hour) / 60;
                int second = (int)between.getSeconds() - 3600 * hour - 60 * minute;

                findTimeLineLog.setEnd(true);
                findTimeLineLog.setEndHour(hour);
                findTimeLineLog.setEndMinute(minute);
                findTimeLineLog.setEndSecond(second);
            }
        }

        return session.getId();
    }

    @Transactional
    public boolean isOnAir(Long sessionId) {
        if (findOne(sessionId).getGroup().isOnAir())
            return true;
        else
            return false;
    }
}
