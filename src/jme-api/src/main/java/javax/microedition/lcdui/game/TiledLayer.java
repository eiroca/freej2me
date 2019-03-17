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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformGraphics;

public class TiledLayer extends Layer {

  private Image image;
  private final Image canvas;
  private final PlatformGraphics gc;
  private final int rows;
  private final int cols;
  private final int width;
  private final int height;
  private int tileWidth;
  private int tileHeight;
  private int tilesWidth;
  private int tilesHeight;

  private final int[] animatedTiles;
  private int animatedTileCount = 0;

  private final int[][] tiles;

  public TiledLayer(final int colsw, final int rowsh, final Image baseimage, final int tilewidth, final int tileheight) {
    Mobile.log("Tiled Layer");
    setStaticTileSet(baseimage, tilewidth, tileheight);
    rows = rowsh;
    cols = colsw;
    x = 0;
    y = 0;
    width = tileWidth * cols;
    height = tileHeight * rows;
    canvas = Image.createImage(width, height);
    gc = canvas.platformImage.getGraphics();
    gc.clearRect(0, 0, width, height);
    animatedTiles = new int[255];
    tiles = new int[colsw][rowsh];
  }

  protected int createAnimatedTile(final int staticTileIndex) {
    animatedTileCount++;
    animatedTiles[animatedTileCount] = staticTileIndex;
    return 0 - animatedTileCount;
  }

  public void fillCells(final int col, final int row, final int numCols, final int numRows, final int tileIndex) {
    for (int c = 0; c < numCols; c++) {
      for (int r = 0; r < numRows; r++) {
        tiles[col + c][row + r] = tileIndex;
      }
    }
  }

  public int getAnimatedTile(final int animatedTileIndex) {
    return animatedTiles[0 - animatedTileIndex];
  }

  public int getCell(final int col, final int row) {
    return tiles[col][row];
  }

  public int getCellHeight() {
    return tileHeight;
  }

  public int getCellWidth() {
    return tileHeight;
  }

  public int getColumns() {
    return cols;
  }

  public int getRows() {
    return rows;
  }

  @Override
  public void paint(final Graphics g) {
    g.drawImage(canvas, x, y, 0);
  }

  private void drawTiles() {
    int tile;
    for (int c = 0; c < cols; c++) {
      for (int r = 0; r < rows; r++) {
        tile = tiles[c][r];
        if (tile < 0) {
          tile = animatedTiles[0 - tile];
        }
        if (tile > 0) {
          drawTile(tile, c * tileWidth, r * tileHeight);
        }
      }
    }
  }

  private void drawTile(int tile, final int xdest, final int ydest) {
    tile--;
    final int r = tileHeight * (tile / tilesWidth);
    final int c = tileWidth * (tile % tilesWidth);
    gc.drawRegion(image, c, r, tileWidth, tileHeight, 0, xdest, ydest, 0);
  }

  public void setAnimatedTile(final int animatedTileIndex, final int staticTileIndex) {
    int tile;
    animatedTiles[0 - animatedTileIndex] = staticTileIndex;
    for (int c = 0; c < cols; c++) {
      for (int r = 0; r < rows; r++) {
        tile = tiles[c][r];
        if (tile == animatedTileIndex) {
          drawTile(animatedTiles[0 - animatedTileIndex], c * tileWidth, r * tileHeight);
        }
      }
    }
  }

  public void setCell(final int col, final int row, final int tileIndex) {
    tiles[col][row] = tileIndex;
    drawTile(tileIndex, col * tileWidth, row * tileHeight);
  }

  public void setStaticTileSet(final Image baseimage, final int tilewidth, final int tileheight) {
    image = baseimage;
    tileWidth = tilewidth;
    tileHeight = tileheight;
    tilesWidth = (int)Math.floor(image.getWidth() / tilewidth);
    tilesHeight = (int)Math.floor(image.getHeight() / tileheight);
  }

}
