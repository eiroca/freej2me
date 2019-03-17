/**
 * This file is part of FreeJ2ME.
 *
 * FreeJ2ME is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * FreeJ2ME is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with FreeJ2ME. If not,
 * see http://www.gnu.org/licenses/
 *
 */
package javax.microedition.pki;

import java.io.IOException;

public class CertificateException extends IOException {

  /**
   *
   */
  private static final long serialVersionUID = 1646961946809632058L;
  public static final byte BAD_EXTENSIONS = 1;
  public static final byte BROKEN_CHAIN = 11;
  public static final byte CERTIFICATE_CHAIN_TOO_LONG = 2;
  public static final byte EXPIRED = 3;
  public static final byte INAPPROPRIATE_KEY_USAGE = 10;
  public static final byte MISSING_SIGNATURE = 5;
  public static final byte NOT_YET_VALID = 6;
  public static final byte ROOT_CA_EXPIRED = 12;
  public static final byte SITENAME_MISMATCH = 7;
  public static final byte UNAUTHORIZED_INTERMEDIATE_CA = 4;
  public static final byte UNRECOGNIZED_ISSUER = 8;
  public static final byte UNSUPPORTED_PUBLIC_KEY_TYPE = 13;
  public static final byte UNSUPPORTED_SIGALG = 9;
  public static final byte VERIFICATION_FAILED = 14;

  private String message;
  private final Certificate certificate;
  private final byte reason;

  CertificateException(final Certificate cert, final byte status) {
    certificate = cert;
    reason = status;
  }

  CertificateException(final String msg, final Certificate cert, final byte status) {
    message = msg;
    certificate = cert;
    reason = status;
  }

  public Certificate getCertificate() {
    return certificate;
  }

  public byte getReason() {
    return reason;
  }

}
