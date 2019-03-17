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
package javax.microedition.lcdui;

import org.recompile.mobile.Mobile;

public class Alert extends Screen {

  public static final Command DISMISS_COMMAND = new Command("OK", Command.OK, 0);
  public static final int FOREVER = -2;

  private String message;
  private Image image;
  private AlertType type;
  private int timeout = Alert.FOREVER;
  private Gauge indicator;
  private Displayable nextScreen = null;

  public Alert(final String title) {
    Mobile.log("Alert: " + title);
    setTitle(title);
    Thread.dumpStack();
  }

  public Alert(final String title, final String alertText, final Image alertImage, final AlertType alertType) {
    Mobile.log("Alert: " + title + " - " + alertText);
    setTitle(title);
    setString(alertText);
    setImage(alertImage);
    setType(alertType);
    setTimeout(getDefaultTimeout());
    addCommand(Alert.DISMISS_COMMAND);
    setCommandListener(defaultListener);
  }

  public int getDefaultTimeout() {
    return Alert.FOREVER;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(final int time) {
    timeout = time;
  }

  public AlertType getType() {
    return type;
  }

  public void setType(final AlertType t) {
    type = t;
  }

  public String getString() {
    return message;
  }

  public void setString(final String text) {
    Mobile.debug(text);
    message = text;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(final Image img) {
    image = img;
  }

  public void setIndicator(final Gauge gauge) {
    indicator = gauge;
  }

  public Gauge getIndicator() {
    return indicator;
  }

  @Override
  public void addCommand(final Command cmd) {
    super.addCommand(cmd);

    if (getCommands().size() == 2) {
      super.removeCommand(Alert.DISMISS_COMMAND);
    }

  }

  @Override
  public void removeCommand(final Command cmd) {
    if (getCommands().size() > 1) {
      super.removeCommand(cmd);
    }
  }

  @Override
  public void setCommandListener(CommandListener listener) {
    if (listener == null) {
      listener = defaultListener;
    }
    super.setCommandListener(listener);
  }

  public CommandListener defaultListener = (cmd, next) -> Mobile.getDisplay().setCurrent(next);

  public void setNextScreen(final Displayable next) {
    nextScreen = next;
  }

}
