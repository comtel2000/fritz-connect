package org.comtel.fritz.connect;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FritzUtils {

	/**
	 * http://www.avm.de/de/Extern/files/session_id/AVM_Technical_Note_-_Session_ID.pdf
	 * 
	 * @param challenge
	 * @param pwd
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getChallengeResponse(String challenge, String pwd) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return challenge + "-" + getMD5Hash(challenge + "-" + pwd); 
	}

	private static String getMD5Hash(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hash = md.digest(message.getBytes("UTF-16LE"));

		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	public static String getEnergy(int energy) {
		return String.format("%.3f kWh", energy > 0 ? (double) energy / 1000 : 0d);
	}
	
	public static String getPower(int power) {
		return String.format("%.2f W", power > 0 ? (double) power / 1000 : 0d);
	}
	
	public static String getTemperature(int celsius) {
		double t = (double) celsius / 10;
		return String.format("%.1f °C", t);
	}
	
	public static String getTemperatureF(int celsius) {
		double t = (double) celsius / 10;
		double f = t = 9*t/5 + 32;
		return String.format("%.1f °F", f);
	}
	
}
