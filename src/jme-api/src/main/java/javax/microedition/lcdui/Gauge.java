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

public class Gauge extends Item {

  public static final int CONTINUOUS_IDLE = 0;
  public static final int CONTINUOUS_RUNNING = 2;
  public static final int INCREMENTAL_IDLE = 1;
  public static final int INCREMENTAL_UPDATING = 3;
  public static final int INDEFINITE = -1;

  private final boolean interactive;
  private int maxValue;
  private final int initialValue;
  private int value;
  //private ArrayList<Command> commands;
  private Command defaultCommand;
  private ItemCommandListener listener;

  public Gauge(final String label, final boolean isInteractive, final int maxvalue, final int initialvalue) {
    Mobile.log("Create Gauge");
    setLabel(label);
    interactive = isInteractive;
    maxValue = maxvalue;
    initialValue = initialvalue;
  }

  //public void addCommand(Command cmd) { commands.add(cmd); }

  public int getMaxValue() {
    return maxValue;
  }

  public int getValue() {
    return value;
  }

  public boolean isInteractive() {
    return interactive;
  }

  @Override
  public void setDefaultCommand(final Command cmd) {
    defaultCommand = cmd;
  }

  @Override
  public void setItemCommandListener(final ItemCommandListener l) {
    listener = l;
  }

  public void setMaxValue(final int maxvalue) {
    maxValue = maxvalue;
  }

  public void setValue(final int newvalue) {
    value = newvalue;
  }

}
