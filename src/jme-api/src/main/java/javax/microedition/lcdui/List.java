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

public class List extends Screen implements Choice {

  public static Command SELECT_COMMAND = new Command("Select", Command.SCREEN, 0);

  private int fitPolicy = Choice.TEXT_WRAP_ON;

  private final int type;

  public List(final String title, final int listType) {
    setTitle(title);
    type = listType;

    platformImage = new PlatformImage(width, height);

    render();
  }

  public List(final String title, final int listType, final String[] stringElements, final Image[] imageElements) {
    setTitle(title);
    type = listType;

    for (int i = 0; i < stringElements.length; i++) {
      if (imageElements != null) {
        items.add(new ImageItem(stringElements[i], imageElements[i], 0, stringElements[i]));
      }
      else {
        items.add(new StringItem(stringElements[i], stringElements[i]));
      }
    }

    platformImage = new PlatformImage(width, height);
    render();
  }

  @Override
  public int append(final String stringPart, final Image imagePart) {
    if (imagePart != null) {
      items.add(new ImageItem(stringPart, imagePart, 0, stringPart));
    }
    else {
      items.add(new StringItem(stringPart, stringPart));
    }
    render();
    return items.size() - 1;
  }

  @Override
  public void delete(final int elementNum) {
    try {
      items.remove(elementNum);
    }
    catch (final Exception e) {
    }
    render();
  }

  @Override
  public void deleteAll() {
    items.clear();
    render();
  }

  @Override
  public int getFitPolicy() {
    return fitPolicy;
  }

  @Override
  public Font getFont(final int elementNum) {
    return Font.getDefaultFont();
  }

  @Override
  public Image getImage(final int elementNum) {
    return ((ImageItem)(items.get(elementNum))).getImage();
  }

  @Override
  public int getSelectedFlags(final boolean[] selectedArray_return) {
    return 0;
  }

  @Override
  public int getSelectedIndex() {
    return currentItem;
  }

  @Override
  public String getString(final int elementNum) {
    return ((StringItem)(items.get(elementNum))).getText();
  }

  @Override
  public void insert(final int elementNum, final String stringPart, final Image imagePart) {
    if ((elementNum < items.size()) && (elementNum > 0)) {
      try {
        if (imagePart != null) {
          items.add(elementNum, new ImageItem(stringPart, imagePart, 0, stringPart));
        }
        else {
          items.add(elementNum, new StringItem(stringPart, stringPart));
        }
      }
      catch (final Exception e) {
        append(stringPart, imagePart);
      }
    }
    else {
      append(stringPart, imagePart);
    }
    render();
  }

  @Override
  public boolean isSelected(final int elementNum) {
    return elementNum == currentItem;
  }

  // public void removeCommand(Command cmd) {  }

  @Override
  public void set(final int elementNum, final String stringPart, final Image imagePart) {
    if (imagePart != null) {
      items.set(elementNum, new ImageItem(stringPart, imagePart, 0, stringPart));
    }
    else {
      items.set(elementNum, new StringItem(stringPart, stringPart));
    }
  }

  @Override
  public void setFitPolicy(final int fitpolicy) {
    fitPolicy = fitpolicy;
  }

  @Override
  public void setFont(final int elementNum, final Font font) {
  }

  public void setSelectCommand(final Command command) {
    List.SELECT_COMMAND = command;
  }

  @Override
  public void setSelectedFlags(final boolean[] selectedArray) {
  }

  @Override
  public void setSelectedIndex(final int elementNum, final boolean selected) {
    if (selected == true) {
      currentItem = elementNum;
    }
    else {
      currentItem = 0;
    }
    render();
  }

  //void setTicker(Ticker ticker)

  //void setTitle(String s)

  @Override
  public int size() {
    return items.size();
  }

  /*
  	Draw list, handle input
  */

  @Override
  public void keyPressed(final int key) {
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
  protected void doDefaultCommand() {
    if (commandlistener != null) {
      commandlistener.commandAction(List.SELECT_COMMAND, this);
    }
  }

  @Override
  public void notifySetCurrent() {
    render();
  }
}
