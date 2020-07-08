package kr.ac.sunmoon.shopface.common;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import kr.ac.sunmoon.shopface.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
public class CommonController {
	private final CommonService commonService;
	
	@GetMapping("/")
	public ModelAndView index(@AuthenticationPrincipal User user) {
		if (user != null) {
			if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authority -> authority.equals("ROLE_ADMIN"))) {
				return new ModelAndView(new RedirectView("/member"));
			} else if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authority -> authority.equals("ROLE_MEMBER"))) {
				return new ModelAndView(new RedirectView("/schedule"));
			} else if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authority -> authority.equals("ROLE_BUSINESSMAN"))) {
				return new ModelAndView(new RedirectView("/timetable"));
			}
		}
		
		return new ModelAndView(new RedirectView("/login"));
	}
	
	@GetMapping("/login")
	public ModelAndView login(HttpSession httpSession, @AuthenticationPrincipal User user) {
		if (user != null) {
			String authority = user.getAuthorities().iterator().next().getAuthority();
			if ("ROLE_ADMIN".equals(authority)) {
				return new ModelAndView(new RedirectView("/member"));
			} else if ("ROLE_MEMBER".equals(authority)) {
				return new ModelAndView(new RedirectView("/schedule"));
			} else if("ROLE_BUSINESSMAN".equals(authority)) {
				return new ModelAndView(new RedirectView("/timetable"));
			}
		}
		
		return new ModelAndView("common/login");
	}
	
	@GetMapping("/forgotpassword")
	public ModelAndView forgotPassword() {
		return new ModelAndView("common/forgotPassword");
	}
	
	@PostMapping("/forgotpassword")
	public ModelAndView forgotPassword(Member member) {
		return null;
	}
	
	
	@PostMapping("/login")
	public ModelAndView login(Member member, HttpSession httpSession
			, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		
		if(commonService.login(member)) {
//			modelAndView.setView(new RedirectView("/member"));
//			
//			httpSession.setAttribute("loginUser", member.getId());
		} else {
			modelAndView.setView(new RedirectView("/login"));
			redirectAttributes.addFlashAttribute("message", "로그인 정보가 일치하지 않습니다.");
		}
		
		return modelAndView;
	}
	
	@GetMapping("/logout")
	public ModelAndView logout(HttpSession httpSession) {
		if (httpSession.getAttribute("loginUser") != null) {
			httpSession.invalidate();
		}
		
		return new ModelAndView(new RedirectView("/login"));
	}
}
