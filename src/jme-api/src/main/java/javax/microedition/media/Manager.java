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
package javax.microedition.media;

import java.io.InputStream;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformPlayer;

public final class Manager {

  public static final String TONE_DEVICE_LOCATOR = "device://tone";

  public static Player createPlayer(final InputStream stream, final String type) throws MediaException {
    Mobile.debug("Create Player Stream " + type);
    return new PlatformPlayer(stream, type);
  }

  public static Player createPlayer(final String locator) throws MediaException {
    Mobile.log("Create Player " + locator);
    return new PlatformPlayer(locator);
  }

  public static String[] getSupportedContentTypes(final String protocol) {
    Mobile.debug("Get Supported Media Content Types");
    return new String[] {
        "audio/midi", //
        "audio/x-wav"
    };
  }

  public static String[] getSupportedProtocols(final String content_type) {
    Mobile.log("Get Supported Media Protocols");
    return new String[] {};
  }

  public static void playTone(final int note, final int duration, final int volume) {
    Mobile.log("Play Tone");
  }

}
