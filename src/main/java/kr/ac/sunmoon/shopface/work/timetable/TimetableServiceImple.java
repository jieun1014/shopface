package kr.ac.sunmoon.shopface.work.timetable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.ac.sunmoon.shopface.work.schedule.Schedule;
import kr.ac.sunmoon.shopface.work.schedule.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Slf4j
@Service
public class TimetableServiceImple implements TimetableService {
	private final TimetableMapper timetableMapper;
	private final ScheduleMapper scheduleMapper;
	
	@Override
	public boolean addTimetable(Timetable timetable, Schedule schedule) {
		boolean isSuccess = false;
		
			if (timetable.getWorkStartTime() != null 
					&& !"".equals(timetable.getWorkStartTime())
					&& timetable.getWorkEndTime() != null
					&& !"".equals(timetable.getWorkEndTime())
					&& timetable.getOccupName() != null
					&& !"".equals(timetable.getOccupName())
					&& timetable.getOccupColor() != null
					&& !"".equals(timetable.getOccupColor())
					&& schedule.getMemberId() != null
					&& !"".equals(schedule.getMemberId())) {
				List<Timetable> timetables = this.timetableMapper.selectAll(timetable);
				
				if (timetables.size() == 0) {
					timetableMapper.insert(timetable);
					
					schedule.setTimetableNo(timetableMapper.selectAll(timetable).get(0).getNo());
					schedule.setState('R');
					scheduleMapper.insert(schedule);
					
					isSuccess = true;
				} 
			}
		return isSuccess;
	}

	@Override
	public List<TimetableSchedule> getTimetableList(int branchNo) {
		List<TimetableSchedule> timetableSchedules = new ArrayList<TimetableSchedule>();
		try {
			if (branchNo > 0) { 
				Timetable timetable = new Timetable();
				timetable.setBranchNo(branchNo);
				List<Timetable> timetables = this.timetableMapper.selectAll(timetable);
				if (timetables.size() > 0) {
					for (int i = 0; i < timetables.size(); i++) {
						int no = timetables.get(i).getNo();
						
						Schedule parameterSchedule = new Schedule();
						parameterSchedule.setTimetableNo(no);
						parameterSchedule.setBranchNo(branchNo);
						
						List<Schedule> schedules = this.scheduleMapper.selectAll(parameterSchedule);
						for (int j = 0; j < schedules.size(); j++) {
							
							timetableSchedules.add(new TimetableSchedule(timetables.get(i), schedules.get(j)));
						}
					}
					return timetableSchedules;
				} else {
					return timetableSchedules;
				}
			} else {
				return timetableSchedules;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return timetableSchedules;
		} 
	}

	@Override
	public boolean editTimetable(Timetable timetable, Schedule schedule) {
		//1. 현재 시간보다 근무 시작 시간이 지났는지 확인
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YY-MM-DD HH24:MI:SS");
			
			Date currentTime = new Date();
			String current = dateFormat.format(currentTime);
			
			Date today = dateFormat.parse(current);
			
			String startTime = timetable.getWorkStartTime();
			Date workStartTime = dateFormat.parse(startTime);
			
			int compare = today.compareTo(workStartTime);
			if (compare < 0) {
				List<Schedule> schedules = this.scheduleMapper.selectAll(schedule);
				if (schedules != null && schedules.size() == 1) {
					this.timetableMapper.update(timetable);
					
					return true;
				} else if (schedules != null && schedules.size() > 1) {
					this.timetableMapper.insert(timetable);
		
					List<Timetable> result = this.timetableMapper.selectAll(timetable);
					if (result != null && result.size() == 1) {
						schedule.setTimetableNo(result.get(0).getNo());
						this.scheduleMapper.update(schedule);
						
						return true;
					}
					return false;
				}
				return false;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeTimetable(Schedule schedule) {
		try {
			if (schedule.getNo() > 0
					&& schedule.getNo() > 0) {
				List<Schedule> schedules = new ArrayList<Schedule>();
				schedules = this.scheduleMapper.selectAll(schedule);
				if (schedules != null) {
					if (schedules != null && schedules.size() == 1) {
						if (schedules.get(0).getState() == 'R' || schedules.get(0).getState() == 'B') {
							this.scheduleMapper.delete(schedule);
							Schedule parameter = new Schedule();
							parameter.setTimetableNo(schedule.getNo());
							
							List<Schedule> resultSchedules = this.scheduleMapper.selectAll(parameter);
							if (resultSchedules != null) {
								if (resultSchedules.size() > 0) {
									return true;
								}
							} else {
								this.timetableMapper.delete(new Timetable(schedule.getNo()));
								return true;
							}
						} else {
							return false;
						}
					} else {
						return false;
					}					
				}
			}
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
}