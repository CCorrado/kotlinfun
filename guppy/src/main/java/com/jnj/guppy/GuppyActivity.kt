package com.jnj.guppy

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jnj.guppy.ShakeDetector.OnShakeListener
import com.jnj.guppy.dialogs.GuppyDialogFragment


abstract class GuppyActivity : AppCompatActivity() {

    // The following are used for the shake detection
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mShakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            val dialogFragment = GuppyDialogFragment()

            mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            mShakeDetector = ShakeDetector()
            mShakeDetector.setOnShakeListener(object : OnShakeListener {
                override fun onShake(count: Int) {
                    if (!dialogFragment.isAdded) {
                        dialogFragment.show(supportFragmentManager, "GuppyDialogFragment")
                    }
                }
            })
        }
    }

    public override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) {
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    public override fun onPause() {
        if (BuildConfig.DEBUG) {
            mSensorManager.unregisterListener(mShakeDetector)
        }
        super.onPause()
    }
}