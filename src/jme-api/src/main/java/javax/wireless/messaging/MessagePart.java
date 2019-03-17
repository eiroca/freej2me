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
package javax.wireless.messaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MessagePart {

  private final String id;
  private final String location;
  private final String encoding;
  private final String mimetype;

  private byte[] content;

  public MessagePart(final byte[] contents, final int offset, final int length, final String mimeType, final String contentId, final String contentLocation, final String enc) {
    id = contentId;
    location = contentLocation;
    encoding = enc;
    mimetype = mimeType;

    content = contents;
  }

  public MessagePart(final byte[] contents, final String mimeType, final String contentId, final String contentLocation, final String enc) {
    id = contentId;
    location = contentLocation;
    encoding = enc;
    mimetype = mimeType;

    content = contents;
  }

  public MessagePart(final InputStream is, final String mimeType, final String contentId, final String contentLocation, final String enc) {
    id = contentId;
    location = contentLocation;
    encoding = enc;
    mimetype = mimeType;

    content = new byte[32768];
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    int count = 0;
    while (count != -1) {
      try {
        count = is.read(content, 0, content.length);
        buffer.write(content, 0, count);
      }
      catch (final Exception e) {
        count = -1;
      }
    }
    content = buffer.toByteArray();
  }

  public byte[] getContent() {
    return content;
  }

  public InputStream getContentAsStream() {
    return new ByteArrayInputStream(content);
  }

  public String getContentID() {
    return id;
  }

  public String getContentLocation() {
    return location;
  }

  public String getEncoding() {
    return encoding;
  }

  public int getLength() {
    return content.length;
  }

  public String getMIMEType() {
    return mimetype;
  }

}
