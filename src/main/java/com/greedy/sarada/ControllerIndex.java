package com.greedy.sarada;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greedy.sarada.admin.controller.AdminController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ControllerIndex {
	
	@GetMapping(value = {"/", "/main"})
	public String main(Model model) {
		
        String asd = "페이지 확인";
        log.info(asd);
        
		return "index";
	}

}
