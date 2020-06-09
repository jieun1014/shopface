package kr.ac.sunmoon.shopface.common;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import kr.ac.sunmoon.shopface.member.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CommonController {
	private final CommonService commonService;
	
	@GetMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("common/login");
	}
	
	/*
	 * 유효성 검사 구현
	 * JWT 가능하면 구현
	 * 로그인 여부 확인 후 로그인 구현
	 * */
	
	@PostMapping("/login")
	public ModelAndView login(Member member, HttpSession httpSession
			, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		
		if(commonService.login(member)) {
			modelAndView.setView(new RedirectView("/member"));
			
			httpSession.setAttribute("loginUser", member.getId());
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
