package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.*;
import com.hawkeye.capstone.repository.HistoryRepository;
import com.hawkeye.capstone.repository.SessionRepository;
import com.hawkeye.capstone.repository.TimeLineLogRepository;
import com.hawkeye.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
    private final TimeLineLogRepository timeLineLogRepository;

    public History findOne(Long historyId) {
        return historyRepository.findOne(historyId);
    }

    //히스토리 생성 및 변경
    public Long createOrUpdateHistory(Long userId, Long sessionId,
                                      int attendanceCount, int attitude, int vibe, boolean isAttend,
                                      List<TimeLineLog> timeLineLogList, RollGraph roll,
                                      YawGraph yaw) {

        User findUser = userRepository.findOne(userId);
        Session findSession = sessionRepository.findOne(sessionId);

        //히스토리 처음 생성
        if (findUser.getHistoryList().isEmpty() ||
                findUser.getHistoryList().get(findUser.getHistoryList().size() - 1).getSession().getId() != sessionId) {

            History history = History.builder()
                    .user(findUser)
                    .session(findSession)
                    .createdAt(LocalDateTime.now())
                    .isAttend(isAttend)
                    .attendanceCount(attendanceCount)
                    .attitude(attitude)
                    .vibe(vibe)
                    .timeLineLogList(timeLineLogList)
                    .rollGraph(roll)
                    .yawGraph(yaw)
                    .build();


            historyRepository.save(history);
            findUser.getHistoryList().add(history);

            for (TimeLineLog timeLineLog : timeLineLogList) {
                timeLineLog.setHistory(history);
                timeLineLogRepository.save(timeLineLog);
            }

            return history.getId();
        }

        //히스토리 변경
        else {

            History findHistory = historyRepository.findOneGuestInSession(userId, sessionId);

            Duration startBetweenLast = Duration.between(findHistory.getSession().getStartTime(), findHistory.getCreatedAt());
            Duration lastBetweenNew = Duration.between(findHistory.getCreatedAt(), LocalDateTime.now());

            long startToLastSeconds = startBetweenLast.getSeconds();
            long lastToNewSeconds = lastBetweenNew.getSeconds();

            int newAttitude = (int) ((findHistory.getAttitude() * startToLastSeconds
                    + attitude * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds));

//            int newVibe = (int) ((findHistory.getVibe() * startToLastSeconds
//                    + vibe * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds));

            RollGraph newRollGraph = new RollGraph(
                    (int) ((findHistory.getRollGraph().getRollLeft() * startToLastSeconds
                            + roll.getRollLeft() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getRollGraph().getRollNormal() * startToLastSeconds
                            + roll.getRollNormal() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getRollGraph().getRollRight() * startToLastSeconds
                            + roll.getRollRight() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds))
            );

            YawGraph newYawGraph = new YawGraph(
                    (int) ((findHistory.getYawGraph().getYawLeft() * startToLastSeconds
                            + yaw.getYawLeft() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getYawGraph().getYawNormal() * startToLastSeconds
                            + yaw.getYawNormal() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getYawGraph().getYawRight() * startToLastSeconds
                            + yaw.getYawRight() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds))
            );

            List<TimeLineLog> newTimeLineLogList = new ArrayList<>();
            timeLineLogList.addAll(findHistory.getTimeLineLogList());
            timeLineLogList.addAll(timeLineLogList);

            for (TimeLineLog timeLineLog : timeLineLogList) {
                timeLineLog.setHistory(findHistory);
                timeLineLogRepository.save(timeLineLog);
            }

            findHistory.setAttitude(newAttitude);
            findHistory.setCreatedAt(LocalDateTime.now());
            findHistory.setRollGraph(newRollGraph);
            findHistory.setYawGraph(newYawGraph);
            findHistory.setTimeLineLogList(newTimeLineLogList);

            return findHistory.getId();
        }

    }



    //히스토리 전체 열람
    public List<HistoryDto> getHistory(Long userId) {

        List<History> findHistoryList = historyRepository.findAll();

        List<HistoryDto> historyDtoList = new ArrayList<>();

        for (History history : findHistoryList) {

            //HOST가 호출한 경우
            if (userId == history.getSession().getGroup().getHostId()) {

                List<HistoryGroupMemberDto> historyGroupMemberDtoList = new ArrayList<>();

                List<History> historyListInSession =
                        historyRepository.findAllGuestsInSession(history.getSession().getId());

                for (History history1 : historyListInSession) {

                    historyGroupMemberDtoList.add(new HistoryGroupMemberDto(
                            history1.getSession().getGroup().getName(),
                            history1.getUser().getEmail(),
                            history1.getAttitude(),
                            getAbsenceTime(history1.getId()),
                            history1.isAttend()
                    ));

                }
                historyDtoList.add(new HistoryDto(GroupRole.HOST, history.getId(),
                        history.getSession().getGroup().getName(), history.getCreatedAt().getYear(),
                        history.getCreatedAt().getMonthValue(), history.getCreatedAt().getDayOfMonth(),
                        history.getAttendanceCount(), history.getVibe(), historyGroupMemberDtoList));

                break;
            }

            //GUEST가 호출한 경우
            else {
                historyDtoList.add(new HistoryDto(GroupRole.GUEST, history.getId(),
                        history.getSession().getGroup().getName(), history.getCreatedAt().getYear(),
                        history.getCreatedAt().getMonthValue(), history.getCreatedAt().getDayOfMonth(),
                        history.getAttendanceCount(), history.getVibe(), history.getAttitude(), history.isAttend(),
                        timeLineLogRepository.findByHistory(history.getId()), history.getRollGraph(), history.getYawGraph()));
            }
        }
        return historyDtoList;
    }

    //자리비움 시간 계산
    public int getAbsenceTime(Long historyId) {
        List<TimeLineLog> findLogList = timeLineLogRepository.findByHistory(historyId);
        int time = 0;

        for (TimeLineLog timeLineLog : findLogList) {

            if (timeLineLog.getState().equals("absence")) {

                time += timeLineLog.getEndHour() - timeLineLog.getStartHour() * 60;
                time += timeLineLog.getEndMinute() - timeLineLog.getStartMinute();
                if (timeLineLog.getEndSecond() - timeLineLog.getStartSecond() >= 30)
                    time += 1;

            }

        }
        return time;
    }

    //세션의 게스트 히스토리 열람
    public GuestHistoryDto getGuestHistory(Long userId, Long sessionId) {

        Session findSession = sessionRepository.findOne(sessionId);
        History findHistory = historyRepository.findByGuestInSession(userId, sessionId);

        return new GuestHistoryDto(findHistory.getId(), GroupRole.GUEST, findSession.getGroup().getName(), findSession.getStartTime(),
                findHistory.getAttendanceCount(), findHistory.getVibe(), findHistory.isAttend(), findHistory.getTimeLineLogList(),
                findHistory.getRollGraph(), findHistory.getYawGraph());
    }

    //세션의 호스트 히스토리 열람
    public List<HostHistoryDto> getHostHistory(Long sessionId) {

        Session findSession = sessionRepository.findOne(sessionId);
        List<History> findHistoryList = historyRepository.findAllGuestsInSession(sessionId);

        List<HostHistoryDto> hostHistoryDtoList = new ArrayList<>();

        for (History history : findHistoryList) {
            hostHistoryDtoList.add(new HostHistoryDto(
                    history.getId(), history.getUser().getName(), history.getUser().getEmail(),
                    history.getVibe(), getAbsenceTime(history.getId()), history.isAttend()));
        }

        return hostHistoryDtoList;
    }

    //최근 10개 세션의 히스토리 평균
//    public RecentTrendDto getGuestRecentTrend(Long userId){
//
//        User findUser = userRepository.findOne(userId);
//    }

}
