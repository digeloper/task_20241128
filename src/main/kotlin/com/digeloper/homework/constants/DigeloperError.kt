package com.digeloper.homework.constants

enum class DigeloperError(val code: Int, val desc: String) {
    NOT_EXIST_EMPLOYEE (1000, "직원이 존재하지 않습니다."),
    NOT_EXIST_EMPLOYEE_INFO (1001, "직원 정보가 존재하지 않습니다."),
    FAILED_SAVE_DUPLICATE_EMPLOYEES (1002, "중복된 직원이 존재 합니다."),
    FAILED_SAVE_EMPLOYEES (1003, "직원 저장이 실패했습니다."),
    FAILED_SAVE_EMPLOYEES_INFO (1004, "직원 정보 등록이 실패했습니다."),
    FAILED_LOAD_EMPLOYEES_INFO (1005, "직원 정보 로딩이 실패했습니다."),

    // 입력정보 오류
    INVALID_FORMAT_EMPLOYEE_CVS (2000, "올바르지 않은 CVS 포멧입니다.")
}