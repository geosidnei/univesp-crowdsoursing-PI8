/*
Códigos Kotlin:
Este aplicativo se baseia em:
"How to Send an Email via Intent - Android Studio Tutorial"
Duração: 10m38s
Canal: Coding in Flow
Publicado em: 25 dez. 2017
Disponível em: <https://youtu.be/tZ2YEw6SoBU/>
Acesso em: 16 nov. 2021
"How to Send Email in Android Studio | SendEmail | Android Coding"
Duração: 08m03s
Canal: Android Coding
Publicado em: 18 abr. 2019
Disponível em: <https://youtu.be/aV7-vcwEeRM/>
Acesso em: 16 nov. 2021
*/

package com.example.mail;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {
private EditText mEditTextTo;
private EditText mEditTextSubject;
private EditText mEditTextMessage;

@Overrideprotected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
mEditTextTo = findViewById(R.id.edit_text_to);
mEditTextSubject = findViewById(R.id.edit_text_subject);
mEditTextMessage = findViewById(R.id.edit_text_message);
Button buttonSend = findViewById(R.id.button_send);
buttonSend.setOnClickListener(v -> sendMail());
}

private void sendMail() {
//permite eviar email para várias contas simultaneamente//
String recipientList = mEditTextTo.getText().toString();
String[] recipients = recipientList.split(",");
String subject = mEditTextSubject.getText().toString();
String message = mEditTextMessage.getText() .toString();
Intent intent = new Intent(Intent.ACTION_SEND);
intent.putExtra(Intent.EXTRA_EMAIL, recipients);
intent.putExtra(Intent.EXTRA_SUBJECT, subject);
intent.putExtra(Intent.EXTRA_TEXT, message);
intent.setType("message/rfc822");
startActivity(Intent.createChooser(intent, "Escolha seu provedor de e-mail..."));
}
}

