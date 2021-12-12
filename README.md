# synalogik
Synalogik code test December 2021

A RESTful Java API to read the contents of a plain text file and enable the display of the total number of words, the average word length, the most frequently occurring word length, and a list of the number of words of each length.

# Input
`Hello world & good morning. The date is 18/05/2016`

# Output
```Word count = 9 
Average word length = 4.556 
Number of words of length 1 is 1 
Number of words of length 2 is 1 
Number of words of length 3 is 1 
Number of words of length 4 is 2 
Number of words of length 5 is 2 
Number of words of length 7 is 1 
Number of words of length 10 is 1 
The most frequently occurring word length is 2, for word lengths of 4 & 5
```


# Installation instructions
1. Install JDK8 and setup a `%JAVA_HOME%` environment variable.
2. Open Eclipse IDE and import the project into your worspace.
3. Run gradle clean build command.
4. Find the created jar file.
5. Run `java -jar <jar-name>.jar` (`<jar-name>` is the jar from step 4 with full path).
6. Once running, my preferred method of calling the API is Postman, settings for which are explained below.


# Postman (or any other REST client) settings:
1. Use URL http://localhost:8080/synalogik-service/countWords
2. Request type is POST
3. Content-Type is multipart/form-data
4. Request body needs a param file (of type File) whose value will be the sample file to be passed.
