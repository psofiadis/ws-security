/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: $ 
 */
package com.adva.mtosi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/frontend/")
public class MonitorControllerImpl {

  @RequestMapping(value = "/monitor", method = RequestMethod.GET)
  public ModelAndView monitor(final ModelAndView model) {
    model.setViewName("monitor");
    return model;
  }
}
