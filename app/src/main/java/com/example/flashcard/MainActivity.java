package com.example.flashcard;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    protected int getRandom(int minNumber, int maxNumber){
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    protected void getCurrentFlashcardInfo(int idxCard){
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

        TextView answer1 = findViewById(R.id.flashcard_answer_choice_1);
        TextView answer2 = findViewById(R.id.flashcard_answer_choice_2);
        TextView answer3 = findViewById(R.id.flashcard_answer_choice_3);

        if (allFlashcards == null || allFlashcards.size() <= 0){
            flashcardQuestion.setText("No flashcard found");
            flashcardAnswer.setText("No flashcard answer");
        } else {
            Flashcard latest_flashcard = allFlashcards.get(idxCard);

            flashcardQuestion.setText(latest_flashcard.getQuestion());
            flashcardAnswer.setText(latest_flashcard.getAnswer());

            // Need to fix this b/c would make it too easy ..........................fix me fix me
            answer1.setText(latest_flashcard.getAnswer()); // need to fix
            answer2.setText(latest_flashcard.getWrongAnswer1());
            answer3.setText(latest_flashcard.getWrongAnswer2());

        }
    }

    protected void displayCurrentFlashcard_AnswerOnly(){
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

        int cx = flashcardAnswer.getWidth() / 2;
        int cy = flashcardAnswer.getWidth() / 2;
        float finalRadius = (float) Math.hypot(cx,cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, cx, cy,0f, finalRadius);

        flashcardAnswer.setVisibility(View.VISIBLE);
        flashcardQuestion.setVisibility(View.INVISIBLE);

        anim.setDuration(2000);
        anim.start();
    }

    protected void displayCurrentFlashcard_QuestionOnly() {
        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
        flashcardQuestion.setVisibility(View.VISIBLE);
        flashcardAnswer.setVisibility(View.INVISIBLE);

        TextView answer1 = findViewById(R.id.flashcard_answer_choice_1);
        TextView answer2 = findViewById(R.id.flashcard_answer_choice_2);
        TextView answer3 = findViewById(R.id.flashcard_answer_choice_3);

        answer1.setBackgroundColor(getResources().getColor(R.color.peach));
        answer2.setBackgroundColor(getResources().getColor(R.color.peach));
        answer3.setBackgroundColor(getResources().getColor(R.color.peach));

    }

    protected void deleteCurrentFlashcard(){
        // will show prior flashcard or (index - 1) flashcard

        if (allFlashcards != null && allFlashcards.size() != 0) {
            Flashcard currentFlashcard = allFlashcards.get(currentCardDisplayedIndex);
            flashcardDatabase.deleteCard(currentFlashcard.getQuestion().toString());
            allFlashcards = flashcardDatabase.getAllCards();

            if(currentCardDisplayedIndex == 0){
                currentCardDisplayedIndex = 0;
            } else {
                currentCardDisplayedIndex--;
            }

            getCurrentFlashcardInfo(currentCardDisplayedIndex);

            displayCurrentFlashcard_QuestionOnly();

            TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
            Snackbar.make(flashcardQuestion, "Selected card was deleted", Snackbar.LENGTH_SHORT).show();

        }
    }



    protected void nextFlashcard(View view){
        System.out.println("Next button clicked!");

        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        boolean hasFlashcards = !(allFlashcards == null || allFlashcards.size() == 0);

        final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
        final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

        if(!hasFlashcards){
            System.out.println("No flashcards in list");
            getCurrentFlashcardInfo(currentCardDisplayedIndex);
            displayCurrentFlashcard_QuestionOnly();
            Snackbar.make(flashcardQuestion, "No cards found", Snackbar.LENGTH_SHORT).show();
        }
        // has flashcards and not end of flashcard list
        else if(hasFlashcards && (currentCardDisplayedIndex < allFlashcards.size() - 1)){
            System.out.println("Has flashcards and not on final card");

            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    System.out.println("Left out anim");
                    flashcardQuestion.startAnimation(leftOutAnim);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    System.out.println("Right in anim");

                    flashcardQuestion.startAnimation(rightInAnim);

                    int priorCardIndex = currentCardDisplayedIndex;
                    currentCardDisplayedIndex = getRandom(0, allFlashcards.size() - 1);
                    if(priorCardIndex == currentCardDisplayedIndex){
                        currentCardDisplayedIndex = getRandom(0, allFlashcards.size() - 1);
                    }
                    // currentCardDisplayedIndex++;
                    getCurrentFlashcardInfo(currentCardDisplayedIndex);
                    displayCurrentFlashcard_QuestionOnly();
                    hideAnswerChoices();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            flashcardQuestion.startAnimation(leftOutAnim);
        }
        // on last flashcard
        else{
            System.out.println("On last flashcard");
            Snackbar.make(flashcardQuestion, "You've reached end of cards", Snackbar.LENGTH_SHORT).show();

            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    System.out.println("Left out anim 1B");

                    flashcardQuestion.startAnimation(leftOutAnim);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    System.out.println("Right in anim 1B");

                    flashcardQuestion.startAnimation(rightInAnim);

                    int priorCardIndex = currentCardDisplayedIndex;
                    currentCardDisplayedIndex = getRandom(0, allFlashcards.size() - 1);
                    if(priorCardIndex == currentCardDisplayedIndex){
                        currentCardDisplayedIndex = getRandom(0, allFlashcards.size() - 1);
                    }
                    //currentCardDisplayedIndex = 0;
                    getCurrentFlashcardInfo(currentCardDisplayedIndex);
                    displayCurrentFlashcard_QuestionOnly();
                    Snackbar.make(flashcardQuestion, "You've reached end of cards", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


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

        getCurrentFlashcardInfo(currentCardDisplayedIndex);
        displayCurrentFlashcard_QuestionOnly();

        //answer1.setText("Placeholder ans 1");
        //answer2.setText("Placeholder ans 2");
        //answer3.setText("Placeholder ans 3");

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

    // passing data between activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Add flashcard
        if (resultCode == RESULT_OK && requestCode == 100) {

            // put in separate method??

            String cardQuestion = data.getExtras().getString("cardQuestion");
            String cardAnswer = data.getExtras().getString("cardAnswer");
            String cardWrongAnswer1 = data.getExtras().getString("cardWrongAns1");
            String cardWrongAnswer2 = data.getExtras().getString("cardWrongAns2");

            flashcardDatabase.insertCard(new Flashcard(cardQuestion, cardAnswer, cardWrongAnswer1, cardWrongAnswer2));
            allFlashcards = flashcardDatabase.getAllCards();

            currentCardDisplayedIndex = allFlashcards.size() - 1;

            getCurrentFlashcardInfo(currentCardDisplayedIndex);
            displayCurrentFlashcard_QuestionOnly();

            //TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
            //TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);

            //flashcardQuestion.setText(cardQuestion);
            //flashcardAnswer.setText(cardAnswer);

            //flashcardDatabase.insertCard(new Flashcard(cardQuestion, cardAnswer));
            //allFlashcards = flashcardDatabase.getAllCards();
        }

        // Edit Flashcard
        else if (resultCode == RESULT_OK && requestCode == 200){

            String cardQuestion = data.getExtras().getString("cardQuestion");
            String cardAnswer = data.getExtras().getString("cardAnswer");
            String cardWrongAnswer1 = data.getExtras().getString("cardWrongAns1");
            String cardWrongAnswer2 = data.getExtras().getString("cardWrongAns2");

            Flashcard cardToModify = allFlashcards.get(currentCardDisplayedIndex);
            cardToModify.setQuestion(cardQuestion);
            cardToModify.setAnswer(cardAnswer);
            cardToModify.setWrongAnswer1(cardWrongAnswer1);
            cardToModify.setWrongAnswer2(cardWrongAnswer2);

            flashcardDatabase.updateCard(cardToModify);
            allFlashcards = flashcardDatabase.getAllCards();

            getCurrentFlashcardInfo(currentCardDisplayedIndex);
            displayCurrentFlashcard_QuestionOnly();
        }
    }



    // main equivalent

        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView flashcardQuestion = findViewById(R.id.flashcard_question_textview);
        TextView flashcardAnswer = findViewById(R.id.flashcard_answer_textview);
        TextView flashcardPossibleAns1 = findViewById(R.id.flashcard_answer_choice_1);
        TextView flashcardPossibleAns2 = findViewById(R.id.flashcard_answer_choice_2);
        TextView flashcardPossibleAns3 = findViewById(R.id.flashcard_answer_choice_3);


        TextView hint = findViewById(R.id.hint);
        ImageView next_flashcard = findViewById(R.id.next_flashcard);
        ImageView delete_flashcard = findViewById(R.id.delete_flashcard);
        ImageView addFlashcardButton = findViewById(R.id.add_flashcard);
        ImageView toggle2HideAnswers = findViewById(R.id.toggle_visible_choices); // I think this is right but uncertain
        ImageView toggle2ShowAnswers = findViewById(R.id.toggle_hidden_choices);  // I think this is right but uncertain
        ImageView edit_flashcard = findViewById(R.id.edit_flashcard);


        // Database
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        getCurrentFlashcardInfo(currentCardDisplayedIndex);
        displayCurrentFlashcard_QuestionOnly();
        hideAnswerChoices();

        // click one of answers
        flashcardPossibleAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAnswer =  flashcardPossibleAns1.getText().toString();
                String actualAnswer = allFlashcards.get(currentCardDisplayedIndex).getAnswer();

                if (userAnswer.equals(actualAnswer)) {
                    flashcardPossibleAns1.setBackgroundColor(Color.parseColor("#00FF00"));
                } else {
                    flashcardPossibleAns1.setBackgroundColor(Color.parseColor("#FFCCCB"));
                }
            }
        });

        flashcardPossibleAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAnswer =  flashcardPossibleAns2.getText().toString();
                String actualAnswer = allFlashcards.get(currentCardDisplayedIndex).getAnswer();
                if (userAnswer.equals(actualAnswer)) {
                    flashcardPossibleAns2.setBackgroundColor(Color.parseColor("#00FF00"));
                } else {
                    flashcardPossibleAns2.setBackgroundColor(Color.parseColor("#FFCCCB"));
                }
            }
        });

        flashcardPossibleAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAnswer =  flashcardPossibleAns3.getText().toString();
                String actualAnswer = allFlashcards.get(currentCardDisplayedIndex).getAnswer();
                if (userAnswer.equals(actualAnswer)) {
                    flashcardPossibleAns3.setBackgroundColor(Color.parseColor("#00FF00"));
                }
                else {
                    flashcardPossibleAns3.setBackgroundColor(Color.parseColor("#FFCCCB"));
                }
            }
        });

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
                nextFlashcard(view);
            }
        });

        // add button
        addFlashcardButton.setOnClickListener(view -> {
            Intent intentAddFlashcard = new Intent(this, MainActivity2.class);
            startActivityForResult(intentAddFlashcard, 100);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });

        // Edit button
            edit_flashcard.setOnClickListener(view -> {
            Intent intentAddFlashcard = new Intent(this, MainActivity2.class);
            startActivityForResult(intentAddFlashcard, 200);

        });


        // un-hide button
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