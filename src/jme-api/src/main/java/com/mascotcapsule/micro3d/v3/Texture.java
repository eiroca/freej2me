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
import org.recompile.mobile.Mobile;

public class Texture {

  private byte[] texture;
  private final boolean isForModel;

  public Texture(final byte[] Texture, final boolean IsForModel) {
    texture = Texture;
    isForModel = IsForModel;
  }

  public Texture(final String name, final boolean IsForModel) throws IOException {
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
      texture = buffer.toByteArray();
    }
    catch (final Exception e) {
    }

    isForModel = IsForModel;
  }

  public final void dispose() {
  }

}
