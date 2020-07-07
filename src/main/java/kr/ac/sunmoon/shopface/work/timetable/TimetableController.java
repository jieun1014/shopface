package kr.ac.sunmoon.shopface.work.timetable;

import java.util.List;

import static java.util.stream.Collectors.toList;

import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import kr.ac.sunmoon.shopface.work.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TimetableController {
	private final TimetableService timetableService;
	
	@PostMapping("/timetable")
	public ModelAndView addTimetable(RedirectAttributes redirect, Timetable timetable, Schedule schedule) {
		ModelAndView mav = new ModelAndView(new RedirectView("/timetable"));
		
		boolean result = this.timetableService.addTimetable(timetable, schedule);
		
		return mav;
	}
	
	@GetMapping("/timetable")
	public ModelAndView getTimetable(@RequestParam(value = "result", required = false, defaultValue = "none") String result, @AuthenticationPrincipal User user) {
		ModelAndView mav = new ModelAndView("work/timetable/list");
		
		List<GrantedAuthority> list = user.getAuthorities().stream().collect(toList());
		log.info(list.get(0).getAuthority());
		
		
		return mav;
	}
	
	@GetMapping(value = "/timetable", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<TimetableSchedule> getTimetable(int branchNo) {
		List<TimetableSchedule> timetableSchedule = this.timetableService.getTimetableList(branchNo);
		log.info(timetableSchedule.get(0).getTimetable().getOccupName());
		
		return timetableSchedule;
	}

	@PutMapping("/timetable")
	public ModelAndView editTimetable(RedirectAttributes redirect, Timetable timetable, Schedule schedule) {
		try {
			boolean result = this.timetableService.editTimetable(timetable, schedule);
			if (result == true) {
				redirect.addAttribute("result", "editSuccess");
			} else {
				redirect.addAttribute("result", "editFail");
			}
		} catch (Exception e) {
			redirect.addAttribute("result", "editFail");
		}
		
		return new ModelAndView(new RedirectView("/timetable"));
	}
	
	@DeleteMapping("/timetable")
	public ModelAndView removeTimetable(RedirectAttributes redirect, Schedule schedule) {
		ModelAndView mav = new ModelAndView(new RedirectView("/timetable"));
		try {
			boolean result = this.timetableService.removeTimetable(schedule);
			if (result == true) {
				mav.addObject("result", "deleteSuccess");
			} else {
				mav.addObject("result", "deleteFail");
			}
			
		} catch(Exception e) {
			mav.addObject("result", "deleteFail");
		}
		
		return mav;
	}
}