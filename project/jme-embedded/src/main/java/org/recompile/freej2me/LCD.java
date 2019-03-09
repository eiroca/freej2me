package org.recompile.freej2me;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import org.recompile.mobile.Mobile;

public class LCD extends Canvas {

  /**
   * 
   */
  private final J2MEHost j2meHost;

  /**
   * @param j2meHost
   */
  LCD(J2MEHost j2meHost) {
    this.j2meHost = j2meHost;
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
    scalex = (double)this.j2meHost.lcdWidth / (double)vw;
    scaley = (double)this.j2meHost.lcdHeight / (double)vh;
  }

  public void paint(Graphics g) {
    try {
      Graphics2D cgc = (Graphics2D)this.getGraphics();
      if (this.j2meHost.config.isRunning) {
        g.drawImage(this.j2meHost.config.getLCD(), cx, cy, cw, ch, null);
      }
      else {
        g.drawImage(Mobile.getPlatform().getLCD(), cx, cy, cw, ch, null);
        if (this.j2meHost.limitFPS > 0) {
          Thread.sleep(this.j2meHost.limitFPS);
        }
      }
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
