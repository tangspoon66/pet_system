package com.example.exception;

/**
 * 重复注册/修改异常
 * TODO
 */
public class RepeatUserException extends RuntimeException{

	public RepeatUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatUserException(String message) {
		super(message);
	}

}
