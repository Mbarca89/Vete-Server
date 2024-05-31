package com.mbarca.vete.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.apache.commons.codec.binary.Base64;


public class afip_wsaa_client {

	public static String invoke_wsaa(byte[] LoginTicketRequest_xml_cms, String endpoint) throws Exception {
		String loginTicketResponse = null;
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName("loginCms");
			call.addParameter("request", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			String encodedRequest = Base64.encodeBase64String(LoginTicketRequest_xml_cms);

			loginTicketResponse = (String) call.invoke(new Object[]{encodedRequest});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (loginTicketResponse);
	}

	public static byte[] create_cms(String p12file, String p12pass, String signer, String dstDN, String service, Long TicketTime) {
		PrivateKey pKey = null;
		X509Certificate pCertificate = null;
		byte[] asn1_cms = null;
		CertStore cstore = null;
		String LoginTicketRequest_xml;
		String SignerDN = null;

		try {
			KeyStore ks = KeyStore.getInstance("pkcs12");
			FileInputStream p12stream = new FileInputStream(p12file);
			ks.load(p12stream, p12pass.toCharArray());
			p12stream.close();

			System.out.println("Aliases disponibles en el KeyStore:");
			Enumeration<String> aliases = ks.aliases();
			while (aliases.hasMoreElements()) {
				System.out.println("Alias: " + aliases.nextElement());
			}

			pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
			pCertificate = (X509Certificate) ks.getCertificate(signer);

			if (pCertificate == null) {
				System.err.println("No se encontró el certificado para el alias: " + signer);
				return null;
			}

			SignerDN = pCertificate.getSubjectDN().toString();

			ArrayList<X509Certificate> certList = new ArrayList<>();
			certList.add(pCertificate);

			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}

			cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
		} catch (Exception e) {
			e.printStackTrace();
		}

		LoginTicketRequest_xml = create_LoginTicketRequest(SignerDN, dstDN, service, TicketTime);

		try {
			CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
			gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
					.build(new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(pKey), pCertificate));
			gen.addCertificates(new JcaCertStore(cstore.getCertificates(null)));

			CMSProcessableByteArray data = new CMSProcessableByteArray(LoginTicketRequest_xml.getBytes());
			CMSSignedData signed = gen.generate(data, true);

			asn1_cms = signed.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (asn1_cms);
	}

	public static String create_LoginTicketRequest(String SignerDN, String dstDN, String service, Long TicketTime) {
		String LoginTicketRequest_xml;

		try {
			Date GenTime = new Date();
			GregorianCalendar gentime = new GregorianCalendar();
			GregorianCalendar exptime = new GregorianCalendar();
			String UniqueId = Long.valueOf(GenTime.getTime() / 1000).toString();

			exptime.setTime(new Date(GenTime.getTime() + TicketTime));

			XMLGregorianCalendar XMLGenTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gentime);
			XMLGregorianCalendar XMLExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(exptime);

			LoginTicketRequest_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
					+ "<loginTicketRequest version=\"1.0\">"
					+ "<header>"
					+ "<source>" + SignerDN + "</source>"
					+ "<destination>" + dstDN + "</destination>"
					+ "<uniqueId>" + UniqueId + "</uniqueId>"
					+ "<generationTime>" + XMLGenTime + "</generationTime>"
					+ "<expirationTime>" + XMLExpTime + "</expirationTime>"
					+ "</header>"
					+ "<service>" + service + "</service>"
					+ "</loginTicketRequest>";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return (LoginTicketRequest_xml);
	}
}
