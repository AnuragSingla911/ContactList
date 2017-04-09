package contactlist.android.com.contactlist.server;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jade on 9/4/17.
 */

public class ServerInteractor extends IntentService {

    private String mRequestUrl = "http://139.162.152.157/contactlist.php";

    public ServerInteractor() {
        super(ServerInteractor.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            String httpData = "";
            URL url = new URL(mRequestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("token", "c149c4fac72d3a3678eefab5b0d3a85a");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            httpData = stringBuffer.toString();
            new ContactListParser().parse(httpData, this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
    }
}
