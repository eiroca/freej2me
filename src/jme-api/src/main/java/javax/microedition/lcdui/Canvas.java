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
import org.recompile.mobile.PlatformImage;

public abstract class Canvas extends Displayable {

  public static final int UP = 1;
  public static final int LEFT = 2;
  public static final int RIGHT = 5;
  public static final int DOWN = 6;
  public static final int FIRE = 8;

  public static final int GAME_A = 9;
  public static final int GAME_B = 10;
  public static final int GAME_C = 11;
  public static final int GAME_D = 12;

  public static final int KEY_NUM0 = 48;
  public static final int KEY_NUM1 = 49;
  public static final int KEY_NUM2 = 50;
  public static final int KEY_NUM3 = 51;
  public static final int KEY_NUM4 = 52;
  public static final int KEY_NUM5 = 53;
  public static final int KEY_NUM6 = 54;
  public static final int KEY_NUM7 = 55;
  public static final int KEY_NUM8 = 56;
  public static final int KEY_NUM9 = 57;
  public static final int KEY_STAR = 42;
  public static final int KEY_POUND = 35;

  protected Canvas() {
    width = Mobile.getPlatform().lcdWidth;
    height = Mobile.getPlatform().lcdHeight;
    platformImage = new PlatformImage(width, height);
    Mobile.log("Create Canvas:" + width + ", " + height);
  }

  public int getGameAction(final int keyCode) {
    switch (keyCode) {
      case Mobile.KEY_NUM2:
        return Canvas.UP;
      case Mobile.KEY_NUM8:
        return Canvas.DOWN;
      case Mobile.KEY_NUM4:
        return Canvas.LEFT;
      case Mobile.KEY_NUM6:
        return Canvas.RIGHT;
      case Mobile.KEY_NUM5:
        return Canvas.FIRE;
      case Mobile.KEY_NUM1:
        return Canvas.GAME_A;
      case Mobile.KEY_NUM3:
        return Canvas.GAME_B;
      case Mobile.KEY_NUM7:
        return Canvas.GAME_C;
      case Mobile.KEY_NUM9:
        return Canvas.GAME_D;
      case Mobile.NOKIA_UP:
        return Canvas.UP;
      case Mobile.NOKIA_DOWN:
        return Canvas.DOWN;
      case Mobile.NOKIA_LEFT:
        return Canvas.LEFT;
      case Mobile.NOKIA_RIGHT:
        return Canvas.RIGHT;
    }
    return 0;
  }

  public int getKeyCode(final int gameAction) {
    switch (gameAction) {
      //case Mobile.GAME_UP: return Mobile.KEY_NUM2;
      //case Mobile.GAME_DOWN: return Mobile.KEY_NUM8;
      //case Mobile.GAME_LEFT: return Mobile.KEY_NUM4;
      //case Mobile.GAME_RIGHT: return Mobile.KEY_NUM6;
      case Mobile.GAME_UP:
        return Mobile.NOKIA_UP;
      case Mobile.GAME_DOWN:
        return Mobile.NOKIA_DOWN;
      case Mobile.GAME_LEFT:
        return Mobile.NOKIA_LEFT;
      case Mobile.GAME_RIGHT:
        return Mobile.NOKIA_RIGHT;
      case Mobile.GAME_FIRE:
        return Mobile.KEY_NUM5;
      case Mobile.GAME_A:
        return Mobile.KEY_NUM1;
      case Mobile.GAME_B:
        return Mobile.KEY_NUM3;
      case Mobile.GAME_C:
        return Mobile.KEY_NUM7;
      case Mobile.GAME_D:
        return Mobile.KEY_NUM9;
    }
    return Mobile.KEY_NUM5;
  }

  public String getKeyName(int keyCode) {
    if (keyCode < 0) {
      keyCode = 0 - keyCode;
    }
    switch (keyCode) {
      case 1:
        return "UP";
      case 2:
        return "DOWN";
      case 5:
        return "LEFT";
      case 6:
        return "RIGHT";
      case 8:
        return "FIRE";
      case 9:
        return "A";
      case 10:
        return "B";
      case 11:
        return "C";
      case 12:
        return "D";
      case 48:
        return "0";
      case 49:
        return "1";
      case 50:
        return "2";
      case 51:
        return "3";
      case 52:
        return "4";
      case 53:
        return "5";
      case 54:
        return "6";
      case 55:
        return "7";
      case 56:
        return "8";
      case 57:
        return "9";
      case 42:
        return "*";
      case 35:
        return "#";
    }
    return "-";
  }

  public boolean hasPointerEvents() {
    return true;
  }

  public boolean hasPointerMotionEvents() {
    return false;
  }

  public boolean hasRepeatEvents() {
    return false;
  }

  @Override
  public void hideNotify() {
  }

  public boolean isDoubleBuffered() {
    return true;
  }

  @Override
  public void keyPressed(final int keyCode) {
  }

  @Override
  public void keyReleased(final int keyCode) {
  }

  public void keyRepeated(final int keyCode) {
  }

  protected abstract void paint(Graphics g);

  @Override
  public void pointerDragged(final int x, final int y) {
  }

  @Override
  public void pointerPressed(final int x, final int y) {
  }

  @Override
  public void pointerReleased(final int x, final int y) {
  }

  public void repaint() {
    try {
      paint(platformImage.getGraphics());
      Mobile.getPlatform().repaint(platformImage, 0, 0, width, height);
    }
    catch (final Exception e) {
      Mobile.error("Create Canvas:" + width + ", " + height, e);
    }
  }

  public void repaint(final int x, final int y, final int width, final int height) {
    paint(platformImage.getGraphics());
    Mobile.getPlatform().repaint(platformImage, x, y, width, height);
  }

  public void serviceRepaints() {
    Mobile.getPlatform().repaint(platformImage, 0, 0, width, height);
  }

  public void setFullScreenMode(final boolean mode) {
    Mobile.debug("Set Canvas Full Screen Mode");
    fullScreen = mode;
    if (fullScreen) {
      width = Mobile.getPlatform().lcdWidth;
      height = Mobile.getPlatform().lcdHeight;
    }
  }

  @Override
  public void showNotify() {
  }

  @Override
  protected void sizeChanged(final int w, final int h) {
    width = w;
    height = h;
  }

  @Override
  public void notifySetCurrent() {
    repaint();
  }

}
