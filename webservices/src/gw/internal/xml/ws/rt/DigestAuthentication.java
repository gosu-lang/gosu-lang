/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.rt;

import gw.util.StreamUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class DigestAuthentication {

  private static final int CNONCE_NUM_BYTES = 16;
  private static String _staticCnonceForTesting;

  private static String convertToHex(byte[] data) {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = (data[i] >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        if ((0 <= halfbyte) && (halfbyte <= 9))
          buf.append((char) ('0' + halfbyte));
        else
          buf.append((char) ('a' + (halfbyte - 10)));
        halfbyte = data[i] & 0x0F;
      } while (two_halfs++ < 1);
    }
    return buf.toString();
  }

  private static String randHexBytes(int nbBytes) {
    byte[] bytes = new byte[nbBytes];
    new SecureRandom().nextBytes(bytes);
    return convertToHex(bytes);
  }

  private static String digest(String src) {
    String target = null;
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(StreamUtil.toBytes(src));
      target = convertToHex(digest.digest());
    } catch (NoSuchAlgorithmException e) {
      System.out.println(e.getMessage());
    }
    return target;
  }

  private static String getUri(String url) {
    String[] strs = url.split("://")[1].split("/");
    if (strs.length == 1) {
      return "/";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < strs.length; i++) {
      sb.append("/").append(strs[i]);
    }
    return sb.toString();
  }


  public static String generateDigestAuthorizationHeader(String wwwAuthenticateHeader, String username, String password, String url) {
    wwwAuthenticateHeader = wwwAuthenticateHeader.replaceFirst("Digest ", "");

    String uri = getUri(url);

    String[] attrs = wwwAuthenticateHeader.split(",");
    Map<String, String> attrMap = new HashMap<String, String>();
    for (String attr : attrs) {
      String[] keyValue = attr.split("=");
      String key = keyValue[0].trim();
      String value = keyValue[1].trim().replaceAll("\"", "");
      attrMap.put(key, value);
    }

    String src = username + ":" + attrMap.get("realm") + ":" + password;
    String ha1 = digest(src);

    src = "POST:" + uri;
    String ha2 = digest(src);

    String cnonce = _staticCnonceForTesting == null ? randHexBytes(CNONCE_NUM_BYTES) : _staticCnonceForTesting;
    src = ha1 + ":" + attrMap.get("nonce") + ":00000001:" + cnonce + ":" + attrMap.get("qop") + ":" + ha2;
    String response = digest(src);

    StringBuilder sb = new StringBuilder();
    sb.append("Digest ");

    sb.append("username=\"");
    sb.append(username);
    sb.append("\",");

    sb.append("realm=\"");
    sb.append(attrMap.get("realm"));
    sb.append("\",");

    sb.append("nonce=\"");
    sb.append(attrMap.get("nonce"));
    sb.append("\",");

    sb.append("uri=\"");
    sb.append(uri);
    sb.append("\",");

    sb.append("cnonce=\"");
    sb.append(cnonce);
    sb.append("\",");

    sb.append("nc=00000001,");
    sb.append("response=\"");
    sb.append(response);
    sb.append("\",");

    sb.append("qop=\"");
    sb.append(attrMap.get("qop"));
    sb.append("\"");

    return sb.toString();
  }

  public static void setStaticClientNonceForTesting( String cnonce ) {
    _staticCnonceForTesting = cnonce;
  }

}
