package com.example.telainicial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
override fun onCreate(savedInstanceState: Bundle?)  {super.onCreate(savedInstanceState)
setContentView(R.layout.activity_main)

val buttonLocalization: Button = findViewById(R.id.button_localization)
val buttonPhotos: Button = findViewById(R.id.button_photos)
val buttonEmail: Button = findViewById(R.id.button_email)
val buttonInformation: Button = findViewById(R.id.button_app_information)

buttonLocalization.setOnClickListener{
}
button_photos.setOnClickListener{
}
button_email.setOnClickListener{
}
button_app_information.setOnClickListener{
}
}
}
