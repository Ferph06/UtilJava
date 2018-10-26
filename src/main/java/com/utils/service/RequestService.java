package com.utils.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Clase que sirve para realizar peticiones REST ha servicios externos
 *
 * @author ferph
 * @version 1.0.0
 */
@Stateless
@LocalBean
@Named("RequestService")
public class RequestService implements Serializable {

    private final StringBuilder SB = new StringBuilder();
    private final StringBuilder METHOD = new StringBuilder();
    private JsonObject data;
    private static final Logger LOG = Logger.getLogger(RequestService.class.getName());

    /**
     * Metodo con el que se crea la url ha donde se realizara la peticion
     *
     * @param hostname Nombre del servidor a donde se realiza la peticion
     * @param endpoint Accion a realizar en la peticion
     * @param method Metodo que se utilizara los metodos soportados son
     * POST,GET,PUT,DELETE
     * @param object
     */
    @Asynchronous
    public void crearURL(String hostname, String endpoint, String method, Object object) {
        this.SB.append(hostname).append(endpoint);
        this.METHOD.append(method);
        if (!Objects.isNull(data)) {
            Map<String, Object> dat = (Map<String, Object>) object;
            this.data = Json.createObjectBuilder(dat).build();
        }
    }

    /**
     * Metodo con el que se envia la peticion por el metodo POST
     *
     * @param claseModelResponse
     * @return La clase definida del response
     */
    public Class<?> executePOST(Class<?> claseModelResponse) {
        try {
            HttpURLConnection connection = this.createNoReusableConnection();
            if (!Objects.isNull(connection)) {
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(data.toString().getBytes("UTF-8"));
                    os.flush();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                } finally {
                    try (InputStream is = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        JsonReader read = Json.createReader(reader);

                        //read.readValue().asJsonObject().
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return claseModelResponse;
    }

    /**
     * Metodo que crea una conexion al servidor
     *
     * @return un objeto del tipo {@link  HttpURLConnection} con los datos
     * necesarios para enviar y recibir datos para un servicio REST
     */
    @Asynchronous
    private HttpURLConnection createNoReusableConnection() {
        try {
            URL url = new URL(this.SB.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.setRequestMethod(this.METHOD.toString());
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            return connection;
        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

}
