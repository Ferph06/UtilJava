/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.test;

import com.utils.pojos.TemplatePojo;
import com.utils.service.EmailService;
import javax.ejb.EJB;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author ferph
 */
public class TestPrincipal {
    @EJB
    public static EmailService service=new EmailService();

    @Test
    public void enviarEmail() {
        Assert.assertNotNull(service.enviarCorreoHTMLAsyncReturn(new TemplatePojo()).isDone());

    }
}
