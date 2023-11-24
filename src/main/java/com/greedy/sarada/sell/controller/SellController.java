package com.greedy.sarada.sell.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;
import com.greedy.sarada.user.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sell")
public class SellController {
	
	@Value("${image.image-dir}")
	private String IMAGE_DIR;
	
	private final SellService sellService;
	private final MessageSourceAccessor messageSourceAccessor;

	public SellController(SellService sellService, MessageSourceAccessor messageSourceAccessor) {
		this.sellService = sellService;
		this.messageSourceAccessor = messageSourceAccessor;
	}

	/* 사업자 등록 페이지 */
	@GetMapping("/sellRegist")
	public String sellSellRegist() {

		return "user/sell/sellRegist";
	}

	@GetMapping(value = "/RefCategory", produces = "application/json; charset=UTF-8")
	public @ResponseBody List<RefCategoryDto> findRefCategoryList() {

		return sellService.findAllRefCategory();
	}

	/* 판매자 등록 */
	@PostMapping("/Regist")
	public String sellRegist(@ModelAttribute SellDto sellUser, @RequestParam String zipCode,
			@RequestParam String address1, @RequestParam String address2, @AuthenticationPrincipal UserDto loginUser,
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

	@GetMapping("/manage")
	public String sellManage() {

		return "user/sell/sellManage";
	}

	@GetMapping("/addProduct")
	public String addProductMove() {

		return "user/sell/addProduct";
	}

	@PostMapping("/addProduct")
	public String addProduct(@RequestParam("attachImageMain") List<MultipartFile> attachImageMain,
			HttpServletRequest request, ListDto list, PtDto pt, @AuthenticationPrincipal UserDto user,
			RedirectAttributes rttr) {

		
		String fileUploadDir = IMAGE_DIR + "original";
		String mainImage = IMAGE_DIR + "mainImage";
		
		File dir1 = new File(fileUploadDir);
		File dir2 = new File(mainImage);
		
		log.info("[ThumbnailController] dir1 : {}", dir1);
		log.info("[ThumbnailController] dir2 : {}", dir2);
		
		/* 디렉토리가 없을 경우 생성한다. */
		if(!dir1.exists() || !dir2.exists()) {
			dir1.mkdirs();
			dir2.mkdirs();
		}
		
		int index = 0;
		Map<String, List<MultipartFile>> dynamicAttachImagesMap = new HashMap<>();

		List<MultipartFile> files;
		while (true) {
			files = ((MultipartHttpServletRequest) request).getFiles("attachImage[" + index + "]");
			log.info("[sellController] request.getFiles: \"attachImage[\" + index + \"]\" {}", ((MultipartHttpServletRequest) request).getFiles("attachImage[" + index + "]"));
			if (files.isEmpty()) {
				break;
			}

			dynamicAttachImagesMap.put("attachImage[" + index + "]", files);
			index++;
		}
		
		log.info("[sellController] dynamicAttachImages{}", dynamicAttachImagesMap);
		log.info("[sellController] ListDto{}", list);
		log.info("[sellController] attachImageMain{}", attachImageMain);

		return "redirect:/";
	}
}
