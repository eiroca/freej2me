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

public class PushRegistry {

  public static String getFilter(String connection) {
    return "";
  }

  public static String getMIDlet(String connection) {
    return "";
  }

  public static String[] listConnections(boolean available) {
    return new String[] {
        ""
    };
  }

  public static long registerAlarm(String midlet, long time) {
    return 0;
  }

  public static void registerConnection(String connection, String midlet, String filter) {
  }

  public static boolean unregisterConnection(String connection) {
    return true;
  }

}
