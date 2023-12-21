package com.greedy.sarada.user.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.common.exception.user.insertOrderException;
import com.greedy.sarada.common.exception.user.insertOrderItemException;
import com.greedy.sarada.common.exception.user.insertPayException;
import com.greedy.sarada.common.paging.ResponseDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.OrderItemDto;
import com.greedy.sarada.user.dto.PayCompleteRequest;
import com.greedy.sarada.user.dto.PayDto;
import com.greedy.sarada.user.dto.UserDto;
import com.greedy.sarada.user.service.AuthenticationService;
import com.greedy.sarada.user.service.UserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
		
		private final PasswordEncoder passwordEncoder;
	    private final MessageSourceAccessor messageSourceAccessor;
	    private final UserService userService;
	    private final AuthenticationService authenticationService;
		
	    public UserController(PasswordEncoder passwordEncoder, MessageSourceAccessor messageSourceAccessor, UserService userService, AuthenticationService authenticationService) {
	    	this.passwordEncoder = passwordEncoder;
	    	this.messageSourceAccessor = messageSourceAccessor;
	    	this.userService = userService;
	    	this.authenticationService = authenticationService;
	    }
	    
	    @GetMapping("/test/login")
	    public @ResponseBody String Testlogin(Authentication authentication
	    		, @AuthenticationPrincipal UserDto userDetails
//	    		, @AuthenticationPrincipal UserDetails userDetails
	    		) {
	    	log.info("[UserController] Testlogin : " + authentication.getPrincipal());
	    	//sns으로 로그인하면 여기서 오류
	    	UserDto user =(UserDto) authentication.getPrincipal();
	    	log.info("[UserController] getPrincipal : " + user);
	    	
//	    	log.info("[UserController] userDeatis : " + userDetails.getUsername());
	    	log.info("[UserController] userDeatis : " + userDetails);
	    	return "세션 정보 확인하기";
	    }
	    
	    @GetMapping("/test/oauth/login")
	    public @ResponseBody String TestOAuthlogin(Authentication authentication
	    		,@AuthenticationPrincipal OAuth2User oAuth
	    		) {
	    	log.info("[UserController] TestOAuthlogin : " + authentication.getPrincipal());
	    	OAuth2User oAuth2User =(OAuth2User) authentication.getPrincipal();
	    	log.info("[UserController] oAuth2User : " + oAuth2User.getAttributes());
	    	log.info("[UserController] oAuth : " + oAuth.getAttributes());
	    	

	    	return "세션 정보 확인하기";
	    }
	    
	    /* 로그인 페이지 이동 */
	    @GetMapping("/login")
	    public String goLogin() {
	    	
	        return "user/login/login";
	    }
	    
	    /* 회원가입 페이지 이동 */
	    @GetMapping("/regist")
	    public String goAgree() {
	    	return "user/login/registAgree";
	    }
	    
	    @GetMapping("/agreeRegist")
	    public String goRegist() {
	    	return "user/login/regist";
	    }
	    /* 로그인 실패 시 */
	    @PostMapping("/loginfail")
	    public String loginFailed(RedirectAttributes rttr) {
	    	
	    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("error.login"));
	    	
	    	return "redirect:/user/login";
	    }
	    
	    /* 회원 가입 */
	    @PostMapping("/regist")
	    public String registMember(@ModelAttribute UserDto user,
	    		@RequestParam String zipCode, @RequestParam String address1, @RequestParam String address2,
	    		RedirectAttributes rttr) throws MemberRegistException {
	    	
	    	String address = zipCode + "$" + address1 + "$" + address2;
	    	user.setAddress(address);
	    	user.setPwd(passwordEncoder.encode(user.getPwd()));
	    	
	    	log.info("[UserController] registMember request user : " + user);
	    	
	    	userService.registUser(user);
	    	
	    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("user.regist"));
	    	
	    	return "redirect:/";
	    }
	    
	    /*아이디 중복 체크*/
	    @PostMapping("/idDupCheck")
	    public ResponseEntity<String> checkDuplication(@RequestBody UserDto user){
	    	
	    	String result = "사용 가능한 아이디입니다.";
	    	log.info("[MemberController] Request Check ID : {}", user.getId());
	    	
	    	if(userService.selectUserById(user.getId())) {
	    		log.info("[UserController] Already Exist");
	    		result = "중복 된 아이디가 존재합니다.";
	    	}    	
	    	
	    	return ResponseEntity.ok(result);
	    }
	    
	    /*마이페이지*/
	    @GetMapping("/myProfile")
	    public String myProFile() {
	    	
	    	return "user/myPage/myProfile";
	    }
	    
	    /*회원 정보 수정 화면이동*/
	    @GetMapping("/myProfile/update")
	    public String myProfileUpdate(@AuthenticationPrincipal UserDto loginUser, Model model) {
	    	
	    	String[] address = loginUser.getAddress().split("\\$");
	    	
	    	model.addAttribute("address", address);
	    	
	    	return "user/myPage/profileUpdate";
	    }
	    
	    /*회원 정보 수정*/
	    @PostMapping("/update")
	    public String modifyUser(@ModelAttribute UserDto updateUser,
	    		@RequestParam String zipCode, @RequestParam String address1, @RequestParam String address2,
	    		@AuthenticationPrincipal UserDto loginUser,
	    		RedirectAttributes rttr) throws MemberModifyException {
	    
	    	String address = zipCode + "$" + address1 + "$" + address2;
	    	updateUser.setAddress(address);
	    	
	    	updateUser.setUserNo(loginUser.getId());
	    	
	    	log.info("[UserController] modifyUser : {}",updateUser);
	    	
	    	userService.modifyUser(updateUser);
	    	
	    	SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(loginUser.getId()));
	    	
	    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("user.modify"));
	    	return "redirect:/";
	    }
	    
	    /* 회원 정보 수정 시 세션에 저장 된 정보 업데이트 */
	    private Authentication createNewAuthentication(String id) {
	    	
	    	UserDetails newPrincipal = authenticationService.loadUserByUsername(id);
	    	UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, newPrincipal.getPassword(), newPrincipal.getAuthorities());

	        return newAuth;
		}
	    
	    /*주문 목록 페이지*/
	    @GetMapping("/myOrderList")
	    public String myOrderList(@AuthenticationPrincipal UserDto user, OrderDto order, Model model, 
	    		@RequestParam(defaultValue="1") int page, 
	  			@RequestParam(required=false) String searchCondition, 
	  			@RequestParam(required=false) String searchValue
	    		) {
	    	
	    	
	    	Map<String, String> searchMap = new HashMap<>();
	  		searchMap.put("searchCondition", searchCondition);
	  		searchMap.put("searchValue", searchValue);
	  		log.info("[UserController] searchMap : {}", searchMap);
	    	Map<String, Object> userInfo = userService.selectUserInfo(user.getId());
	    	
	    	Map<String, Object> orderListAndPaging = userService.findOrderListViewPaging(page, searchMap, user.getUserNo());
//	    	Map<String, Object> orderLists = userService.selectOrderList(user.getUserNo());
//	    	
//	    	int orderReadyCounting = userService.orderReadyCounting(user.getUserNo());
//	    	model.addAttribute("userInfo", userInfo.get("userInfo"));
//	    	model.addAttribute("orderLists", orderLists.get("orderLists"));
//	    	model.addAttribute("orderReadyCounting", orderReadyCounting);	    	
	    	model.addAttribute("paging", orderListAndPaging.get("paging"));
	    	model.addAttribute("orderLists", orderListAndPaging.get("orderList"));
//	    	log.info("[userController] userInfo{}", userInfo);
//	    	log.info("[userController] orderLists{}", orderLists);
//	    	log.info("[userController] orderReadyCounting{}", orderReadyCounting);
	    	log.info("[userController] paging{}", orderListAndPaging.get("paging"));
	    	log.info("[userController] orderListAndPaging{}",  orderListAndPaging.get("orderList"));
	    	return "user/myPage/myOrderList";
	    }
	    
	    @GetMapping("/orderDetail")
	    public String orderPtDetail(@RequestParam(value="userNo") String userNo, 
                @RequestParam(value="orderNo") String orderNo, 
                Model model) {
	    	log.info("[userController] orderPtDetail orderNo 확인 및 시작{}", orderNo +" "+userNo);
	    	OrderDto orderPtDetail = userService.selectOrderDetail(orderNo,userNo);
	    	
	    	model.addAttribute("orderPtDetail", orderPtDetail);
	    	
	    	log.info("[userController] orderPtDetail{}", orderPtDetail);
	    	return "user/myPage/orderPtDetail";
	    }

		/* 사업자 등록 페이지*/
	    @GetMapping("/sell/sellRegist")
	    public String sellSellRegist() {
	    	
	    	return "user/sell/sellRegist";
	    }
	    
	    
	    /*판매자 등록*/
	    @PostMapping("/sell/Regist")
	    public String sellRegist() {
	    	
	    	return "null";
	    }
	    
		/* 아이디 찾기 */
		@GetMapping("/findId")
		public String goSearchId(Model model) {
			
			
			return "user/login/findId";
		}
		
		@PostMapping("/findId")
		public String doFindIdSearch(@ModelAttribute UserDto user, Model model, RedirectAttributes rttr) {
			log.info("[UserController] user findId : " + user);
			
			String id = userService.findUserById(user.getUserNm(), user.getPhone());
			
			log.info("[UserController] user findId : " + id);
			
			String result = "";
			if(id != null) {
				model.addAttribute("id", id);
				result = "/user/login/findIdResult";
			} else {
				rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("user.noId"));
				result = "redirect:/user/findId";
			}
			
			return result;
		}
	    
		/* 비밀번호 찾기 */
		@GetMapping("/findPwd")
		public String goSearchPwd() {

			return "user/login/findPwd";
		}
		
		/* 결제*/
		@PostMapping("/payComplete")
		public ResponseEntity<ResponseDto> postPayComplete(@RequestBody PayCompleteRequest request, @AuthenticationPrincipal UserDto loginUser) throws insertOrderItemException, insertOrderException, insertPayException {
		    log.info("[UserController] request{}", request);
		    
		    String payNo = request.getPayNo();
		    String imp_key = "4472688766767282";
		    String imp_secret = "uVesZr8wTfgxjSI8vfCN61pqnRsyMdmru5w81ZiHhdMH2TMv0qllSYW81Pi8sqeQKEBbIoZNZ4yOerY6";
		    
		    IamportClient iamportClient = new IamportClient(imp_key, imp_secret);
		    
		    try {
		        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(payNo);
		        Payment paymentResult = paymentResponse.getResponse();

		        // 가맹점에서는 merchant_uid 기준으로 결제되어야 하는 금액 조회
		        // 예를 들어, queryAmountToBePaid(paymentResult.getMerchantUid())로 가정합니다.
//		        int amountToBePaid = queryAmountToBePaid(paymentResult.getMerchantUid());
		        BigDecimal amountToBePaid = paymentResult.getAmount();
		        // 결제 상태 및 결제 금액 확인 후 처리
		        if ("paid".equals(paymentResult.getStatus()) && paymentResult.getAmount().equals(amountToBePaid)) {
		        	return successInsertOrder(request, paymentResult, loginUser); // 결제까지 성공적으로 완료
		        } else {
		        	return failPostProcess(paymentResult); // 결제 실패 처리
		            
//		            return ResponseEntity.ok().body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "결제 금액 비교 실패", paymentResult));
		        }

		        // TODO: 결제 완료 후 추가 처리 로직

		    } catch (IamportResponseException e) {
		        System.out.println(e.getMessage());

		        switch (e.getHttpStatusCode()) {
		            case 401:
		                log.info("[UserController] 권한 정보 틀림{}");
		                return ResponseEntity.ok().body(new ResponseDto(HttpStatus.UNAUTHORIZED, "권한 정보 틀림"));
		            case 404:
		                log.info("[UserController] payNo 에 해당되는 거래내역이 존재하지 않음{}", payNo);
		                return ResponseEntity.ok().body(new ResponseDto(HttpStatus.NOT_FOUND, "payNo 틀림"));
		            case 500:
		                log.info("[UserController] 서버 응답 오류");
		                return ResponseEntity.ok().body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "서버 응답 오류"));
		        }

		    } catch (IOException e) {
		        // 서버 연결 실패
		        e.printStackTrace();
		    }

		    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "로그 확인 중", request));
//		    return ResponseEntity.ok().body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "로그 확인 중", request));
		}

		private ResponseEntity<ResponseDto> failPostProcess(Payment paymentResult) {
			
			
			return ResponseEntity.ok().body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "결제 금액 비교 실패", paymentResult));
		}

		private ResponseEntity<ResponseDto> successInsertOrder(PayCompleteRequest request, Payment paymentResult, @AuthenticationPrincipal UserDto loginUser) throws insertOrderItemException, insertOrderException, insertPayException {
		    ResponseEntity<ResponseDto> responseEntity;

		    try {
		        userService.insertOrder(request, paymentResult, loginUser);
		        log.info("[UserController] insertOrder{}", "메소드확인");
		        responseEntity = ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결제 성공", paymentResult));
		    } catch (insertOrderItemException | insertOrderException | insertPayException e) {
		        log.error("[UserController] insertOrder 실패: {}", e.getMessage());
		        responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
		    }

		    return responseEntity;
		}
		
		
		
		
		
		
}
