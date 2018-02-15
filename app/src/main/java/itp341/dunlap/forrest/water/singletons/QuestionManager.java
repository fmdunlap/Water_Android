package itp341.dunlap.forrest.water.singletons;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import itp341.dunlap.forrest.water.fragments.QuestionFragment;
import itp341.dunlap.forrest.water.models.Question;

/**
 * Created by FDUNLAP on 4/27/2017.
 */

public class QuestionManager {
    private static final QuestionManager ourInstance = new QuestionManager();

    private ArrayList<Question> QuestionBank;

    int lastAsked = 0;
    int currentCategory = 0;

    public static QuestionManager getInstance() {
        return ourInstance;
    }

    private QuestionManager() {

    }

    public void clearAndLoadNewQuestions(int category, RequestQueue queue, final QuestionFragment questionFragment){
        currentCategory = category;
        if(QuestionBank != null) {
            QuestionBank.clear();
        } else {
            QuestionBank = new ArrayList<>();
        }

        int toGet = 5;





        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("opentdb.com")
                .appendPath("api.php")
                .appendQueryParameter("amount", ((Integer)toGet).toString())
                .appendQueryParameter("category", ((Integer)category).toString());

        String URL = builder.build().toString();

        Log.d(null, URL);
        //TODO: Implement category functionality

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(null, "On Response");
                        try {
                            JSONArray jos = (JSONArray) response.get("results");
                            for(int i = 0; i < jos.length(); i++){
                                JSONObject jo = jos.getJSONObject(i);
                                Question q = new Question();
                                q.setText(StringEscapeUtils.unescapeHtml4(jo.getString("question")));
                                q.setCorrect(StringEscapeUtils.unescapeHtml4(jo.getString("correct_answer")));
                                JSONArray inc_arr = jo.getJSONArray("incorrect_answers");
                                for(int j = 0; j < inc_arr.length(); j++){
                                    q.addIncorrect(StringEscapeUtils.unescapeHtml4((String)inc_arr.get(j)));
                                }
                                addQuestion(q);
                            }
                            if(questionFragment != null) {
                                questionFragment.onFinishQuestionLoad();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } {

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(null, "On Error Response");
                        // TODO Auto-generated method stub

                    }
                });

        queue.add(jsObjRequest);
    }


    //SIMPLY adds the question to the current questionbank. Nothing more.
    public void addQuestion(Question q){
        QuestionBank.add(q);
    }

    public Question nextQuestion(RequestQueue queue){
        Question toRet = QuestionBank.get(lastAsked);

        if(lastAsked == QuestionBank.size() - 2) {
            clearAndLoadNewQuestions(currentCategory, queue, null);
            lastAsked = 0;
        } else {
            lastAsked++;
        }

        return toRet;
    }
}
