package com.example.clintnieuwendijk.tictactoeclicker;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GameRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    Context context;
    GameRequest.Callback activity;

    public GameRequest(Context context) {
        this.context = context;
    }

    public interface Callback {
        void gotGame(JSONObject response) throws JSONException;
        void gotGameError(String message);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("error", error.toString());
        activity.gotGameError(error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            activity.gotGame(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void requestGame(Callback activity, int gridSize, String playerID, String status) {
        this.activity = activity;

        RequestQueue queue = Volley.newRequestQueue(context);
        String requestURL = "http://ide50-manhut.c9users.io:8080/ClickTacToeGameRequest";
        JSONObject postJSON = new JSONObject();
        try {
            postJSON.put("gridSize", gridSize);
            postJSON.put("playerID", playerID);
            postJSON.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestURL, postJSON, this, this);
        queue.add(jsonRequest);
    }
}
