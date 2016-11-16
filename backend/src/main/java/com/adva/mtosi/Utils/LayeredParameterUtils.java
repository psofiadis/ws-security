package com.adva.mtosi.Utils;/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: CreateAndActivateFlowDomainFragmentWorkerCM.java 96000 2016-02-08 10:56:27Z ext_psofiadis $
 */

import org.tmforum.mtop.fmw.xsd.gen.v1.NameAndStringValueType;
import org.tmforum.mtop.fmw.xsd.gen.v1.NameAndValueStringListType;
import org.tmforum.mtop.nrb.xsd.lay.v1.LayerRateType;
import org.tmforum.mtop.nrb.xsd.lp.v1.LayeredParametersListType;
import org.tmforum.mtop.nrb.xsd.lp.v1.LayeredParametersType;

import java.util.List;

public class LayeredParameterUtils {


  public static void addLayer(LayeredParametersListType layeredParametersListType, String layer) {
    LayerRateType layerRateType = new LayerRateType();
    layerRateType.setValue(layer);
    NameAndValueStringListType nameAndValueStringListType = findLayer(layeredParametersListType, layerRateType);
    //if null layer does not exist so create a new layer
    if (nameAndValueStringListType == null) {
      org.tmforum.mtop.nrb.xsd.lp.v1.ObjectFactory objFactory = new org.tmforum.mtop.nrb.xsd.lp.v1.ObjectFactory();
      LayeredParametersType layeredParametersType = objFactory.createLayeredParametersType();
      layeredParametersType.setLayerRate(layerRateType);

      org.tmforum.mtop.fmw.xsd.gen.v1.ObjectFactory objGen = new org.tmforum.mtop.fmw.xsd.gen.v1.ObjectFactory();
      layeredParametersType.setParameterList(objGen.createNameAndValueStringListType());
      layeredParametersListType.getTransmissionParameters().add(layeredParametersType);
    }
  }

  public static NameAndValueStringListType findLayer(LayeredParametersListType layeredParametersListType, LayerRateType layer) {
    List<LayeredParametersType> layeredParametersTypeList = layeredParametersListType.getTransmissionParameters();
    for (LayeredParametersType theParameters : layeredParametersTypeList) {
      if (theParameters.getLayerRate().getValue().compareTo(layer.getValue()) == 0) {
        return theParameters.getParameterList();
      }
    }

    return null;
  }

  public static void addLayeredParameter(LayeredParametersListType layeredParametersListType, String layer, String name, String value) {
    //TODO: how it works...
    if(layer==null)
      return;

    LayerRateType layerRateType = new LayerRateType();
    layerRateType.setValue(layer);
    NameAndValueStringListType nameAndValueStringListType = findLayer(layeredParametersListType, layerRateType);
    //if layer does not exist then do not put the parameters in the
    if (nameAndValueStringListType != null) {
      org.tmforum.mtop.fmw.xsd.gen.v1.ObjectFactory objGen = new org.tmforum.mtop.fmw.xsd.gen.v1.ObjectFactory();
      NameAndStringValueType nameAndStringValueType = objGen.createNameAndStringValueType();
      nameAndStringValueType.setName(name);
      nameAndStringValueType.setValue(value);
      nameAndValueStringListType.getNvs().add(nameAndStringValueType);
    }
  }
}
