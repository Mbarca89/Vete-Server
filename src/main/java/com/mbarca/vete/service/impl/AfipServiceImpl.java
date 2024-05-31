package com.mbarca.vete.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AfipServiceImpl {

    static String response = null;
    static String token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiBkc3Q9IkNOPXdzZmUsIE89QUZJUCwgQz1BUiIgdW5pcXVlX2lkPSIzNjM1Njc2MzkxIiBnZW5fdGltZT0iMTcxNzAwNTI5NiIgZXhwX3RpbWU9IjE3MTcwNDg1NTYiLz4KICAgIDxvcGVyYXRpb24gdHlwZT0ibG9naW4iIHZhbHVlPSJncmFudGVkIj4KICAgICAgICA8bG9naW4gZW50aXR5PSIzMzY5MzQ1MDIzOSIgc2VydmljZT0id3NmZSIgdWlkPSJTRVJJQUxOVU1CRVI9Q1VJVCAyMDI5MjMyMjQ1NCwgQ049dmV0ZSIgYXV0aG1ldGhvZD0iY21zIiByZWdtZXRob2Q9IjIyIj4KICAgICAgICAgICAgPHJlbGF0aW9ucz4KICAgICAgICAgICAgICAgIDxyZWxhdGlvbiBrZXk9IjIwMjkyMzIyNDU0IiByZWx0eXBlPSI0Ii8+CiAgICAgICAgICAgIDwvcmVsYXRpb25zPgogICAgICAgIDwvbG9naW4+CiAgICA8L29wZXJhdGlvbj4KPC9zc28+Cg==";
    static String sign = "nNaMdr2XQQM8YepgA1VLd6JkFotd1Rgs/TTEyqdeeJGP07ERErA76d4TXFa2EPNNYcmA40PWQIeyzohvUyhFKejiVfe8Z/uMQF45UQzqyoY6vTl74vMqFAU1+AAdCxNU6xFAXWiwt890uSDpjzHJIOeYV+g/XSxHHNUvxgWhP/M=";
    static String cuit = "20292322454";
    static String ptoVenta = "10";
    static String opener = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" +
            "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            " <soap:Body>\n";
    static String closer = "</soap:Body>\n" +
            "</soap:Envelope>\n";

    static public String getSalePoint() {
        String endpoint = "https://servicios1.afip.gov.ar/wsfev1/service.asmx";
        String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <FEParamGetPtosVenta xmlns=\"http://ar.gov.afip.dif.FEV1/\">\n" +
                "      <Auth>\n" +
                "        <Token>" + token + "</Token>\n" +
                "        <Sign>" + sign + "</Sign>\n" +
                "        <Cuit>" + cuit + "</Cuit>\n" +
                "      </Auth>\n" +
                "    </FEParamGetPtosVenta>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println(request);
        try {
            // Establecer la conexión
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("SOAPAction", "http://ar.gov.afip.dif.FEV1/FEParamGetPtosVenta");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setDoOutput(true);

            // Enviar la solicitud
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(request.getBytes("UTF-8"));
            }

            // Leer la respuesta
            try (InputStream responseStream = connection.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = responseStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                String response = byteArrayOutputStream.toString("UTF-8");

                // Imprimir la respuesta para depuración
                System.out.println("Response: " + response);
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "error " + e.getMessage();
        }
    }

    //    public static String getSalePoint() {
//        String response = null;
//        String endpoint = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECompConsultar";
//        String soapAction = "http://ar.gov.afip.dif.FEV1/FECompConsultar";
//        String token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhaG9tbywgTz1BRklQLCBDPUFSLCBTRVJJQUxOVU1CRVI9Q1VJVCAzMzY5MzQ1MDIzOSIgZHN0PSJDTj13c2ZlLCBPPUFGSVAsIEM9QVIiIHVuaXF1ZV9pZD0iMzU1MjE3Mjk3NCIgZ2VuX3RpbWU9IjE3MTY4MTIwMzEiIGV4cF90aW1lPSIxNzE2ODU1MjkxIi8+CiAgICA8b3BlcmF0aW9uIHR5cGU9ImxvZ2luIiB2YWx1ZT0iZ3JhbnRlZCI+CiAgICAgICAgPGxvZ2luIGVudGl0eT0iMzM2OTM0NTAyMzkiIHNlcnZpY2U9IndzZmUiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjAzNDE4MjY4NDYsIENOPXZldGUiIGF1dGhtZXRob2Q9ImNtcyIgcmVnbWV0aG9kPSIyMiI+CiAgICAgICAgICAgIDxyZWxhdGlvbnM+CiAgICAgICAgICAgICAgICA8cmVsYXRpb24ga2V5PSIyMDI5MjMyMjQ1NCIgcmVsdHlwZT0iNCIvPgogICAgICAgICAgICA8L3JlbGF0aW9ucz4KICAgICAgICA8L2xvZ2luPgogICAgPC9vcGVyYXRpb24+Cjwvc3NvPgo=";
//        String sign = "A5HtFKzETVstJaWhoxd2l5QZdnmAlHn+piPqzrlNPjvyobepA/xNd5/XkOyvC2X4kvQHFNrWcAGunv8LKDjTpi+NAyKHh3tllW52YCGxs7BjI2U33ICpRJvGuKZbpq6dPQGW+jxWvsh03zandQ+Y7rBlxISgNHe/ePcV8GXqTlY=";
//        String cuit = "20292322454";
//        String ptoVenta = "2";
//        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ar=\"http://ar.gov.afip.dif.FEV1/\">"
//                + "<soapenv:Header/>"
//                + "<soapenv:Body>"
//                + "<ar:FECompConsultar>"
//                + "<ar:Auth>"
//                + "<ar:Token>" + token + "</ar:Token>"
//                + "<ar:Sign>" + sign + "</ar:Sign>"
//                + "<ar:Cuit>" + cuit + "</ar:Cuit>"
//                + "</ar:Auth>"
//                + "<ar:FeCompConsReq>"
//                + "<ar:CbteTipo>6</ar:CbteTipo>"
//                + "<ar:CbteNro>1580</ar:CbteNro>"
//                + "<ar:PtoVta>2</ar:PtoVta>"
//                + "</ar:FeCompConsReq>"
//                + "</ar:FECompConsultar>"
//                + "</soapenv:Body>"
//                + "</soapenv:Envelope>";
//
//        try {
//            // Send the request and get the raw response
//            URL url = new URL(endpoint);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
////            connection.setRequestProperty("SOAPAction", soapAction);
//            connection.setDoOutput(true);
//
//            // Write the request
//            connection.getOutputStream().write(request.getBytes("UTF-8"));
//
//            // Read the response
//            InputStream responseStream = connection.getInputStream();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = responseStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, len);
//            }
//            response = byteArrayOutputStream.toString("UTF-8");
//
//            // Print out the response for debugging
//            System.out.println("Response: " + response);
//            return response;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }
    public static String consultarComprobante() {
        String response = null;
        String endpoint = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
        String token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhaG9tbywgTz1BRklQLCBDPUFSLCBTRVJJQUxOVU1CRVI9Q1VJVCAzMzY5MzQ1MDIzOSIgdW5pcXVlX2lkPSIyNjY3MDYyNTcyIiBnZW5fdGltZT0iMTcxNjg5Mzg0NSIgZXhwX3RpbWU9IjE3MTY5MzcxMDUiLz4KICAgIDxvcGVyYXRpb24gdHlwZT0ibG9naW4iIHZhbHVlPSJncmFudGVkIj4KICAgICAgICA8bG9naW4gZW50aXR5PSIzMzY5MzQ1MDIzOSIgc2VydmljZT0id3NtdHhjYSIgdWlkPSJTRVJJQUxOVU1CRVI9Q1VJVCAyMDM0MTgyNjg0NiwgQ049dmV0ZSIgYXV0aG1ldGhvZD0iY21zIiByZWdtZXRob2Q9IjIyIj4KICAgICAgICAgICAgPHJlbGF0aW9ucz4KICAgICAgICAgICAgICAgIDxyZWxhdGlvbiBrZXk9IjIwMjkyMzIyNDU0IiByZWx0eXBlPSI0Ii8+CiAgICAgICAgICAgICAgICA8cmVsYXRpb24ga2V5PSIyMDM0MTgyNjg0NiIgcmVsdHlwZT0iNCIvPgogICAgICAgICAgICA8L3JlbGF0aW9ucz4KICAgICAgICA8L2xvZ2luPgogICAgPC9vcGVyYXRpb24+Cjwvc3NvPgo=";
        String sign = "dEVOvVQ5GymwN2YNTN+0iXSseM8g7ymA14tygNCnAWaQXYet55QuG48eHpQ0/Oe8beh2qwVAzmKqvqRZzXE42Rol6h/ZrPtlqtyjk5n2l0mKbrH6NhcTjmhrkNl0k1bLFx+vYD1NHtm9WK0MHPe9g4Woe6I+jT0KnswFN1wj0aQ=";
        String cuit = "20292322454";
        String ptoVenta = "0002"; // Ajusta según sea necesario
        String numeroComprobante = "00001580"; // Ajusta según sea necesario

        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://impl.service.wsmtxca.afip.gov.ar/service/\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<ser:consultarComprobanteRequest>"
                + "<authRequest>"
                + "<token>" + token + "</token>"
                + "<sign>" + sign + "</sign>"
                + "<cuitRepresentada>" + cuit + "</cuitRepresentada>"
                + "</authRequest>"
                + "<consultaComprobanteRequest>"
                + "<codigoTipoComprobante>006</codigoTipoComprobante>"
                + "<numeroPuntoVenta>2</numeroPuntoVenta>"
                + "<numeroComprobante>1579</numeroComprobante>"
                + "</consultaComprobanteRequest>"
                + "</ser:consultarComprobanteRequest>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
        try {
            // Send the request and get the raw response
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", "http://impl.service.wsmtxca.afip.gov.ar/service/consultarComprobante");
            connection.setDoOutput(true);

            // Write the request
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.getBytes("UTF-8"));
            outputStream.close();

            // Read the response
            InputStream responseStream;
            if (connection.getResponseCode() == 200) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = responseStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            response = byteArrayOutputStream.toString("UTF-8");
            responseStream.close();

            // Print out the response for debugging
            System.out.println("Response: " + response);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "error " + e.getMessage();
        }
    }

    public static String consultarPuntosVenta() {
        String response = null;
        String endpoint = "https://serviciosjava.afip.gob.ar/wsmtxca/services/MTXCAService";
        String token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiB1bmlxdWVfaWQ9IjgzODY4NTc4MiIgZ2VuX3RpbWU9IjE3MTY5ODA0MjkiIGV4cF90aW1lPSIxNzE3MDIzNjg5Ii8+CiAgICA8b3BlcmF0aW9uIHR5cGU9ImxvZ2luIiB2YWx1ZT0iZ3JhbnRlZCI+CiAgICAgICAgPGxvZ2luIGVudGl0eT0iMzM2OTM0NTAyMzkiIHNlcnZpY2U9IndzbXR4Y2EiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjAyOTIzMjI0NTQsIENOPXZldGUiIGF1dGhtZXRob2Q9ImNtcyIgcmVnbWV0aG9kPSIyMiI+CiAgICAgICAgICAgIDxyZWxhdGlvbnM+CiAgICAgICAgICAgICAgICA8cmVsYXRpb24ga2V5PSIyMDI5MjMyMjQ1NCIgcmVsdHlwZT0iNCIvPgogICAgICAgICAgICA8L3JlbGF0aW9ucz4KICAgICAgICA8L2xvZ2luPgogICAgPC9vcGVyYXRpb24+Cjwvc3NvPgo=";
        String sign = "nNaMdr2XQQM8YepgA1VLd6JkFotd1Rgs/TTEyqdeeJGP07ERErA76d4TXFa2EPNNYcmA40PWQIeyzohvUyhFKejiVfe8Z/uMQF45UQzqyoY6vTl74vMqFAU1+AAdCxNU6xFAXWiwt890uSDpjzHJIOeYV+g/XSxHHNUvxgWhP/M=";
        String cuit = "20292322454";

        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://impl.service.wsmtxca.afip.gov.ar/service/\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<ser:consultarPuntosVentaRequest>"
                + "<authRequest>"
                + "<token>" + token + "</token>"
                + "<sign>" + sign + "</sign>"
                + "<cuitRepresentada>" + cuit + "</cuitRepresentada>"
                + "</authRequest>"
                + "</ser:consultarPuntosVentaRequest>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
        try {
            // Send the request and get the raw response
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//            connection.setRequestProperty("SOAPAction", "http://impl.service.wsmtxca.afip.gov.ar/service/consultaUltimoComprobanteAutorizado");
            connection.setDoOutput(true);

            // Write the request
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.getBytes("UTF-8"));
            outputStream.close();

            // Read the response
            InputStream responseStream;
            if (connection.getResponseCode() == 200) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = responseStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            response = byteArrayOutputStream.toString("UTF-8");
            responseStream.close();

            // Print out the response for debugging
            System.out.println("Response: " + response);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "error " + e.getMessage();
        }
    }

    public static String crearComprobante() {
        String response = null;
        String endpoint = "https://serviciosjava.afip.gob.ar/wsmtxca/services/MTXCAService";
        String token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiB1bmlxdWVfaWQ9IjM2NzU4MTQ5MTgiIGdlbl90aW1lPSIxNzE2OTcyNzIyIiBleHBfdGltZT0iMTcxNzAxNTk4MiIvPgogICAgPG9wZXJhdGlvbiB0eXBlPSJsb2dpbiIgdmFsdWU9ImdyYW50ZWQiPgogICAgICAgIDxsb2dpbiBlbnRpdHk9IjMzNjkzNDUwMjM5IiBzZXJ2aWNlPSJ3c210eGNhIiB1aWQ9IlNFUklBTE5VTUJFUj1DVUlUIDIwMjkyMzIyNDU0LCBDTj12ZXRlIiBhdXRobWV0aG9kPSJjbXMiIHJlZ21ldGhvZD0iMjIiPgogICAgICAgICAgICA8cmVsYXRpb25zPgogICAgICAgICAgICAgICAgPHJlbGF0aW9uIGtleT0iMjAyOTIzMjI0NTQiIHJlbHR5cGU9IjQiLz4KICAgICAgICAgICAgPC9yZWxhdGlvbnM+CiAgICAgICAgPC9sb2dpbj4KICAgIDwvb3BlcmF0aW9uPgo8L3Nzbz4K";
        String sign = "frUdQf2ftRAoeG7xqjsqGgMEPmCEZ1Zfmw7POznAXK0OcX3nEJ9EMQVCbx1O/nw4UrA884i/rZAdf02KdDT0nF0M134EJyeUfsJJ5jBRHNmEhDi1WSutWyz1+frS46Csjqh1uYMBq2BIWIIpdLVE+voDE7nl9PlPn77LvXF7EKI=";
        String cuit = "20292322454";

        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:ser=\"http://impl.service.wsmtxca.afip.gov.ar/service/\">\n" +
                " <soapenv:Header/>\n" +
                " <soapenv:Body>\n" +
                " <ser:autorizarComprobanteRequest>\n" +
                " <authRequest>\n" +
                " <token>" + token + "</token>\n" +
                " <sign>" + sign + "</sign>\n" +
                " <cuitRepresentada>" + cuit + "</cuitRepresentada>\n" +
                " </authRequest>\n" +
                " <comprobanteCAERequest>\n" +
                " <codigoTipoComprobante>6</codigoTipoComprobante>\n" +
                " <numeroPuntoVenta>6000</numeroPuntoVenta>\n" +
                " <numeroComprobante>1</numeroComprobante> \n" +
                " <fechaEmision>2024-05-29</fechaEmision>\n" +
                " <codigoTipoDocumento>96</codigoTipoDocumento> \n" +
                " <numeroDocumento>34182684</numeroDocumento>\n" +
                " <importeGravado>100.00</importeGravado>\n" +
                " <importeNoGravado>0.00</importeNoGravado>\n" +
                " <importeExento>100.00</importeExento>\n" +
                " <importeSubtotal>200.00</importeSubtotal>\n" +
                " <importeOtrosTributos>0.01</importeOtrosTributos>\n" +
                " <importeTotal>221.01</importeTotal>\n" +
                " <codigoMoneda>PES</codigoMoneda>\n" +
                " <cotizacionMoneda>1</cotizacionMoneda>\n" +
                " <observaciones>Campo Observaciones </observaciones>\n" +
                " <codigoConcepto>1</codigoConcepto>\n" +
                " <arrayOtrosTributos>\n" +
                " <otroTributo>\n" +
                " <codigo>99</codigo>\n" +
                " <descripcion>Descripcion de otros tributos</descripcion>\n" +
                " <baseImponible>100</baseImponible>\n" +
                " <importe>0.01</importe>\n" +
                " </otroTributo>\n" +
                " </arrayOtrosTributos>\n" +
                " <arrayItems>\n" +
                " <item>\n" +
                " <unidadesMtx>1</unidadesMtx>\n" +
                " <codigoMtx>0123456789913</codigoMtx>\n" +
                " <codigo>12</codigo>\n" +
                " <descripcion>Producto 1</descripcion>\n" +
                " <cantidad>1</cantidad>\n" +
                " <codigoUnidadMedida>1</codigoUnidadMedida>\n" +
                " <precioUnitario>121</precioUnitario>\n" +
                " <importeBonificacion>0</importeBonificacion>\n" +
                " <codigoCondicionIVA>5</codigoCondicionIVA>\n" +
                " <importeItem>121.00</importeItem>\n" +
                " </item>\n" +
                " <item>\n" +
                " <unidadesMtx>1</unidadesMtx>\n" +
                " <codigoMtx>0123456779914</codigoMtx>\n" +
                " <codigo>31</codigo>\n" +
                " <descripcion>Producto 2</descripcion>\n" +
                " <cantidad>1</cantidad>\n" +
                " <codigoUnidadMedida>7</codigoUnidadMedida>\n" +
                " <precioUnitario>100</precioUnitario>\n" +
                " <codigoCondicionIVA>2</codigoCondicionIVA>\n" +
                " <importeItem>100</importeItem>\n" +
                " </item>\n" +
                " </arrayItems>\n" +
                " <arraySubtotalesIVA>\n" +
                " <subtotalIVA>\n" +
                " <codigo>5</codigo>\n" +
                " <importe>21</importe>\n" +
                " </subtotalIVA>\n" +
                " </arraySubtotalesIVA>\n" +
                " <arrayActividades>\n" +
                " <actividad>\n" +
                " <codigo>120010</codigo>\n" +
                " </actividad>\n" +
                "19 de 265\n" +
                "Autorizar un Comprobante CAE (autorizarComprobante)\n" +
                " <actividad>\n" +
                " <codigo>477470</codigo>\n" +
                " </actividad>\n" +
                " </arrayActividades>\n" +
                " </comprobanteCAERequest>\n" +
                " </ser:autorizarComprobanteRequest>\n" +
                " </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        try {
            // Send the request and get the raw response
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//            connection.setRequestProperty("SOAPAction", "http://impl.service.wsmtxca.afip.gov.ar/service/consultaUltimoComprobanteAutorizado");
            connection.setDoOutput(true);

            // Write the request
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(request.getBytes("UTF-8"));
            outputStream.close();

            // Read the response
            InputStream responseStream;
            if (connection.getResponseCode() == 200) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = responseStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            response = byteArrayOutputStream.toString("UTF-8");
            responseStream.close();

            // Print out the response for debugging
            System.out.println("Response: " + response);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "error " + e.getMessage();
        }
    }
}
