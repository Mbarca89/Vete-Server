package com.mbarca.vete.service;

import java.io.FileInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import com.mbarca.vete.domain.WSAAAuthResponse;
import com.mbarca.vete.utils.afip_wsaa_client;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.scheduling.annotation.Scheduled;

public class WSAAService {
	private static String token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhaG9tbywgTz1BRklQLCBDPUFSLCBTRVJJQUxOVU1CRVI9Q1VJVCAzMzY5MzQ1MDIzOSIgZHN0PSJDTj13c2ZlLCBPPUFGSVAsIEM9QVIiIHVuaXF1ZV9pZD0iMzMwNzAxNjcwNCIgZ2VuX3RpbWU9IjE3MTg4NDE2NTQiIGV4cF90aW1lPSIxNzE4ODg0OTE0Ii8+CiAgICA8b3BlcmF0aW9uIHR5cGU9ImxvZ2luIiB2YWx1ZT0iZ3JhbnRlZCI+CiAgICAgICAgPGxvZ2luIGVudGl0eT0iMzM2OTM0NTAyMzkiIHNlcnZpY2U9IndzZmUiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjAzNDE4MjY4NDYsIENOPXZldGUiIGF1dGhtZXRob2Q9ImNtcyIgcmVnbWV0aG9kPSIyMiI+CiAgICAgICAgICAgIDxyZWxhdGlvbnM+CiAgICAgICAgICAgICAgICA8cmVsYXRpb24ga2V5PSIyMDI5MjMyMjQ1NCIgcmVsdHlwZT0iNCIvPgogICAgICAgICAgICA8L3JlbGF0aW9ucz4KICAgICAgICA8L2xvZ2luPgogICAgPC9vcGVyYXRpb24+Cjwvc3NvPgo=";
//	private static String token = null;
	private static String sign = "l+mFmyAdO2KA5abE3MXzQH/hthWdabqsGniKeEsjVESZuUoqG0hrnCtSqw5PRbLgXEEurRbllab8RaGbebB46ZYj2wa8MzsevjZNAQvCU6h48tzSzfLsVY7svXmjG4mZYizse2VHLb4o/Ssn0wttyA0rnMd9NG/jr85gC5SskvA=";
//	private static String sign = null;
	private static String message = null;

	@Scheduled(cron = "0 00 09 * * *")
	public static void refreshTokenAndSign() {
		WSAAAuthResponse authResponse = WSAAAuth();
		if (authResponse.getToken() != null && authResponse.getSign() != null) {
			token = authResponse.getToken();
			sign = authResponse.getSign();
			message = authResponse.getMessage();
		}
	}

	public static WSAAAuthResponse getTokenAndSign() {
		if (token == null || sign == null) {
			System.out.println("Obteniendo credenciales");
			refreshTokenAndSign();
		} else {
			System.out.println("Credenciales recuperadas");
		}
		return new WSAAAuthResponse(token, sign, "OK");
	}

	public static WSAAAuthResponse WSAAAuth() {

		String LoginTicketResponse = null;
	
		System.setProperty("http.proxyHost", "");
		System.setProperty("http.proxyPort", "80");
				
		// Read config from phile
		Properties config = new Properties();
		
		try {
			config.load(new FileInputStream("./wsaa_client.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		String endpoint = config.getProperty("endpoint","http://wsaahomo.afip.gov.ar/ws/services/LoginCms"); 
		String service = config.getProperty("service","test");
		String dstDN = config.getProperty("dstdn","cn=wsaahomo,o=afip,c=ar,serialNumber=CUIT 20341826846");
		
		String p12file = config.getProperty("keystore","afipCert.p12");
		String signer = config.getProperty("keystore-signer","vete");
		String p12pass = config.getProperty("keystore-password","veterinaria2123");
		
		// Set proxy system vars
		System.setProperty("http.proxyHost", config.getProperty("http_proxy",""));
		System.setProperty("http.proxyPort", config.getProperty("http_proxy_port",""));
		System.setProperty("http.proxyUser", config.getProperty("http_proxy_user",""));
		System.setProperty("http.proxyPassword", config.getProperty("http_proxy_password",""));
		
		// Set the keystore used by SSL
		System.setProperty("javax.net.ssl.trustStore", config.getProperty("trustStore",""));
		System.setProperty("javax.net.ssl.trustStorePassword",config.getProperty("trustStore_password","")); 
		
		Long TicketTime = Long.valueOf(config.getProperty("TicketTime","36000"));
	
		// Create LoginTicketRequest_xml_cms
		byte [] LoginTicketRequest_xml_cms = afip_wsaa_client.create_cms(p12file, p12pass,
					signer, dstDN, service, TicketTime);
			
		// Invoke AFIP wsaa and get LoginTicketResponse
		try {
			LoginTicketResponse = afip_wsaa_client.invoke_wsaa ( LoginTicketRequest_xml_cms, endpoint );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Get token & sign from LoginTicketResponse
		try {
			Reader tokenReader = new StringReader(LoginTicketResponse);
			Document tokenDoc = new SAXReader(false).read(tokenReader);
			
			String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
			String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
			
			System.out.println("TOKEN: " + token);
			System.out.println("SIGN: " + sign);
			return new WSAAAuthResponse(token, sign, "OK");
		} catch (Exception e) {
			System.out.println(e);
			return new WSAAAuthResponse("", "", "Fallo de auntenticacion: " + e.getMessage());
		}
	}
}
