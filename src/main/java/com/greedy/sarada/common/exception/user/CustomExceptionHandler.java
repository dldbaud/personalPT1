//package com.greedy.sarada.common.exception.user;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@ControllerAdvice
//public class CustomExceptionHandler  {
//
//	@ExceptionHandler(EmailCheckException.class)
//	   public String handleUserAlreadyExistsException(EmailCheckException ex, RedirectAttributes rttr) {
//        rttr.addFlashAttribute("message", ex.getMessage());
//        return "redirect:/"; // 리다이렉션 경로는 실제로 사용하는 URL로 수정
//    }
//
//}
