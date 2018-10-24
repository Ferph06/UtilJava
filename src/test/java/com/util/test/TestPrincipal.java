/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.test;

import com.utils.pojos.TemplatePojo;
import com.utils.service.EmailService;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ferph
 */
public class TestPrincipal {
    @Resource(lookup = "emailService")
    @EJB
    public static EmailService service=new EmailService();

    @Test
    public void enviarEmail() {
        Assert.assertNotNull(service.enviarCorreoHTMLAsyncReturn(new TemplatePojo()));

    }
}
