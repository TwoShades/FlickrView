package ca.keko.flickrproject.API;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ca.keko.flickrproject.MainGridActivity;
import ca.keko.flickrproject.R;


public class PhotoFetcherAsyncService extends AsyncTask<String, Void, List<PhotoPojo>> {
    static final String TAG = "FlickrProjectTag";
    MainGridActivity mainActivity;

    public PhotoFetcherAsyncService(MainGridActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Deprecated
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Executes in main UI thread
        // Default progressBar visibility set to "gone", appears on pre-execute
        // We use indeterminate bar (no time specified) to show spinner wheel
        // We will change visibility back to GONE in post-execute
        ProgressBar progressBar = mainActivity.findViewById(R.id.progressBarMain);
        progressBar.setVisibility(View.VISIBLE);
    }

    /* What we doInBackground:
    * 1) open the URL request
    * 2) download the JSON response
    * 3) parse the JSON response into PhotoPojo objects
    */
    @Deprecated
    @Override
    protected List<PhotoPojo> doInBackground(String... strings) {
        String url = strings[0];
        List<PhotoPojo> photoList = new ArrayList<>();

        // NOTE: base URL of FlickrAPI uses https
        try {
            // 1. Try to open URL over HTTPS protocol
            URL urlObject = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) urlObject.openConnection();

            // 2. Successfully opened URL, download JSON response
            String jsonResult = "";
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1) {
                jsonResult += (char) data;
                data = reader.read();
            }
            // Tagged for testing to show preview of JSON response in logcat
            Log.d(TAG, "doInBackground: " + jsonResult.substring(0, 200));

            // 3. Parse the JSON into a JSON object
            JSONObject jsonObject = new JSONObject(jsonResult);
            // Grab "photos" which is our "root" in jsonObject
            JSONObject photosObject = jsonObject.getJSONObject("photos");
            // Array is labeled "photo"
            JSONArray photosArray = photosObject.getJSONArray("photo");
            for(int i = 0; i < photosArray.length(); i++) {
                JSONObject singlePhotoObject = photosArray.getJSONObject(i);
                // Our parse method will return null if it fails
                PhotoPojo photoPojo = parsePhotoPojo(singlePhotoObject);
                if(photoPojo != null) {
                    photoList.add(photoPojo);
                }
            }

        } catch (IOException | JSONException e) {
            // Catch malformed URLs, connection errors or JSON parse exceptions
            e.printStackTrace();
        }
        return photoList;
    }

    // Parse JSONObject photos into PhotoPojo
    private PhotoPojo parsePhotoPojo(JSONObject singlePhotoObject) {
        PhotoPojo photoPojo = null;

        try {
            String id = singlePhotoObject.getString("id");
            String title = singlePhotoObject.getString("title");
            String dateTaken = singlePhotoObject.getString("datetaken");
            String photoURL = singlePhotoObject.getString("url_h");
            photoPojo = new PhotoPojo(id, title, dateTaken, photoURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photoPojo;
    }

    @Override
    protected void onPostExecute(List<PhotoPojo> photoPojos) {
        super.onPostExecute(photoPojos);

        Log.d(TAG, "onPostExecute: " + photoPojos.size());
        mainActivity.receivedPhotos(photoPojos);

        // MAINACTIVITY RecyclerView init methods
        // We need to wait until our PhotoList is sent to MainActivity
        // before we can call these methods
        try {
            mainActivity.initializeRecyclerView();
            mainActivity.setRecyclerViewLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // We remove our ProgressBar once the photo list has loaded from our URL
        try {
            ProgressBar progressBar = mainActivity.findViewById(R.id.progressBarMain);
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
