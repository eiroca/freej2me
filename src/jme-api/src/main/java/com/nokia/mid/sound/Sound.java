/*
	This file is part of FreeJ2ME.

	FreeJ2ME is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	FreeJ2ME is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with FreeJ2ME.  If not, see http://www.gnu.org/licenses/
*/
package com.nokia.mid.sound;

public class Sound
{
  private static final int SOUND_OBJECT = 0;
  private static final int NOTE_OBJECT = 1;
  private int objectType;
  public static final int SOUND_PLAYING = 0;
  public static final int SOUND_STOPPED = 1;
  public static final int SOUND_UNINITIALIZED = 3;
  private int curSoundState = 3;
  private int curSoundVolume;
  private int curSoundLoop;
  private SoundListener soundListener;
  static Sound curPlayingSound;
  private static SoundListenerThread soundListenerThread = null;
  public static final int FORMAT_TONE = 1;
  public static final int FORMAT_WAW = 5;
  private static final int[] supportedToneFormats = { 1 };
  private byte[] curSoundData;
  private static final int NSMCONV_MAX_RINGING_TONE_SEQ_LEN_LONG = 2700;
  static final int OFF = 0;
  static final int hz1 = 1;
  static final int A1 = 440;
  static final int B1b = 466;
  static final int B1 = 494;
  static final int C1 = 523;
  static final int D1b = 554;
  static final int D1 = 587;
  static final int E1b = 622;
  static final int E1 = 659;
  static final int F1 = 698;
  static final int G1b = 740;
  static final int G1 = 784;
  static final int A2b = 831;
  static final int A2 = 880;
  static final int B2b = 932;
  static final int B2 = 988;
  static final int C2 = 1047;
  static final int D2b = 1109;
  static final int D2 = 1175;
  static final int E2b = 1245;
  static final int E2 = 1319;
  static final int F2 = 1397;
  static final int G2b = 1480;
  static final int G2 = 1568;
  static final int A3b = 1661;
  static final int A3 = 1760;
  static final int B3b = 1865;
  static final int B3 = 1976;
  static final int C3 = 2093;
  static final int D3b = 2217;
  static final int D3 = 2349;
  static final int E3b = 2489;
  static final int E3 = 2637;
  static final int F3 = 2794;
  static final int G3b = 2960;
  static final int G3 = 3136;
  static final int A4b = 3322;
  static final int A4 = 3520;
  static final int B4b = 3729;
  static final int B4 = 3951;
  private static final int MIN_TONE_FREQUENCY = 0;
  private static final int MAX_TONE_FREQUENCY = 3951;
  private static NoteTable curNoteTable;
  private int curNoteData;
  private static final long MAX_NOTE_DURATION = 3800L;
  private int curNoteDuration;
  
  static
  {
    soundListenerThread = new SoundListenerThread();
    soundListenerThread.start();
  }
  
  public Sound(byte[] data, int type)
  {
    init(data, type);
    this.curSoundVolume = getGain0();
  }
  
  public Sound(int freq, long duration)
  {
    init(freq, duration);
    this.curSoundVolume = getGain0();
  }
  
  public static int getConcurrentSoundCount(int type)
  {
    if (type != 1) {
      throw new IllegalArgumentException();
    }
    return 1;
  }
  
  public int getGain()
  {
    return this.curSoundVolume;
  }
  
  public void setGain(int gain)
  {
    if (gain < 0) {
      gain = 0;
    } else if (gain > 255) {
      gain = 255;
    }
    this.curSoundVolume = gain;
    setGain0(this.curSoundVolume);
  }
  
  public int getState()
  {
    return this.curSoundState;
  }
  
  public static int[] getSupportedFormats()
  {
    return supportedToneFormats;
  }
  
  public void init(byte[] data, int type)
  {
    if (this.curSoundState == 0) {
      stop();
    }
    if (data == null)
    {
      soundStateChanged(this, 3);
      throw new NullPointerException();
    }
    if (type != 1)
    {
      soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    if ((data.length > 2700) || (data.length < 4))
    {
      soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    if (data[1] != 74)
    {
      soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    byte[] tmpData;
    try
    {
      tmpData = new byte[data.length];
      System.arraycopy(data, 0, tmpData, 0, data.length);
    }
    catch (Exception ex)
    {
      soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    this.objectType = 0;
    this.curSoundData = tmpData;
    if (this.curSoundState == 3) {
      soundStateChanged(this, 1);
    }
  }
  
  public void init(int freq, long duration)
  {
    if (this.curSoundState == 0) {
      stop();
    }
    if ((duration <= 0L) || (freq < 0) || (freq > 3951))
    {
      soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    if (curNoteTable == null) {
      curNoteTable = new NoteTable(this);
    }
    int index = curNoteTable.get(freq);
    if (index == -1)
    {
      int lastNote = 0;
      
      int size = NoteTable.MAX_TONES;
      for (int pos = 0; pos < size; pos++)
      {
        int note = curNoteTable.listeVal(pos);
        if (note > freq)
        {
          int dif1 = note - freq;
          int dif2 = freq - lastNote;
          if (dif1 > dif2)
          {
            index = curNoteTable.get(lastNote);
            break;
          }
          index = curNoteTable.get(note);
          break;
        }
        lastNote = note;
      }
      if (index == -1)
      {
        soundStateChanged(this, 3);
        throw new IllegalArgumentException();
      }
    }
    this.objectType = 1;
    this.curNoteData = index;
    if (duration > 3800L) {
      duration = 3800L;
    }
    this.curNoteDuration = ((int)duration);
    if (this.curSoundState == 3) {
      soundStateChanged(this, 1);
    }
  }
  
  public synchronized void play(int loop)
  {
    if (this.curSoundState == 3) {
      return;
    }
    if (loop < 0) {
      throw new IllegalArgumentException();
    }
    if (loop > 255) {
      loop = 255;
    }
    stopPlaying(curPlayingSound);
    

    curPlayingSound = this;
    this.curSoundLoop = loop;
    soundStateChanged(this, 0);
    if (this.curSoundVolume == 0)
    {
      stopPlaying(this);
    }
    else
    {
      setGain0(this.curSoundVolume);
      if (this.objectType == 0) {
        play0(this.curSoundData, this.curSoundData.length, loop);
      } else {
        playNote0(this.curNoteData, this.curNoteDuration, loop);
      }
    }
  }
  
  public void stop()
  {
    if (curPlayingSound == this) {
      stopPlaying(this);
    }
  }
  
  static synchronized void stopPlaying(Sound sound)
  {
    if (sound == null) {
      return;
    }
    if (sound.curSoundState == 0)
    {
      stop0();
      soundStateChanged(sound, 1);
    }
  }
  
  public void release()
  {
    if (this.objectType == 0) {
      this.curSoundData = null;
    } else {
      this.curNoteData = 0;
    }
    if (this.curSoundState == 0) {
      stop();
    }
    this.curSoundVolume = getGain0();
    if (curPlayingSound == this) {
      curPlayingSound = null;
    }
    soundStateChanged(this, 3);
    setSoundListener(null);
  }
  
  public void resume()
  {
    if ((this.curSoundState == 0) || (this.curSoundState == 3)) {
      return;
    }
    play(this.curSoundLoop);
  }
  
  public void setSoundListener(SoundListener listener)
  {
    this.soundListener = listener;
  }
  
  static synchronized void soundStateChanged(Sound sound, int event)
  {
    sound.curSoundState = event;
    if (sound.soundListener != null) {
      sound.soundListener.soundStateChanged(sound, event);
    }
  }
  
  private static native int getGain0();
  
  private static native void setGain0(int paramInt);
  
  private static native void play0(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private static native void playNote0(int paramInt1, int paramInt2, int paramInt3);
  
  private static native void stop0();
  
  private static native void soundListenerDestroy0();
  
  static native int soundListenerWait0();
}

