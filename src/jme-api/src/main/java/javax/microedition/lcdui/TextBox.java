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

public class TextBox extends Screen {

  private String title;
  private String text;
  private int max;
  private int constraints;
  private String mode;
  private Ticker ticker;

  public TextBox(final String Title, final String value, final int maxSize, final int Constraints) {
    title = Title;
    text = value;
    max = maxSize;
    constraints = Constraints;
  }

  public void delete(final int offset, final int length) {
    text = text.substring(0, offset) + text.substring(offset + length);
  }

  public int getCaretPosition() {
    return 0;
  }

  public int getChars(final char[] data) {
    for (int i = 0; i < text.length(); i++) {
      data[i] = text.charAt(i);
    }
    return text.length();
  }

  public int getConstraints() {
    return constraints;
  }

  public int getMaxSize() {
    return max;
  }

  public String getString() {
    return text;
  }

  public void insert(final char[] data, final int offset, final int length, final int position) {
    final StringBuilder out = new StringBuilder();
    out.append(text, 0, position);
    out.append(data, offset, length);
    out.append(text.substring(position));
    text = out.toString();
  }

  public void insert(final String src, final int position) {
    final StringBuilder out = new StringBuilder();
    out.append(text, 0, position);
    out.append(src);
    out.append(text.substring(position));
    text = out.toString();
  }

  public void setChars(final char[] data, final int offset, final int length) {
    final StringBuilder out = new StringBuilder();
    out.append(data, offset, length);
    text = out.toString();
  }

  public void setConstraints(final int Constraints) {
    constraints = Constraints;
  }

  public void setInitialInputMode(final String characterSubset) {
    mode = characterSubset;
  }

  public int setMaxSize(final int maxSize) {
    max = maxSize;
    return max;
  }

  public void setString(final String value) {
    text = value;
  }

  @Override
  public void setTicker(final Ticker tick) {
    ticker = tick;
  }

  @Override
  public void setTitle(final String s) {
    title = s;
  }

  public int size() {
    return text.length();
  }

}
