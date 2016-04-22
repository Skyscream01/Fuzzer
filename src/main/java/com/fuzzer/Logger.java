package com.fuzzer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by yauhen.bialiayeu on 22.04.2016.
 */
public class Logger {
    final static public org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();
    public static void Init()
    {
        String path = System.getProperty("user.dir")+"\\src\\main\\java\\com\\fuzzer\\log4j.properties";
        PropertyConfigurator.configure(path);
    }
    public static void MutateInfo(String part,String original, String mutated)
    {
        Logger.log.info("-------------------------------------------------------------");
        Logger.log.info("Source "+part+":");
        Logger.log.info(original);
        Logger.log.info("Was replaced by mutated:");
        Logger.log.info(mutated);
        Logger.log.info("-------------------------------------------------------------");
    }
}
