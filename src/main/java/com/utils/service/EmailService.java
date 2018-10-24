/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.service;

import com.utils.pojos.TemplatePojo;
import com.utils.utilidades.ReadPropiedades;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 *
 * @author ferph
 */
@Stateless
@LocalBean
public class EmailService {

    @EJB
    @Resource(lookup = "ReadPropiedades")
    private ReadPropiedades properties=new ReadPropiedades();

    private Session sesionemail;

    /**
     * Metodo con el que se inicializa los datos a usar
     */
    @PostConstruct
    public void init() {
        this.sesionemail = this.iniciarSesionEmail();
    }

    /**
     * Metodo que envia el correo con un HTML dado por el usuario de manera
     * asincrona en caso de no usar el boolean para validar que se envio usar el
     * metodo {@link  enviarCorreoHTMLAsyncNoReturn} En caso de no querer que sea
     * asincrono usar el metodo {@link enviarCorreoHTMLNoAsync}
     *
     *
     * @param template objeto del tipo {@link TemplatePojo} el cual contiene la
     * informacion necesaria para enviar el correo
     * @return El interface {@link Future<Boolean>} que es la manera de
     * implementar lo de manera asincrona con EJB y Java 8
     */
    @Asynchronous
    public Future<Boolean> enviarCorreoHTMLAsyncReturn(TemplatePojo template) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            this.sesionemail = this.iniciarSesionEmail();
            completableFuture.complete(true);
            System.out.println("SE CREO");
            return null;
        });
        return completableFuture;
    }

    /**
     * Metodo que carga he inicializa la sesion del correo que esta contenida en
     * el archivo de propiedades
     *
     * @return Objeto del tipo {@link Session} con los datos necesarios para
     * mandar los correoss
     */
    @Asynchronous
    private Session iniciarSesionEmail() {
        Map<String, Object> data = this.properties.lookupPropiedades();
        Properties defaultp = new Properties();

        defaultp.put("mail.smtp.starttls.enable", "true");

        defaultp.put("mail.smtp.host", data.get("hostName"));

        defaultp.put("mail.smtp.user", data.get("correoOrigen"));

        defaultp.put("mail.smtp.password", data.get("password"));

        defaultp.put("mail.smtp.port", data.get("port"));

        defaultp.put("mail.smtp.auth", "true");

        Session sessionl = Session.getInstance(defaultp, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication((String) data.get("proveedor"), (String) data.get("password"));
            }
        });
        return sessionl;
    }
}
