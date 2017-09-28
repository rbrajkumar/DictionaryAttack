package com.company.dept.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import com.company.dept.constants.DAConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
	
	public static String getSalted(String saltToken, String word) throws Exception {
		byte[] added = addSalt(saltToken, word);

		MessageDigest md = MessageDigest.getInstance(DAConstants.HASHING);
		md.update(added);

		// Convert the string's digest to HEX
		String hex = getHexValue(md.digest());
		return hex;
	}
	
	private static String getHexValue(byte[] b) {
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
        		sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	private static byte[] addSalt(String salt, String word) throws Exception {
		// get the byte array for both
		byte[] saltBytes = DatatypeConverter.parseHexBinary(salt);
		byte[] wordBytes = word.getBytes("UTF-8");
		
		// create container and fill both
		byte[] added = new byte[saltBytes.length + wordBytes.length];
		System.arraycopy(saltBytes, 0, added, 0, saltBytes.length);
		System.arraycopy(wordBytes, 0, added, saltBytes.length, wordBytes.length);
		return added;
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static String replaceSpecialChars(String word) {
		return word.replaceAll("@", "a").replaceAll("\\|", "l").replaceAll("!", "i").replaceAll("5", "s"); // Add more if needed
	}
	
	public static String matchConditions(String salt, String src, String passwd) throws Exception {
		String found = null;
		// Assume case insensitive: Since dictionary contains lowercase only as source, and purpose/scope
		// is only find out password is used from dictionary.
		if(src.equalsIgnoreCase(getSalted(salt, passwd))) {
			found = passwd;
		}
		
		// Check to replacement of special charecter
		if(replaceSpecialChars(src).equalsIgnoreCase(getSalted(salt, passwd))) {
			found = passwd;
		}
		
		// Reverse
		String reversed = new StringBuilder(passwd).reverse().toString();
		if(src.equalsIgnoreCase(getSalted(salt, reversed))) {
			found = passwd;
		}
		
		return found;
	}
}
