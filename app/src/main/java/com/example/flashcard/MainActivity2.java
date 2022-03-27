package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        EditText new_flashcard_text = findViewById(R.id.new_flashcard_question);
        EditText new_flashcard_answer = findViewById(R.id.new_flashcard_answer);
        EditText new_flashcard_wrong_ans1 = findViewById(R.id.wrong_answer_choice_1);
        EditText new_flashcard_wrong_ans2 = findViewById(R.id.wrong_answer_choice_2);



        ImageView save_flashcard_icon = findViewById(R.id.save_flashcard);
        ImageView cancel_flashcard_icon = findViewById(R.id.cancel_flashcard);

        cancel_flashcard_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Cancel button clicked");
                finish();
            }
        });


        save_flashcard_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Save button clicked");

                String question = new_flashcard_text.getText().toString();
                String answer = new_flashcard_answer.getText().toString();
                String wrongAnswer1 = new_flashcard_wrong_ans1.getText().toString();
                String wrongAnswer2 = new_flashcard_wrong_ans2.getText().toString();

                System.out.println("Entered Question:    " + question);
                System.out.println("Entered answer:     " + answer);
                System.out.println("Entered Wrong Ans 1 :    " + wrongAnswer1);
                System.out.println("Entered Wrong Ans 2 :     " + wrongAnswer2);


                Intent data_to_flashcard = new Intent();
                data_to_flashcard.putExtra("cardQuestion", question);
                data_to_flashcard.putExtra("cardAnswer", answer);
                data_to_flashcard.putExtra("cardWrongAns1", wrongAnswer1);
                data_to_flashcard.putExtra("cardWrongAns2", wrongAnswer2);

                setResult(RESULT_OK, data_to_flashcard);

                finish();



            }
        });



    }


}