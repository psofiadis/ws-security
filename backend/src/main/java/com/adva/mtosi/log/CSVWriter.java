package com.adva.mtosi.log;

import org.apache.log4j.Logger;

public class CSVWriter {
  private static Logger log = Logger.getLogger(CSVWriter.class);
//  public static boolean IS_INIT = true;

  static{
    log.info(CSVCreator.toHeader());
  }

  public static void info(CSVCreator csvCreator){
    log.info(csvCreator.toString());
  }

}
