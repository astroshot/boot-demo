compile:
	mvn compile
	
start:
	mvn package && java -jar target/boot-everything-0.1.0.jar
