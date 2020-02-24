package com.yashoid.modelfeaturesvalueparser.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yashoid.mmv.ModelFeatures;
import com.yashoid.modelfeaturesvalueparser.ModelFeaturesValueParser;
import com.yashoid.yashson.Yashson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String sample = getSampleObject().toString();

                Yashson yashson = new Yashson();
                yashson.addValueParser(ModelFeatures.class, new ModelFeaturesValueParser());

                try {
                    ModelFeatures modelFeatures = yashson.parse(sample, ModelFeatures.class);

                    ((TextView) view).setText("SUCCESS!");
                } catch (IOException e) {
                    e.printStackTrace();

                    ((TextView) view).setText("Failure!");
                }
            }

        });
    }

    private JSONObject getSampleObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", "Yashar");
            obj.put("age", 12);
            obj.put("grades", getGrades());
        } catch (JSONException e) { }

        return obj;
    }

    private JSONArray getGrades() {
        JSONArray arr = new JSONArray();

        for (int i = 0; i < 3; i++) {
            arr.put(getGrade());
        }

        return arr;
    }

    private JSONObject getGrade() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("score", Math.random() * 20);
        } catch (JSONException e) { }

        return obj;
    }

}
