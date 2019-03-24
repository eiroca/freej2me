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
package javax.microedition.m3g;

public abstract class Object3D {

  private int userid;

  public void addAnimationTrack(final AnimationTrack animationTrack) {
  }

  public int animate(final int time) {
    return 0;
  }

  public Object3D duplicate() {
    return this;
  }

  public Object3D find(final int userID) {
    return this;
  }

  public AnimationTrack getAnimationTrack(final int index) {
    return new AnimationTrack(new KeyframeSequence(0, 0, 0), 0);
  }

  public int getAnimationTrackCount() {
    return 0;
  }

  public int getReferences(final Object3D[] references) {
    return 0;
  }

  public int getUserID() {
    return userid;
  }

  public Object getUserObject() {
    return null;
  }

  public void removeAnimationTrack(final AnimationTrack animationTrack) {
  }

  public void setUserID(final int userID) {
    userid = userID;
  }

  public void setUserObject(final java.lang.Object userObject) {
  }

}