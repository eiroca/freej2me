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
package com.nokia.mid.sound;

import java.util.Hashtable;

class NoteTable {

  public static final int MAX_TONES = 41;
  private final Hashtable<Integer, Integer> data;
  private final int[] liste;
  private final Sound t;

  public NoteTable(final Sound t) {
    this.t = t;
    data = new Hashtable<>(NoteTable.MAX_TONES);
    liste = new int[NoteTable.MAX_TONES];

    data.put(new Integer(0), new Integer(0));
    data.put(new Integer(1), new Integer(0));
    data.put(new Integer(440), new Integer(1));
    data.put(new Integer(466), new Integer(2));
    data.put(new Integer(494), new Integer(3));
    data.put(new Integer(523), new Integer(4));
    data.put(new Integer(554), new Integer(5));
    data.put(new Integer(587), new Integer(6));
    data.put(new Integer(622), new Integer(7));
    data.put(new Integer(659), new Integer(8));
    data.put(new Integer(698), new Integer(9));
    data.put(new Integer(740), new Integer(10));
    data.put(new Integer(784), new Integer(11));
    data.put(new Integer(831), new Integer(12));
    data.put(new Integer(880), new Integer(13));
    data.put(new Integer(932), new Integer(14));
    data.put(new Integer(988), new Integer(15));
    data.put(new Integer(1047), new Integer(16));
    data.put(new Integer(1109), new Integer(17));
    data.put(new Integer(1175), new Integer(18));
    data.put(new Integer(1245), new Integer(19));
    data.put(new Integer(1319), new Integer(20));
    data.put(new Integer(1397), new Integer(21));
    data.put(new Integer(1480), new Integer(22));
    data.put(new Integer(1568), new Integer(23));
    data.put(new Integer(1661), new Integer(24));
    data.put(new Integer(1760), new Integer(25));
    data.put(new Integer(1865), new Integer(26));
    data.put(new Integer(1976), new Integer(27));
    data.put(new Integer(2093), new Integer(28));
    data.put(new Integer(2217), new Integer(29));
    data.put(new Integer(2349), new Integer(30));
    data.put(new Integer(2489), new Integer(31));
    data.put(new Integer(2637), new Integer(32));
    data.put(new Integer(2794), new Integer(33));
    data.put(new Integer(2960), new Integer(34));
    data.put(new Integer(3136), new Integer(35));
    data.put(new Integer(3322), new Integer(36));
    data.put(new Integer(3520), new Integer(37));
    data.put(new Integer(3729), new Integer(38));
    data.put(new Integer(3951), new Integer(39));

    liste[0] = 0;
    liste[1] = 1;
    liste[2] = 440;
    liste[3] = 466;
    liste[4] = 494;
    liste[5] = 523;
    liste[6] = 554;
    liste[7] = 587;
    liste[8] = 622;
    liste[9] = 659;
    liste[10] = 698;
    liste[11] = 740;
    liste[12] = 784;
    liste[13] = 831;
    liste[14] = 880;
    liste[15] = 932;
    liste[16] = 988;
    liste[17] = 1047;
    liste[18] = 1109;
    liste[19] = 1175;
    liste[20] = 1245;
    liste[21] = 1319;
    liste[22] = 1397;
    liste[23] = 1480;
    liste[24] = 1568;
    liste[25] = 1661;
    liste[26] = 1760;
    liste[27] = 1865;
    liste[28] = 1976;
    liste[29] = 2093;
    liste[30] = 2217;
    liste[31] = 2349;
    liste[32] = 2489;
    liste[33] = 2637;
    liste[34] = 2794;
    liste[35] = 2960;
    liste[36] = 3136;
    liste[37] = 3322;
    liste[38] = 3520;
    liste[39] = 3729;
    liste[40] = 3951;
  }

  int get(final int note) {
    final Integer key = new Integer(note);
    final Integer n = data.get(key);
    if (n == null) { return -1; }
    return n.intValue();
  }

  int listeVal(final int k) {
    if (k < NoteTable.MAX_TONES) {
      return liste[k];
    }
    else {
      return -1;
    }
  }

}
