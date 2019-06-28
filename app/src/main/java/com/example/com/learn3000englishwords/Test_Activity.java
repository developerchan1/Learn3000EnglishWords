package com.example.com.learn3000englishwords;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Test_Activity extends AppCompatActivity {

    public int soal = 1;
    public int random = 0;
    public int change = 0;
    public int[] nomorSoal;
    public boolean[] sudahDitanyakan;
    public Word kata;
    TextToSpeech tts;
    SpeechRecognizer speechRecognizer;
    TextView QuestionNumber1,QuestionDescription1, Question1, Answer1;
    TextView QuestionNumber2,QuestionDescription2, Answer2;
    TextView QuestionNumber3,QuestionDescription3, kesempatan, Question3;
    Button lanjut,speak;
    Intent intentForSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_);

        QuestionNumber1 = findViewById(R.id.questionNumber);
        QuestionDescription1 = findViewById(R.id.questionDescription);
        Question1 = findViewById(R.id.question);
        Answer1 = findViewById(R.id.answer);
        lanjut = findViewById(R.id.btn_next);
        kata = new Word();
        nomorSoal = new int[10];
        sudahDitanyakan = new boolean[10];


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);
            }
        });

        nomorSoal[0] = getIntent().getExtras().getInt("no.1");
        nomorSoal[1] = getIntent().getExtras().getInt("no.2");
        nomorSoal[2] = getIntent().getExtras().getInt("no.3");
        nomorSoal[3] = getIntent().getExtras().getInt("no.4");
        nomorSoal[4] = getIntent().getExtras().getInt("no.5");
        nomorSoal[5] = getIntent().getExtras().getInt("no.6");
        nomorSoal[6] = getIntent().getExtras().getInt("no.7");
        nomorSoal[7] = getIntent().getExtras().getInt("no.8");
        nomorSoal[8] = getIntent().getExtras().getInt("no.9");
        nomorSoal[9] = getIntent().getExtras().getInt("no.10");

        for(int i=0;i<10;i++){
            sudahDitanyakan[i] = false;
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(Test_Activity.this);
        intentForSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentForSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentForSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(result.get(0).toLowerCase().equalsIgnoreCase(kata.englishWord[nomorSoal[random]])){
                    Lanjut(lanjut);
                }
                else
                    if(change == 5){
                        backToLearnAgain();
                    }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        setQuestionType1();

    }

    public void setQuestionType1(){
        do{
            random = (int) (Math.random()*10);
        }while(sudahDitanyakan[random]);

        QuestionNumber1.setText("Question "+soal);
        QuestionDescription1.setText("Translate from Indonesian to English");
        Question1.setText(kata.indoWord[nomorSoal[random]]);

        sudahDitanyakan[random] = true;
    }

    public void setQuestionType2(){
        do{
            random = (int) (Math.random()*10);
        }while(sudahDitanyakan[random]);

        QuestionNumber1.setText("Question "+soal);
        QuestionDescription1.setText("Translate from English to Indonesian");
        Question1.setText(kata.englishWord[nomorSoal[random]]);

        sudahDitanyakan[random] = true;
    }

    public void setQuestionType3(){
        do{
            random = (int) (Math.random()*10);
        }while(sudahDitanyakan[random]);

        QuestionNumber2.setText("Question "+soal);
        QuestionDescription2.setText("Heard the word and translate it to Indonesian");

        sudahDitanyakan[random] = true;
    }

    public void setQuestionType4(){
      change = 0;
        do{
            random = (int) (Math.random()*10);
        }while(sudahDitanyakan[random]);

        QuestionNumber3.setText("Question "+soal);
        QuestionDescription3.setText("Speak the word in english version");
        Question3.setText(kata.indoWord[nomorSoal[random]]);
        kesempatan.setText(change+" of 3 change");

        sudahDitanyakan[random] = true;
    }

    public void speak(View view){
        change++;
        kesempatan.setText(change+" of 5 change");
        speechRecognizer.startListening(intentForSpeech);
    }

    public void backToLearnAgain(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Test_Activity.this);
        builder.setMessage("Please tap 'Oke' button to learn again").setTitle("Wrong Answer");
        builder.setCancelable(false);
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void play(View view){
        tts.speak(kata.englishWord[nomorSoal[random]],TextToSpeech.QUEUE_FLUSH,null);
    }

    public void Lanjut(View view){
        if(soal <= 2){
            if(Answer1.getText().toString().equals("")) {
                Toast.makeText(this, "Answer Can't Empty", Toast.LENGTH_SHORT).show();
            }
            else {
                if(!Answer1.getText().toString().equalsIgnoreCase(kata.englishWord[nomorSoal[random]])){
                    backToLearnAgain();
                }
                else{
                    Toast.makeText(this, "Good, true answer", Toast.LENGTH_SHORT).show();
                    Answer1.setText("");
                    soal++;
                    if(soal > 2){
                        setQuestionType2();
                    }
                    else
                        setQuestionType1();
                }
            }
        }
        else
        if(soal <= 4){
           if(Answer1.getText().toString().equals("")){
               Toast.makeText(this, "Answer Can't Empty", Toast.LENGTH_SHORT).show();
           }
           else
           {
               if(!Answer1.getText().toString().equalsIgnoreCase(kata.indoWord[nomorSoal[random]])){
                   backToLearnAgain();
               }
               else{
                   Toast.makeText(this, "Good, true answer", Toast.LENGTH_SHORT).show();
                   Answer1.setText("");
                   soal++;
                   if(soal > 4){
                       setContentView(R.layout.layout_questiontype3);
                       QuestionNumber2 = findViewById(R.id.questionNumber);
                       QuestionDescription2 = findViewById(R.id.questionDescription);
                       Answer2 = findViewById(R.id.answer);
                       lanjut = findViewById(R.id.btn_next);
                       setQuestionType3();
                   }
                   else
                       setQuestionType2();
               }
           }
        }
        else if(soal <= 7)
        {
            if(Answer2.getText().toString().equals("")){
                Toast.makeText(this, "Answer Can't Empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(!Answer2.getText().toString().equalsIgnoreCase(kata.indoWord[nomorSoal[random]])){
                    backToLearnAgain();
                }
                else{
                    Toast.makeText(this, "Good, true answer", Toast.LENGTH_SHORT).show();
                    Answer2.setText("");
                    soal++;
                    if(soal > 7){
                        setContentView(R.layout.layout_questiontype4);
                        QuestionNumber3= findViewById(R.id.questionNumber);
                        QuestionDescription3 = findViewById(R.id.questionDescription);
                        Question3 = findViewById(R.id.question);
                        kesempatan = findViewById(R.id.tv_change);
                        speak = findViewById(R.id.speak);
                        setQuestionType4();
                    }
                    else
                        setQuestionType3();
                }
            }
        }
        else if(soal <= 10){
            Toast.makeText(this, "Good, true answer", Toast.LENGTH_SHORT).show();
            Answer2.setText("");
            soal++;
            if(soal <= 10)
            setQuestionType4();
            else
            {
                //berhasil melewati test
                Intent intent = new Intent(Test_Activity.this,MainActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("Words", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                String unCompletedWord = sharedPreferences.getString("Uncompleted_Word","");
                String updatedCompletedWord = "" ;
                for(int i=0;i<unCompletedWord.length();i++){
                    if(unCompletedWord.charAt(i) == '2'){
                        updatedCompletedWord += '1';
                    }
                    else {
                        updatedCompletedWord += unCompletedWord.charAt(i);
                    }

                }
                edit.putString("Uncompleted_Word","");
                edit.putString("Completed_Word",updatedCompletedWord);

                edit.commit();


                AlertDialog.Builder builder = new AlertDialog.Builder(Test_Activity.this);
                builder.setCancelable(false);
                builder.setTitle("All Question Solved").setMessage("Please tap 'Oke' button to learn new words");
                builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

}
