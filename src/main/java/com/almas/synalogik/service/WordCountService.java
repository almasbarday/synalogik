package com.almas.synalogik.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.almas.synalogik.exception.FileContentException;

/**
 * Service that does the actual processing of the incoming file and returns the word count details
 * Assumptions:
 * 1. A word is a set of alphanumeric characters not including special characters and separated from other words by whitespaces.
 * 2. The only special characters exempt from rule 1 are & and /. & be considered a word on its own and / will be considered part of a word if enclosed between two alphanumeric characters, but not on its own.
 * 3. Numbers will be considered words separated by whitespaces. so formatted numbers like 1,000 will be considered as 1 word of length 5 but 1, 000 will be 2 words of lengths 1, 3
 *  
 * 
 * @author AlmasBarday
 *
 */

@Service
public class WordCountService {
	
	private static final List<String> ALLOWED_SYMBOLS = Arrays.asList("&");
	
	/**
	 * This method does the actual processing of the incoming file, counting the words, lengths and average.
	 * 
	 * @param file The incoming file
	 * @return
	 */
	public String countWords(MultipartFile file) {
		try {
			// Read the incoming file into a BufferedReader
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

			// Check if the buffer was read properly and is ready for processing. If not, throw custom exception.
			if (!reader.ready()) {
				throw new FileContentException("File has no contents");
			}
			
			// Everything is ready, processing begins
			// Initialise some useful variables
			int wordCount = 0; //count of words in file, useful for calculating average
			int characterCount =0; // count of characters, useful for calculating average
			Map<Integer, Integer> lengthAndCountMap = new HashMap<>(); // keeps track of <word length, count for length>
			
			while(reader.ready()) {
				String line = reader.readLine();
				
				// If line is blank, ignore and move on to next
				if(line == null) {
					continue;
				}
				
				// Split the line into words based on whitespaces
				String[] wordsInLine = line.split("\\s");
				
				for(String word : wordsInLine) {
					//remove any special characters that is not allowed
					if(!ALLOWED_SYMBOLS.contains(word)) {
						word = word.replaceAll("(^\\W*)|(\\W*$)", "");
					}
					
					//If the word still has characters, count the word
					if(!word.isEmpty()) {
						lengthAndCountMap.merge(word.length(), 1, Integer::sum);
						wordCount++;
						characterCount += word.length();
					}
				}
			}
			
			// Calculate average
			final double countAverage;
			if(wordCount == 0) {
				// Avoiding divide by 0. This can happen with a blank file or a file full of special characters
				countAverage = 0;
			}
			else {
				countAverage = characterCount/(double)wordCount;
			}
			
			return formatCountSummary(wordCount, countAverage, lengthAndCountMap);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This method builds the formatted string that prints out all the output
	 * 
	 * @param wordCount
	 * @param countAverage
	 * @param lengthAndCountMap
	 * @return
	 */
	private String formatCountSummary(int wordCount, double countAverage, Map<Integer, Integer> lengthAndCountMap) {
		// We need to display the counts in order of length, so sort the keys from the Map and print corresponding values
		List<Integer> keyList = new ArrayList<>(lengthAndCountMap.keySet());
		Collections.sort(keyList);
		
		StringBuilder lengthAndCountOutput = new StringBuilder();
		
		// While building the output, we can also find out the most frequent length(s)
		int mostFrequentCount = 0;
		List<Integer> mostFrequentLengths = new ArrayList<>();
		
		for(Integer key : keyList) {
			final int count = lengthAndCountMap.get(key);
			lengthAndCountOutput.append(String.format("Number of words of length %d is %d%n", key, count));
			
			if(count > mostFrequentCount) {
				mostFrequentCount = count;
				mostFrequentLengths.clear();
			}
			
			// if mostFrequentLengths does not contain this key and it needs to be added, do it
			if(count == mostFrequentCount && !mostFrequentLengths.contains(key)) {
				mostFrequentLengths.add(key);
			}
		}
		
		String mostFrequentOutput = "";
		if(mostFrequentCount > 0) {
			mostFrequentOutput = "The most frequently occurring word length is " + mostFrequentCount + ", for word lengths of " + formatLengthList(mostFrequentLengths);
		}
		
		return String.format(
				"Word count = %d%n"
				+ "Average word length = %2.3f%n"
				+ "%s"
				+ "%s", 
				wordCount, countAverage, lengthAndCountOutput, mostFrequentOutput);
	}

	/**
	 * Format the most frequent lengths into a printable string
	 * 
	 * @param mostFrequentLengths
	 * @return
	 */
	private String formatLengthList(List<Integer> mostFrequentLengths) {
		StringBuilder formattedList = new StringBuilder();
		Iterator<Integer> iterator = mostFrequentLengths.iterator();
		
		// append the first length
		if(iterator.hasNext()) {
			formattedList.append(iterator.next());
		}
		
		// if there are more values in list, append the correct punctuation and the next values
		while(iterator.hasNext()) {
			final Integer nextValue = iterator.next();
			if(iterator.hasNext()) {
				formattedList.append(", ").append(nextValue);
			}
			else {
				formattedList.append(" & ").append(nextValue);
			}
		}
		return formattedList.toString();
	}

}
