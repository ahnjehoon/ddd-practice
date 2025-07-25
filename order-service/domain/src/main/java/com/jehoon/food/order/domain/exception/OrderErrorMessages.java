package com.jehoon.food.order.domain.exception;

/**
 * 주문 관련 에러 메시지 상수 클래스
 */
public final class OrderErrorMessages {

    // 주문 상태 전환 관련
    public static final String INVALID_STATE_FOR_PAYMENT = "결제를 진행할 수 없는 주문 상태입니다: %s";
    public static final String INVALID_STATE_FOR_APPROVAL = "승인할 수 없는 주문 상태입니다: %s";
    public static final String INVALID_STATE_FOR_INIT_CANCEL = "취소 요청을 진행할 수 없는 주문 상태입니다: %s";
    public static final String INVALID_STATE_FOR_CANCEL = "주문을 취소할 수 없는 상태입니다: %s";
    public static final String ORDER_ALREADY_INITIALIZED = "주문이 이미 초기화되었거나 잘못된 상태입니다";

    // 주문 금액 검증 관련
    public static final String TOTAL_PRICE_INVALID = "총 금액은 0보다 커야 합니다";
    public static final String TOTAL_PRICE_MISMATCH = "총 금액(%.2f)이 주문 항목 합계(%.2f)와 일치하지 않습니다";
    public static final String ITEM_PRICE_INVALID = "주문 항목의 가격(%.2f)이 상품(%s)에 대해 유효하지 않습니다";

    // 엔티티 조회 관련
    public static final String RESTAURANT_NOT_FOUND = "레스토랑 ID로 레스토랑을 찾을 수 없습니다: %s";
    public static final String CUSTOMER_NOT_FOUND = "고객 ID로 고객을 찾을 수 없습니다: %s";
    public static final String ORDER_NOT_FOUND = "주문 ID로 주문을 찾을 수 없습니다: %s";

    // 비즈니스 규칙 관련
    public static final String RESTAURANT_INACTIVE = "음식점 ID %s는 현재 활성화되지 않았습니다";
    public static final String ORDER_SAVE_FAILED = "주문을 저장할 수 없습니다";

    // 구분자
    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    private OrderErrorMessages() {
        // 유틸리티 클래스 - 인스턴스화 방지
    }
}