package com.greedy.sarada.sell.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.FileDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.ReplyDto;
import com.greedy.sarada.user.dto.UserDto;
import com.greedy.sarada.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/sell")
public class SellController {

	@Value("${image.image-dir}")
	private String IMAGE_DIR;

	private final SellService sellService;
	private final MessageSourceAccessor messageSourceAccessor;
	private final UserService userService;

	public SellController(SellService sellService, MessageSourceAccessor messageSourceAccessor, UserService userService) {
		this.sellService = sellService;
		this.messageSourceAccessor = messageSourceAccessor;
		this.userService = userService;
	}


	@GetMapping("/manage")
	public String sellManage(Model model) {
		
		return "user/sell/sellManage";
	}

	@GetMapping("/addProduct")
	public String addProductMove() {

		return "user/sell/addProduct";
	}

	@PostMapping("/addProduct")
	public String addProduct(@RequestParam("attachImageMain") MultipartFile attachImageMain, HttpServletRequest request,
			ListDto list, @AuthenticationPrincipal UserDto user, RedirectAttributes rttr) {

		String fileUploadDir = IMAGE_DIR + "original";
		String mainImage = IMAGE_DIR + "mainImage";

		File dir1 = new File(fileUploadDir);
		File dir2 = new File(mainImage);

		log.info("[sellController] dir1 : {}", dir1);
		log.info("[sellController] dir2 : {}", dir2);

		/* 디렉토리가 없을 경우 생성한다. */
		if (!dir1.exists() || !dir2.exists()) {
			dir1.mkdirs();
			dir2.mkdirs();
		}

		int index = 0;
		Map<String, List<MultipartFile>> dynamicAttachImagesMap = new HashMap<>();

		List<MultipartFile> files;
		while (true) {
			files = ((MultipartHttpServletRequest) request).getFiles("attachImage[" + index + "]");
			log.info("[sellController] request.getFiles: attachImage[" + index + "] {}",
					((MultipartHttpServletRequest) request).getFiles("attachImage[" + index + "]"));
			if (files.isEmpty()) {
				break;
			}

			dynamicAttachImagesMap.put("attachImage[" + index + "]", files);
			index++;
		}

		log.info("[sellController] dynamicAttachImages{}", dynamicAttachImagesMap);
		log.info("[sellController] ListDto{}", list);
		log.info("[sellController] attachImageMain{}", attachImageMain);

		/* 상품 이미지 리스트 */
		Map<String, List<FileDto>> savedAttachImagesMap = new HashMap<>();
		List<FileDto> savedAttachmentList = new ArrayList<>();

		boolean isFirestList = true;
		boolean isFirstMainImage = true;

		SellDto seller = sellService.selectSeller(user);

		log.info("[sellController] 판매자의 유저정보 확인 selectSellNo{}", seller);

		list.setSell(seller);
		/* 대표이미지 */
		FileDto fileInfo2 = new FileDto();

		if (isFirestList) {
			sellService.insertList(list);
			isFirestList = false;
		}
		try {

			String originalFileName = "";
			log.info("[sellController] originalFileName : {}", originalFileName);

			String ext = "";
			String savedFileName = "";

			for (int i = 0; i < dynamicAttachImagesMap.size(); i++) {
				if (dynamicAttachImagesMap.get("attachImage[" + i + "]").size() > 0) {

					PtDto pt = list.getPtList().get(i);
					List<MultipartFile> fileList = dynamicAttachImagesMap.get("attachImage[" + i + "]");
					
					log.info("[sellController] fileList size: {}", fileList.size());
					log.info("[sellController] Is fileList empty?: {}", fileList.isEmpty());
					
					pt.setSell(seller);
					sellService.insertPt(pt);
					
					savedAttachmentList = new ArrayList<>();

					if (attachImageMain != null && isFirstMainImage) {
						originalFileName = attachImageMain.getOriginalFilename();
						ext = originalFileName.substring(originalFileName.lastIndexOf("."));
						savedFileName = UUID.randomUUID().toString() + ext;

						attachImageMain.transferTo(new File(mainImage + "/" + savedFileName));
						
						log.info("[sellController] originalFileName : {}", originalFileName);
						
						log.info("[sellController] savedFileName : {}", savedFileName);
						fileInfo2.setFileType("대표이미지");
						fileInfo2.setOriginalFileNm(originalFileName);
						fileInfo2.setSavedFileNm(savedFileName);
						fileInfo2.setMainFilePath("/upload/mainImage/");
						/* 원래는 fileInfo2.setThumbnailPath("/upload/thumbnail/thumbnail_" + savedFileName) 이런식으로도 가능*/

						list.setFileMain(fileInfo2);
						/* 메인 이미지 및 판매 등록 */
						sellService.registSellMainImage(list);

						isFirstMainImage = false;
					}
					
					for (MultipartFile file : fileList) {
						
						/* 빈 파일 정보 확인 */
						if (!file.isEmpty()) {
							log.info("[sellController] file : {}", file);
							originalFileName = file.getOriginalFilename();
							log.info("[sellController] originalFileName : {}", originalFileName);
							
							ext = originalFileName.substring(originalFileName.lastIndexOf("."));
							savedFileName = UUID.randomUUID().toString() + ext;
							
							log.info("[sellController] savedFileName : {}", savedFileName);
							
							/* 서버의 설정 디렉토리에 파일 저장하기 */
							file.transferTo(new File(fileUploadDir + "/" + savedFileName));
							
							/* DB에 저장할 파일의 정보 처리 */
							FileDto fileInfo = new FileDto();
							fileInfo.setFileType("상품");
							fileInfo.setOriginalFileNm(originalFileName);
							fileInfo.setSavedFileNm(savedFileName);
							fileInfo.setFilePath("/upload/original/");
							
							savedAttachmentList.add(fileInfo);							
							
						}
													
					}
					list.setFileImageList(savedAttachmentList);

					/* 이미지 및 상품 */
					sellService.insertProdectFileList(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			/* 실패 시 이미 저장 된 파일을 삭제한다. */
			for (FileDto attachment : savedAttachmentList) {

				File deleteFile = new File(attachment.getFilePath() + "/" + attachment.getSavedFileNm());
				File deleteMainImage = new File(((FileDto) attachImageMain).getMainFilePath() + "/" + ((FileDto) attachImageMain).getSavedFileNm());
				deleteFile.delete();
			}
			
			File deleteMainImage = new File(((FileDto) attachImageMain).getMainFilePath() + "/" + ((FileDto) attachImageMain).getSavedFileNm());
			deleteMainImage.delete();
			
		}

		return "redirect:/";
	}
	
//    @GetMapping("/productDetail")
//    public String productDetail(@RequestParam String listNo, Model model) {
//    	
//    	Map<String, Object> productDetails = sellService.productDetails(listNo);
//    	
//    	log.info(listNo);
//    	
//    	model.addAttribute("sList", productDetails.get("sList"));
//    	model.addAttribute("sPtList", productDetails.get("sPtList"));
//    	model.addAttribute("sFilelist", productDetails.get("sFilelist"));
//  	
//    	log.info("[sellController] productDetail{}", productDetails);
//    	return "user/pay/productDetail";
//    }
	
//    @GetMapping("/productDetail")
//    public String productDetail(@RequestParam String listNo, 
//    		@RequestParam(defaultValue="1") int page,
//    		@AuthenticationPrincipal UserDto user,
//    		Model model) {
//    	
//    	ReplyDto loadReply = new ReplyDto();
//    	loadReply.setRefListNo(listNo);
//    	
//    	log.info(listNo);
//    	Map<String, Object> productDetails = sellService.productDetails(listNo);
//    	Map<String, Object> replyListAndPaging = userService.selectReplyList(loadReply, page);
//    	model.addAttribute("replylist", replyListAndPaging.get("replyList"));
//    	model.addAttribute("paging", replyListAndPaging.get("paging"));
//    	
//    	if(user != null) {
//    		ReplyDto replyCheck = userService.replyCheck(listNo, user);
//    		OrderDto order = new OrderDto();
//    		order.setUserNo(user.getUserNo());
//    		order.setListNo(listNo);
//    		List<OrderDto> orderCheck = userService.orderCheck(order);
//    		
//    		model.addAttribute("replyCheck", replyCheck);
//    		model.addAttribute("orderCheck", orderCheck);
//    		log.info("[sellController] replyCheck : {}", replyCheck);
//    		log.info("[sellController] orderCheck : {}", orderCheck);
//    	}
//    	
//    	model.addAttribute("sList", productDetails.get("sList"));
//    	model.addAttribute("sPtList", productDetails.get("sPtList"));
//    	model.addAttribute("sFilelist", productDetails.get("sFilelist"));
//    	
//    	log.info("[sellController] productDetail{}", productDetails);
//    	log.info("[sellController] replyListAndPaging : {}", replyListAndPaging);
//    	return "user/pay/productDetail";
//    }
    
    /*상품 관리 페이지*/
    @GetMapping("/manageProducts")
    public String manageProducts(@AuthenticationPrincipal UserDto user, Model model) {
    	
    	List<PtDto> ptLists = sellService.manageProducts(user);
    	
    	log.info(" 셀 컨트롤러 ptLists : {}", ptLists);
    	
    	model.addAttribute("ptLists", ptLists);
    	return "/user/sell/manageProducts";
    }
    
    /* 상품 재고,가격 관리*/
    @GetMapping("/ptManage")
    public String ptManage(String ptNo, String sellNo, Model model) {
    	
    	PtDto ptManage = sellService.ptManage(ptNo,sellNo);
    	
    	model.addAttribute("ptManage" , ptManage);
    	log.info(" 셀 컨트롤러 ptNo : {}", ptNo);
    	log.info(" 셀 컨트롤러 sellNo : {}", sellNo);
    	log.info(" 셀 컨트롤러 ptManage : {}", ptManage);
    	return "/user/sell/ptManage";
    }
    
//    @PostMapping("/ptUpdate")
//    public String ptUpdate(@ModelAttribute PtDto pt) {
//    	
//    	log.info("ptUpdate 확인{}", pt);
//    	
//    	return "redirect:/sell/ptManage?ptNo=" + pt.getPtNo() + "&sellNo=" + pt.getSellNo();
//    }
    
  @PostMapping("/ptUpdate")
  public @ResponseBody String ptUpdate(@RequestBody PtDto pt) {
  	
  	log.info("ptUpdate 확인{}", pt);
  	sellService.ptUpdate(pt);
  	
  	return "sucess";
  }
}
