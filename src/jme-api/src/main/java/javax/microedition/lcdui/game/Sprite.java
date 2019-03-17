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
package javax.microedition.lcdui.game;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.recompile.mobile.Mobile;

public class Sprite extends Layer {

  public static final int TRANS_MIRROR = 2;
  public static final int TRANS_MIRROR_ROT180 = 1;
  public static final int TRANS_MIRROR_ROT270 = 4;
  public static final int TRANS_MIRROR_ROT90 = 7;
  public static final int TRANS_NONE = 0;
  public static final int TRANS_ROT180 = 3;
  public static final int TRANS_ROT270 = 6;
  public static final int TRANS_ROT90 = 5;

  private int refX = 0;
  private int refY = 0;

  private int hitX;
  private int hitY;
  private int hitWidth;
  private int hitHeight;

  private int transform;

  private final Vector<Integer> sequence = new Vector<>();

  private int frame;
  private int frameWidth;
  private int frameHeight;
  private int frameCount;

  public Image sprite;
  public int rowCount;
  public int colCount;

  public Sprite(final Image image) {
    setImage(image, image.width, image.height);
  }

  public Sprite(final Image image, final int frameW, final int frameH) {
    setImage(image, frameW, frameH);
  }

  public Sprite(final Sprite s) {
    Mobile.log("Sprite sprite");
    sprite = s.sprite;
  }

  public boolean collidesWith(final Image image, final int x, final int y, final boolean pixelLevel) {
    return false;
  }

  public boolean collidesWith(final Sprite s, final boolean pixelLevel) {
    final int Ax = (x + refX) + hitX;
    final int Ay = (y + refY) + hitY;
    final int Aw = hitWidth;
    final int Ah = hitHeight;

    final int Bx = (s.getX() + s.getRefPixelX()) + s.getHitX();
    final int By = (s.getY() + s.getRefPixelY()) + s.getHitY();
    final int Bw = s.getHitWidth();
    final int Bh = s.getHitHeight();

    if (((Ax + Aw) > Bx) && (Ax < (Bx + Bw)) && ((Ay + Ah) > By) && (Ay < (By + Bh))) { return true; }

    return false;
  }

  public boolean collidesWith(final TiledLayer t, final boolean pixelLevel) {
    return false;
  }

  public void defineCollisionRectangle(final int x, final int y, final int width, final int height) {
    hitX = x;
    hitY = y;
    hitWidth = width;
    hitHeight = height;
  }

  public void defineReferencePixel(final int x, final int y) {
    refX = x;
    refY = y;
  }

  public int getFrame() {
    return frame;
  }

  public int getFrameSequenceLength() {
    return sequence.size();
  }

  public int getRawFrameCount() {
    return frameCount;
  }

  public int getRefPixelX() {
    return refX;
  }

  public int getRefPixelY() {
    return refY;
  }

  public void nextFrame() {
    if (frame < (sequence.size() - 1)) {
      frame++;
    }
    else {
      frame = 0;
    }
  }

  @Override
  public void paint(final Graphics g) {
    try {
      final int f = sequence.get(frame);
      final int r = frameHeight * (f / colCount);
      final int c = frameWidth * (f % colCount);
      g.drawRegion(sprite, c, r, frameWidth, frameHeight, transform, x, y, 0);
    }
    catch (final Exception e) {
      Mobile.log("Problem drawing sprite");
    }
  }

  public void prevFrame() {
    if (frame > 0) {
      frame--;
    }
    else {
      frame = sequence.size() - 1;
    }
  }

  public void setFrame(final int sequenceIndex) {
    frame = sequenceIndex;
  }

  public void setFrameSequence(final int[] fsequence) {
    Mobile.debug("Set Frame Sequence");
    try {
      frame = 0;
      sequence.clear();
      for (final int element : fsequence) {
        sequence.add(element);
      }
    }
    catch (final Exception e) {
      Mobile.log("Problem with Sequence");
    }
  }

  public void setImage(final Image img, final int frameW, final int frameH) {
    sprite = img;
    frameWidth = frameW;
    frameHeight = frameH;

    hitX = 0;
    hitY = 0;
    hitWidth = frameWidth;
    hitHeight = frameHeight;

    final double spriteW = sprite.platformImage.width;
    final double spriteH = sprite.platformImage.height;

    colCount = (int)Math.floor(spriteW / frameW);
    rowCount = (int)Math.floor(spriteH / frameH);

    frameCount = colCount * rowCount;

    sequence.clear();

    for (int i = 0; i < frameCount; i++) {
      sequence.add(i);
    }
  }

  public void setRefPixelPosition(final int x, final int y) {
    refX = x;
    refY = y;
  }

  public void setTransform(final int value) {
    transform = value;
  }

  public int getHitX() {
    return hitX;
  }

  public int getHitY() {
    return hitY;
  }

  public int getHitWidth() {
    return hitWidth;
  }

  public int getHitHeight() {
    return hitHeight;
  }

}
