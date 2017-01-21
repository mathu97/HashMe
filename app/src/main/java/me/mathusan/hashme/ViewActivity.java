package me.mathusan.hashme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;


import clarifai2.dto.prediction.Prediction;

/**
 * Created by 100603498 on 1/21/2017.
 */

public class ViewActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    ClarifaiClient client;
    String path;
    List<String> list = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            Intent intent = getIntent();
            Uri uri = intent.getParcelableExtra("uri");
            path = intent.getStringExtra("path");


            client =
                    new ClarifaiBuilder("Ps8k7AruPpKIXZBUSsQl-lqWPxQ2mh-07CAMtsQZ", "DdmH9xGFLRzoZdJcJY50Vec_eT_ykW9-nC4m2HUH").buildSync();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.imageview);
                imageView.setImageBitmap(bitmap);
                new clarifaiTask().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


    class clarifaiTask extends AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>> {


        @Override
        protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
            // The default Clarifai model that identifies concepts in images
            final ConceptModel generalModel = App.get().clarifaiClient().getDefaultModels().generalModel();

            // Use this model to predict, with the image that the user just selected as the input
            return generalModel.predict()
                    .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(path)))
                    .executeSync();
            }

        @Override
        protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
            if (!response.isSuccessful()) {
                System.out.println("Error While Contacting API");
                return;
            }
            final List<ClarifaiOutput<Concept>> predictions = response.get();

            if (predictions.isEmpty()) {
                System.out.println("Empty");
                return;
            }
            else
            {

            }//ADD TO LIST
        }


        }

    }
