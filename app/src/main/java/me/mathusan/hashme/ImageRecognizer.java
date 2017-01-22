package me.mathusan.hashme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.ClarifaiRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import static me.mathusan.hashme.ViewActivity.lv;


public class ImageRecognizer {

    // Provide your Client ID
    private final static String CLIENT_ID = "E-0ayOcMgV0EacsUcwfJi3k_L1xTG0EQ9VCWBO_V";

    // Provider Your Client Secret Key
    private final static String CLIENT_SECRET_KEY = "D_1SMArmFqsSpazj_-nzuKyTD4PvsYwAbrU9v3rs";

    // Defining List Object
    private static List<String> resultList = new ArrayList<String>();

    private static String path;

    static ClarifaiClient client;

    public static List<String> recognize(String imageUrl, Activity activity) {

        path = imageUrl;
        // Defining List Object
        List<String> resultList = new ArrayList<String>();

        if (imageUrl != null && !imageUrl.isEmpty()) {

            client = new ClarifaiBuilder(CLIENT_ID, CLIENT_SECRET_KEY).buildSync();
            ArrayAdapter adapter = new ArrayAdapter<String>
                    (activity, android.R.layout.simple_list_item_1, resultList);
            new Clarifaitask(activity,adapter).execute();

        }
        return resultList;
    }

    private static class Clarifaitask extends AsyncTask<List<String>, String, List<String>>{

        private Context mContext;
        private ArrayAdapter adapter;

        public Clarifaitask(Context context, ArrayAdapter adapter) {
            this.mContext = context;
            this.adapter = adapter;
            ViewActivity.lv.setAdapter(adapter);
        }

        @Override
        protected  void onPreExecute ()
        {
            ViewActivity.mProgress.show();
        }

        @Override
        protected List<String> doInBackground(List<String>... params) {
            final List<ClarifaiOutput<Concept>> predictionResults =
                    client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get custom models
                            .predict()
                            .withInputs(
                                    ClarifaiInput.forImage(ClarifaiImage.of(new File(path)))
                            )
                            .executeSync()
                            .get();


            if (predictionResults != null && predictionResults.size() > 0) {

                // Prediction List Iteration
                for (int i = 0; i < predictionResults.size(); i++) {

                    ClarifaiOutput<Concept> clarifaiOutput = predictionResults.get(i);

                    List<Concept> concepts = clarifaiOutput.data();

                    if(concepts != null && concepts.size() > 0) {
                        for (int j = 0; j < concepts.size(); j++) {
                            String chance = String.format("%.2f", concepts.get(j).value()*100);
                            resultList.add(concepts.get(j).name() + ", Place:" + (j+1) + ", Percent:" + chance + "%");
                        }
                    }
                }
            }

        return resultList;
    }

        @Override
        protected void onPostExecute(List<String> resultList)
        {
            adapter.notifyDataSetChanged();
            ViewActivity.mProgress.hide();
        }

        }
    }
