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
package net.eiroca.j2me.host.freej2me;

import java.awt.Canvas;
import java.awt.Graphics;
import org.recompile.mobile.Mobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LCD extends Canvas {

  private static Logger logger = LoggerFactory.getLogger(LCD.class);

  private static final long serialVersionUID = -959791062723408787L;

  private final FreeeJ2meHost j2meHost;

  public int cx = 0;
  public int cy = 0;
  public int cw = 240;
  public int ch = 320;

  public double scalex = 1;
  public double scaley = 1;

  /**
   * @param j2meHost
   */
  public LCD(final FreeeJ2meHost j2meHost) {
    this.j2meHost = j2meHost;
  }

  public void updateScale(final int vw, final int vh) {
    cx = (getWidth() - vw) / 2;
    cy = (getHeight() - vh) / 2;
    cw = vw;
    ch = vh;
    scalex = (double)j2meHost.lcdWidth / (double)vw;
    scaley = (double)j2meHost.lcdHeight / (double)vh;
  }

  @Override
  public void paint(final Graphics g) {
    try {
      getGraphics();
      if (j2meHost.config.isRunning) {
        g.drawImage(j2meHost.config.getLCD(), cx, cy, cw, ch, null);
      }
      else {
        g.drawImage(Mobile.getPlatform().getLCD(), cx, cy, cw, ch, null);
        if (j2meHost.limitFPS > 0) {
          Thread.sleep(j2meHost.limitFPS);
        }
      }
    }
    catch (final Exception e) {
      LCD.logger.warn(e.getMessage());
    }
  }

}
