package com.example.exception;

/**
 * 发布话题失败异常
 * TODO
 */
public class NoTopicException extends RuntimeException{

	public NoTopicException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoTopicException(String message) {
		super(message);
	}

}
