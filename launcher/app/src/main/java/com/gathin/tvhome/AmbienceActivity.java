package com.gathin.tvhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public final class AmbienceActivity extends Activity {
    private static final String SCENE = "ambience_scene";
    private static final String DELAY = "ambience_delay";
    private static final int[] MINUTES = {0, 1, 2, 5, 10, 15, 20, 30, 60};
    private static final String[] SEASONS = {"winter", "spring", "summer", "autumn"};

    private static final String SCREENSAVER_ENABLED = "screensaver_enabled";
    private static final String LOCAL_MODE = "ambience_local_mode";
    static final int GOOGLE_TV = 0, CLEAN_YOUR_TV = 1, OFF = 2;

    static int screensaverMode(Context context) {
        if (android.provider.Settings.Secure.getInt(context.getContentResolver(),
                SCREENSAVER_ENABLED, 1) != 0) return GOOGLE_TV;
        return context.getSharedPreferences(Config.SETTINGS_NAME, MODE_PRIVATE)
                .getInt(LOCAL_MODE, OFF) == CLEAN_YOUR_TV ? CLEAN_YOUR_TV : OFF;
    }
    static int idleMinutes(Context context) {
        return context.getSharedPreferences(Config.SETTINGS_NAME, MODE_PRIVATE).getInt(DELAY, 0);
    }

    static void bindPanel(Activity activity, android.widget.LinearLayout panel) {
        android.content.SharedPreferences settings =
                activity.getSharedPreferences(Config.SETTINGS_NAME, MODE_PRIVATE);
        String[] scenes = activity.getResources().getStringArray(R.array.ambience_scenes);
        boolean hasFireplace = fireplaceUri(activity) != null;
        float density = activity.getResources().getDisplayMetrics().density;
        android.widget.TextView active = panel.findViewById(R.id.ambienceActiveScene);
        Runnable refreshScene = () -> {
            int saved = settings.getInt(SCENE, 0);
            int scene = selectedScene(saved, hasFireplace);
            active.setText(activity.getString(R.string.ambience_active_scene, scenes[scene]));
        };
        refreshScene.run();
        int rowIndex = 0;
        for (int i = hasFireplace ? 0 : 1; i < scenes.length; i++) {
            if (i == 0 || i == 1) {
                android.widget.TextView heading = new android.widget.TextView(activity);
                heading.setText(i == 0 ? R.string.ambience_animations : R.string.ambience_stills);
                heading.setTextColor(0xFFF1F5FA);
                heading.setTextSize(17);
                heading.setTypeface(null, android.graphics.Typeface.BOLD);
                heading.setPadding(0, (int) (12 * density), 0, (int) (8 * density));
                panel.addView(heading, rowIndex++);
            }
            final int scene = i;
            android.widget.LinearLayout row = new android.widget.LinearLayout(activity);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            row.setPadding((int) (12 * density), 0, 0, 0);
            row.setBackgroundResource(R.drawable.settings_group_bg);
            row.setFocusable(true);
            row.setNextFocusLeftId(R.id.ambienceSection);
            row.setOnFocusChangeListener((view, focused) ->
                    row.setBackgroundColor(focused ? 0xFF364C63 : 0xFF1B232C));
            android.widget.ImageView image = new android.widget.ImageView(activity);
            image.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
            image.setImageBitmap(preview(activity, scene));
            row.addView(image, (int) (112 * density), (int) (63 * density));
            android.widget.TextView label = new android.widget.TextView(activity);
            label.setText(scenes[i]);
            label.setTextColor(Color.WHITE);
            label.setTextSize(16);
            label.setPadding((int) (16 * density), 0, 0, 0);
            row.addView(label);
            android.widget.LinearLayout.LayoutParams size = new android.widget.LinearLayout.LayoutParams(
                    -1, (int) (72 * density));
            size.bottomMargin = (int) (8 * density);
            panel.addView(row, rowIndex++, size);
            if (i == (hasFireplace ? 0 : 1)) row.setId(R.id.ambienceFirstScene);
            row.setOnClickListener(v -> {
                settings.edit().putInt(SCENE, scene).apply();
                refreshScene.run();
                activity.startActivity(new Intent(activity, AmbienceActivity.class));
            });
        }
        android.widget.SeekBar delay = panel.findViewById(R.id.ambienceChoice);
        android.widget.TextView value = panel.findViewById(R.id.ambienceDelayValue);
        String[] labels = activity.getResources().getStringArray(R.array.ambience_delays);
        int selected = 0;
        for (int i = 0; i < MINUTES.length; i++) {
            if (MINUTES[i] == idleMinutes(activity)) selected = i;
        }
        android.widget.TextView system = panel.findViewById(R.id.ambienceSystemScreensaver);
        String[] modes = activity.getResources().getStringArray(R.array.ambience_modes);
        Runnable refreshMode = () -> {
            int mode = screensaverMode(activity);
            system.setText(activity.getString(R.string.ambience_mode_current, modes[mode]));
            delay.setEnabled(mode == CLEAN_YOUR_TV);
        };
        refreshMode.run();
        system.setOnClickListener(button -> new android.app.AlertDialog.Builder(activity)
                .setTitle(R.string.ambience_mode_title)
                .setSingleChoiceItems(modes, screensaverMode(activity), (dialog, mode) -> {
                    dialog.dismiss();
                    try {
                        boolean applied = android.provider.Settings.Secure.putInt(
                                activity.getContentResolver(), SCREENSAVER_ENABLED,
                                mode == GOOGLE_TV ? 1 : 0);
                        if (!applied) throw new SecurityException();
                        android.content.SharedPreferences.Editor edit = settings.edit().putInt(LOCAL_MODE, mode);
                        if (mode == CLEAN_YOUR_TV && idleMinutes(activity) == 0) {
                            edit.putInt(DELAY, 5);
                            delay.setProgress(3);
                        }
                        edit.apply();
                        refreshMode.run();
                    } catch (SecurityException unavailable) {
                        android.widget.Toast.makeText(activity, R.string.ambience_mode_permission,
                                android.widget.Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton(android.R.string.cancel, null).show());
        panel.findViewById(R.id.ambienceSystemSettings).setOnClickListener(button -> {
            try {
                activity.startActivity(new Intent(android.provider.Settings.ACTION_DREAM_SETTINGS));
            } catch (android.content.ActivityNotFoundException | SecurityException unavailable) {
                android.widget.Toast.makeText(activity, R.string.not_available,
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });
        delay.setNextFocusLeftId(R.id.ambienceSection);
        delay.setMax(MINUTES.length - 1);
        delay.setKeyProgressIncrement(1);
        delay.setProgress(selected);
        value.setText(labels[selected]);
        delay.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(android.widget.SeekBar bar, int index, boolean fromUser) {
                value.setText(labels[index]);
                if (fromUser) settings.edit().putInt(DELAY, MINUTES[index]).apply();
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar bar) { }
            @Override public void onStopTrackingTouch(android.widget.SeekBar bar) { }
        });
    }
    private static Bitmap preview(Context context, int scene) {
        String name = scene == 0 ? "fireplace-preview" : stillScene(scene);
        try (InputStream input = context.getAssets().open("ambience/" + name + ".jpg")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = scene == 0 ? 1 : 4;
            return BitmapFactory.decodeStream(input, null, options);
        } catch (IOException unavailable) {
            return null;
        }
    }

    private static String stillScene(int scene) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        return scene == 2 ? "christmas" : SEASONS[((month + 1) % 12) / 3];
    }

    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(LauncherLocale.wrap(base));
    }

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        int saved = getSharedPreferences(Config.SETTINGS_NAME, MODE_PRIVATE).getInt(SCENE, 0);
        android.net.Uri source = fireplaceUri(this);
        int scene = selectedScene(saved, source != null);
        if (scene == 0) {
            showChristmasFireplace(source);
            return;
        }
        String name = stillScene(scene);
        try (InputStream input = getAssets().open("ambience/" + name + ".jpg")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            ImageView image = new ImageView(this);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageBitmap(BitmapFactory.decodeStream(input, null, options));
            setContentView(image);
        } catch (IOException failure) {
            finish();
        }
    }

    private static int selectedScene(int saved, boolean hasFireplace) {
        if (saved == 1 || saved == 2) return saved;
        return hasFireplace ? 0 : 1;
    }

    private static android.net.Uri fireplaceUri(Context context) {
        java.io.File directory = context.getExternalFilesDir(null);
        if (directory != null) {
            java.io.File clip = new java.io.File(directory, "christmas-fireplace.mp4");
            if (clip.isFile()) return android.net.Uri.fromFile(clip);
        }
        int resource = context.getResources().getIdentifier("ambience_christmas_fireplace", "raw",
                context.getPackageName());
        return resource == 0 ? null : android.net.Uri.parse("android.resource://"
                + context.getPackageName() + "/" + resource);
    }
    private ExoPlayer christmasPlayer;

    private void showChristmasFireplace(android.net.Uri source) {
        android.view.SurfaceView surface = new android.view.SurfaceView(this);
        setContentView(surface);
        MediaItem clip = MediaItem.fromUri(source);
        christmasPlayer = new ExoPlayer.Builder(this).build();
        christmasPlayer.setVideoSurfaceView(surface);
        christmasPlayer.setAudioAttributes(androidx.media3.common.AudioAttributes.DEFAULT, true);
        christmasPlayer.setMediaItems(java.util.Arrays.asList(clip, clip));
        christmasPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        christmasPlayer.addListener(new Player.Listener() {
            @Override public void onPlayerError(PlaybackException error) {
                android.util.Log.w("CleanYourTV", "Christmas fireplace playback failed", error);
                christmasPlayer.release();
                christmasPlayer = null;
                showFireplace(source);
                if (hasWindowFocus()) fireplace.start();
            }
        });
        christmasPlayer.prepare();
    }

    private android.widget.VideoView fireplace;
    private int fireplacePosition;

    private void showFireplace(android.net.Uri source) {
        fireplace = new android.widget.VideoView(this);
        android.widget.FrameLayout frame = new android.widget.FrameLayout(this);
        frame.setBackgroundColor(Color.BLACK);
        frame.addView(fireplace, new android.widget.FrameLayout.LayoutParams(
                -1, -1, android.view.Gravity.CENTER));
        setContentView(frame);
        fireplace.setOnPreparedListener(player -> player.setLooping(true));
        fireplace.setOnErrorListener((player, what, extra) -> {
            android.widget.Toast.makeText(this, R.string.not_available, android.widget.Toast.LENGTH_SHORT).show();
            finish();
            return true;
        });
        fireplace.setVideoURI(source);
    }

    @Override protected void onResume() {
        super.onResume();
        if (christmasPlayer != null) christmasPlayer.play();
        if (fireplace != null) {
            if (fireplacePosition > 0) fireplace.seekTo(fireplacePosition);
            fireplace.start();
        }
    }

    @Override protected void onPause() {
        if (christmasPlayer != null) christmasPlayer.pause();
        if (fireplace != null) {
            fireplacePosition = fireplace.getCurrentPosition();
            fireplace.pause();
        }
        super.onPause();
    }

    @Override protected void onDestroy() {
        if (christmasPlayer != null) christmasPlayer.release();
        if (fireplace != null) fireplace.stopPlayback();
        super.onDestroy();
    }
}