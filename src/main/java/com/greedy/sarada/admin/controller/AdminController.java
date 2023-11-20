package com.greedy.sarada.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greedy.sarada.admin.service.AdminService;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;
import com.greedy.sarada.user.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping("/main")
	public String adminPage() {
		
		return "admin/admin/main";
	}
	
	@GetMapping("/sellerManage")
	public String sellerManage(Model model) {
		
		List<SellDto> sellRegistList = adminService.findSellRegistList();
		
		model.addAttribute("sellRegistList", sellRegistList);
		
		log.info("[AdminController] sellRegistList{}", sellRegistList);
		
		return "admin/seller/manage";
	}
	
	@GetMapping("/sellSearchList")
	public String sellSearchList(@RequestParam(defaultValue="1") int page, 
			@RequestParam(required=false) String searchCondition, 
			@RequestParam(required=false) String searchValue,
			Model model) {
		Map<String, String> searchMap = new HashMap<>();
		searchMap.put("searchCondition", searchCondition);
		searchMap.put("searchValue", searchValue);
		
		Map<String, Object> boardListAndPaging = adminService.selectSellList(searchMap, page);
		
		model.addAttribute("paging", boardListAndPaging.get("paging"));
		model.addAttribute("sellList", boardListAndPaging.get("sellList"));
		
		log.info("[AdminController] searchMap : {}", searchMap);
		return "admin/seller/manage";
	}
	
	@GetMapping("/reqDetail")
	public String sellReqDetail(@RequestParam String sellNo, Model model) {
		
		log.info("[AdminController] sellDetail sellNo {}", sellNo);
		SellDto sellDetail = adminService.selectSellDetail(sellNo);
		
		String[] address = sellDetail.getSellAddress().split("\\$");
		
		model.addAttribute("sellDetail", sellDetail);
		model.addAttribute("address", address);
		
		
		log.info("[AdminController] sellDetail{}", sellDetail);
		
		return "admin/seller/SellReqDetail";
	}
	
    @PostMapping("/sellRegist")
    public ResponseEntity<String> checkDuplication(@RequestBody SellDto seller){
    	
    	String result = "사용 가능한 아이디입니다.";
    	log.info("[MemberController] Request Check ID : {}", seller);
    	
//    	if(userService.selectUserById(user.getId())) {
//    		log.info("[UserController] Already Exist");
//    		result = "중복 된 아이디가 존재합니다.";
//    	}    	
    	
    	return ResponseEntity.ok(result);
    }
}
