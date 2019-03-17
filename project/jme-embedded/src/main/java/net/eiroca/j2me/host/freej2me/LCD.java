package net.eiroca.j2me.host.freej2me;

import java.awt.Canvas;
import java.awt.Graphics;
import org.recompile.mobile.Mobile;

public class LCD extends Canvas {

  /**
   *
   */
  private static final long serialVersionUID = -959791062723408787L;
  /**
   *
   */
  private final FreeeJ2meHost j2meHost;

  /**
   * @param j2meHost
   */
  LCD(final FreeeJ2meHost j2meHost) {
    this.j2meHost = j2meHost;
  }

  public int cx = 0;
  public int cy = 0;
  public int cw = 240;
  public int ch = 320;

  public double scalex = 1;
  public double scaley = 1;

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
      System.out.println(e.getMessage());
    }
  }
}
