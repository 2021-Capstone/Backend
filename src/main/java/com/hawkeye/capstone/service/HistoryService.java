package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.*;
import com.hawkeye.capstone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final GroupService groupService;
    private final GroupRepository groupRepository;

    public History findOne(Long historyId) {
        return historyRepository.findOne(historyId);
    }

    //히스토리 생성 알고리즘
    public Long createRequest(Long userId, Long sessionId, float pitch, float yaw, boolean absence) {
        User findUser = userRepository.findOne(userId);
        History findHistory = historyRepository.findOneGuestInSession(userId, sessionId);
        Session findSession = sessionRepository.findOne(sessionId);

        LocalDateTime sessionStartTime = findSession.getStartTime();

        PitchGraph pitchGraph = calculatePitch(pitch);
        YawGraph yawGraph = calculateYaw(yaw);
        int attitude = calculateAttitude(pitch);
        //자리를 비운 경우
        if (absence) {
            //새로운 TimeLineLog 생성
            if (findHistory == null || findHistory.getTimeLineLogList().isEmpty() ||
                    findHistory.getTimeLineLogList().get(findHistory.getTimeLineLogList().size() - 1).isEnd() == true) {

                TimeLineLog timeLineLog = new TimeLineLog();

                LocalDateTime startTime = LocalDateTime.now();

                Duration between = Duration.between(sessionStartTime, startTime);

                int hour = (int)between.getSeconds() / 3600;
                int minute = (int)(between.getSeconds() - 3600 * hour) / 60;
                int second = (int)between.getSeconds() - 3600 * hour - 60 * minute;

                timeLineLog.setStartHour(hour);
                timeLineLog.setStartMinute(minute);
                timeLineLog.setStartSecond(second);

                timeLineLog.setState("absence");
                timeLineLogRepository.save(timeLineLog);

                return createOrUpdateHistory(userId, sessionId, attitude, absence, timeLineLog, pitchGraph, yawGraph);
            }

        }

        //자리를 비우지 않은 경우
        else {
            //히스토리 첫 생성
            if (findHistory == null) ;

                //자리를 비웠다가 돌아온 경우
            else if (!findHistory.getTimeLineLogList().isEmpty() && findHistory.getTimeLineLogList().get(findHistory.getTimeLineLogList().size() - 1).isEnd() == false) {
                Long timeLineLogId = findHistory.getTimeLineLogList().get(findHistory.getTimeLineLogList().size() - 1).getId();
                //변경 감지
                TimeLineLog findTimeLineLog = timeLineLogRepository.findOne(timeLineLogId);
                LocalDateTime endTime = LocalDateTime.now();

                Duration between = Duration.between(sessionStartTime, endTime);

                int hour = (int)between.getSeconds() / 3600;
                int minute = (int)(between.getSeconds() - 3600 * hour) / 60;
                int second = (int)between.getSeconds() - 3600 * hour - 60 * minute;

                findTimeLineLog.setEndHour(hour);
                findTimeLineLog.setEndMinute(minute);
                findTimeLineLog.setEndSecond(second);

                findTimeLineLog.setEnd(true);
            }

        }
        return createOrUpdateHistory(userId, sessionId, attitude, absence, null, pitchGraph, yawGraph);
    }

    //히스토리 생성 및 변경
    public Long createOrUpdateHistory(Long userId, Long sessionId, int attitude, boolean absence,
                                      TimeLineLog timeLineLog, PitchGraph pitch, YawGraph yaw) {

        User findUser = userRepository.findOne(userId);
        Session findSession = sessionRepository.findOne(sessionId);

        //히스토리 처음 생성
        if (findUser.getHistoryList().isEmpty() ||
                findUser.getHistoryList().get(findUser.getHistoryList().size() - 1).getSession().getId() != sessionId) {

            List<TimeLineLog> timeLineLogList = new ArrayList<>();
            if (timeLineLog != null)
                timeLineLogList.add(timeLineLog);

            History history = History.builder()
                    .user(findUser)
                    .session(findSession)
                    .createdAt(LocalDateTime.now())
                    .isAttend(true)
                    .attendanceCount(groupService.getGroupMemberCount(findSession.getGroup().getId()))
                    .attitude(attitude)
                    .vibe(3)
                    .timeLineLogList(timeLineLogList)
                    .pitchGraph(pitch)
                    .yawGraph(yaw)
                    .build();


            historyRepository.save(history);
            findUser.getHistoryList().add(history);

            if (timeLineLog != null) {
                //변경 감지
                TimeLineLog findTimeLineLog = timeLineLogRepository.findOne(timeLineLog.getId());
                findTimeLineLog.setHistory(history);
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

//            int newAttitude = (int) ((findHistory.getAttitude() * startToLastSeconds
//                    + attitude * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds));
            float newAttitude = ((findHistory.getAttitude() * startToLastSeconds
                    + attitude * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds));

//            int newVibe = (int) ((findHistory.getVibe() * startToLastSeconds
//                    + vibe * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds));

            PitchGraph newPitchGraph = new PitchGraph(
                    (int) ((findHistory.getPitchGraph().getPitchUp() * startToLastSeconds
                            + pitch.getPitchUp() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getPitchGraph().getPitchNormal() * startToLastSeconds
                            + pitch.getPitchNormal() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getPitchGraph().getPitchDown() * startToLastSeconds
                            + pitch.getPitchDown() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds))
            );

            YawGraph newYawGraph = new YawGraph(
                    (int) ((findHistory.getYawGraph().getYawLeft() * startToLastSeconds
                            + yaw.getYawLeft() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getYawGraph().getYawNormal() * startToLastSeconds
                            + yaw.getYawNormal() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds)),
                    (int) ((findHistory.getYawGraph().getYawRight() * startToLastSeconds
                            + yaw.getYawRight() * lastToNewSeconds) / (startToLastSeconds + lastToNewSeconds))
            );


            List<TimeLineLog> newTimeLineLogList = findHistory.getTimeLineLogList();

            if (timeLineLog != null) {
                timeLineLog.setHistory(findHistory);
                newTimeLineLogList.add(timeLineLog);
            }

            //자리를 비운 경우에는 태도, pitch, yaw 업데이트 안함
            if(!absence){
                findHistory.setAttitude(newAttitude);
                findHistory.setPitchGraph(newPitchGraph);
                findHistory.setYawGraph(newYawGraph);
            }
            findHistory.setCreatedAt(LocalDateTime.now());
            findHistory.setTimeLineLogList(newTimeLineLogList);
            findHistory.setAttendanceCount(calculateAttendance(sessionId));
            findHistory.setVibe(calculateVibe(sessionId));
            if(getAbsenceTime(findHistory.getId()) > findSession.getGroup().getAbsenceTime())
                findHistory.setAttend(false);

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
                            history1.getUser().getName(),
                            history1.getUser().getEmail(),
                            (int)history1.getAttitude(),
                            getAbsenceTime(history1.getId()),
                            history1.isAttend()
                    ));

                }
                historyDtoList.add(new HistoryDto(GroupRole.HOST, history.getId(),
                        history.getSession().getGroup().getName(), history.getCreatedAt().getYear(),
                        history.getCreatedAt().getMonthValue(), history.getCreatedAt().getDayOfMonth(),
                        history.getAttendanceCount(), history.getVibe(), historyGroupMemberDtoList));

            }

            //GUEST가 호출한 경우
            else {
                historyDtoList.add(new HistoryDto(GroupRole.GUEST, history.getId(),
                        history.getSession().getGroup().getName(), history.getCreatedAt().getYear(),
                        history.getCreatedAt().getMonthValue(), history.getCreatedAt().getDayOfMonth(),
                        history.getAttendanceCount(), history.getVibe(), (int)history.getAttitude(), history.isAttend(),
                        timeLineLogRepository.findByHistory(history.getId()), history.getPitchGraph(), history.getYawGraph()));
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
                findHistory.getPitchGraph(), findHistory.getYawGraph());
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

    private int calculateAttitude(float pitch) {
        if (pitch >= 10 || pitch < 3)
            return 1;
        else if (pitch > 8 || pitch < 5)
            return 2;
        else
            return 3;
    }

    private PitchGraph calculatePitch(float pitch) {
        if (pitch > 8)
            return new PitchGraph(100, 0, 0);
        else if (pitch < 5)
            return new PitchGraph(0, 0, 100);
        else
            return new PitchGraph(0, 100, 0);
    }

    private YawGraph calculateYaw(float yaw) {
        if (yaw < -3.6)
            return new YawGraph(0, 0, 100);
        else if (yaw > -2)
            return new YawGraph(100, 0, 0);
        else
            return new YawGraph(0, 100, 0);
    }

    private int calculateVibe(Long sessionId) {

        int vibe = 0;
        int memberCount = groupService.getGroupMemberCount(sessionRepository.findOne(sessionId).getGroup().getId());

        List<History> findHistoryList = historyRepository.findAllGuestsInSession(sessionId);
        for (History history : findHistoryList) {
            vibe += history.getAttitude();
        }
        return (int) vibe / memberCount;
    }

    private int calculateAttendance(Long sessionId) {

        int memberCount = 0;

        List<History> findHistoryList = historyRepository.findAllGuestsInSession(sessionId);
        for (History history : findHistoryList) {
            if(history.isAttend())
                memberCount++;
        }

        return memberCount;
    }

    //호스트 최근 동향
    public RecentTrendDto getRecentHost(Long userId) {

        Long findSessionId = null;

        List<Group> findGroupList = groupRepository.findByHost(userId);

        //마지막 Host 수업의 sessionId
        for (Group group : findGroupList) {

            List<Session> findSessionList = group.getSessionList();
            //그룹의 수업이 있으면
            if(!findSessionList.isEmpty()){

                findSessionId = findSessionList.get(0).getId();

                for (Session session : findSessionList) {
                    if (findSessionId < session.getId())
                        findSessionId = session.getId();
                }
            }
        }

        //Host 이력이 없는 경우
        if (findSessionId == null)
            return new RecentTrendDto(GroupRole.HOST, 0, 0);
            //Host 이력이 있는 경우
        else {
            List<History> findHistoryList = historyRepository.findAllGuestsInSession(findSessionId);
            Session findSession = sessionRepository.findOne(findSessionId);
            int totalCount = groupService.getGroupMemberCount(findSession.getGroup().getId());
            int memberCount = findHistoryList.get(0).getAttendanceCount();
            int vibe = findHistoryList.get(0).getVibe();

            return new RecentTrendDto(GroupRole.HOST, (int) memberCount / totalCount * 100,
                    vibe);
        }
    }

    //게스트 최근 동향
    public RecentTrendDto getRecentGuest(Long userId) {

        User findUser = userRepository.findOne(userId);
        List<History> findHistoryList = findUser.getHistoryList();

        //Guest 이력이 없는 경우
        if(findHistoryList.isEmpty())
        {
            return new RecentTrendDto(GroupRole.GUEST, 0, 0);
        }
        int attendCount = 0;
        int totalCount = 0;
        int attitude = 0;

        //히스토리가 10개 미만
        if (findHistoryList.size() <= 10) {
            for (History history : findHistoryList) {
                if (history.isAttend())
                    attendCount++;
                totalCount++;
                attitude += history.getAttitude();
            }
        }

        //히스토리가 10개 이상
        else {
            for (int i = 0; i < 10; i++) {
                if (findHistoryList.get(i).isAttend())
                    attendCount++;
                totalCount++;
                attitude += findHistoryList.get(i).getAttitude();
            }
        }

        return new RecentTrendDto(GroupRole.GUEST, (int) attendCount / totalCount * 100,
                attitude / totalCount);
    }
}
