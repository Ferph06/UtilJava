/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * Clase con la cual se lee el archivo de propiedades o se escribe en caso de
 * que no exista
 *
 * @author ferph
 * @version 1.0.0
 */
@Stateless
@LocalBean
public class ReadPropiedades implements Serializable {

    private Properties properties;

    private final static String NAMEPROPERTIES = System.getProperty("user.dir")
            .concat(File.separator.concat("..")
                    .concat(File.separator.concat("..")
                            .concat(File.separator.concat("configuracion")).concat(File.separator)));

    private final static String DIR = Paths.get(NAMEPROPERTIES).normalize().toString();

    private final static Logger LOG = Logger.getLogger(ReadPropiedades.class.getName());
    private final String SALTO = "\n";

    /**
     * Metodo que inicializa la obtencion de las propiedades
     */
    @PostConstruct
    public void init() {
        this.properties = this.obtenerPropiedad();
    }

    public Map<String, Object> lookupPropiedades() {
      
        return (Map<String, Object>) this.properties.entrySet().parallelStream().filter(l -> l != null);
    }

    /**
     * Metodo mediante el cual se obtiene el archivo de propiedades cargado en
     * el archivo de propiedades
     *
     * @return Un objeto del tipo {@link  Properties} con la configuracion para
     * el envio de correos
     */
    private Properties obtenerPropiedad() {
        if (!Paths.get(DIR).toFile().exists()) {
            Paths.get(DIR).toFile().mkdirs();
            try {
                try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
                        Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))).normalize().toString(),
                        false), StandardCharsets.UTF_8)) {
                    out.append("cuenta=").append(SALTO);
                    out.append("password=").append(SALTO);
                    out.append("port=587").append(SALTO);
                    out.append("hostName=smtp.gmail.com").append(SALTO);
                    out.flush();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                } finally {
                    this.cargarPropiedades();
                }

            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        } else {
            if (Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))).toFile().exists()) {
                this.cargarPropiedades();
            }
        }

        return this.properties;
    }

    /**
     * Metodo mediante el cual se carga el archivo de propiedades
     */
    private void cargarPropiedades() {
        try (InputStream is = Files.newInputStream(Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))))) {
            if (!Objects.isNull(is)) {
                this.properties = new Properties();
                this.properties.load(is);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
