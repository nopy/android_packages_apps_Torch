
package net.cactii.flash2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private TorchWidgetProvider mWidgetProvider;

    // On button
    private Button buttonOn;

    private boolean bright;

    private boolean mTorchOn;


    // Period of strobe, in milliseconds
    private int strobeperiod;

    private Context context;


    // Preferences
    private SharedPreferences mPrefs;

    private SharedPreferences.Editor mPrefsEditor = null;
    
    // Labels
    private String labelOn = null;
    private String labelOff = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainnew);
        context = this.getApplicationContext();
        buttonOn = (Button) findViewById(R.id.buttonOn);

        strobeperiod = 100;
        mTorchOn = false;
        
        labelOn = this.getString(R.string.label_on);
        labelOff = this.getString(R.string.label_off);

        mWidgetProvider = TorchWidgetProvider.getInstance();

        // Preferences
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // preferenceEditor
        this.mPrefsEditor = this.mPrefs.edit();

        buttonOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TorchService.class);
                intent.putExtra("period", strobeperiod);
                intent.putExtra("bright", bright);

                if (!mTorchOn) {
                    startService(intent);
                    mTorchOn = true;
                    buttonOn.setText(labelOff);
                } else {
                    stopService(intent);
                    mTorchOn = false;
                    buttonOn.setText(labelOn);
                }
            }

        });


        strobeperiod = this.mPrefs.getInt("strobeperiod", 100);

        // Show the about dialog, the first time the user runs the app.
        if (!this.mPrefs.getBoolean("aboutSeen", false)) {
            this.openAboutDialog();
            this.mPrefsEditor.putBoolean("aboutSeen", true);
        }
    }

    public void onPause() {

        this.mPrefsEditor.putInt("strobeperiod", this.strobeperiod);
        this.mPrefsEditor.commit();
        this.updateWidget();
        super.onPause();
    }

    public void onDestroy() {
        this.updateWidget();
        super.onDestroy();
    }

    public void onResume() {
        if (this.TorchServiceRunning(context)) {
            buttonOn.setText(labelOff);
            this.mTorchOn = true;
        }
        this.updateWidget();
        super.onResume();
    }

    private boolean TorchServiceRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> svcList = am.getRunningServices(100);

        if (!(svcList.size() > 0))
            return false;
        for (int i = 0; i < svcList.size(); i++) {
            RunningServiceInfo serviceInfo = svcList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().endsWith(".TorchService")
                    || serviceName.getClassName().endsWith(".RootTorchService"))
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean supRetVal = super.onCreateOptionsMenu(menu);
        menu.addSubMenu(0, 0, 0, this.getString(R.string.about_btn));
        return supRetVal;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean supRetVal = super.onOptionsItemSelected(menuItem);
        this.openAboutDialog();
        return supRetVal;
    }

    private void openAboutDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.aboutview, null);
        new AlertDialog.Builder(MainActivity.this).setTitle(this.getString(R.string.about_title)).setView(view)
                .setNegativeButton(this.getString(R.string.about_close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Log.d(MSG_TAG, "Close pressed");
                    }
                }).show();
    }

    private void openBrightDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.brightwarn, null);
        new AlertDialog.Builder(MainActivity.this).setTitle(this.getString(R.string.brightwarn_title))
                .setView(view)
                .setNegativeButton(this.getString(R.string.brightwarn_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //MainActivity.this.buttonBright.setChecked(false);
                    }
                }).setNeutralButton(this.getString(R.string.brightwarn_accept), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //MainActivity.this.bright = true;
                        mPrefsEditor.putBoolean("bright", true);
                        mPrefsEditor.commit();
                    }
                }).show();
    }

    public void updateWidget() {
        this.mWidgetProvider.updateAllStates(context);
    }

}
