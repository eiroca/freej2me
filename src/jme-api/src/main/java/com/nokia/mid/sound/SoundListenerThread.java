package com.nokia.mid.sound;

class SoundListenerThread
  extends Thread
{
  public void run()
  {
    while (Sound.soundListenerWait0() == 0) {
      Sound.stopPlaying(Sound.curPlayingSound);
    }
  }
}
