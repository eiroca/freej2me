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
 */
package org.recompile.mobile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class InternalLoader extends ClassLoader implements MIDletLoader {

  public String name;
  public String icon;
  public String className;

  public String suitename;

  private Class<?> mainClass;
  private MIDlet mainInst;

  private final Properties properties = new Properties();

  public InternalLoader() {
    super();
    try {
      updateProperties(System.getProperties());
    }
    catch (final Exception e) {
      Mobile.warn("Can't add CLDC System Properties");
    }
    try {
      loadManifest();
      updateProperties(properties);
    }
    catch (final Exception e) {
      Mobile.warn("Can't Read Manifest!");
      return;
    }
  }

  protected void updateProperties(final Properties p) {
    updateProperties(p, Locale.getDefault());
  }

  protected void updateProperties(final Properties p, final Locale l) {
    String locale = null;
    if (l != null) {
      locale = l.toLanguageTag();
    }
    if (locale == null) {
      locale = "en-US";
    }
    p.setProperty("microedition.platform", "j2me");
    p.setProperty("microedition.profiles", "MIDP-2.0");
    p.setProperty("microedition.configuration", "CLDC-1.0");
    p.setProperty("microedition.locale", locale);
    p.setProperty("microedition.encoding", "file.encoding");
  }

  public InternalLoader(final Class<?> midletClass) {
    super(null);
    updateProperties(System.getProperties());
    name = midletClass.getSimpleName();
    icon = null;
    className = midletClass.getCanonicalName();
    suitename = name;
    mainClass = midletClass;
  }

  @Override
  public void start() throws MIDletStateChangeException {
    try {
      mainInst = (MIDlet)mainClass.newInstance();
    }
    catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    mainInst.startApp();
  }

  private void loadManifest() {
    final String resource = "META-INF/MANIFEST.MF";

    final URL url = findResource(resource);

    if (url == null) { return; }

    String line;
    String[] parts;
    int split;
    String key;
    String value;
    try {
      final InputStream is = url.openStream();
      final BufferedReader br = new BufferedReader(new InputStreamReader(is));
      while ((line = br.readLine()) != null) {
        if (line.startsWith("MIDlet-1:")) {
          Mobile.log(line);
          line = line.substring(9);
          parts = line.split(",");
          if (parts.length == 3) {
            name = parts[0].trim();
            icon = parts[1].trim();
            className = parts[2].trim();
            suitename = name;
          }
          Mobile.debug("Loading " + name);
        }

        split = line.indexOf(":");
        if (split > 0) {
          key = line.substring(0, split).trim();
          value = line.substring(split + 1).trim();
          properties.put(key, value);
        }

      }
      // for RecordStore, remove illegal chars from name
      suitename = suitename.replace(":", "");
    }
    catch (final Exception e) {
      Mobile.error("Can't Read Jar Manifest!", e);
    }

  }

  @Override
  public InputStream getResourceAsStream(String resource) {
    Mobile.debug("Loading Resource: " + resource);
    URL url;
    if (resource.startsWith("/")) {
      resource = resource.substring(1);
    }
    try {
      url = findResource(resource);
      return url.openStream();
    }
    catch (final Exception e) {
      Mobile.log(resource + " Not Found");
      return super.getResourceAsStream(resource);
    }
  }

  @Override
  public URL getResource(String resource) {
    if (resource.startsWith("/")) {
      resource = resource.substring(1);
    }
    try {
      final URL url = findResource(resource);
      return url;
    }
    catch (final Exception e) {
      Mobile.log(resource + " Not Found");
      return super.getResource(resource);
    }
  }

  /*
   ******** loadClass Modifies Methods with ObjectWeb ASM ******** Replaces
   * java.lang.Class.getResourceAsStream calls with calls to
   * Mobile.getResourceAsStream which calls
   * MIDletLoader.getResourceAsStream(class, string)
   */

  @Override
  public InputStream getMIDletResourceAsStream(final String resource) {
    Mobile.debug("Get Resource: " + resource);
    final URL url = getResource(resource);
    // Read all bytes, return ByteArrayInputStream //
    try {
      final InputStream stream = url.openStream();

      final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int count = 0;
      final byte[] data = new byte[4096];
      while (count != -1) {
        count = stream.read(data);
        if (count != -1) {
          buffer.write(data, 0, count);
        }
      }
      return new ByteArrayInputStream(buffer.toByteArray());
    }
    catch (final Exception e) {
      return super.getResourceAsStream(resource);
    }
  }

  @Override
  public String getSuiteName() {
    return suitename;
  }

  @Override
  public String getName() {
    return name;
  }
}
