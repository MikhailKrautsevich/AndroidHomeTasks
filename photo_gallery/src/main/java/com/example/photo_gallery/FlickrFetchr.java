package com.example.photo_gallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "ced9274278907edee91ca0c4da74fe68";

    public void fetchItems() {
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build()
                    .toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON:" + jsonString);
            JSONObject body = new JSONObject(jsonString);
        } catch (IOException ioe) {
            Log.i(TAG, "RFailed to fetch items", ioe);
        } catch (JSONException je) {
            Log.i(TAG, "Failed to parse JSON", je);
        }
    }

    private void parseItems(List<GalleryItem> items, JSONObject body)
            throws JSONException, IOException {
        JSONObject photosJsonObject = body.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJSONObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJSONObject.getString("id"));
            item.setCaption(photoJSONObject.getString("title"));

            if (!photoJSONObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJSONObject.getString("url_s"));
            items.add(item);
        }
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + " with: " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
