package com.greedy.sarada;

import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.greedy.sarada.sell.service.SellService;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ControllerAdviceIndex {
	
	private final SellService sellService;
	
	public ControllerAdviceIndex(SellService sellService) {
		this.sellService  = sellService;
		}
	
    @ModelAttribute
    public void globalAttributes(Model model) {
        
    	Map<String, Object> categoryList = sellService.findCategory();


		model.addAttribute("refCategory", categoryList.get("refCategory"));
		model.addAttribute("category", categoryList.get("category"));
  
        log.info("[ControllerAdviceIndex] refCategory{}",categoryList.get("refCategory"));
        
    }
}
