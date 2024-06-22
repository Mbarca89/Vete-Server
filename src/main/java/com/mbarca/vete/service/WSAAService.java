package com.mbarca.vete.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Properties;
import java.io.IOException;
import java.io.Reader;

import com.mbarca.vete.domain.WSAAAuthResponse;
import com.mbarca.vete.utils.afip_wsaa_client;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.scheduling.annotation.Scheduled;

public class WSAAService {

	private static String token = null;
	private static String sign = null;
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
		if (token == null || token.isEmpty() || sign == null || sign.isEmpty()) {
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

		// Read config from file
		Properties config = new Properties();

		try (InputStream input = WSAAService.class.getResourceAsStream("/wsaa_client.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find wsaa_client.properties");
				return new WSAAAuthResponse("", "", "Failed to load properties file");
			}
			config.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			return new WSAAAuthResponse("", "", "Failed to load properties file: " + ex.getMessage());
		}

		String endpoint = config.getProperty("endpoint", "http://wsaahomo.afip.gov.ar/ws/services/LoginCms");
		String service = config.getProperty("service", "test");
		String dstDN = config.getProperty("dstdn", "cn=wsaahomo,o=afip,c=ar,serialNumber=CUIT 20341826846");

		String p12file = config.getProperty("keystore", "afipCert.p12");
		String signer = config.getProperty("keystore-signer", "vete");
		String p12pass = config.getProperty("keystore-password", "veterinaria2123");

		// Set proxy system vars
		System.setProperty("http.proxyHost", config.getProperty("http_proxy", ""));
		System.setProperty("http.proxyPort", config.getProperty("http_proxy_port", ""));
		System.setProperty("http.proxyUser", config.getProperty("http_proxy_user", ""));
		System.setProperty("http.proxyPassword", config.getProperty("http_proxy_password", ""));

		// Set the keystore used by SSL
		System.setProperty("javax.net.ssl.trustStore", config.getProperty("trustStore", ""));
		System.setProperty("javax.net.ssl.trustStorePassword", config.getProperty("trustStore_password", ""));

		Long TicketTime = Long.valueOf(config.getProperty("TicketTime", "36000"));

		// Load keystore file from classpath and save to a temporary file
		File tempFile = null;
		try (InputStream keystoreInput = WSAAService.class.getResourceAsStream("/" + p12file)) {
			if (keystoreInput == null) {
				System.out.println("Sorry, unable to find keystore file: " + p12file);
				return new WSAAAuthResponse("", "", "Failed to load keystore file");
			}

			tempFile = File.createTempFile("keystore", ".p12");
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = keystoreInput.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}

			// Create LoginTicketRequest_xml_cms
			byte[] LoginTicketRequest_xml_cms = afip_wsaa_client.create_cms(tempFile.getAbsolutePath(), p12pass,
					signer, dstDN, service, TicketTime);

			// Invoke AFIP wsaa and get LoginTicketResponse
			try {
				LoginTicketResponse = afip_wsaa_client.invoke_wsaa(LoginTicketRequest_xml_cms, endpoint);
			} catch (Exception e) {
				e.printStackTrace();
				return new WSAAAuthResponse("", "", "Failed to invoke WSAA: " + e.getMessage());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return new WSAAAuthResponse("", "", "Failed to load keystore file: " + ex.getMessage());
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
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
