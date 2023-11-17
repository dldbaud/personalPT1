package com.greedy.sarada.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greedy.sarada.admin.service.AdminService;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;

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
	public String sellReqDetail(@RequestParam Long sellNo, Model model) {
		
		return "admin/seller/SellReqDetail";
	}
}
