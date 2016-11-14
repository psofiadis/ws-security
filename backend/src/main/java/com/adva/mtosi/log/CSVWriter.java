/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: CSVWriter.java 96000 2016-02-08 10:56:27Z ext_psofiadis $
 */

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
