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

public class Sound {

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
  private static final int[] supportedToneFormats = {
      1
  };
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

  static {
    Sound.soundListenerThread = new SoundListenerThread();
    Sound.soundListenerThread.start();
  }

  public Sound(final byte[] data, final int type) {
    init(data, type);
    curSoundVolume = Sound.getGain0();
  }

  public Sound(final int freq, final long duration) {
    init(freq, duration);
    curSoundVolume = Sound.getGain0();
  }

  public static int getConcurrentSoundCount(final int type) {
    if (type != 1) { throw new IllegalArgumentException(); }
    return 1;
  }

  public int getGain() {
    return curSoundVolume;
  }

  public void setGain(int gain) {
    if (gain < 0) {
      gain = 0;
    }
    else if (gain > 255) {
      gain = 255;
    }
    curSoundVolume = gain;
    Sound.setGain0(curSoundVolume);
  }

  public int getState() {
    return curSoundState;
  }

  public static int[] getSupportedFormats() {
    return Sound.supportedToneFormats;
  }

  public void init(final byte[] data, final int type) {
    if (curSoundState == 0) {
      stop();
    }
    if (data == null) {
      Sound.soundStateChanged(this, 3);
      throw new NullPointerException();
    }
    if (type != 1) {
      Sound.soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    if ((data.length > 2700) || (data.length < 4)) {
      Sound.soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    if (data[1] != 74) {
      Sound.soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    byte[] tmpData;
    try {
      tmpData = new byte[data.length];
      System.arraycopy(data, 0, tmpData, 0, data.length);
    }
    catch (final Exception ex) {
      Sound.soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    objectType = 0;
    curSoundData = tmpData;
    if (curSoundState == 3) {
      Sound.soundStateChanged(this, 1);
    }
  }

  public void init(final int freq, long duration) {
    if (curSoundState == 0) {
      stop();
    }
    if ((duration <= 0L) || (freq < 0) || (freq > 3951)) {
      Sound.soundStateChanged(this, 3);
      throw new IllegalArgumentException();
    }
    if (Sound.curNoteTable == null) {
      Sound.curNoteTable = new NoteTable(this);
    }
    int index = Sound.curNoteTable.get(freq);
    if (index == -1) {
      int lastNote = 0;

      final int size = NoteTable.MAX_TONES;
      for (int pos = 0; pos < size; pos++) {
        final int note = Sound.curNoteTable.listeVal(pos);
        if (note > freq) {
          final int dif1 = note - freq;
          final int dif2 = freq - lastNote;
          if (dif1 > dif2) {
            index = Sound.curNoteTable.get(lastNote);
            break;
          }
          index = Sound.curNoteTable.get(note);
          break;
        }
        lastNote = note;
      }
      if (index == -1) {
        Sound.soundStateChanged(this, 3);
        throw new IllegalArgumentException();
      }
    }
    objectType = 1;
    curNoteData = index;
    if (duration > 3800L) {
      duration = 3800L;
    }
    curNoteDuration = ((int)duration);
    if (curSoundState == 3) {
      Sound.soundStateChanged(this, 1);
    }
  }

  public synchronized void play(int loop) {
    if (curSoundState == 3) { return; }
    if (loop < 0) { throw new IllegalArgumentException(); }
    if (loop > 255) {
      loop = 255;
    }
    Sound.stopPlaying(Sound.curPlayingSound);

    Sound.curPlayingSound = this;
    curSoundLoop = loop;
    Sound.soundStateChanged(this, 0);
    if (curSoundVolume == 0) {
      Sound.stopPlaying(this);
    }
    else {
      Sound.setGain0(curSoundVolume);
      if (objectType == 0) {
        Sound.play0(curSoundData, curSoundData.length, loop);
      }
      else {
        Sound.playNote0(curNoteData, curNoteDuration, loop);
      }
    }
  }

  public void stop() {
    if (Sound.curPlayingSound == this) {
      Sound.stopPlaying(this);
    }
  }

  static synchronized void stopPlaying(final Sound sound) {
    if (sound == null) { return; }
    if (sound.curSoundState == 0) {
      Sound.stop0();
      Sound.soundStateChanged(sound, 1);
    }
  }

  public void release() {
    if (objectType == 0) {
      curSoundData = null;
    }
    else {
      curNoteData = 0;
    }
    if (curSoundState == 0) {
      stop();
    }
    curSoundVolume = Sound.getGain0();
    if (Sound.curPlayingSound == this) {
      Sound.curPlayingSound = null;
    }
    Sound.soundStateChanged(this, 3);
    setSoundListener(null);
  }

  public void resume() {
    if ((curSoundState == 0) || (curSoundState == 3)) { return; }
    play(curSoundLoop);
  }

  public void setSoundListener(final SoundListener listener) {
    soundListener = listener;
  }

  static synchronized void soundStateChanged(final Sound sound, final int event) {
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
