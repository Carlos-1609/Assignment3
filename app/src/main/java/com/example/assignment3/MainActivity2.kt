package com.example.assignment3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val textViewTitle : TextView = findViewById(R.id.textViewTitle)
        val name = intent.getStringExtra("name")
        val last = intent.getStringExtra("last")
        textViewTitle.text = "$name $last, please let us know how we did!"

        // Create notification channel
        createNotificationChannel()

        // Find the btnFinish button and set the click listener
        val btnFinish = findViewById<Button>(R.id.btnFinish)
        btnFinish.setOnClickListener {
            sendNotification()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val name = intent.getStringExtra("name")
        val last = intent.getStringExtra("last")
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // Using the default launcher icon
            .setContentTitle("Customer Service")
            .setContentText("Hello, $name $last! Thank you for letting us know how we did!")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set high priority
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // Set category
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Set visibility

        with(NotificationManagerCompat.from(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(
                    this@MainActivity2,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity2,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, send the notification
                sendNotification()
            } else {
                // Permission denied, handle as necessary
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "my_channel_id"
        private const val NOTIFICATION_ID = 1
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
