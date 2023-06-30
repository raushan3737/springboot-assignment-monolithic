package com.zemoso.logger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class LoggerApp {
    private static Logger log = LogManager.getLogger(LoggerApp.class);

    public static void generateLog() {
        log.info("Inside generateLog()");
        log.info("Info logging");
        log.warn("Warn logging");
        log.error("Error logging");
        log.debug("Debug logging");

    }

    public static void main(String[] args) {
        // Demo for logging
        generateLog();

        // Demo for lombok
        Student student = new Student(1L, "Raushan", "9102615948");
        log.info("Student: " + student.toString());

    }
}