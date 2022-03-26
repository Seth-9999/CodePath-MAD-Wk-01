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

        if (allFlashcards == null || allFlashcards.size() <= 0){
            flashcardQuestion.setText("No flashcard found");
            flashcardAnswer.setText("No flashcard answer");
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
        System.out.println("Start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");

        if (allFlashcards != null && allFlashcards.size() != 0) {
            Flashcard currentFlashcard = allFlashcards.get(currentCardDisplayedIndex);
            System.out.println("Weird!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
            flashcardDatabase.deleteCard(currentFlashcard.getQuestion().toString());
            allFlashcards = flashcardDatabase.getAllCards();

            if(currentCardDisplayedIndex == 0){
                currentCardDisplayedIndex = 0;
            } else {
                currentCardDisplayedIndex--;
            }
            System.out.println("Temp!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");

            getCurrentFlashcardInfo(currentCardDisplayedIndex);

            displayCurrentFlashcard_QuestionOnly();

            TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
            Snackbar.make(flashcardQuestion, "Selected card was deleted", Snackbar.LENGTH_SHORT).show();

            System.out.println("End!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");

        }
    }

    // passing data between activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) {
            String cardQuestion = data.getExtras().getString("cardQuestion");
            String cardAnswer = data.getExtras().getString("cardAnswer");

            flashcardDatabase.insertCard(new Flashcard(cardQuestion, cardAnswer));
            allFlashcards = flashcardDatabase.getAllCards();

            getCurrentFlashcardInfo(currentCardDisplayedIndex++);
            displayCurrentFlashcard_QuestionOnly();

            //TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
            //TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

            //flashcardQuestion.setText(cardQuestion);
            //flashcardAnswer.setText(cardAnswer);

            //flashcardDatabase.insertCard(new Flashcard(cardQuestion, cardAnswer));
            //allFlashcards = flashcardDatabase.getAllCards();
        }
    }

    protected void nextFlashcard(){
        System.out.println("Next button clicked!");

        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
        boolean hasFlashcards = !(allFlashcards == null || allFlashcards.size() == 0);

        if(!hasFlashcards){
            System.out.println("Why not working!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            getCurrentFlashcardInfo(currentCardDisplayedIndex);
            displayCurrentFlashcard_QuestionOnly();
            Snackbar.make(flashcardQuestion, "No cards found", Snackbar.LENGTH_SHORT).show();

        }
        // has flashcards and not end of flashcard list
        else if(hasFlashcards && (currentCardDisplayedIndex < allFlashcards.size() - 1)){
            currentCardDisplayedIndex++;
            getCurrentFlashcardInfo(currentCardDisplayedIndex);
            displayCurrentFlashcard_QuestionOnly();
        }
        // on last flashcard
        else{
            currentCardDisplayedIndex = 0;
            getCurrentFlashcardInfo(currentCardDisplayedIndex);
            displayCurrentFlashcard_QuestionOnly();
            Snackbar.make(flashcardQuestion, "You've reached end of cards", Snackbar.LENGTH_SHORT).show();
        }
    }

    protected void hideAnswerChoices(){
        System.out.println("Trying to hide answer choices");

        TextView answer1 = findViewById(R.id.flashcard_answer_choice_1);
        TextView answer2 = findViewById(R.id.flashcard_answer_choice_2);
        TextView answer3 = findViewById(R.id.flashcard_answer_choice_3);
        ImageView toggle2HideAnswers = findViewById(R.id.toggle_visible_choices); // I think this is right but uncertain
        ImageView toggle2ShowAnswers = findViewById(R.id.toggle_hidden_choices);  // I think this is right but uncertain

        answer1.setText("Hidden Possible answer");
        answer2.setText("Hidden Possible answer");
        answer3.setText("Hidden Possible answer");

        toggle2HideAnswers.setVisibility(View.INVISIBLE);
        toggle2ShowAnswers.setVisibility(View.VISIBLE);
    }

    protected void showAnswerChoices(){
        System.out.println("Trying to show answer choices");
        TextView answer1 = findViewById(R.id.flashcard_answer_choice_1);
        TextView answer2 = findViewById(R.id.flashcard_answer_choice_2);
        TextView answer3 = findViewById(R.id.flashcard_answer_choice_3);
        ImageView toggle2HideAnswers = findViewById(R.id.toggle_visible_choices); // I think this is right but uncertain
        ImageView toggle2ShowAnswers = findViewById(R.id.toggle_hidden_choices);  // I think this is right but uncertain

        answer1.setText("Placeholder ans 1");
        answer2.setText("Placeholder ans 2");
        answer3.setText("Placeholder ans 3");

        toggle2HideAnswers.setVisibility(View.VISIBLE);
        toggle2ShowAnswers.setVisibility(View.INVISIBLE);
    }

    protected void showHint(){
        TextView hint = findViewById(R.id.hint);
        hint.setText("No hint found. Try editing flashcard");
    }

    protected  void hideHint(){
        TextView hint = findViewById(R.id.hint);
        hint.setText("Hint __ click to see");
    }


    // main equivalent

        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
        TextView hint = findViewById(R.id.hint);
        ImageView next_flashcard = findViewById(R.id.next_flashcard);
        ImageView delete_flashcard = findViewById(R.id.delete_flashcard);
        ImageView addFlashcardButton = findViewById(R.id.add_flashcard);
        ImageView toggle2HideAnswers = findViewById(R.id.toggle_visible_choices); // I think this is right but uncertain
        ImageView toggle2ShowAnswers = findViewById(R.id.toggle_hidden_choices);  // I think this is right but uncertain

            // Database
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        getCurrentFlashcardInfo(currentCardDisplayedIndex);
        displayCurrentFlashcard_QuestionOnly();
        hideAnswerChoices();

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
                nextFlashcard();
            }
        });

        // add button
        addFlashcardButton.setOnClickListener(view -> {
            Intent intentAddFlashcard = new Intent(this, MainActivity2.class);
            startActivityForResult(intentAddFlashcard, 100);
        });

        // unhide button
        toggle2ShowAnswers.setOnClickListener(view -> {
            showAnswerChoices();
        });

        // hide button
        toggle2HideAnswers.setOnClickListener(view -> {
            hideAnswerChoices();
        });



        //  Not very good .... how to check if hint clicked or not......

        // show hint
        hint.setOnClickListener(view -> {
            if(hint.getText().equals("No hint found. Try editing flashcard")){
                showHint();
                
            } else {
                hideHint();
            }
        });



    }






}