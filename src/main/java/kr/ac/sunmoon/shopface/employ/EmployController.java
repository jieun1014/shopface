    package kr.ac.sunmoon.shopface.employ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class EmployController {
    private final EmployService employService;
        
    @PostMapping("/employ")
    public Map<String, Object> addEmploy(Employ employ) {
        boolean isSuccess = employService.addEmploy(employ);
        
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("isSuccess", isSuccess);
        
        return responseMap;
    }
    
    @GetMapping("/employ/{branchNo}")
    public ModelAndView getEmployList(@PathVariable int branchNo) {
        return new ModelAndView("employ/list.html");
    }
    
    @GetMapping(value = "/employ/{branchNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Employ> getEmployList(@PathVariable int branchNo, Employ employ) {
        return employService.getEmployList(employ);
    }
    
    @GetMapping("/employ")
    public ModelAndView getEmploy(Employ employ) {
        ModelAndView modelAndView = new ModelAndView("employ/detail.html");
        modelAndView.addObject("employ", employService.getEmploy(employ));
        
        return modelAndView;
    }
    
    @PutMapping(value = "/employ")
    public ModelAndView editEmploy(Employ employ) {
        boolean isSuccess = employService.editEmploy(employ);
        ModelAndView modelAndView = new ModelAndView("employ/detail.html");
        
        return modelAndView;
    }
    
    @DeleteMapping("/employ")
    public ModelAndView removeEmploy(Employ employ) {
        employService.removeEmploy(employ);
        return new ModelAndView("redirect:/employ/" + employ.getBranchNo());
    }
    
    @PutMapping("/employ/invite")
    public Map<String, Object> resendInviteMessage(@RequestBody Employ employ) {
        boolean isSuccess = employService.resendInviteMessage(employ);
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("isSuccess", isSuccess);
        
        return responseMap;
    }
    
    @GetMapping("/employ/auth")
    public ModelAndView certificationCode(@RequestParam("date") String expiredDate) {
    	ModelAndView modelAndView = new ModelAndView("/member/authenticationCode");
    	modelAndView.addObject("date", expiredDate);
    	
    	return modelAndView;
    }
    
    @GetMapping(value = "/employ/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> checkCertiCode(Employ employ, @RequestParam("expiredDate") String expiredDate) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	String checker = employService.checkCertiCode(employ, expiredDate);
    	result.put("checker", checker);
    	
    	return result;
    }
}