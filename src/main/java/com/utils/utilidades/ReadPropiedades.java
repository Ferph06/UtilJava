/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Clase con la cual se lee el archivo de propiedades o se escribe en caso de
 * que no exista
 *
 * @author ferph
 * @version 1.0.0
 */
@Stateless
@LocalBean
public class ReadPropiedades {

    private Properties properties;

    private final static String NAMEPROPERTIES = System.getProperty("user.dir")
            .concat(File.separator.concat("..")
                    .concat(File.separator.concat("..")
                            .concat(File.separator.concat("configuracion")).concat(File.separator)));

    private final static String DIR = Paths.get(NAMEPROPERTIES).normalize().toString();

    private final static Logger LOG = Logger.getLogger(ReadPropiedades.class.getName());

    /**
     * Metodo que inicializa la obtencion de las propiedades
     */
    @PostConstruct
    public void init() {
        this.obtenerPropiedad();
    }

    public Map<String, Object> lookupPropiedades() {
        this.properties = this.obtenerPropiedad();
        return (Map<String, Object>) this.properties.entrySet().parallelStream().filter(l -> l != null);
    }

    /**
     * Metodo mediante el cual se obtiene el archivo de propiedades cargado en
     * el archivo de propiedades
     *
     * @return Un objeto del tipo {@link  Properties} con la configuracion para
     * el envio de correos
     */
    private synchronized Properties obtenerPropiedad() {
        if (!Paths.get(DIR).toFile().exists()) {
            Paths.get(DIR).toFile().mkdirs();
            try {
                Properties p = new Properties();
                p.setProperty("cuenta", DIR);
                Files.createFile(Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))).normalize());
                try (InputStream is = Files.newInputStream(Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))))) {
                    
                        p.load(is);
                        p.setProperty("cuenta", DIR);
                        try (OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))).normalize().toFile()), StandardCharsets.UTF_8);) {
                            w.append("cuenta=fernando");
                            p.store(w, null);
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, e.getMessage(), e);
                        }
                    
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }

            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        } else {
            System.out.println("FILE \n" + Paths.get(DIR).toString());
            if (Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))).toFile().exists()) {
                try (InputStream is = Files.newInputStream(Paths.get(DIR.concat(File.separator.concat("cuenta.properties"))))) {
                    if (!Objects.isNull(is)) {
                        this.properties.load(is);
                    }
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }

        return this.properties;
    }
}
