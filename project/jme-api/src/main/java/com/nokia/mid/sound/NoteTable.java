package com.nokia.mid.sound;

import java.util.Hashtable;

class NoteTable
{
  public static final int MAX_TONES = 41;
  private Hashtable<Integer, Integer> data;
  private int[] liste;
  private final Sound t;
  
  public NoteTable(Sound t)
  {  
    this.t = t;
    this.data = new Hashtable<Integer, Integer>(MAX_TONES);
    this.liste = new int[MAX_TONES];
    
    
    this.data.put(new Integer(0), new Integer(0));
    this.data.put(new Integer(1), new Integer(0));
    this.data.put(new Integer(440), new Integer(1));
    this.data.put(new Integer(466), new Integer(2));
    this.data.put(new Integer(494), new Integer(3));
    this.data.put(new Integer(523), new Integer(4));
    this.data.put(new Integer(554), new Integer(5));
    this.data.put(new Integer(587), new Integer(6));
    this.data.put(new Integer(622), new Integer(7));
    this.data.put(new Integer(659), new Integer(8));
    this.data.put(new Integer(698), new Integer(9));
    this.data.put(new Integer(740), new Integer(10));
    this.data.put(new Integer(784), new Integer(11));
    this.data.put(new Integer(831), new Integer(12));
    this.data.put(new Integer(880), new Integer(13));
    this.data.put(new Integer(932), new Integer(14));
    this.data.put(new Integer(988), new Integer(15));
    this.data.put(new Integer(1047), new Integer(16));
    this.data.put(new Integer(1109), new Integer(17));
    this.data.put(new Integer(1175), new Integer(18));
    this.data.put(new Integer(1245), new Integer(19));
    this.data.put(new Integer(1319), new Integer(20));
    this.data.put(new Integer(1397), new Integer(21));
    this.data.put(new Integer(1480), new Integer(22));
    this.data.put(new Integer(1568), new Integer(23));
    this.data.put(new Integer(1661), new Integer(24));
    this.data.put(new Integer(1760), new Integer(25));
    this.data.put(new Integer(1865), new Integer(26));
    this.data.put(new Integer(1976), new Integer(27));
    this.data.put(new Integer(2093), new Integer(28));
    this.data.put(new Integer(2217), new Integer(29));
    this.data.put(new Integer(2349), new Integer(30));
    this.data.put(new Integer(2489), new Integer(31));
    this.data.put(new Integer(2637), new Integer(32));
    this.data.put(new Integer(2794), new Integer(33));
    this.data.put(new Integer(2960), new Integer(34));
    this.data.put(new Integer(3136), new Integer(35));
    this.data.put(new Integer(3322), new Integer(36));
    this.data.put(new Integer(3520), new Integer(37));
    this.data.put(new Integer(3729), new Integer(38));
    this.data.put(new Integer(3951), new Integer(39));
    


    this.liste[0] = 0;
    this.liste[1] = 1;
    this.liste[2] = 440;
    this.liste[3] = 466;
    this.liste[4] = 494;
    this.liste[5] = 523;
    this.liste[6] = 554;
    this.liste[7] = 587;
    this.liste[8] = 622;
    this.liste[9] = 659;
    this.liste[10] = 698;
    this.liste[11] = 740;
    this.liste[12] = 784;
    this.liste[13] = 831;
    this.liste[14] = 880;
    this.liste[15] = 932;
    this.liste[16] = 988;
    this.liste[17] = 1047;
    this.liste[18] = 1109;
    this.liste[19] = 1175;
    this.liste[20] = 1245;
    this.liste[21] = 1319;
    this.liste[22] = 1397;
    this.liste[23] = 1480;
    this.liste[24] = 1568;
    this.liste[25] = 1661;
    this.liste[26] = 1760;
    this.liste[27] = 1865;
    this.liste[28] = 1976;
    this.liste[29] = 2093;
    this.liste[30] = 2217;
    this.liste[31] = 2349;
    this.liste[32] = 2489;
    this.liste[33] = 2637;
    this.liste[34] = 2794;
    this.liste[35] = 2960;
    this.liste[36] = 3136;
    this.liste[37] = 3322;
    this.liste[38] = 3520;
    this.liste[39] = 3729;
    this.liste[40] = 3951;
  }
  
  int get(int note)
  {
    Integer key = new Integer(note);
    Integer n = (Integer)this.data.get(key);
    if (n == null) {
      return -1;
    }
    return n.intValue();
  }
  
  int listeVal(int k)
  {
    if(k < MAX_TONES)
    {
        return liste[k];
    }
    else
    {
        return -1;
    }
  }
}
