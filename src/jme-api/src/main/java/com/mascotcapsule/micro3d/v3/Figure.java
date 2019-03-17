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
package com.mascotcapsule.micro3d.v3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.recompile.mobile.Mobile;

public class Figure {

  private byte[] figure;
  private final ArrayList<Texture> textures = new ArrayList<>();
  private int selected = 0;
  private int pattern = 0;

  public Figure(final byte[] fig) {
    figure = fig;
  }

  public Figure(final String name) throws IOException {
    final InputStream stream = Mobile.getPlatform().loader.getResourceAsStream(name);
    try {
      final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int count = 0;
      final byte[] data = new byte[4096];
      while (count != -1) {
        count = stream.read(data);
        if (count != -1) {
          buffer.write(data, 0, count);
        }
      }
      figure = buffer.toByteArray();
    }
    catch (final Exception e) {
    }
  }

  public final void dispose() {
  }

  public final void setPosture(final ActionTable actiontable, final int i, final int j) {
  }

  public final Texture getTexture() {
    return null;
  }

  public final void setTexture(final Texture texture) {
  }

  public final void setTexture(final Texture[] atexture) {
  }

  public final int getNumTextures() {
    return textures.size();
  }

  public final void selectTexture(final int i) {
    selected = i;
  }

  public final int getNumPattern() {
    return pattern;
  }

  public final void setPattern(final int value) {
    pattern = value;
  }

}
