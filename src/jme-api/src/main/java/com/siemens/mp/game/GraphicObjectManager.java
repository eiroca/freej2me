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
package com.siemens.mp.game;

import javax.microedition.lcdui.Image;

public class GraphicObjectManager extends com.siemens.mp.misc.NativeMem {

  public GraphicObjectManager() {
  }

  public static byte[] createTextureBits(final int width, final int height, final byte[] texture) {
    return texture;
  }

  public void addObject(final GraphicObject g) {
  }

  public void insertObject(final GraphicObject g, final int pos) {
  }

  public void deleteObject(final GraphicObject g) {
  }

  public GraphicObject getObjectAt(final int index) {
    return null;
  }

  public int getObjectPosition(final GraphicObject gobject) {
    return 0;
  }

  public void paint(final ExtendedImage img, final int x, final int y) {
  }

  public void paint(final Image image, final int x, final int y) {
  }

}
