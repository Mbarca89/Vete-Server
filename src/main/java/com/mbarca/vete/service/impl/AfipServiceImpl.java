package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.WSAAAuthResponse;
import com.mbarca.vete.dto.request.BillRequestDto;
import com.mbarca.vete.service.AfipService;
import com.mbarca.vete.service.WSAAService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AfipServiceImpl implements AfipService {
    String endpoint = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?WSDL";
    String cuit = "20292322454";
    String salePoint = "6";
    String token = null;
    String sign = null;

//    @Override
//    public String consultarPuntosVenta() {
//
//        getAuth();
//
//        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://impl.service.wsmtxca.afip.gov.ar/service/\">"
//                + "<soapenv:Header/>"
//                + "<soapenv:Body>"
//                + "<ser:consultarPuntosVentaRequest>"
//                + "<authRequest>"
//                + "<token>" + token + "</token>"
//                + "<sign>" + sign + "</sign>"
//                + "<cuitRepresentada>" + cuit + "</cuitRepresentada>"
//                + "</authRequest>"
//                + "</ser:consultarPuntosVentaRequest>"
//                + "</soapenv:Body>"
//                + "</soapenv:Envelope>";
//
//        return makeRequest(request);
//    }

    @Override
    public String consultarPuntosVenta() {

        getAuth();

        String request = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ar=\"http://ar.gov.afip.dif.FEV1/\">" +
                "<soap:Header/>" +
                "<soap:Body>" +
                "<ar:FEParamGetPtosVenta>" +
                "<ar:Auth>" +
                "<ar:Token>" + token + "</ar:Token>" +
                "<ar:Sign>" + sign + "</ar:Sign>" +
                "<ar:Cuit>" + cuit + "</ar:Cuit>" +
                "</ar:Auth>" +
                "</ar:FEParamGetPtosVenta>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        return makeRequest(request);
    }

    @Override
    public String generarComprobante(BillRequestDto billRequestDto) {
        getAuth();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = currentDate.format(formatter);

        String request = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ar=\"http://ar.gov.afip.dif.FEV1/\">" +
                "<soap:Header/>" +
                "<soap:Body>" +
                "<ar:FECAESolicitar>" +
                "<ar:Auth>" +
                "<ar:Token>" + token + "</ar:Token>" +
                "<ar:Sign>" + sign + "</ar:Sign>" +
                "<ar:Cuit>" + cuit + "</ar:Cuit>" +
                "</ar:Auth>" +
                "<ar:FeCAEReq>" +
                "<ar:FeCabReq>" +
                "<ar:CantReg>1</ar:CantReg>" +
                "<ar:PtoVta>" + salePoint + "</ar:PtoVta>" +
                "<ar:CbteTipo>" + billRequestDto.getTipo() + "</ar:CbteTipo>" +
                "</ar:FeCabReq>" +
                "<ar:FeDetReq>" +
                "<ar:FECAEDetRequest>" +
                "<ar:Concepto>1</ar:Concepto>" +
                "<ar:DocTipo>" + billRequestDto.getTipoDocumento() + "</ar:DocTipo>" +
                "<ar:DocNro>" + billRequestDto.getDocumento() + "</ar:DocNro>" +
                "<ar:CbteDesde>" + billRequestDto.getNumero() + "</ar:CbteDesde>" +
                "<ar:CbteHasta>" + billRequestDto.getNumero() + "</ar:CbteHasta>" +
                "<ar:CbteFch>" + date + "</ar:CbteFch>" +
                "<ar:ImpTotal>" + billRequestDto.getImporteTotal() + "</ar:ImpTotal>" +
                "<ar:ImpTotConc>" + billRequestDto.getImporteNoGravado() + "</ar:ImpTotConc>" +
                "<ar:ImpNeto>" + billRequestDto.getImporteGravado() + "</ar:ImpNeto>" +
                "<ar:ImpOpEx>0</ar:ImpOpEx>" +
                "<ar:ImpTrib>0</ar:ImpTrib>" +
                "<ar:ImpIVA>" + billRequestDto.getImporteIva() + "</ar:ImpIVA>" +
                "<ar:MonId>PES</ar:MonId>" +
                "<ar:MonCotiz>1</ar:MonCotiz>";
        if (billRequestDto.getTipo().equals("1")) {
            request = request +
                    "<ar:Iva>" +
                    "<ar:AlicIva>" +
                    "<ar:Id>5</ar:Id>" +
                    "<ar:BaseImp>" + billRequestDto.getImporteNoGravado() + "</ar:BaseImp>" +
                    "<ar:Importe>" + billRequestDto.getImporteIva() + "</ar:Importe>" +
                    "</ar:AlicIva>" +
                    "</ar:Iva>";
        }
        request = request +
                "</ar:FECAEDetRequest>" +
                "</ar:FeDetReq>" +
                "</ar:FeCAEReq>" +
                "</ar:FECAESolicitar>" +
                "</soap:Body>" +
                "</soap:Envelope>";
        System.out.println(request);
        return makeRequest(request);
    }

    private String makeRequest(String request) {
        String response = null;
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setDoOutput(true);

            // Write the request
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(request.getBytes("UTF-8"));
            }

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
            return response;

        } catch (Exception e) {
            return "error " + e.getMessage();
        }
    }

    private void getAuth() {
        WSAAAuthResponse authResponse = WSAAService.getTokenAndSign();

        if (authResponse.getMessage().equals("OK")) {
            token = authResponse.getToken();
            sign = authResponse.getSign();
        }
    }
}
