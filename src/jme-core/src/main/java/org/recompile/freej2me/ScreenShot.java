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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.recompile.mobile.Mobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenShot {

  private static Logger logger = LoggerFactory.getLogger(ScreenShot.class);

  public static void takeScreenshot() {
    try {
      final Date date = new Date();
      final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
      final String fileName = System.getProperty("user.home") + "/Pictures/Screenshot from " + dateFormat.format(date) + ".png";
      final File outputfile = new File(fileName);
      ImageIO.write(Mobile.getPlatform().getLCD(), "png", outputfile);
      ScreenShot.logger.info("Saved screenshot: " + outputfile.toString() + "\n");
    }
    catch (final Exception e) {
      ScreenShot.logger.error("Error saving screenshot", e);
    }
  }

}
