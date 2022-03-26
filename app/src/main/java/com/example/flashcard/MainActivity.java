package com.example.flashcard;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    protected void getCurrentFlashcardInfo(int idxCard){
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

        if (allFlashcards == null || allFlashcards.size() == 0){
            flashcardQuestion.setText("I left main are Siafu ants typically found?");
            flashcardAnswer.setText("Africa");
        } else {
            Flashcard latest_flashcard = allFlashcards.get(idxCard);
            flashcardQuestion.setText(latest_flashcard.getQuestion());
            flashcardAnswer.setText(latest_flashcard.getAnswer());
        }
    }

    protected void displayCurrentFlashcard_AnswerOnly(){
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

        flashcardAnswer.setVisibility(View.VISIBLE);
        flashcardQuestion.setVisibility(View.INVISIBLE);
    }

    protected void displayCurrentFlashcard_QuestionOnly() {
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
        flashcardQuestion.setVisibility(View.VISIBLE);
        flashcardAnswer.setVisibility(View.INVISIBLE);
    }

    protected void deleteCurrentFlashcard(){
        // will show prior flashcard or (index - 1) flashcard

        if (allFlashcards != null && allFlashcards.size() != 0) {
            Flashcard currentFlashcard = allFlashcards.get(currentCardDisplayedIndex);
            flashcardDatabase.deleteCard(currentFlashcard.getQuestion().toString());
            allFlashcards = flashcardDatabase.getAllCards();

            // ShowFlashcard() function
            currentCardDisplayedIndex--;
            displayCurrentFlashcard_QuestionOnly();

            // empty flashcard piece of show flashcard function
            TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
            TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
            Snackbar.make(flashcardQuestion, "Selected card was deleted", Snackbar.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) {
            String cardQuestion = data.getExtras().getString("cardQuestion");
            String cardAnswer = data.getExtras().getString("cardAnswer");

            TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
            TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

            flashcardQuestion.setText(cardQuestion);
            flashcardAnswer.setText(cardAnswer);

            flashcardDatabase.insertCard(new Flashcard(cardQuestion, cardAnswer));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        getCurrentFlashcardInfo(currentCardDisplayedIndex);

        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

        // show flashcard answer when question question clicked
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCurrentFlashcard_AnswerOnly();
            }
        });

        // show flashcard question when answer clicked
        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCurrentFlashcard_QuestionOnly();
            }
        });

        ImageView next_flashcard = findViewById(R.id.next_flashcard);
        ImageView delete_flashcard = findViewById(R.id.delete_flashcard);

        // delete current flashcard
        delete_flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCurrentFlashcard();
            }
        });

        // go to next flashcard
        next_flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCardDisplayedIndex++;

                if(allFlashcards == null || allFlashcards.size() == 0){
                    getCurrentFlashcardInfo(currentCardDisplayedIndex);
                    displayCurrentFlashcard_AnswerOnly();
                    //Snackbar.make(flashcardQuestion, "You've reached end of cards", Snackbar.LENGTH_SHORT).show();

                }
                else if(currentCardDisplayedIndex < allFlashcards.size() - 1){
                    currentCardDisplayedIndex++;

                    Flashcard currentFlashcard = allFlashcards.get(currentCardDisplayedIndex);
                    flashcardQuestion.setText(currentFlashcard.getQuestion());
                    flashcardAnswer.setText(currentFlashcard.getQuestion());

                } else{
                    currentCardDisplayedIndex = 0;
                    Snackbar.make(flashcardQuestion, "You've reached end of cards", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        ImageView addFlashcardButton = findViewById(R.id.add_flashcard);

        addFlashcardButton.setOnClickListener(view -> {
            Intent intentAddFlashcard = new Intent(this, MainActivity2.class);
            startActivityForResult(intentAddFlashcard, 100);
        });



    }






}