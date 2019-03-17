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

  public static void main(String[] args) {
    RetroPie app = new RetroPie(args);
  }

  private SDL sdl;

  private int lcdWidth;
  private int lcdHeight;

  private Runnable painter;

  public RetroPie(String args[]) {
    lcdWidth = 240;
    lcdHeight = 320;

    if (args.length < 3) {
      System.out.println("Insufficient parameters provided");
      return;
    }
    lcdWidth = Integer.parseInt(args[1]);
    lcdHeight = Integer.parseInt(args[2]);

    Mobile.setPlatform(new MobilePlatform(lcdWidth, lcdHeight));

    painter = new Runnable() {

      public void run() {
        try {
          // Send Frame to SDL interface
          int[] data = Mobile.getPlatform().getLCD().getRGB(0, 0, lcdWidth, lcdHeight, null, 0, lcdWidth);
          byte[] frame = new byte[data.length * 3];
          int cb = 0;
          for (int i = 0; i < data.length; i++) {
            frame[cb + 0] = (byte)(data[i] >> 16);
            frame[cb + 1] = (byte)(data[i] >> 8);
            frame[cb + 2] = (byte)(data[i]);
            cb += 3;
          }
          sdl.frame.write(frame);
        }
        catch (Exception e) {
        }
      }
    };

    Mobile.getPlatform().setPainter(painter);

    String file = getFormattedLocation(args[0]);
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

  private static String getFormattedLocation(String loc) {
    if (loc.startsWith("file://") || loc.startsWith("http://")) { return loc; }
    File file = new File(loc);
    if (!file.exists() || file.isDirectory()) {
      System.out.println("File not found...");
      System.exit(0);
    }
    return "file://" + file.getAbsolutePath();
  }

}
