package com.hongweizhuo.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {

    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;

    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;
    private float mPlaybackRate = 1.f;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public void loadSounds() {

        mSounds.clear();

        try {
            String[] soundNames = mAssets.list(SOUNDS_FOLDER);

            Log.i(TAG, "Found " + soundNames.length + " sounds");

            for (String soundName: soundNames) {
                Sound sound = new Sound(SOUNDS_FOLDER + "/" + soundName);
                loadSound(sound);
                mSounds.add(sound);
            }

        } catch (IOException ioe) {
            Log.e(TAG, "Unable to load sounds", ioe);
        }

    }

    private void loadSound(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void playSound(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, mPlaybackRate);
    }

    public void release() {
        mSoundPool.release();
    }

    public float getPlaybackRate() {
        return mPlaybackRate;
    }

    public void setPlaybackRate(float playbackRate) {
        mPlaybackRate = playbackRate;
    }

}
