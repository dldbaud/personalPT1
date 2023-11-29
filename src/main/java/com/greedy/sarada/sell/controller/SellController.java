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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.greedy.sarada.user.dto.UserDto;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

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
	
	/*상위카테고리 코드 조회*/
	@GetMapping(value = "/RefCategory", produces = "application/json; charset=UTF-8")
	public @ResponseBody List<RefCategoryDto> findRefCategoryList() {
		
		return sellService.findAllRefCategory();
	}

	@GetMapping(value = "/category/{categoryCode}", produces = "application/json; charset=UTF-8")
	public @ResponseBody List<CategoryDto> findCategoryList(@PathVariable String categoryCode) {
		
		log.info("categoryCode{}", categoryCode);
		
		return sellService.findAllCategoryList(categoryCode);
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

				File deleteFile = new File(attachment.getSavedFileNm() + "/" + attachment.getSavedFileNm());

				deleteFile.delete();
			}
			
			File deleteMainImage = new File(((FileDto) attachImageMain).getMainFilePath() + "/" + ((FileDto) attachImageMain).getMainFilePath());
			deleteMainImage.delete();
			
		}

		return "redirect:/";
	}
}
