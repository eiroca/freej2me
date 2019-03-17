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

import java.util.Vector;

public class Group extends Node {

  private Vector<Node> nodes;

  public Group() {
  }

  public void addChild(final Node child) {
    try {
      nodes.add(child);
    }
    catch (final Exception e) {
    }
  }

  public Node getChild(final int index) {
    return nodes.get(index);
  }

  public int getChildCount() {
    return nodes.size();
  }

  public boolean pick(final int scope, final float x, final float y, final Camera camera, final RayIntersection ri) {
    return false;
  }

  public boolean pick(final int scope, final float ox, final float oy, final float oz, final float dx, final float dy, final float dz, final RayIntersection ri) {
    return false;
  }

  public void removeChild(final Node child) {
    nodes.remove(child);
  }

}
