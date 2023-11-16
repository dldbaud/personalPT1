package com.greedy.sarada.sell.controller;

import java.util.List;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;
import com.greedy.sarada.user.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sell")
public class SellController {
	
	private final SellService sellService;
	private final MessageSourceAccessor messageSourceAccessor;
	
	public SellController(SellService sellService, MessageSourceAccessor messageSourceAccessor) {
		this.sellService = sellService;
		this.messageSourceAccessor = messageSourceAccessor;
	}
	
	/* 사업자 등록 페이지*/
    @GetMapping("/sellRegist")
    public String sellSellRegist() {
    		
    	return "user/sell/sellRegist";
    }
    
	@GetMapping(value = "/RefCategory", produces = "application/json; charset=UTF-8")
	public @ResponseBody List<RefCategoryDto> findRefCategoryList() {
		
		
		return sellService.findAllRefCategory();
	}

    /*판매자 등록*/
    @PostMapping("/Regist")
    public String sellRegist(@ModelAttribute SellDto sellUser,
    		@RequestParam String zipCode, @RequestParam String address1, @RequestParam String address2,
    		@AuthenticationPrincipal UserDto loginUser,
    		RedirectAttributes rttr) throws SellRegistException {
    	
    	String address = zipCode + "$" + address1 + "$" + address2;
    	String status = "대기";
    	sellUser.setSellAddress(address);
    	sellUser.setSellStatus(status);
    	sellUser.setUser(loginUser);
    	
    	log.info("[sellController] regist request sell : {}", sellUser);
    	
    	
    	sellService.registUser(sellUser);

    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("sell.regist"));
    	
    	return "redirect:/";
    }
}
