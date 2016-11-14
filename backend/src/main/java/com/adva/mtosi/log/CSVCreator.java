/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: CSVCreator.java 96000 2016-02-08 10:56:27Z ext_psofiadis $
 */
package com.adva.mtosi.log;

public class CSVCreator {

  private final String mdName, meName, AID, additionalText, nmsTime, nativeProbableCause, neTime, value, s, x733EventType, notificationId,
  probableCause,layerRate;

  public CSVCreator(String mdName, String meName, String AID, String additionalText,
                    String nmsTime, String nativeProbableCause, String neTime, String value,
                    String s, String x733EventType, String notificationId, String probableCause, String layerRate){
    this.mdName = mdName;
    this.meName = meName;
    this.AID = AID;
    this.additionalText = additionalText;
    this.nmsTime = nmsTime;
    this.nativeProbableCause = nativeProbableCause;
    this.neTime = neTime;
    this.value = value;
    this.s = s;
    this.x733EventType = x733EventType;
    this.notificationId = notificationId;
    this.layerRate = layerRate;
    this.probableCause = probableCause;

  }

  public static String toHeader() {
    return
        "MD"+
        "|NE"+
        "|AID"+
        "|Additional Text" +
        "|OS Time" +
        "|Native Probable Cause" +
        "|Probable Cause" +
        "|Source Time" +
        "|Service Affecting" +
        "|Category"+
        "|Perceived Severity" +
        "|Notification Id" +
        "|Layer Rate";
  }


  @Override
  public String toString() {
    return
        mdName +
        "|" + meName+
        "|" + AID +
        "|" + additionalText +
        "|" + nmsTime +
        "|" + nativeProbableCause +
        "|" + probableCause +
        "|" + neTime +
        "|" + value +
        "|" + x733EventType +
        "|" + s+
        "|" + notificationId +
        "|" + layerRate;
  }
}
