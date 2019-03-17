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

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.recompile.mobile.Mobile;

public class LCD extends Canvas {

  private static final long serialVersionUID = 6527317485663850138L;
  private final FreeJ2ME freeJ2ME;

  /**
   * @param freeJ2ME
   */
  public LCD(FreeJ2ME freeJ2ME) {
    this.freeJ2ME = freeJ2ME;
  }

  public int cx = 0;
  public int cy = 0;
  public int cw = 240;
  public int ch = 320;

  public double scalex = 1;
  public double scaley = 1;

  public void updateScale(int vw, int vh) {
    cx = (this.getWidth() - vw) / 2;
    cy = (this.getHeight() - vh) / 2;
    cw = vw;
    ch = vh;
    scalex = (double)this.freeJ2ME.lcdWidth / (double)vw;
    scaley = (double)this.freeJ2ME.lcdHeight / (double)vh;
  }

  public void paint(Graphics g) {
    try {
      Graphics2D cgc = (Graphics2D)this.getGraphics();
      if (this.freeJ2ME.config.isRunning) {
        g.drawImage(this.freeJ2ME.config.getLCD(), cx, cy, cw, ch, null);
      }
      else {
        g.drawImage(Mobile.getPlatform().getLCD(), cx, cy, cw, ch, null);
        if (this.freeJ2ME.limitFPS > 0) {
          Thread.sleep(this.freeJ2ME.limitFPS);
        }
      }
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
