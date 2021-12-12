/**
 * 
 */
package com.almas.synalogik.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import com.almas.synalogik.SynalogikApplication;


/**
 * @author AlmasBarday
 *
 */
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
		classes = {SynalogikApplication.class }, 
		properties = { "application.properties" })
@ActiveProfiles("test")
class WordCountServiceTest {
	static Logger logger = LoggerFactory.getLogger(WordCountServiceTest.class);
	
	@Autowired private WordCountService wordCountService;
	  private MultipartFile example;

	  private MultipartFile createMultipartFile(String classPathLocation) throws IOException {
	    Resource classPathResource = new ClassPathResource(classPathLocation);

	    File file = classPathResource.getFile();
	    final byte[] bytes = new byte[(int) file.length()];
	    DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
	    dataInputStream .readFully(bytes);
	    dataInputStream.close();
	    return new MockMultipartFile(
	        Objects.requireNonNull(classPathResource.getFilename()),
	        classPathResource.getFilename(),
	        "text/plain",
	        bytes);
	  }

	  private MultipartFile mockMultipartFile(String content) {
	    return new MockMultipartFile(
	        "mock.txt", "mock.txt", "text/plain", content.getBytes(StandardCharsets.UTF_8));
	  }

	  @BeforeEach
	  void loadExamples() throws IOException {
	    example = createMultipartFile("data/example.txt");
	  }

	  @Test
	  void testExample() {
	    String result = wordCountService.countWords(example);

	    assertEquals(
	        String.format(
	            "Word count = 9%n"
	                + "Average word length = 4.556%n"
	                + "Number of words of length 1 is 1%n"
	                + "Number of words of length 2 is 1%n"
	                + "Number of words of length 3 is 1%n"
	                + "Number of words of length 4 is 2%n"
	                + "Number of words of length 5 is 2%n"
	                + "Number of words of length 7 is 1%n"
	                + "Number of words of length 10 is 1%n"
	                + "The most frequently occurring word length is 2, for word lengths of 4 & 5"),
	        result);
	  }

	  @Test
	  void testBlacklistedSymbols() {
	    String result = wordCountService.countWords(mockMultipartFile("&"));

	    assertEquals(
	        String.format(
	            "Word count = 1%n"
	                + "Average word length = 1.000%n"
	                + "Number of words of length 1 is 1%n"
	                + "The most frequently occurring word length is 1, for word lengths of 1"),
	        result);
	  }

	  @Test
	  void testSymbolsExample() {
	    String result = wordCountService.countWords(mockMultipartFile("**** hello world! ****"));

	    assertEquals(
	        String.format(
	            "Word count = 2%n"
	                + "Average word length = 5.000%n"
	                + "Number of words of length 5 is 2%n"
	                + "The most frequently occurring word length is 2, for word lengths of 5"),
	        result);
	  }

}
