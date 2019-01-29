/*
 * The move requester for ClickTacToe by Clint Nieuwendijk
 * It includes 1 Volley request queue so a function to clear the queue can be called upon
 */

package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MoveRequest implements Response.Listener<JSONObject>, Response.ErrorListener {
    Context context;
    MoveRequest.Callback activity;
    private RequestQueue queue;

    public MoveRequest(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public interface Callback {
        void gotMove(JSONObject response) throws JSONException;
        void gotMoveError(String message);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        activity.gotMoveError(error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            activity.gotMove(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // post move to server as JSON object
    public void postMove(Callback activity, int movePlayed, int gameID) {
        this.activity = activity;

        JSONObject postJSON = new JSONObject();
            try {
                postJSON.put("gameID", gameID);
                postJSON.put("lastMove", movePlayed);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

        String requestURL = "http://ide50.manhut.c9users.io:8080/ClickTacToeMoveHandler";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestURL, postJSON, this, this);
        jsonRequest.setTag("moveRequest");
        queue.add(jsonRequest);
    }

    // clear current move requests in the queue
    public void cancelRequests(){
        queue.cancelAll("moveRequest");
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
