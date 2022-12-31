package org.white_sdev.templates.no_spring_logger_template;
@lombok.extern.slf4j.Slf4j
public class NoSpringTemplate{
	public static void main(String[] args){
		String logID="::main([args]): ";
		
		System.out.println("Hello World System.out");
		log.trace("{} Hello World TRACE", logID);
		log.debug("{} Hello World DEBUG", logID);
		log.info("{} Hello World INFO", logID);
		log.warn("{} Hello World WARN", logID);
		log.error(logID+"Start - Hello World ERROR");
		
		System.exit(0);
	}
}