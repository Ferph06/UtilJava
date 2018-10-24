/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utils.pojos;

import com.utils.service.LogManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * Clase la cual modela La informacion para el envio de correos
 *
 * @author ferph
 * @version 1.0.0
 */
public class TemplatePojo {

    private String to;

    private String msg;

    private Multipart multipart;

    private String subject;
    @Inject
    private Logger log;

    /**
     *
     */
    public TemplatePojo() {
        super();
    }

    /**
     *
     * @param to
     * @param multipart
     * @param subject
     */
    public TemplatePojo(String to, Multipart multipart, String subject) {
        this.to = to;
        this.multipart = multipart;
        this.subject = subject;
    }

    /**
     *
     * @param to
     * @param msg
     * @param subject
     */
    public TemplatePojo(String to, String msg, String subject) {
        this.to = to;
        this.msg = msg;
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Metodo estatico con el que se crea el Template con la informacion del
     * multipart con un HTML
     *
     * @param temp String del html para el correo
     * @param html Objeto inicializado con el contructor para el multipart
     * @return El objeto {@link  TemplatePojo} recibido con la configuracion para
     * el envio con el HTML
     */
    public TemplatePojo crearTemplateHTML(TemplatePojo temp, String html) {
        try {
            BodyPart part = new MimeBodyPart();
            MimeMultipart multiparta = new MimeMultipart("related");
            part.setContent(html, "text/html");
            multiparta.addBodyPart(part);
            temp.setMultipart(multiparta);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return temp;
    }

}
