package org.cric.back_office.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cric.back_office.global.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

        /**
         * API 요청인지 확인
         */
        private boolean isApiRequest(HttpServletRequest request) {
                return request.getRequestURI().startsWith("/api/");
        }

        /**
         * 에러 페이지로 리다이렉트
         */
        private RedirectView redirectToErrorPage(String errorCode, String message) {
                try {
                        String encodedMessage = URLEncoder.encode(message, "UTF-8");
                        return new RedirectView(
                                        "/global/error.html?error=" + errorCode + "&message=" + encodedMessage);
                } catch (UnsupportedEncodingException e) {
                        return new RedirectView("/global/error.html?error=" + errorCode);
                }
        }

        /**
         * Validation 예외 처리 (@Valid, @Validated)
         * MethodArgumentNotValidException: @RequestBody에 @Valid가 적용된 경우
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseBody
        public Object handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                if (!isApiRequest(request)) {
                        return redirectToErrorPage("validation_error", "입력값 검증에 실패했습니다");
                }

                List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> ErrorResponse.FieldError.of(
                                                error.getField(),
                                                error.getRejectedValue(),
                                                error.getDefaultMessage()))
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "입력값 검증에 실패했습니다",
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * Validation 예외 처리 (@ModelAttribute)
         * BindException: @ModelAttribute에 @Valid가 적용된 경우
         */
        @ExceptionHandler(BindException.class)
        @ResponseBody
        public Object handleBindException(
                        BindException ex,
                        HttpServletRequest request) {

                if (!isApiRequest(request)) {
                        return redirectToErrorPage("validation_error", "입력값 검증에 실패했습니다");
                }

                List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> ErrorResponse.FieldError.of(
                                                error.getField(),
                                                error.getRejectedValue(),
                                                error.getDefaultMessage()))
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "입력값 검증에 실패했습니다",
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * JSON 파싱 오류 처리 (잘못된 JSON 형식, null 요청 본문 등)
         */
        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseBody
        public Object handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                if (!isApiRequest(request)) {
                        return redirectToErrorPage("bad_request", "요청 본문을 읽을 수 없습니다");
                }

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * IllegalArgumentException 처리
         */
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseBody
        public Object handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                if (!isApiRequest(request)) {
                        return redirectToErrorPage("bad_request", ex.getMessage());
                }

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * 기타 예외 처리 (500 에러 포함)
         */
        @ExceptionHandler(Exception.class)
        @ResponseBody
        public Object handleException(
                        Exception ex,
                        HttpServletRequest request) {

                if (!isApiRequest(request)) {
                        return redirectToErrorPage("server_error", "서버 내부 오류가 발생했습니다");
                }

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "서버 내부 오류가 발생했습니다",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}
