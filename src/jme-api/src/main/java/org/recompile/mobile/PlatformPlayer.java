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
 */
package org.recompile.mobile;

import java.io.InputStream;
import java.util.Vector;
import javax.microedition.media.Control;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlatformPlayer implements Player {

  private String contentType = "";

  private audioplayer player;

  private int state = Player.UNREALIZED;

  private final Vector<PlayerListener> listeners;

  private final Control[] controls;

  public PlatformPlayer(final InputStream stream, final String type) {
    listeners = new Vector<>();
    controls = new Control[3];
    contentType = type;
    if (type.equals("audio/midi") || type.equals("sp-midi") || type.equals("audio/spmidi")) {
      player = new midiPlayer(stream);
    }
    else {
      if (type.equals("audio/x-wav")) {
        player = new wavPlayer(stream);
      }
      else {
        Mobile.log("No Player For: " + contentType);
        player = new audioplayer();
      }
    }
    controls[0] = new volumeControl();
    controls[1] = new tempoControl();
    controls[2] = new midiControl();
    Mobile.debug("media type: " + type);
  }

  public PlatformPlayer(final String locator) {
    listeners = new Vector<>();
    controls = new Control[3];
    Mobile.log("Player locator: " + locator);
  }

  @Override
  public void close() {
    player.stop();
    state = Player.CLOSED;
    notifyListeners(PlayerListener.CLOSED, null);
  }

  @Override
  public int getState() {
    if (player.isRunning() == false) {
      state = Player.PREFETCHED;
    }
    return state;
  }

  @Override
  public void start() {
    Mobile.debug("Play " + contentType);
    if (Mobile.getPlatform().sound) {
      try {
        player.start();
      }
      catch (final Exception e) {
      }
    }
  }

  @Override
  public void stop() {
    try {
      player.stop();
    }
    catch (final Exception e) {
    }
  }

  @Override
  public void addPlayerListener(final PlayerListener playerListener) {
    Mobile.debug("Add Player Listener");
    listeners.add(playerListener);
  }

  @Override
  public void removePlayerListener(final PlayerListener playerListener) {
    listeners.remove(playerListener);
  }

  private void notifyListeners(final String event, final Object eventData) {
    for (int i = 0; i < listeners.size(); i++) {
      listeners.get(i).playerUpdate(this, event, eventData);
    }
  }

  @Override
  public void deallocate() {
    stop();
    if (player instanceof midiPlayer) {
      ((midiPlayer)player).midi.close();
    }
    player = null;
    state = Player.CLOSED;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public long getDuration() {
    return Player.TIME_UNKNOWN;
  }

  @Override
  public long getMediaTime() {
    return player.getMediaTime();
  }

  @Override
  public void prefetch() {
    state = Player.PREFETCHED;
  }

  @Override
  public void realize() {
    state = Player.REALIZED;
  }

  @Override
  public void setLoopCount(final int count) {
    player.setLoopCount(count);
  }

  @Override
  public long setMediaTime(final long now) {
    return player.setMediaTime(now);
  }

  // Controllable interface //

  @Override
  public Control getControl(final String controlType) {
    if (controlType.equals("VolumeControl")) { return controls[0]; }
    if (controlType.equals("TempoControl")) { return controls[1]; }
    if (controlType.equals("MIDIControl")) { return controls[2]; }
    if (controlType.equals("javax.microedition.media.control.VolumeControl")) { return controls[0]; }
    if (controlType.equals("javax.microedition.media.control.TempoControl")) { return controls[1]; }
    if (controlType.equals("javax.microedition.media.control.MIDIControl")) { return controls[2]; }
    return null;
  }

  @Override
  public Control[] getControls() {
    return controls;
  }

  // Players //

  private class audioplayer {

    public void start() {
    }

    public void stop() {
    }

    public void setLoopCount(final int count) {
    }

    public long setMediaTime(final long now) {
      return now;
    }

    public long getMediaTime() {
      return 0;
    }

    public boolean isRunning() {
      return false;
    }
  }

  private class midiPlayer extends audioplayer {

    private Sequencer midi;

    private int loops = 0;

    public midiPlayer(final InputStream stream) {
      try {
        midi = MidiSystem.getSequencer();
        midi.open();
        midi.setSequence(stream);
        state = Player.PREFETCHED;
      }
      catch (final Exception e) {
      }
    }

    @Override
    public void start() {
      midi.start();
      state = Player.STARTED;
      notifyListeners(PlayerListener.STARTED, new Long(0));
    }

    @Override
    public void stop() {
      midi.stop();
      state = Player.REALIZED;
    }

    @Override
    public void setLoopCount(final int count) {
      loops = count;
      midi.setLoopCount(count);
    }

    @Override
    public long setMediaTime(final long now) {
      try {
        midi.setTickPosition(now);
      }
      catch (final Exception e) {
      }
      return now;
    }

    @Override
    public long getMediaTime() {
      return 0;
    }

    @Override
    public boolean isRunning() {
      return midi.isRunning();
    }
  }

  private class wavPlayer extends audioplayer {

    private AudioInputStream wavStream;
    private Clip wavClip;

    private int loops = 0;

    private Long time = new Long(0);

    public wavPlayer(final InputStream stream) {
      try {
        wavStream = AudioSystem.getAudioInputStream(stream);
        wavClip = AudioSystem.getClip();
        wavClip.open(wavStream);
        state = Player.PREFETCHED;
      }
      catch (final Exception e) {
      }
    }

    @Override
    public void start() {
      if (isRunning()) {
        wavClip.setFramePosition(0);
      }
      time = wavClip.getMicrosecondPosition();
      wavClip.start();
      state = Player.STARTED;
      notifyListeners(PlayerListener.STARTED, time);
    }

    @Override
    public void stop() {
      wavClip.stop();
      time = wavClip.getMicrosecondPosition();
      state = Player.PREFETCHED;
      notifyListeners(PlayerListener.STOPPED, time);
    }

    @Override
    public void setLoopCount(final int count) {
      loops = count;
      wavClip.loop(count);
    }

    @Override
    public long setMediaTime(final long now) {
      wavClip.setMicrosecondPosition(now);
      return now;
    }

    @Override
    public long getMediaTime() {
      return wavClip.getMicrosecondPosition();
    }

    @Override
    public boolean isRunning() {
      return wavClip.isRunning();
    }
  }

  // Controls //

  private class midiControl implements javax.microedition.media.control.MIDIControl {

    @Override
    public int[] getBankList(final boolean custom) {
      return new int[] {};
    }

    @Override
    public int getChannelVolume(final int channel) {
      return 0;
    }

    @Override
    public java.lang.String getKeyName(final int bank, final int prog, final int key) {
      return "";
    }

    @Override
    public int[] getProgram(final int channel) {
      return new int[] {};
    }

    @Override
    public int[] getProgramList(final int bank) {
      return new int[] {};
    }

    @Override
    public java.lang.String getProgramName(final int bank, final int prog) {
      return "";
    }

    @Override
    public boolean isBankQuerySupported() {
      return false;
    }

    @Override
    public int longMidiEvent(final byte[] data, final int offset, final int length) {
      return 0;
    }

    @Override
    public void setChannelVolume(final int channel, final int volume) {
    }

    @Override
    public void setProgram(final int channel, final int bank, final int program) {
    }

    @Override
    public void shortMidiEvent(final int type, final int data1, final int data2) {
    }
  }

  private class volumeControl implements javax.microedition.media.control.VolumeControl {

    private int level = 100;
    private boolean muted = false;

    @Override
    public int getLevel() {
      return level;
    }

    @Override
    public boolean isMuted() {
      return muted;
    }

    @Override
    public int setLevel(final int value) {
      level = value;
      return level;
    }

    @Override
    public void setMute(final boolean mute) {
      muted = mute;
    }
  }

  private class tempoControl implements javax.microedition.media.control.TempoControl {

    int tempo = 5000;
    int rate = 5000;

    @Override
    public int getTempo() {
      return tempo;
    }

    @Override
    public int setTempo(final int millitempo) {
      tempo = millitempo;
      return tempo;
    }

    // RateControl interface
    @Override
    public int getMaxRate() {
      return rate;
    }

    @Override
    public int getMinRate() {
      return rate;
    }

    @Override
    public int getRate() {
      return rate;
    }

    @Override
    public int setRate(final int millirate) {
      rate = millirate;
      return rate;
    }
  }

}
