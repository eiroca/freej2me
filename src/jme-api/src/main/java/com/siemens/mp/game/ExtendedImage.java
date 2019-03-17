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
import org.recompile.mobile.PlatformImage;

public class ExtendedImage extends com.siemens.mp.misc.NativeMem {

  private final PlatformImage image;

  public ExtendedImage(final Image img) {
    image = new PlatformImage(img);
  }

  public Image getImage() {
    return image;
  }

  public int getPixel(final int x, final int y) {
    return 1;
  }

  public void setPixel(final int x, final int y, final byte color) {
  }

  public void getPixelBytes(final byte[] pixels, final int x, final int y, final int width, final int height) {
  }

  public void setPixels(final byte[] pixels, final int x, final int y, final int width, final int height) {
  }

  public void clear(final byte color) {
  }

}
