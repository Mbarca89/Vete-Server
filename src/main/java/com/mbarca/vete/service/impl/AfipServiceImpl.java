package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.AfipResponse;
import com.mbarca.vete.domain.AfipResponseObject;
import com.mbarca.vete.domain.WSAAAuthResponse;
import com.mbarca.vete.dto.request.BillRequestDto;
import com.mbarca.vete.service.AfipService;
import com.mbarca.vete.service.BillService;
import com.mbarca.vete.service.WSAAService;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AfipServiceImpl implements AfipService {
    String endpoint = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?WSDL";
    String cuit = "20292322454";
    String salePoint = "6";
    String token = null;
    String sign = null;
    BillService billService;

    public AfipServiceImpl(BillService billService) {
        this.billService = billService;
    }

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
    public String consultarUltimoComprobante(String type) {
        getAuth();

        String request = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ar=\"http://ar.gov.afip.dif.FEV1/\">" +
                "<soap:Header/>" +
                "<soap:Body>" +
                "<ar:FECompUltimoAutorizado>" +
                "<ar:Auth>" +
                "<ar:Token>" + token + "</ar:Token>" +
                "<ar:Sign>" + sign + "</ar:Sign>" +
                "<ar:Cuit>" + cuit + "</ar:Cuit>" +
                "</ar:Auth>" +
                "<ar:PtoVta>6</ar:PtoVta>" +
                "<ar:CbteTipo>" + type + "</ar:CbteTipo>" +
                "</ar:FECompUltimoAutorizado>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        String response = makeRequest(request);

        try {
            Reader responseReader = new StringReader(response);
            Document tokenDoc = new SAXReader(false).read(responseReader);

            Namespace ns = new Namespace("ns", "http://ar.gov.afip.dif.FEV1/");
            XPath xpath = DocumentHelper.createXPath("//ns:FECompUltimoAutorizadoResponse/ns:FECompUltimoAutorizadoResult/ns:CbteNro");
            xpath.setNamespaceURIs(Collections.singletonMap("ns", "http://ar.gov.afip.dif.FEV1/"));

            String number = xpath.valueOf(tokenDoc);
            System.out.println("Numero: " + number);
            return number;
        } catch (Exception e) {
            return "Error al obtener comprobante";
        }
    }

    @Override
    public AfipResponse generarComprobante(BillRequestDto billRequestDto) {
        getAuth();
        System.out.println(billRequestDto.getBillProducts());
        if(billRequestDto.getBillProducts().isEmpty()) {
            return new AfipResponse("R","La lista de productos esta vacía");
        }

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
                    "<ar:BaseImp>" + billRequestDto.getImporteGravado() + "</ar:BaseImp>" +
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
        String response = makeRequest(request);
        System.out.println(response);
        AfipResponse afipResponse = extractData(response);
        String billResponse = billService.saveBill(billRequestDto,afipResponse);
        afipResponse.setMessage(billResponse);
        return afipResponse;
    }

    private void getAuth() {
        WSAAAuthResponse authResponse = WSAAService.getTokenAndSign();

        if (authResponse.getMessage().equals("OK")) {
            token = authResponse.getToken();
            sign = authResponse.getSign();
        }
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

    public static AfipResponse extractData(String response) {
        List<AfipResponseObject> errorsList = new ArrayList<>();
        List<AfipResponseObject> observationsList = new ArrayList<>();
        String result = null;
        try {
            Reader responseReader = new StringReader(response);
            Document document = new SAXReader(false).read(responseReader);

            Map<String, String> namespaceMap = Collections.singletonMap("ns", "http://ar.gov.afip.dif.FEV1/");
            Namespace ns = new Namespace("ns", "http://ar.gov.afip.dif.FEV1/");

            XPath resultXpath = DocumentHelper.createXPath("//ns:FECAESolicitarResult/ns:FeCabResp/ns:Resultado");
            resultXpath.setNamespaceURIs(namespaceMap);
            result = resultXpath.valueOf(document);

            String cae = null;
            String caeFchVto = null;

            XPath errorsXpath = DocumentHelper.createXPath("//ns:FECAESolicitarResult/ns:Errors/ns:Err");
            errorsXpath.setNamespaceURIs(namespaceMap);
            List<Node> errors = errorsXpath.selectNodes(document);

            for (Node errorNode : errors) {
                if (errorNode instanceof Element error) {
                    String code = error.elementText("Code");
                    String msg = error.elementText("Msg");
                    errorsList.add(new AfipResponseObject(code, msg));
                }
            }

            XPath observationsXpath = DocumentHelper.createXPath("//ns:FECAESolicitarResult/ns:FeDetResp/ns:FECAEDetResponse/ns:Observaciones/ns:Obs");
            observationsXpath.setNamespaceURIs(namespaceMap);
            List<Node> observations = observationsXpath.selectNodes(document);

            for (Node observationNode : observations) {
                if (observationNode instanceof Element) {
                    Element observation = (Element) observationNode;
                    String code = observation.elementText("Code");
                    String msg = observation.elementText("Msg");
                    observationsList.add(new AfipResponseObject(code, msg));
                }
            }
            XPath caeXpath = DocumentHelper.createXPath("//ns:FECAESolicitarResult/ns:FeDetResp/ns:FECAEDetResponse/ns:CAE");
            caeXpath.setNamespaceURIs(namespaceMap);
            cae = caeXpath.valueOf(document);

            XPath caeFchVtoXpath = DocumentHelper.createXPath("//ns:FECAESolicitarResult/ns:FeDetResp/ns:FECAEDetResponse/ns:CAEFchVto");
            caeFchVtoXpath.setNamespaceURIs(namespaceMap);
            caeFchVto = caeFchVtoXpath.valueOf(document);

            return new AfipResponse(errorsList, observationsList, cae, caeFchVto, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new AfipResponse(errorsList, observationsList, "", "", result);
        }
    }
}
