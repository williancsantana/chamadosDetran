/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;

/**
 * Classe responsavel por produzir o Logger Factory. Para criar logs do aplicativo.
 * @author Maycon
 */
public class LoggerFactory {
    @Produces
    @Dependent
    public Logger producer(InjectionPoint injectionPoint) {        
        return org.slf4j.LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());  
    }
}
