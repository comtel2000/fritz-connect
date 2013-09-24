package org.comtel.fritz.connect;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.comtel.fritz.connect.cmd.SwitchCmd;
import org.comtel.fritz.connect.device.SwitchDevice;
import org.comtel.fritz.connect.device.SwitchDevice.State;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Main {

	private final String USER = "";
	private final String PWD = "comtel";

	private boolean sslEnabled = false;
	private static String PROTOCOL = "http";

	private static String HOST = "192.168.178.1";
	private static int PORT = -1;

	private final static String EMPTY_SID = "0000000000000000";

	private final static String SID_REQUEST_URL = "/login_sid.lua";
	private final static String SID_CREDENTIAL_URL = "/login_sid.lua?username=%s&response=%s";

	private final static String SID_SWITCHCMD_URL = "/webservices/homeautoswitch.lua?switchcmd=%s&sid=%s";
	private final static String SID_AIN_SWITCHCMD_URL = "/webservices/homeautoswitch.lua?ain=%s&switchcmd=%s&sid=%s";

	private final static String SID_AHA_SCRIPT_QUERY_URL = "/net/home_auto_query.lua?xhr=1&command=%s&id=%s&sid=%s";

	private final DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();

	/**
	 * FritzBox firmware >= 84.05.55
	 */
	public Main() {
		try {

			setSslEnabled(true);
			PORT = 499;

			String sid = getSessionId();
			String resp;

			/*
			 * int deviceID = 16; resp = sendScriptCmd(ScriptCmd.outletStates,
			 * deviceID, sid); System.err.println(resp); resp =
			 * sendScriptCmd(ScriptCmd.switchOnOff + "&value_to_set=1",
			 * deviceID, sid); System.err.println(resp);
			 */

			resp = sendSwitchCmd(SwitchCmd.GETSWITCHLIST, sid);
			List<String> ainList = Arrays.asList(resp.split(","));
			System.err.println(ainList);

			Set<SwitchDevice> deviceList = new HashSet<>();
			for (String ain : ainList) {
				if (!deviceList.add(new SwitchDevice(ain))) {
					System.err.println("double ain: " + ain);
				}
			}

			for (SwitchDevice dev : deviceList) {
				dev.setPresent("1".equals(sendSwitchCmd(dev.getAin(), SwitchCmd.GETSWITCHPRESENT, sid)));
				dev.setName(sendSwitchCmd(dev.getAin(), SwitchCmd.GETSWITCHNAME, sid));
				dev.setState(sendSwitchCmd(dev.getAin(), SwitchCmd.GETSWITCHSTATE, sid));
				dev.setPower(Integer.valueOf(sendSwitchCmd(dev.getAin(), SwitchCmd.GETSWITCHPOWER, sid)));
				dev.setEnergy(Integer.valueOf(sendSwitchCmd(dev.getAin(), SwitchCmd.GETSWITCHENERGY, sid)));

				if (dev.getState() != State.ON) {
					dev.setState(sendSwitchCmd(dev.getAin(), SwitchCmd.SETSWITCHON, sid));
				}
				System.err.println(dev);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setSslEnabled(boolean flag) {
		sslEnabled = flag;
		PROTOCOL = sslEnabled ? "https" : "http";
		if (sslEnabled) {
			try {
				trustAllSslCertificate();
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	private void trustAllSslCertificate() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
		} };

		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());

		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String host, SSLSession session) {
				System.err.println("auto verify host: " + host);
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	private String sendSwitchCmd(String switchcmd, String sid) throws MalformedURLException, IOException {
		return sendSwitchCmd(null, switchcmd, sid);
	}

	private String sendSwitchCmd(String ain, String switchcmd, String sid) throws MalformedURLException, IOException {
		final String path;
		if (ain == null) {
			path = String.format(SID_SWITCHCMD_URL, switchcmd, sid);
		} else {
			path = String.format(SID_AIN_SWITCHCMD_URL, ain, switchcmd, sid);
		}
		HttpURLConnection con = createConnection(path);

		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("response code: " + con.getResponseCode());
		}

		StringBuilder response = new StringBuilder();
		try (Scanner scanner = new Scanner(con.getInputStream())) {
			while (scanner.hasNextLine()) {
				response.append(scanner.nextLine());
			}
		}
		return response.toString();

	}

	@SuppressWarnings("unused")
	private String sendScriptCmd(String scriptcmd, int id, String sid) throws MalformedURLException, IOException {
		String path = String.format(SID_AHA_SCRIPT_QUERY_URL, scriptcmd, id, sid);
		HttpURLConnection con = createConnection(path);
		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("response code: " + con.getResponseCode());
		}

		StringBuilder response = new StringBuilder();
		try (Scanner scanner = new Scanner(con.getInputStream())) {
			while (scanner.hasNextLine()) {
				response.append(scanner.nextLine());
			}
		}
		return response.toString();

	}

	private String getSessionId() throws IOException, ParserConfigurationException, SAXException, NoSuchAlgorithmException {

		HttpURLConnection con = createConnection(SID_REQUEST_URL);
		Document doc = null;

		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("response code: " + con.getResponseCode());
		}
		doc = xmlFactory.newDocumentBuilder().parse(con.getInputStream());
		System.out.println(XmlUtils.docToString(doc, true));

		if (doc == null) {
			throw new IOException("SessionInfo element not available");
		}

		String sid = XmlUtils.getValue(doc.getDocumentElement(), "SID");

		if (EMPTY_SID.equals(sid)) {
			String challenge = XmlUtils.getValue(doc.getDocumentElement(), "Challenge");
			sid = getSessionId(challenge);
		}

		if (sid == null || EMPTY_SID.equals(sid)) {
			throw new IOException("sid request failed: " + sid);
		}

		return sid;

	}

	private String getSessionId(String challenge) throws IOException, ParserConfigurationException, SAXException, NoSuchAlgorithmException {
		HttpURLConnection con = createConnection(String.format(SID_CREDENTIAL_URL, USER, FritzUtils.getChallengeResponse(challenge, PWD)));

		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("response code: " + con.getResponseCode());
		}
		Document doc2 = xmlFactory.newDocumentBuilder().parse(con.getInputStream());
		System.out.println(XmlUtils.docToString(doc2, true));
		String sid = XmlUtils.getValue(doc2.getDocumentElement(), "SID");

		return sid;

	}

	private final HttpURLConnection createConnection(String path) throws MalformedURLException, IOException {
		URL url = new URL(PROTOCOL, HOST, PORT, path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		System.out.println("Sending request to URL: " + url);
		return con;
	}

	public static void main(String[] args) {
		new Main();
	}
}
