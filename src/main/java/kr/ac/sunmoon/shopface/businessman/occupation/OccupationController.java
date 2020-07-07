package kr.ac.sunmoon.shopface.businessman.occupation;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OccupationController {
    private final OccupationService occupationService;
    
    @PostMapping("/occupation")
    public ModelAndView addOccupation(Occupation occupation) {
        boolean isSuccess = occupationService.addOccupation(occupation);
        
        return new ModelAndView("redirect:/occupation/" + occupation.getBranchNo());
    }
    
    @GetMapping("/occupation/{branchNo}")
    public ModelAndView getOccupationList(@PathVariable int branchNo) {
        return new ModelAndView("businessman/occupation/list.html");
    }
    
    @GetMapping(value = "/occupation/{branchNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Occupation> getOccupationList(@PathVariable int branchNo, Occupation occupation) {
        return occupationService.getOccupationList(occupation);
    }
    
    @PutMapping(value = "/occupation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> editOccupation(@RequestBody Occupation occupation) {
         boolean isSuccess = occupationService.editOccupation(occupation);
        
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("isSuccess", isSuccess);
        
        return responseMap;
    }
    
    @DeleteMapping(value = "/occupation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteOccupation(@RequestBody Occupation occupation) {
        boolean isSuccess = occupationService.removeOccupation(occupation);
        
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("isSuccess", isSuccess);
        
        return responseMap;
    }
}