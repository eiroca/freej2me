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
 */
package org.recompile.freej2me;

import java.io.File;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.MobilePlatform;

public class RetroPie {

  public static void main(final String[] args) {
    new RetroPie(args);
  }

  private SDL sdl;

  private int lcdWidth;
  private int lcdHeight;

  private Runnable painter;

  public RetroPie(final String args[]) {
    lcdWidth = 240;
    lcdHeight = 320;

    if (args.length < 3) {
      System.out.println("Insufficient parameters provided");
      return;
    }
    lcdWidth = Integer.parseInt(args[1]);
    lcdHeight = Integer.parseInt(args[2]);

    Mobile.setPlatform(new MobilePlatform(lcdWidth, lcdHeight));

    painter = () -> {
      try {
        // Send Frame to SDL interface
        final int[] data = Mobile.getPlatform().getLCD().getRGB(0, 0, lcdWidth, lcdHeight, null, 0, lcdWidth);
        final byte[] frame = new byte[data.length * 3];
        int cb = 0;
        for (final int element : data) {
          frame[cb + 0] = (byte)(element >> 16);
          frame[cb + 1] = (byte)(element >> 8);
          frame[cb + 2] = (byte)(element);
          cb += 3;
        }
        sdl.frame.write(frame);
      }
      catch (final Exception e) {
      }
    };

    Mobile.getPlatform().setPainter(painter);

    final String file = RetroPie.getFormattedLocation(args[0]);
    System.out.println(file);

    if (Mobile.getPlatform().loadJar(file)) {
      // Check config

      // Start SDL
      sdl = new SDL();
      sdl.start(args);

      // Run jar
      Mobile.getPlatform().runJar();
    }
    else {
      System.out.println("Couldn't load jar...");
      System.exit(0);
    }
  }

  private static String getFormattedLocation(final String loc) {
    if (loc.startsWith("file://") || loc.startsWith("http://")) { return loc; }
    final File file = new File(loc);
    if (!file.exists() || file.isDirectory()) {
      System.out.println("File not found...");
      System.exit(0);
    }
    return "file://" + file.getAbsolutePath();
  }

}
