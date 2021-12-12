package com.almas.synalogik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.almas.synalogik.service.WordCountService;

/**
 * Processes incoming HTTP requests
 * 
 * @author AlmasBarday
 *
 */
@Validated
@RestController
public class WordCounterController {
	@Autowired
	private WordCountService wordCountservice;
	
	@PostMapping("/countWords")
	public ResponseEntity<?> countWords(@RequestParam("file") @NonNull MultipartFile file) {
		return new ResponseEntity<>(wordCountservice.countWords(file), HttpStatus.OK);
	}

}
