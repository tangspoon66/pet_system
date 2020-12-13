package com.example.exception;

/**
 * 重复异常
 * TODO
 */
public class OperationRepeaException extends RuntimeException{

	public OperationRepeaException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationRepeaException(String message) {
		super(message);
	}

}
