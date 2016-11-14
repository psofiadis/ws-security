/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: $ 
 */
package com.adva.mtosi.Utils;

import org.tmforum.mtop.fmw.xsd.nam.v1.NamingAttributeType;
import org.tmforum.mtop.fmw.xsd.nam.v1.RelativeDistinguishNameType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MtosiAddress {
    private NamingAttributeType naming;
    //private boolean hasOs;
    private boolean hasMd;
    private boolean hasMe;
    private boolean hasEh;
    private boolean hasEq;
    private boolean hasPtp;
    private boolean hasFtp;
    private boolean hasCtp;
    private boolean hasPU;

    //version 2
    private String mdName;
    private String meName;
    private String ehName;
    private String eqName;
    private String ptpName;
    private String ftpName;
    private String ctpName;
    private String psName;
    private String shelfPTPName;

    private String shelfName;
    private String slotName;
    private String subSlotName;
    private String portName;

    public MtosiAddress(NamingAttributeType naming) {
        this.naming = naming;

        if (naming != null) {

            fillInNames(naming);
            // parse eh-/ ptp-Name
            // elements of a EH-name
            final String regexpShelf = "/(shelf)=(\\d+)";
            final String regexpShelfPassive = "/(shelf)=(\\d+\\-\\d+\\-I\\d+)";
            final String regexpSlot = "/(slot)=([\\w-]+)";
            final String regexpSubSlot = "/(sub_slot)=([\\w-]+)";
            final String regexpPort = "/(port)=([\\w-]+)";

            Matcher matcher = null;

            if (hasEh) {
                // setup pattern for EH-Names (and EQ-Names)
                //               ehName = this.getEhName();
                // pattern for valid variants of EH-names
                Pattern pattern = Pattern.compile(
                        "^(" +                                             // Begin of Line
                                regexpShelf + "|" +                                // matches /shelf=x
                                regexpShelfPassive  + "|" +                        // matches /shelf=x-y-z
                                regexpShelf + regexpSlot + "|" +                   // matches /shelf=x/slot=y
                                regexpShelfPassive + regexpSlot + "|" +                   // matches /shelf=x/slot=y
                                regexpShelf + regexpSlot + regexpSubSlot +    // matches /shelf=x/slot=y/sub_slot=z
                                ")$");                                             // End of Line
                matcher = pattern.matcher(ehName);
            } else if (hasPtp) {
                // setup pattern for PTP-Names
                //              ptpName = this.getPtpName();
                // pattern for valid variants of EH-names
                Pattern pattern = Pattern.compile(
                        "^(" +
                                // Begin of Line
                                regexpShelf + regexpSlot + regexpSubSlot + regexpPort + "|" + // matches /shelf=x/slot=y/sub_slot=z/port=p
                                regexpShelf + regexpSlot + regexpPort + "|" +      // matches /shelf=x/slot=y/port=z
                                regexpShelfPassive + regexpSlot + regexpPort + "|" +      // matches /shelf=x/slot=y/port=z
                                regexpShelf + regexpSlot + regexpSubSlot + "|"+regexpPort +   // matches /shelf=x/slot=y/sub_slot=p, where p is for port
                                ")$");                                             // End of Line
                matcher = pattern.matcher(ptpName);
            }

            // determine shelf, slot, sub-slot and port
            if (matcher != null) {
                if (matcher.find()) {
                    String key;
                    int grpCnt = matcher.groupCount();
                    for (int idx = 1; idx <= grpCnt; idx++) {
                        key = matcher.group(idx);
                        if (key == null || key.startsWith("/")) {
                        }
                        else if (key.equals("shelf")) {
                            shelfName = matcher.group(++idx);
                            if(isPassiveUnit(shelfName)){
                                hasPU = true;
//                                puName=shelfName;
                                String[] shelfNameArray = shelfName.split("\\-");
                                psName=shelfNameArray[1]+"-"+shelfNameArray[2];
                                shelfPTPName = shelfNameArray[0];
                                String shelfNameLastIndex = shelfNameArray[2];
                                shelfNameLastIndex = shelfNameLastIndex.substring(1).length() > 1 ? shelfNameLastIndex.substring(1) : "0"+shelfNameLastIndex.substring(1) ;
                                shelfName = shelfNameArray[0]+ (shelfNameArray[1].length() == 1 ? 0+shelfNameArray[1] : shelfNameArray[1])+shelfNameLastIndex;
                            }
                        } else if (key.equals("slot")) {
                            if(hasPU) slotName = psName;
                            else slotName = matcher.group(++idx);
                        } else if (key.equals("sub_slot")) {
                            int idxH = ++idx;
                            subSlotName = matcher.group(idxH);
                            // if matches /shelf=x/slot=y/sub_slot=p, where p is for port assign portName to p found under sub_slot
                            if (hasPtp && portName == null && ++idxH >= grpCnt) {
                                portName = subSlotName;
                            }
                        } else if (key.equals("port")) {
                            portName = matcher.group(++idx);
                        }
                    }
                }

            }
        }
    }


    public static boolean isPassiveUnit(String shelfName){
        if(shelfName.matches("(\\d+\\-\\d+\\-I\\d+)")) return true;
        else return false;
    }

    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }

    public String getMeName() {
        return meName;
    }

    public void setMeName(String meName) {
        this.meName = meName;
    }

    public String getEhName() {
        return ehName;
    }

    public void setEhName(String ehName) {
        this.ehName = ehName;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getPtpName() {
        return ptpName;
    }

    public void setPtpName(String ptpName) {
        this.ptpName = ptpName;
    }

    public String getFtpName() {
        return ftpName;
    }

    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    public String getCtpName() {
        return ctpName;
    }

    public void setCtpName(String ctpName) {
        this.ctpName = ctpName;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getSubSlotName() {
        return subSlotName;
    }

    public void setSubSlotName(String subSlotName) {
        this.subSlotName = subSlotName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getShelfPTPName() {
        return shelfPTPName;
    }

    public void setShelfPTPName(String shelfPTPName) {
        this.shelfPTPName = shelfPTPName;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public NamingAttributeType getNaming() {
        return naming;
    }

    public void setNaming(NamingAttributeType naming) {
        this.naming = naming;
    }

    public String getMtosiAddress(){
        StringBuilder sb = new StringBuilder();
        if(hasEh) {
            sb.append(ehName);
//            if(hasEq) sb.append(" && 1");
        }else if(hasPtp){
            sb.append(ptpName);
        }else{
          sb.append("/aid=n/a");
        }
        return sb.toString();
    }

    private  void fillInNames(NamingAttributeType naming)
    {

        for (RelativeDistinguishNameType rdn : naming.getRdn()) {
            if (rdn.getType().equals("MD")) {
                this.setMdName(rdn.getValue());
                hasMd = (this.getMdName() != null);
            } else if (rdn.getType().equals("ME")) {
                this.setMeName(rdn.getValue());
                hasMe = (this.getMeName() != null);
            } else if (rdn.getType().equals("EH")) {
                this.setEhName(rdn.getValue());
                hasEh = (this.getEhName() != null);
            } else if (rdn.getType().equals("EQ")) {
                this.setEqName(rdn.getValue());
                hasEq = (this.getEqName() != null);
            } else if (rdn.getType().equals("PTP")) {
                this.setPtpName(rdn.getValue());
                hasPtp = (this.getPtpName() != null);
            } else if (rdn.getType().equals("CTP")) {
                this.setCtpName(rdn.getValue());
                hasCtp = (this.getCtpName() != null);
            } else if (rdn.getType().equals("FTP")) {
                this.setFtpName(rdn.getValue());
                hasFtp = (this.getFtpName() != null);
            }
        }
    }
}
