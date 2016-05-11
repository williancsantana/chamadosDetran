/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

/**
 *
 * @author verborghs
 */
public class HttpParamProducer {

    //@Produces @HttpParam("")

   String getParamValue(InjectionPoint ip) {
      ServletRequest request = (ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      return request.getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
   }
}
