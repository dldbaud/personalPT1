package com.greedy.sarada.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greedy.sarada.admin.service.AdminService;
import com.greedy.sarada.common.paging.ResponseDto;
import com.greedy.sarada.sell.dto.FileDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
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
	public String adminPage(Model model) {
		
		return "admin/admin/main";
	}
	
	@GetMapping("/sellerManage")
	public String sellerManage(Model model) {
		
		List<SellDto> sellRegistList = adminService.findSellRegistList();
		
		model.addAttribute("sellList", sellRegistList);
		
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
		
		log.info("[AdminController] boardListAndPaging : {}", boardListAndPaging);
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
    public ResponseEntity<String> insertSeller(@RequestBody SellDto seller){
    	
    	String result = "승인 실패";
    	log.info("[AdminController] Request Check seller : {}", seller);
    	log.info("[AdminController] seller.user.userNo Check seller : {}", seller.getUser().getUserNo());
    	
    	UserDto user = new UserDto();
    	user.setUserNo(seller.getUser().getUserNo());
    	seller.setUser(user);
    	seller.setSellStatus("승인");
    	log.info("[AdminController] user.setUserNo(seller.getUser().getUserNo()) : {}", user.getUserNo());
    	if(adminService.insertSeller(seller) == 1) {
    		result = "등록 승인";
    	};
    	
    	return ResponseEntity.ok(result);
    }
    
    @PostMapping("/sellReject")
    public ResponseEntity<String> rejectSeller(@RequestBody SellDto seller){
    	
    	String result = "거절 실패";
    	log.info("[AdminController] Request Check seller : {}", seller);
    	seller.setSellStatus("거절");
    	
    	if(adminService.rejectSeller(seller) == 1) {
    		result = "거절 완료";
    	};
    	
    	return ResponseEntity.ok(result);
    }
    

    
}