package com.greedy.sarada.user.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.greedy.sarada.admin.service.AdminService;
import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.common.exception.user.InsertReplyException;
import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.common.exception.user.MemberRemoveException;
import com.greedy.sarada.common.exception.user.insertOrderException;
import com.greedy.sarada.common.exception.user.insertOrderItemException;
import com.greedy.sarada.common.exception.user.insertPayException;
import com.greedy.sarada.common.paging.ResponseDto;
import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.PayCompleteRequest;
import com.greedy.sarada.user.dto.RefundDto;
import com.greedy.sarada.user.dto.ReplyDto;
import com.greedy.sarada.user.dto.UserDto;
import com.greedy.sarada.user.service.AuthenticationService;
import com.greedy.sarada.user.service.MailService;
import com.greedy.sarada.user.service.UserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
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
	    private final MailService mailService;
	    private final SellService sellService;
	    private final AdminService adminService;
	    
		
	    public UserController(PasswordEncoder passwordEncoder, MessageSourceAccessor messageSourceAccessor, UserService userService, AuthenticationService authenticationService
	    		,MailService mailService, SellService sellService, AdminService adminService
	    		) {
	    	this.passwordEncoder = passwordEncoder;
	    	this.messageSourceAccessor = messageSourceAccessor;
	    	this.userService = userService;
	    	this.authenticationService = authenticationService;
	    	this.mailService = mailService;
	    	this.sellService = sellService;
	    	this.adminService = adminService;
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
	    	
	    	String loginStatus = AuthenticationService.getLoginStatus();
	    	
//	        if ("DisabledException".equals(loginStatus)) {
//	            rttr.addFlashAttribute("message", "탈퇴 한 회원입니다.");
//	        }  else {
//	        }
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
	    
	    @GetMapping("/refund")
	    public String payRefund(@AuthenticationPrincipal UserDto user, @RequestParam(value="orderNo") String orderNo
	    		,@RequestParam(value="userNo") String userNo
	    		,@RequestParam(value="amount") String amount
	    		,RedirectAttributes rttr
	    		) throws IOException, IamportResponseException {
	    	
	    	String payNo = userService.findPayNo(orderNo);
	    	log.info("[UserController] payNo확인{}",payNo);
	    	String listNm = userService.findListNm(orderNo);
	    	String imp_key = "4472688766767282";
			String imp_secret = "uVesZr8wTfgxjSI8vfCN61pqnRsyMdmru5w81ZiHhdMH2TMv0qllSYW81Pi8sqeQKEBbIoZNZ4yOerY6";
	    	String result = "";
			int amountPrice = Integer.parseInt(amount);
	    	
			
			IamportClient iamportClient = new IamportClient(imp_key, imp_secret);
			
			/* 결제 정보 조회*/
			IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(payNo);
	        Payment paymentResult = paymentResponse.getResponse();
	        
	        /*결제 금액가져오기*/
	        BigDecimal amountToBePaid = paymentResult.getCancelAmount();
	        int amountToBePaidInt = amountToBePaid.intValue();
	        /* 결제한금액 결제요청금액 확인 후*/
	        int refundMoney = amountPrice - amountToBePaidInt;
	        if(refundMoney <= 0) {
	        	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("refund.nonMoney"));
	        	return "redirect:/";
	        }
			CancelData cancelData = new CancelData(payNo, true, amountToBePaid);
			
			IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);
			
			Payment cancelResult = cancelResponse.getResponse();

			if(cancelResult.getCancelAmount() != null) {
				
				log.info("[UserController] cancelResult.getCancelAmount(){}", cancelResult.getCancelAmount());
				
				RefundDto refund = new RefundDto();
				refund.setOrderNo(orderNo);
				refund.setRefundPrice(refundMoney);
				refund.setListNm(listNm);
				refund.setUserNo(user.getUserNo());
				userService.insertRefund(refund);
				rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("refund.success"));
				return "redirect:/";
			} else {
				rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("refund.failed"));
				return "redirect:/";
			}
	    }

	    /* 환불 목록 페이지*/
	    @GetMapping("myRefundList")
	    public String myRefundList(@AuthenticationPrincipal UserDto user,
	    		Model model, 
	    		@RequestParam(defaultValue="1") int page, 
	  			@RequestParam(required=false) String searchCondition, 
	  			@RequestParam(required=false) String searchValue) {
	    	
	    	Map<String, String> searchMap = new HashMap<>();
	    	
	    	searchMap.put("searchCondition", searchCondition);
	    	searchMap.put("searchValue", searchValue);
	    	
	    	Map<String, Object> myRefundListPaging = userService.myRefundPaging(page, searchMap, user.getUserNo());
	    	
	    	model.addAttribute("paging", myRefundListPaging.get("paging"));
	    	model.addAttribute("refundList", myRefundListPaging.get("refundList"));
	    	return "user/myPage/myRefund";
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
				result = "user/login/findIdResult";
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
		
		/* 이메일 전송 */
		@PostMapping("/mailsend")
		@ResponseBody
		public String mailConfirm(@RequestBody UserDto user) throws Exception {

			String id = user.getId();
			String email = user.getEmail();
			String phone = user.getPhone();
			
			log.info("[UserController] email : {}", id);
			log.info("[UserController] email : {}", email);
			log.info("[UserController] email : {}", phone);

			String userTyped = userService.findUserByUserType(id,phone);
			log.info("[userTyped] userTyped : {}", userTyped);
			if("일반".equals(userTyped)) {
				log.info("[userTyped] userTyped : {}", userTyped);
				String tempPwd = mailService.sendSimpleMessage(email); // 임시 비밀번호 발급
				// DB에 임시 비밀번호 저장
				UserDto tempUser = userService.findUserByEmailId(id);
				tempUser.setPwd(passwordEncoder.encode(tempPwd)); // 비밀번호 인코딩
				userService.modifyTpwd(tempUser);
				return tempPwd; // 클라이언트에게 발급된 임시 비밀번호 반환
//			} else if("google".equals(userTyped)){
//				log.info("[userTyped] userTyped : {}", userTyped);
//				return "google";
//			} else if("naver".equals(userTyped)){
//				log.info("[userTyped] userTyped : {}", userTyped);
//				return "naver";
			} else {
				log.info("[userTyped] userTyped : {}", userTyped);
				return "null";
			}
			
			
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
		    		 
		    		 Map<String, Object> ptStCountMap = userService.findPtStCount(request.getPtList());
		    		 List<String> ptStCountMinus = (List<String>) ptStCountMap.get("ptStCountMinus");

		    		 if (!ptStCountMinus.isEmpty()) {
		    		     log.info("[UserController] 재고 수량 부족: {}", ptStCountMinus);
		    		     /* 바로 결제 취소*/
		    		     IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(payNo);
			 		     Payment paymentResult = paymentResponse.getResponse();
			 		    	
		    		     CancelData cancelData = new CancelData(payNo, true, paymentResult.getAmount());
		    				
		    		    /*웹훅을 설정해야 이걸 안 하나?*/
		    			IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);
		 		    	return ResponseEntity.ok().body(new ResponseDto(HttpStatus.BAD_REQUEST, "재고 수량 부족", ptStCountMap));
		 		    } else {
		 		    	
		 		    	IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(payNo);
		 		    	Payment paymentResult = paymentResponse.getResponse();
		 		    	
		 		    	// 가맹점에서는 merchant_uid 기준으로 결제되어야 하는 금액 조회
		 		    	// 예를 들어, queryAmountToBePaid(paymentResult.getMerchantUid())로 가정합니다.
//				        int amountToBePaid = queryAmountToBePaid(paymentResult.getMerchantUid());
		 		    	BigDecimal amountToBePaid = paymentResult.getAmount();
		 		    	// 결제 상태 및 결제 금액 확인 후 처리
		 		    	if ("paid".equals(paymentResult.getStatus()) && paymentResult.getAmount().equals(amountToBePaid)) {
		 		    		return successInsertOrder(request, paymentResult, loginUser); // 결제까지 성공적으로 완료
		 		    	} else {
		 		    		return failPostProcess(paymentResult); // 결제 실패 처리
		 		    		
		 		    	}
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
		    }
		    
		

		private ResponseEntity<ResponseDto> failPostProcess(Payment paymentResult) {
			
			
			return ResponseEntity.ok().body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "결제 금액 비교 실패", paymentResult));
		}

		private ResponseEntity<ResponseDto> successInsertOrder(PayCompleteRequest request, Payment paymentResult, @AuthenticationPrincipal UserDto loginUser) throws insertOrderItemException, insertOrderException, insertPayException {
		    ResponseEntity<ResponseDto> responseEntity;

		    try {
		        userService.insertOrder(request, paymentResult, loginUser);
		        userService.updatePtCount(request);
		        log.info("[UserController] insertOrder{}", "메소드확인");
		        responseEntity = ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "결제 성공", paymentResult));
		    } catch (insertOrderItemException | insertOrderException | insertPayException e) {
		        log.error("[UserController] insertOrder 실패: {}", e.getMessage());
		        responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
		    }

		    return responseEntity;
		}
		
		
		
		
	/*댓글*/
	@PostMapping("/replyInsert")
	public ResponseEntity<String> replyInsert(@AuthenticationPrincipal UserDto user ,@RequestBody ReplyDto reply) {
		
		reply.setWriter(user);
		log.info("[UserController 댓글시 컨트롤러 화인]{}", reply);
		String result = "";
		try {
			userService.replyInsert(reply);
			
			result = "댓글작성 성공";
		} catch (InsertReplyException e) {
			result = "댓글 작성 실패";
			e.printStackTrace();
		}
		return ResponseEntity.ok("댓글 등록 완료");
	}
	
	/*댓글 수정 replyUpdate*/
	@PostMapping("/replyUpdate")
	public ResponseEntity<String> replyUpdate(@AuthenticationPrincipal UserDto user ,@RequestBody ReplyDto reply) {
		
		reply.setWriter(user);
		log.info("[UserController 댓글 수정 컨트롤러 화인]{}", reply);
		String result = "";
		userService.replyUpdate(reply);
		
		result = "댓글 수정 성공";
		
		return ResponseEntity.ok("댓글 수정 완료");
	}
	//댓글 비동기 페이징 방식
	@GetMapping("/loadReply")
	/* get에서는 DTO에 @RequestParam 생략가능 (get은 쿼리스트링이기 떄문) post는 작성해야함 */
	public ResponseEntity<ResponseDto> loadReply(ReplyDto loadReply
			,@RequestParam(defaultValue="1") int page
			) {
		
		log.info("[UserController] loadReply : {}", loadReply);
		
		Map<String, Object> replyListAndPaging = userService.selectReplyList(loadReply, page); 
		
		log.info("[UserController] replyListAndPaging : {}", replyListAndPaging);
		
		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", replyListAndPaging));
	}
	
	/* 회원탈퇴*/
	@GetMapping("/leave")
	public String userLeave(@AuthenticationPrincipal UserDto user) {
		
		return "user/mypage/leave";
	}
	
	@GetMapping("/leaveUpdate")
	public String leaveUpdate(@AuthenticationPrincipal UserDto user , RedirectAttributes rttr) throws MemberRemoveException {
		
		userService.removeUser(user);
		SecurityContextHolder.clearContext();
		
		rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("user.delete"));
		
		return "redirect:/";
	}
	
    @GetMapping("/productDetail")
    public String productDetail(@RequestParam String listNo, 
    		@RequestParam(defaultValue="1") int page,
    		@AuthenticationPrincipal UserDto user,
    		Model model) {
    	
    	ReplyDto loadReply = new ReplyDto();
    	loadReply.setRefListNo(listNo);
    	
    	log.info(listNo);
    	Map<String, Object> productDetails = sellService.productDetails(listNo);
    	Map<String, Object> replyListAndPaging = userService.selectReplyList(loadReply, page);
    	model.addAttribute("replylist", replyListAndPaging.get("replyList"));
    	model.addAttribute("paging", replyListAndPaging.get("paging"));
    	
    	if(user != null) {
    		ReplyDto replyCheck = userService.replyCheck(listNo, user);
    		OrderDto order = new OrderDto();
    		order.setUserNo(user.getUserNo());
    		order.setListNo(listNo);
    		List<OrderDto> orderCheck = userService.orderCheck(order);
    		
    		model.addAttribute("replyCheck", replyCheck);
    		model.addAttribute("orderCheck", orderCheck);
    		log.info("[sellController] replyCheck : {}", replyCheck);
    		log.info("[sellController] orderCheck : {}", orderCheck);
    	}
    	
    	model.addAttribute("sList", productDetails.get("sList"));
    	model.addAttribute("sPtList", productDetails.get("sPtList"));
    	model.addAttribute("sFilelist", productDetails.get("sFilelist"));
    	
    	log.info("[sellController] productDetail{}", productDetails);
    	log.info("[sellController] replyListAndPaging : {}", replyListAndPaging);
    	return "user/pay/productDetail";
    }
    
    /* 메인 페이지 이미지 비동기 로드*/  
    @GetMapping(value = "/listView", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ResponseDto> getListView(
  		  @RequestParam(defaultValue="1") int page, 
  			@RequestParam(required=false) String searchCondition, 
  			@RequestParam(required=false) String searchValue
  		  ) {
   
        Map<String, String> searchMap = new HashMap<>();
  		searchMap.put("searchCondition", searchCondition);
  		searchMap.put("searchValue", searchValue);
  		
  		log.info("[AdminController] searchMap : {}", searchMap);
  		
  		Map<String, Object> boardListAndPaging = adminService.findListViewPageing(page,searchMap);
        // 이미지 파일 경로 추가
//        for (ListDto item : boardListAndPaging) {
//        	FileDto file = item.getFileMain();
//        	file.setMainFilePath(file.getMainFilePath() + file.getSavedFileNm());
//        	item.setFileMain(file);
//        }
        

  		return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", boardListAndPaging));

    }
    
	/* 사업자 등록 페이지 */
	@GetMapping("/sellRegist")
    public String sellSellRegist(@AuthenticationPrincipal UserDto user,
    		Model model) {
    	
    	SellDto sellResist =sellService.findSellRegist(user);
    	if(sellResist != null) {
    		String[] address = sellResist.getSellAddress().split("\\$");    		
    		model.addAttribute("address", address);
    	}

    	model.addAttribute("sellResist", sellResist);
    	
    	log.info("[판매자 신청 정보 확인] : {}", sellResist);
    	return "user/sell/sellRegist";
    }
	
	/* 판매자 등록 */
	@PostMapping("/sellRegist")
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
}

