package com.example.exception;

/**
 * 发布话题系统异常
 * TODO
 */
public class RootTopicException extends RuntimeException{

	public RootTopicException(String message, Throwable cause) {
		super(message, cause);
	}

	public RootTopicException(String message) {
		super(message);
	}

}
