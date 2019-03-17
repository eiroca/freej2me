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
package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Connector {

  public static final int READ = 1;
  public static final int READ_WRITE = 3;
  public static final int WRITE = 2;

  public static Connection open(final String name) {
    return null;
  }

  public static Connection open(final String name, final int mode) {
    return null;
  }

  public static Connection open(final String name, final int mode, final boolean timeouts) {
    return null;
  }

  public static DataInputStream openDataInputStream(final String name) {
    return null;
  }

  public static DataOutputStream openDataOutputStream(final String name) {
    return null;
  }

  public static InputStream openInputStream(final String name) {
    return null;
  }

  public static OutputStream openOutputStream(final String name) {
    return null;
  }

}
