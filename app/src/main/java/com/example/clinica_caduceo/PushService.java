package com.example.clinica_caduceo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.clinica_caduceo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PushService extends FirebaseMessagingService {

    private static final String TAG = "PushService";
    private static final String CHANNEL_ID = "clinica_caduceo_channel";
    private static String fcmToken;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // Envia el token al servidor para registrar el dispositivo
        enviarTokenAlServidor(token);

        // Almacena el token en las preferencias compartidas
        almacenarTokenEnPreferencias(token);

        // Almacena el token en la variable de clase
        fcmToken = token;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Mensaje recibido de: " + remoteMessage.getFrom());

        // Procesa el mensaje recibido
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Datos del mensaje: " + remoteMessage.getData());

            // Extrae la fecha y hora del mensaje
            String fechaHora = remoteMessage.getData().get("fecha_hora");
            procesarFechaHora(fechaHora);

            // Puedes manejar otros datos del mensaje aquí
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Cuerpo del mensaje: " + remoteMessage.getNotification().getBody());

            // Crea una notificación local en la aplicación
            mostrarNotificacion(remoteMessage.getNotification().getBody());
        }
    }

    private void enviarTokenAlServidor(String token) {
        // Lógica para enviar el token al servidor (puede ser una llamada HTTP)
        // ...

        // Ejemplo usando AsyncTask (ten en cuenta que AsyncTask no es la mejor opción, pero es simple para ilustrar)
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... tokens) {
                // Aquí deberías realizar la lógica para enviar el token al servidor
                // tokens[0] contiene el token FCM
                String token = tokens[0];
                // ...

                return null;
            }
        }.execute(token);
    }

    private void almacenarTokenEnPreferencias(String token) {
        // Almacena el token en las preferencias compartidas
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();
    }

    private void procesarFechaHora(String fechaHora) {
        // Formato de fecha y hora esperado: "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            Date fecha = formato.parse(fechaHora);
            // Puedes trabajar con la fecha en tu lógica aquí
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void mostrarNotificacion(String cuerpoMensaje) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Clinica Caduceo Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Recordatorio de Cita")
                .setContentText(cuerpoMensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }

    public static String getFcmToken() {
        return fcmToken;
    }
}
