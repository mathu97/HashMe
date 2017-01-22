package me.mathusan.hashme;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.ClarifaiRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;


public class ImageRecognizer {

    // Provide your Client ID
    private final static String CLIENT_ID = "Ps8k7AruPpKIXZBUSsQl-lqWPxQ2mh-07CAMtsQZ";

    // Provider Your Client Secret Key
    private final static String CLIENT_SECRET_KEY = "DdmH9xGFLRzoZdJcJY50Vec_eT_ykW9-nC4m2HUH";


    // Defining List Object
    private static List<String> resultList = new ArrayList<String>();

    private static String path;

    static ClarifaiClient client;

    public static List<String> recognize(String imageUrl) {

        path = imageUrl;

        // Defining List Object
        List<String> resultList = new ArrayList<String>();

        if (imageUrl != null && !imageUrl.isEmpty()) {

            client = new ClarifaiBuilder(CLIENT_ID, CLIENT_SECRET_KEY).buildSync();

            new Clarifaitask().execute();

        }
        return resultList;
    }

    private static class Clarifaitask extends AsyncTask<List<String>, String, List<String>>{


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

                            resultList.add(concepts.get(j).name());
                        }
                    }
                }
            }

        return resultList;
    }

        @Override
        protected void onPostExecute(List<String> resultList)
        {
            // Iteration of Result
            for(String result : resultList) {

                ViewActivity.textView.append("\n");
                ViewActivity.textView.append(" #" + result);
            }
        }

        }
    }
