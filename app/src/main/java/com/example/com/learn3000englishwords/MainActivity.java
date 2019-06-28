package com.example.com.learn3000englishwords;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Word words;
    ListView lv_word;
    TextToSpeech tts;
    String completedWord;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> listWord= new ArrayList<String>();
    ArrayList<Integer> wordPosition = new ArrayList<>();
    LinearLayout emptyWords;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, 200);

        words = new Word();
        lv_word = findViewById(R.id.lv_word);
        emptyWords = findViewById(R.id.emptyword);
        sharedPreferences = getSharedPreferences("Words", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        completedWord = sharedPreferences.getString("Completed_Word","");


        String emptyCompletedWord = "";

        System.out.println(completedWord);

        if(completedWord.isEmpty()){
            for(int i=0;i<3000;i++){
                emptyCompletedWord += '0';
            }
            completedWord = emptyCompletedWord;
            editor.putString("Completed_Word",completedWord);
            editor.commit();
        }



        tts= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });




        for (int i = 0; i < 3000;i++){
                if(completedWord.charAt(i) == '1'){
                    listWord.add(words.englishWord[i]+" -> "+words.indoWord[i]);
                    wordPosition.add(i);
                }
        }

        if(listWord.isEmpty()){
            emptyWords.setVisibility(View.VISIBLE);
            lv_word.setVisibility(View.GONE);
        }
        else {
            emptyWords.setVisibility(View.GONE);
            lv_word.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listWord);
            lv_word.setAdapter(adapter);

            lv_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    tts.speak(words.englishWord[wordPosition.get(i)], TextToSpeech.QUEUE_FLUSH, null);
                }
            });
        }





     }

     public void nextActivity(View view){
        Intent intent = new Intent(MainActivity.this, Learn_New_Word.class);
        startActivity(intent);
     }
}
