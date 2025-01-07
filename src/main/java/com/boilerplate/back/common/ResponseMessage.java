package com.boilerplate.back.common;

public interface ResponseMessage {

    // Http Status 200
    String SUCCESS = "Success";

    // Http Status 400
    String VALIDATION_FAILED = "Validation failed";
    String DUPLICATE_EMAIL = "Duplicate Email";
    String DUPLICATE_NICKNAME = "Duplicate Nickname";
    String DUPLICATE_TEL_NUMBER = "Duplicate Tel Number";
    String NOT_EXISTED_USER = "This user does not exist";
    String NOT_EXISTED_BOARD = "This board does not exist";

    // Http Status 401
    String SIGN_IN_FAIL = "Login information mismatch";
    String AUTHENTICATION_FAIL = "Authorization fail";

    // Http Status 403
    String NO_PERMISSION = "Do not have permission";

    // Http Status 500
    String DATABASE_ERROR = "Database error";
}
