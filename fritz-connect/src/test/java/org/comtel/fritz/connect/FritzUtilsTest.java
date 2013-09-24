package org.comtel.fritz.connect;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class FritzUtilsTest {

	@Test
	public void testChallengeResponse() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String challenge = "1234567z";
		String pwd = "äbc";

		assertEquals("1234567z-9e224a41eeefa284df7bb0f26c2913e2", FritzUtils.getChallengeResponse(challenge, pwd));

	}
}
