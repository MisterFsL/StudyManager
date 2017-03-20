package be.formation.studymanager.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by student on 20-03-17.
 */

public class LoadTask extends AsyncTask<String,Void,Bitmap> {

    public interface LoadTaskCallback{
        void updateView(Bitmap drawable);
    }

    private LoadTaskCallback callback;

    public LoadTask(LoadTaskCallback callback){
        this.callback = callback;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        callback.updateView(bitmap);
    }
}
