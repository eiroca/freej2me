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

public abstract class CustomItem extends Item {

  protected static final int KEY_PRESS = 4;
  protected static final int KEY_RELEASE = 8;
  protected static final int KEY_REPEAT = 0x10;
  protected static final int NONE = 0x00;
  protected static final int POINTER_DRAG = 0x80;
  protected static final int POINTER_PRESS = 0x20;
  protected static final int POINTER_RELEASE = 0x40;
  protected static final int TRAVERSE_HORIZONTAL = 1;
  protected static final int TRAVERSE_VERTICAL = 2;

  protected CustomItem(final String label) {
    setLabel(label);
  }

  public int getGameAction(final int keycode) {
    return 0;
  }

  protected final int getInteractionModes() {
    return 0xFC;
  }

  protected abstract int getMinContentHeight();

  protected abstract int getMinContentWidth();

  protected abstract int getPrefContentHeight(int width);

  protected abstract int getPrefContentWidth(int height);

  protected void hideNotify() {
  }

  protected final void invalidate() {
  }

  protected void keyPressed(final int keyCode) {
  }

  protected void keyReleased(final int keyCode) {
  }

  protected void keyRepeated(final int keyCode) {
  }

  protected abstract void paint(Graphics g, int w, int h);

  protected void pointerDragged(final int x, final int y) {
  }

  protected void pointerPressed(final int x, final int y) {
  }

  protected void pointerReleased(final int x, final int y) {
  }

  protected final void repaint() {
  }

  protected final void repaint(final int x, final int y, final int w, final int h) {
  }

  protected void showNotify() {
  }

  protected void sizeChanged(final int w, final int h) {
  }

  protected boolean traverse(final int dir, final int viewportWidth, final int viewportHeight, final int[] visRect_inout) {
    return true;
  }

  protected void traverseOut() {
  }

}
