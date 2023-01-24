package ca.keko.flickrproject.API;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ca.keko.flickrproject.MainGridActivity;
import ca.keko.flickrproject.SinglePhotoActivity;

public class FlickrAPI {

    static final String BASE_URL = "https://www.flickr.com/services/rest";
    /* TODO: put the key somewhere safer later, for now just for testing */
    static final String API_KEY = "3003118ff1a9e12bd27f95dc9927edb1";
    static final String TAG = "FlickrProjectTag";   // for test purposes, find in logcat

    MainGridActivity mainActivity;

    public FlickrAPI(MainGridActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /* Use flickr's "interestingness" endpoint
    * LINK: https://www.flickr.com/services/api/flickr.interestingness.getList.html
    * ARGUMENTS:
    * api_key (required), date (YYYY-MM-DD, optional), extras (optional),
    * per_page (optional), page (optional)
    */
    public void fetchInterestingPhotos() {
        String url = constructInterestingPhotoListURL();
        Log.d(TAG, "fetchInterestingPhotos: " + url);

        PhotoFetcherAsyncService asyncService = new PhotoFetcherAsyncService(mainActivity);
        asyncService.execute(url);
    }

    private String constructInterestingPhotoListURL() {
        String url = BASE_URL;
        url += "?method=flickr.interestingness.getList";
        url += "&api_key=" + API_KEY;
        url += "&format=json";
        url += "&nojsoncallback=1";
        url += "&extras=date_taken,url_h";
        return url;
    }
}
