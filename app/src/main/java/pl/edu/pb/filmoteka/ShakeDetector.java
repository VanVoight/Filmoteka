package pl.edu.pb.filmoteka;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    // Czas między kolejnymi potrząśnięciami
    private static final int SHAKE_INTERVAL = 4000;

    // Ostatni czas potrząśnięcia
    private long lastShakeTime;

    // Słuchacz potrząśnięcia
    private OnShakeListener onShakeListener;

    // Konstruktor
    public ShakeDetector() {
        lastShakeTime = System.currentTimeMillis();
    }

    // Ustawienie słuchacza potrząśnięcia
    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    // Metoda wywoływana przy zmianie wartości sensora
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShakeTime > SHAKE_INTERVAL) {
                lastShakeTime = currentTime;

                if (onShakeListener != null) {
                    onShakeListener.onShake();
                }
            }
        }
    }

    // Metoda wywoływana przy zmianie dokładności sensora
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nie jest używane w tym przypadku
    }

    // Interfejs do obsługi zdarzenia potrząśnięcia
    public interface OnShakeListener {
        void onShake();
    }
}