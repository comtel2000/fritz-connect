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
	
}
