package com.yourcompany.micstreamexample;

import android.media.AudioAttributes;
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.Bundle;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import java.util.HashMap;
public class MainActivity extends FlutterActivity {
  private static final String AUDIO_CHANNEL = "com.yourcompany.flutter/audioFragmentPlayer";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    final FragmentPlayer fPlayer = new FragmentPlayer();
    new MethodChannel(getFlutterView(), AUDIO_CHANNEL).setMethodCallHandler(
      new MethodCallHandler(){
        @Override
        public void onMethodCall(MethodCall call, Result result) {
          if (call.method.equals("play")) {
            byte[] args =  call.arguments();
            fPlayer.play(args);
            result.success(1);
            return;
          } if(call.method.equals("stop")) {
            fPlayer.stop();
            result.success(1);
            return;
          } else {
            result.notImplemented();
            return;
          }
        }
      }
    );
  }
}

class FragmentPlayer{
  private int sampleRate;
  private AudioTrack audioTrack;
  private AudioFormat audioFormat;
  FragmentPlayer(){
    sampleRate = 48000;
    audioFormat = new AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build();
  }
  public void play(byte [] audioFrag){
    if(audioTrack == null){
      AudioAttributes aa = new AudioAttributes.Builder().build();
      audioTrack = new AudioTrack( aa, new AudioFormat.Builder(audioFormat).build(), audioFrag.length,
              AudioTrack.MODE_STATIC, AudioManager.AUDIO_SESSION_ID_GENERATE);
    }
    audioTrack.write(audioFrag,0,audioFrag.length);
    audioTrack.play();
  }
  public void stop(){
    if(audioTrack != null) {
      audioTrack.stop();
      audioTrack.release();
      audioTrack = null;
    }
  }
}

