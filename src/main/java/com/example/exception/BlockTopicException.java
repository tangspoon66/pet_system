package com.example.exception;

/**
 * 用户禁用状态发布话题异常
 */
public class BlockTopicException extends RuntimeException{

	public BlockTopicException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlockTopicException(String message) {
		super(message);
	}

}
