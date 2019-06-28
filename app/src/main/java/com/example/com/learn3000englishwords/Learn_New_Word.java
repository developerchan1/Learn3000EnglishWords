package com.example.com.learn3000englishwords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Learn_New_Word extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ListView lv_word;
    String completedWord,uncompletedWord;
    Word word;
    TextToSpeech tts;
    TextView back;
    int[] RandomWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn__new__word);

        sharedPreferences = getSharedPreferences("Words",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        completedWord = sharedPreferences.getString("Completed_Word","");
        uncompletedWord = sharedPreferences.getString("Uncompleted_Word","");
        System.out.println(uncompletedWord);

        lv_word = findViewById(R.id.lv_newWord);
        back = findViewById(R.id.back);
        word = new Word();

        RandomWord = new int[10];

        tts= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(uncompletedWord.isEmpty()){
            for(int i=0;i<10;i++){
                do{
                    RandomWord[i] = (int) (Math.random()*3000);
                }while(completedWord.charAt(RandomWord[i]) == '1');
            }

            for(int i=0;i<3000;i++){
                 boolean ada = false;
                for(int j=0;j<10;j++){
                    if(i == RandomWord[j]){
                        ada = true;
                        break;
                    }
                }
                if(ada){
                    uncompletedWord += '2';
                }
                else{
                    uncompletedWord += completedWord.charAt(i);
                }
            }
            editor.putString("Uncompleted_Word",uncompletedWord);
            editor.commit();
        }
        else
        {
            int j = 0;
            for(int i = 0; i < 3000; i++){
                if(uncompletedWord.charAt(i) == '2'){
                    RandomWord[j] = i;
                    j++;
                }
            }
        }


        for(int i = 0;i < 9;i++){
            for(int j = i+1; j<10;j++){
                if(RandomWord[i] > RandomWord[j]){
                    int swap = RandomWord[i];
                    RandomWord[i] =  RandomWord[j];
                    RandomWord[j] = swap;
                }
            }
        }

        final ArrayList<String> listWord = new ArrayList<String>();
        for(int i = 0; i < 10; i++ ){
            listWord.add(word.englishWord[RandomWord[i]] + " -> " + word.indoWord[RandomWord[i]]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listWord);
        lv_word.setAdapter(adapter);

        lv_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tts.speak(word.englishWord[RandomWord[i]], TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    public void Test_Activity (View view){
        Intent intent = new Intent(Learn_New_Word.this,Test_Activity.class);
        intent.putExtra("no.1",RandomWord[0]);
        intent.putExtra("no.2",RandomWord[1]);
        intent.putExtra("no.3",RandomWord[2]);
        intent.putExtra("no.4",RandomWord[3]);
        intent.putExtra("no.5",RandomWord[4]);
        intent.putExtra("no.6",RandomWord[5]);
        intent.putExtra("no.7",RandomWord[6]);
        intent.putExtra("no.8",RandomWord[7]);
        intent.putExtra("no.9",RandomWord[8]);
        intent.putExtra("no.10",RandomWord[9]);
        startActivity(intent);
    }
}
