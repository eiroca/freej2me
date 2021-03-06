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
package javax.microedition.midlet;

import java.util.Properties;
import javax.microedition.lcdui.Display;
import org.recompile.mobile.Mobile;

public abstract class MIDlet {

  public static Properties properties;

  private final Display display = new Display();

  protected MIDlet() {
    Mobile.log("Create MIDlet");
  }

  public final int checkPermission(final String permission) {
    // 0 - denied; 1 - allowed; -1 unknown
    Mobile.log("checkPermission: " + permission);
    return -1;
  }

  public abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

  public String getAppProperty(final String key) {
    return MIDlet.properties.getProperty(key);
  }

  public static void initAppProperties(final Properties initProperties) {
    MIDlet.properties = initProperties;
  }

  public final void notifyDestroyed() {
    Mobile.log("MIDlet sent Destroyed Notification");
    System.exit(0);
  }

  public final void notifyPaused() {
  }

  public abstract void pauseApp();

  public final boolean platformRequest(final String URL) {
    return false;
  }

  public final void resumeRequest() {
  }

  public abstract void startApp() throws MIDletStateChangeException;

  public Display getDisplay() {
    return display;
  }

}
