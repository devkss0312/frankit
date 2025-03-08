package com.frankit.task.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Enumeration;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🔹 클라이언트의 요청 정보를 로깅하는 메서드
    private void logRequestDetails(HttpServletRequest request) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n📌 [요청 정보]\n");
        logMessage.append("➡️ 요청 URL: ").append(request.getRequestURI()).append("\n");
        logMessage.append("➡️ 요청 메서드: ").append(request.getMethod()).append("\n");
        logMessage.append("➡️ 요청 IP: ").append(request.getRemoteAddr()).append("\n");
        logMessage.append("➡️ 요청 시간: ").append(LocalDateTime.now()).append("\n");

        // 🔹 요청 헤더 정보 추가
        logMessage.append("➡️ 요청 헤더:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            logMessage.append("   🔸 ").append(header).append(": ").append(request.getHeader(header)).append("\n");
        }

        logger.error(logMessage.toString());
    }

    /**
     * 🔹 400 Bad Request - 잘못된 요청 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        logger.warn("❌ 잘못된 요청 발생: {}", ex.getMessage());
        logRequestDetails(request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다: " + ex.getMessage());
    }

    /**
     * 🔹 500 Internal Server Error - 런타임 예외 처리
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("⚠️ 서버 내부 오류 발생: {}", ex.getMessage());
        logRequestDetails(request);
        logger.error("📌 [오류 상세 정보]", ex); // 🔹 스택 트레이스 로깅
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + ex.getMessage());
    }

    /**
     * 🔹 500 Internal Server Error - 예기치 못한 오류 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, HttpServletRequest request) {
        logger.error("🚨 예기치 못한 오류 발생: {}", ex.getMessage());
        logRequestDetails(request);
        logger.error("📌 [예외 스택 트레이스]", ex); // 🔹 전체 스택 트레이스 출력
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 예기치 못한 오류가 발생했습니다.");
    }
}
