package kr.ac.sunmoon.shopface.businessman.branch;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BranchController {
	private final BranchService branchService;

	/**
	 * 지점 등록 폼
	 */
	@GetMapping("/branch/form")
	public ModelAndView addBranchForm() {
		ModelAndView mav = new ModelAndView("businessman/branch/add");
		return mav;
	}

	/**
	 * 지점 등록
	 */
	@PostMapping("/branch")
	public ModelAndView addBranch(RedirectAttributes redirect, Branch branch) {
		ModelAndView mav = new ModelAndView(new RedirectView("/branch"));
		try {
			boolean result = this.branchService.addBranch(branch);
			if (result == true) {
				redirect.addAttribute("result", "addSuccess");
			} else {
				redirect.addAttribute("result", "addFail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addAttribute("result", "addFail");
		} finally {
			return mav;
		}
	}

	/**
	 * 지점 목록 조회 폼
	 **/
	@GetMapping(value = "/branch")
	public ModelAndView getBranchList(
			@RequestParam(value = "result", required = false, defaultValue = "none") String result) {
		ModelAndView mav = new ModelAndView("businessman/branch/list");
		mav.addObject("result", result);
		return mav;
	}

	/**
	 * 지점 목록 조회
	 */
	@GetMapping(value = "/branch", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Branch> getBranchList(Branch branch) {
		List<Branch> branches = new ArrayList<Branch>();
		try {
			branches = this.branchService.getBranchList(branch);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return branches;
		}
	}

	/**
	 * 지점 조회
	 */
	@GetMapping(value = "/branch/{no}")
	public ModelAndView getBranch(
			@RequestParam(value = "result", required = false, defaultValue = "none") String result,
			@PathVariable(value = "no") int no) {
		ModelAndView mav = new ModelAndView("businessman/branch/detail");
		try {
			Branch branch = this.branchService.getBranch(no);
			mav.addObject("branch", branch);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("branch", new Branch());
		} finally {
			return mav;
		}
	}

	/**
	 * 지점 수정
	 */
	@PutMapping("/branch/{no}")
	public ModelAndView editBranch(Branch branch, MultipartFile licenseImage, RedirectAttributes redirect) {
		try {
			boolean result = this.branchService.editBranch(branch, licenseImage);
			if (result == true) {
				redirect.addAttribute("result", "editSuccess");
			} else {
				redirect.addAttribute("result", "editFail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addAttribute("result", "editFail");
		}

		return new ModelAndView(new RedirectView("/branch"));
	}

	/**
	 * 지점 삭제
	 */
	@DeleteMapping("/branch/{no}")
	public ModelAndView removeBranch(Branch branch) {
		ModelAndView mav = new ModelAndView(new RedirectView("/branch"));
		try {
			boolean result = this.branchService.removeBranch(branch);
			if (result == true) {
				mav.addObject("result", "deleteSuccess");
			} else {
				mav.addObject("result", "deleteFail");
			}

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("result", "deleteFail");
		}

		return mav;
	}
	
	@PutMapping(value = "/branch")
	public Map<String, Object> changeApprovalStatus(@RequestBody Branch branch) throws IOException {
		log.info("" + branch.getApprovalStatus());
		
		Map<String, Object> result = new HashMap<>();
		if (branchService.editBranch(branch, null)) {
			result.put("isSuccess", true);
		} else {
			result.put("isSuccess", false);
		}
		
		return result;
	}
}
