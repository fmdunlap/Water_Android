package itp341.dunlap.forrest.water.models;

import java.util.ArrayList;

/**
 * Created by FDUNLAP on 4/30/2017.
 */

public class Question {

    String text;
    String correctAnswer;
    ArrayList<String> incAnswers;

    //First answer in array list is always the right answer.

    public Question(){
        text = null;
        incAnswers = new ArrayList<>();
        correctAnswer = null;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void addQuestion(){

    }

    public String getText() {
        return text;
    }

    public boolean correctAnswer(String str){
        return str.compareTo(correctAnswer) == 0;
    }

    public void addIncorrect(String ans){
        incAnswers.add(ans);
    }

    public ArrayList<String> getIncAnswers() {
        return incAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrect(String str){
        correctAnswer = str;
    }

}
