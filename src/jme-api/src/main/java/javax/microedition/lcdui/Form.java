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
package javax.microedition.lcdui;

import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformImage;

public class Form extends Screen {

  public ItemStateListener listener;

  public Form(final String title) {
    setTitle(title);
    platformImage = new PlatformImage(width, height);
    render();
  }

  public Form(final String title, final Item[] itemarray) {
    setTitle(title);

    if (items != null) {
      for (final Item element : itemarray) {
        items.add(element);
      }
    }
    platformImage = new PlatformImage(width, height);
    render();
  }

  public int append(final Image img) {
    items.add(new ImageItem("", img, 0, ""));
    render();
    return items.size() - 1;
  }

  public int append(final Item item) {
    items.add(item);
    render();
    return items.size() - 1;
  }

  public int append(final String str) {
    items.add(new StringItem("", str));
    render();
    return items.size() - 1;
  }

  public void delete(final int itemNum) {
    items.remove(itemNum);
    render();
  }

  public void deleteAll() {
    items.clear();
    render();
  }

  public Item get(final int itemNum) {
    return items.get(itemNum);
  }

  @Override
  public int getHeight() {
    return 128;
  }

  @Override
  public int getWidth() {
    return 64;
  }

  public void insert(final int itemNum, final Item item) {
    items.add(itemNum, item);
    render();
  }

  public void set(final int itemNum, final Item item) {
    items.set(itemNum, item);
    render();
  }

  public void setItemStateListener(final ItemStateListener iListener) {
    listener = iListener;
  }

  public int size() {
    return items.size();
  }

  /*
  	Draw form, handle input
  */

  @Override
  public void keyPressed(final int key) {
    if (listCommands == true) {
      keyPressedCommands(key);
      return;
    }

    if (items.size() < 1) { return; }
    switch (key) {
      case Mobile.KEY_NUM2:
        currentItem--;
        break;
      case Mobile.KEY_NUM8:
        currentItem++;
        break;
      case Mobile.NOKIA_UP:
        currentItem--;
        break;
      case Mobile.NOKIA_DOWN:
        currentItem++;
        break;
      case Mobile.NOKIA_SOFT1:
        doLeftCommand();
        break;
      case Mobile.NOKIA_SOFT2:
        doRightCommand();
        break;
      case Mobile.KEY_NUM5:
        doDefaultCommand();
        break;
    }
    if (currentItem >= items.size()) {
      currentItem = 0;
    }
    if (currentItem < 0) {
      currentItem = items.size() - 1;
    }
    render();
  }

  @Override
  public void notifySetCurrent() {
    render();
  }

}
