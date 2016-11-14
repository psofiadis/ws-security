/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.adva.mtosi.gui.beans;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.beans.Model;

/**
 * Describes a musical Notification and provides bound bean properties.
 * This class is used throughout the different tutorial examples.<p>
 * 
 * This class has not been marked as final although it is not intended
 * to be subclassed. However some persistency frameworks (like Hibernate)
 * can optimize the data transfer and locking for extensible classes only.
 *  
 * @author  Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public class Notification extends Model {

    public static final List<Notification> NOTIFICATIONS = new ArrayList();
//    {{
//        add(new Notification("","",false,""));
//    }};


    // Names of the Bound Bean Properties *************************************

    public static final String PROPERTYNAME_ID = "notificationId";
    public static final String PROPERTYNAME_SOURCE_TIME = "sourceTime";

    public static final String PROPERTYNAME_NE_NAME = "ne";
    public static final String PROPERTYNAME_MD_NAME = "md";
    public static final String PROPERTYNAME_AID = "aid";

    public static final String PROPERTYNAME_OS_TIME = "osTime";
    public static final String PROPERTYNAME_LAYER_RATE = "layerRate";
    public static final String PROPERTYNAME_PROBABLE_CAUSE = "probableCause";
    public static final String PROPERTYNAME_PERCEIVED_SEVERITY = "perceivedSeverity";
    public static final String PROPERTYNAME_SERVICE_AFFECTING = "serviceAffecting";
    public static final String PROPERTYNAME_ADDITIONAL_TEXT = "additionalText";
    public static final String PROPERTYNAME_NATIVE_PROBABLE_CAUSE = "nativeProbableCause";

    public static final String PROPERTYNAME_CATEGORY = "category";

//    public static final String PROPERTYNAME_SECURITY = "security";
//    public static final String PROPERTYNAME_IMPAIRMENT = "impairment";
//    public static final String PROPERTYNAME_MODULE_TYPE = "moduleType";
//    public static final String PROPERTYNAME_ENTITY_ALIAS = "entityAlias";



    
    // Instance Fields ********************************************************

    private String notificationId;
    private String sourceTime;
    private String ne;
    private String md;
    private String aid;
    private String osTime;
    private String layerRate;
    private String probableCause;
    private String perceivedSeverity;
    private String serviceAffecting;
    private String additionalText;
    private String nativeProbableCause;
    private String category;

    public Notification() {
    }

    // Instance Creation ******************************************************

    public Notification(String md, String ne, String aid, String additionalText, String osTime, String nativeProbableCause,
//                        String moduleType, String entityAlias,
                        String sourceTime, String serviceName, String perceivedSeverity, String category, String notificationId,
                        String probableCause, String layerRate
//                        Boolean security, String impairment
    ) {
        setMd(md);
        setNe(ne);
        setAid(aid);
        setAdditionalText(additionalText);
        setOsTime(osTime);
        setNativeProbableCause(nativeProbableCause);
//        setModuleType(moduleType);
//        setEntityAlias(entityAlias);
        setSourceTime(sourceTime);
        setServiceAffecting(serviceName);
        setPerceivedSeverity(perceivedSeverity);
        setCategory(category);
        setNotificationId(notificationId);
        setProbableCause(probableCause);
        setLayerRate(layerRate);
//        setSecurity(security);
//        setImpairment(impairment);
    }

    // Accessors **************************************************************

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(String sourceTime) {
        this.sourceTime = sourceTime;
    }

    public String getNe() {
        return ne;
    }

    public void setNe(String ne) {
        this.ne = ne;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getOsTime() {
        return osTime;
    }

    public void setOsTime(String osTime) {
        this.osTime = osTime;
    }

    public String getLayerRate() {
        return layerRate;
    }

    public void setLayerRate(String layerRate) {
        this.layerRate = layerRate;
    }

    public String getProbableCause() {
        return probableCause;
    }

    public void setProbableCause(String probableCause) {
        this.probableCause = probableCause;
    }

    public String getPerceivedSeverity() {
        return perceivedSeverity;
    }

    public void setPerceivedSeverity(String perceivedSeverity) {
        this.perceivedSeverity = perceivedSeverity;
    }

    public String getServiceAffecting() {
        return serviceAffecting;
    }

    public void setServiceAffecting(String serviceAffecting) {
        this.serviceAffecting = serviceAffecting;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public String getNativeProbableCause() {
        return nativeProbableCause;
    }

    public void setNativeProbableCause(String nativeProbableCause) {
        this.nativeProbableCause = nativeProbableCause;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    // Misc *******************************************************************


    @Override
    public String toString() {
        return "Notification{" +
            "notificationId='" + notificationId + '\'' +
            ", sourceTime='" + sourceTime + '\'' +
            ", ne='" + ne + '\'' +
            ", md='" + md + '\'' +
            ", aid='" + aid + '\'' +
            ", osTime='" + osTime + '\'' +
            ", layerRate='" + layerRate + '\'' +
            ", probableCause='" + probableCause + '\'' +
            ", perceivedSeverity='" + perceivedSeverity + '\'' +
            ", serviceAffecting='" + serviceAffecting + '\'' +
            ", additionalText='" + additionalText + '\'' +
            ", nativeProbableCause='" + nativeProbableCause + '\'' +
            ", category='" + category + '\'' +
            '}';
    }
}
