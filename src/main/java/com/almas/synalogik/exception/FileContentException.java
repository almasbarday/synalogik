package com.almas.synalogik.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileContentException extends RuntimeException {

	public FileContentException(String message) {
		super(message);
	}
	

}
