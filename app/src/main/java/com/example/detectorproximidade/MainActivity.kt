package com.example.detectorproximidade

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensorProximidade: Sensor? = null
    private lateinit var tvStatus: TextView
    private lateinit var tvValor: TextView
    private lateinit var viewIndicador: View
    private var valorAtual: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvValor = findViewById(R.id.tvValor)
        viewIndicador = findViewById(R.id.viewIndicador)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorProximidade = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (sensorProximidade == null) {
            tvStatus.text = "Sensor nao disponivel"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorProximidade?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            valorAtual = event.values[0]
            tvValor.text = "Valor do sensor: $valorAtual cm"

            if (valorAtual < 1f) {
                tvStatus.text = "PERTO"
                tvStatus.setTextColor(Color.parseColor("#E53935"))
                viewIndicador.setBackgroundColor(Color.parseColor("#FFCDD2"))
            } else {
                tvStatus.text = "LONGE"
                tvStatus.setTextColor(Color.parseColor("#43A047"))
                viewIndicador.setBackgroundColor(Color.parseColor("#C8E6C9"))
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}