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

import java.util.ArrayList;

public class ChoiceGroup extends Item implements Choice {

  private final String label;

  private final int type;

  private final ArrayList<String> strings = new ArrayList<>();

  private final ArrayList<Image> images = new ArrayList<>();

  private int fitPolicy;

  public ChoiceGroup(final String choiceLabel, final int choiceType) {
    label = choiceLabel;
    type = choiceType;
  }

  public ChoiceGroup(final String choiceLabel, final int choiceType, final String[] stringElements, final Image[] imageElements) {
    label = choiceLabel;
    type = choiceType;
    for (int i = 0; i < stringElements.length; i++) {
      strings.add(stringElements[i]);
      images.add(imageElements[i]);
    }
  }

  ChoiceGroup(final String choiceLabel, final int choiceType, final boolean validateChoiceType) {
    label = choiceLabel;
    type = choiceType;
  }

  ChoiceGroup(final String choiceLabel, final int choiceType, final String[] stringElements, final Image[] imageElements, final boolean validateChoiceType) {
    label = choiceLabel;
    type = choiceType;
    for (int i = 0; i < stringElements.length; i++) {
      strings.add(stringElements[i]);
      images.add(imageElements[i]);
    }
  }

  @Override
  public int append(final String stringPart, final Image imagePart) {
    return strings.size();
  }

  @Override
  public void delete(final int itemNum) {
    strings.remove(itemNum);
    images.remove(itemNum);
  }

  @Override
  public void deleteAll() {
    strings.clear();
    images.clear();
  }

  @Override
  public int getFitPolicy() {
    return fitPolicy;
  }

  @Override
  public Font getFont(final int itemNum) {
    return Font.getDefaultFont();
  }

  @Override
  public Image getImage(final int elementNum) {
    return images.get(elementNum);
  }

  @Override
  public int getSelectedFlags(final boolean[] selectedArray) {
    return 1;
  }

  @Override
  public int getSelectedIndex() {
    return 1;
  }

  @Override
  public String getString(final int elementNum) {
    return strings.get(elementNum);
  }

  @Override
  public void insert(final int elementNum, final String stringPart, final Image imagePart) {
    strings.add(elementNum, stringPart);
    images.add(elementNum, imagePart);
  }

  @Override
  public boolean isSelected(final int elementNum) {
    return false;
  }

  @Override
  public void set(final int elementNum, final String stringPart, final Image imagePart) {
    strings.set(elementNum, stringPart);
    images.set(elementNum, imagePart);
  }

  @Override
  public void setFitPolicy(final int policy) {
    fitPolicy = policy;
  }

  @Override
  public void setFont(final int itemNum, final Font font) {
  }

  @Override
  public void setSelectedFlags(final boolean[] selectedArray) {
  }

  @Override
  public void setSelectedIndex(final int elementNum, final boolean selected) {
  }

  @Override
  public int size() {
    return strings.size();
  }

}
