package com.arlefreak.archillectmuzei;

/**
 * Created by arlefreak on 08/08/2015.
 */


import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.JsPromptResult;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import com.arlefreak.archillectmuzei.ArchillectService.Photo;
import com.arlefreak.archillectmuzei.ArchillectService.PhotosResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;

public class ArchillectSource extends RemoteMuzeiArtSource {
    private static final String TAG = "Archillect";
    private static final String SOURCE_NAME = "SOURCE_NAME";//res.getString(R.string.source_name);
    private static final String A_URL = "http://archillect.com/";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours

    public ArchillectSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : null;

        Random random = new Random();
        Photo photo;
        int id = randomId();
        String token = Integer.toString(id);

        String imgUrl = getArchillectImage(id);

        publishArtwork(new Artwork.Builder()
                .title(token)
                .byline(TAG)
                .imageUri(Uri.parse(imgUrl))
                .token(token)
                .viewIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(imgUrl)))
                .build());
    }

    public int randomId(){
        try {
            Document doc = Jsoup.connect(A_URL).get();
            Element element = doc.select("div.overlay").first();
            int lasID = Integer.parseInt(element.text());
            Random ran = new Random();
            int x = ran.nextInt(lasID) + 1;
            return  x;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public String getArchillectImage(int id){
        try {
            // get the tables on this page, note I masked the phone number
            Document doc = Jsoup.connect(A_URL + id).get();
            Element img = doc.select("#ii").first();
            String imgUrl = img.attr("src");
            Log.d("GETIMAGE", imgUrl);
            return imgUrl;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
