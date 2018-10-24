/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.service;

import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Clase para el logger general
 *
 * @author ferph
 * @version 1.0.0
 */
@Named
@Singleton
public class LogManager {

    /**
     * Metodo mediante el cual se obtiene la clase en la que es injectada el
     * componente del loger
     *
     * @param point Se obtiene la clase actual donde se manda a llamar
     * @return
     */
    @Produces
    public Logger obtenerLogger(InjectionPoint point) {
        return Logger.getLogger(point.getBean().getBeanClass().getName());
    }
}
