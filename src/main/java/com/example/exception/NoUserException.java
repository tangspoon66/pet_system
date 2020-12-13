package com.example.exception;

/**
 * 注册失败异常
 */
public class NoUserException extends RuntimeException{

	public NoUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoUserException(String message) {
		super(message);
	}

}
