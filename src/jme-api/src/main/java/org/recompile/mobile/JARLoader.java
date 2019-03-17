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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Properties;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JARLoader extends URLClassLoader implements MIDletLoader {

  public String name;
  public String icon;
  private String className;

  public String suitename;

  private Class<?> mainClass;
  private MIDlet mainInst;

  private final Properties properties = new Properties();

  public JARLoader(final URL urls[]) {
    super(urls);

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

  public JARLoader(final Class<?> midletClass) {
    super(null);
    updateProperties(properties);
    name = midletClass.getSimpleName();
    icon = null;
    className = midletClass.getCanonicalName();

    suitename = name;

    mainClass = midletClass;
    try {
      mainInst = (MIDlet)mainClass.newInstance();
    }
    catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
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

  @Override
  public void start() throws MIDletStateChangeException {
    Method start;

    try {
      mainClass = loadClass(className, true);

      Constructor<?> constructor;
      constructor = mainClass.getConstructor();
      constructor.setAccessible(true);

      MIDlet.initAppProperties(properties);
      mainInst = (MIDlet)constructor.newInstance();
    }
    catch (final Exception e) {
      Mobile.warn("Problem Constructing " + name + " class: " + className);
      Mobile.warn("Reason: " + e.getMessage());
      e.printStackTrace();
      System.exit(0);
      return;
    }

    try {
      start = mainClass.getDeclaredMethod("startApp");
      start.setAccessible(true);
    }
    catch (final Exception e) {
      try {
        mainClass = loadClass(mainClass.getSuperclass().getName(), true);
        start = mainClass.getMethod("startApp");
        start.setAccessible(true);
      }
      catch (final Exception f) {
        Mobile.warn("Can't Find startApp Method");
        f.printStackTrace();
        System.exit(0);
        return;
      }
    }

    try {
      start.invoke(mainInst);
    }
    catch (final Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
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
          Mobile.log("Loading " + name);
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
    URL url;
    Mobile.debug("Loading Resource: " + resource);
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
      Mobile.warn(resource + " Not Found");
      return super.getResource(resource);
    }
  }

  /*
  	********  loadClass Modifies Methods with ObjectWeb ASM  ********
  	Replaces java.lang.Class.getResourceAsStream calls with calls
  	to Mobile.getResourceAsStream which calls
  	MIDletLoader.getResourceAsStream(class, string)
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
  public Class<?> loadClass(final String name) throws ClassNotFoundException {
    InputStream stream;
    String resource;
    byte[] code;
    Mobile.debug("Load Class " + name);
    if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("com.nokia") ||
        name.startsWith("com.mascotcapsule") || name.startsWith("com.samsung") || name.startsWith("sun.") ||
        name.startsWith("com.siemens") || name.startsWith("org.recompile")) { return loadClass(name, true); }
    try {
      Mobile.debug("Instrumenting Class " + name);
      resource = name.replace(".", "/") + ".class";
      stream = super.getResourceAsStream(resource);
      code = instrument(stream);
      return defineClass(name, code, 0, code.length);
    }
    catch (final Exception e) {
      Mobile.warn("Error Adapting Class " + name);
      return null;
    }
  }

  private byte[] instrument(final InputStream stream) throws Exception {
    final ClassReader reader = new ClassReader(stream);
    final ClassWriter writer = new ClassWriter(0);
    final ClassVisitor visitor = new ASMVisitor(writer);
    reader.accept(visitor, 0);
    return writer.toByteArray();
  }

  private class ASMVisitor extends ClassAdapter {

    public ASMVisitor(final ClassVisitor visitor) {
      super(visitor);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
      super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
      return new ASMMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
    }

    private class ASMMethodVisitor extends MethodAdapter implements Opcodes {

      public ASMMethodVisitor(final MethodVisitor visitor) {
        super(visitor);
      }

      @Override
      public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        if ((opcode == Opcodes.INVOKEVIRTUAL) && name.equals("getResourceAsStream") && owner.equals("java/lang/Class")) {
          mv.visitMethodInsn(Opcodes.INVOKESTATIC, "org/recompile/mobile/Mobile", name, "(Ljava/lang/Class;Ljava/lang/String;)Ljava/io/InputStream;");
        }
        else {
          mv.visitMethodInsn(opcode, owner, name, desc);
        }
      }
    }
  }

  public void run() throws MIDletStateChangeException {
    mainInst.startApp();
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
