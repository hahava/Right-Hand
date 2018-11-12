package com.righthand.common.type;

/**
 * 프로젝트에 전반적으로 사용 되는 리턴 타입
 *
 * 기본 : 0 -> OK, 100 -> NG
 * 추가 : 상세 에러 -> 임의의 번호를 지정하고 에러 메시지와 매칭
 *
 */
public enum ReturnType {

    // General
    RTN_TYPE_OK(0, "Success"),
    RTN_TYPE_NG(100, "Internal Server Error"),
    RTN_TYPE_SESSION(101, "Session error"),
    RTN_TYPE_NO_DATA(102, "No Data"),
    RTN_TYPE_BAD_REQUEST(103, "Bad Request"),

    // Specific Errors
    RTN_TYPE_MEMBERSSHIP_ELEMENT_EXIST_NG(200, ""),
    RTN_TYPE_MEMBERSSHIP_ELEMENT_NO_EXIST_NG(201, ""),
    RTN_TYPE_MEMBERSSHIP_USERID_EXIST_NG(202, "User ID exists"),
    RTN_TYPE_MEMBERSSHIP_USERID_NO_EXIST_NG(203, ""),
    RTN_TYPE_MEMBERSSHIP_USER_ID_PATTERN_NG(204, ""),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_NO_EXIST_NG(205, ""),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_ENC_NG(206, ""),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_PATTERN_NG(207, "Password pattern error!!"),
    RTN_TYPE_MEMBERSSHIP_TELEPHONE_NO_EXIST_NG(208, ""),
    RTN_TYPE_MEMBERSSHIP_NAME_NO_EXIST_NG(209, ""),
    RTN_TYPE_MEMBERSSHIP_EMAIL_NO_EXIST_NG(210, ""),
    RTN_TYPE_MEMBERSSHIP_EAMIL_EXIST_NG(211, "Email Already Exist"),
    RTN_TYPE_MEMBERSSHIP_GENDER_NO_EXIST_NG(212, ""),
    RTN_TYPE_MEMBERSSHIP_ADDRESS_NO_EXIST_NG(213, ""),
    RTN_TYPE_MEMBERSSHIP_COUNTRY_NO_EXIST_NG(214, ""),
    RTN_TYPE_MEMBERSSHIP_EMAIL_PATTERN_NG(215, "Email pattern error!!"),
    RTN_TYPE_MEMBERSSHIP_USERID_MATCH_NG(216, ""),
    RTN_TYPE_MEMBERSSHIP_PASSWORD_MATCH_NG(217, "password match error"),
    RTN_TYPE_MEMBERSSHIP_AUTHORITY_NG(218, "Authroity error"),
    RTN_TYPE_MEMBERSSHIP_USER_ID_PENDING_NG(219, ""),

    // Activate
    RTN_TYPE_MEMBERSSHIP_ACTIVATE_NO_ID(220, ""),
    RTN_TYPE_MEMBERSSHIP_ACTIVATE_ALREADY(221, ""),
    RTN_TYPE_MEMBERSSHIP_ACTIVATE_MISSMATCH(222, ""),
    RTN_TYPE_MEMBERSSHIP_CHGPWD_NO_ID(223, ""),
    RTN_TYPE_IP_BLOCK_NG(224, "Block IP"),
    RTN_TYPE_USERID_BLOCK_NG(225, "Block User ID"),
    RTN_TYPE_PROFILE_BLOCK_NG(226, "Block Profile"),
    RTN_TYPE_WORD_BLOCK_NG(227, "Block Word"),



    //3rd part
    RTN_TYPE_MEMBERSSHIP_ACCESSTOKEN_PERIOD(231, "옳바르지 않은 토큰"),

    // BOARD
    RTN_TYPE_BOARD_TITLE_NO_EXIST(300, ""),
    RTN_TYPE_BOARD_CONTENT_NO_EXIST(301, ""),
    RTN_TYPE_BOARD_USER_ID_NO_EXIST(302, ""),
    RTN_TYPE_BOARD_USER_NAME_NO_EXIST(303, ""),
    RTN_TYPE_BOARD_USER_EMAIL_NO_EXIST(304, ""),
    RTN_TYPE_BOARD_LIST_NO_EXIST(305, "글이 존재하지 않습니다.");

    private int code;
    private String message;

    ReturnType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getValue() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getStrValue()
    {
        return String.valueOf(code);
    }

}
