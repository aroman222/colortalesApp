package colortales.colortalesapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by aroman on 03/09/2017.
 */

public class consulta_comentario extends AsyncTask<String, Void, String> {

    private Context context;

    public consulta_comentario(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
//        String fullName = arg0[0];
//        String userName = arg0[1];
//        String passWord = arg0[2];
//        String phoneNumber = arg0[3];
//        String emailAddress = arg0[4];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result;

        try {
//            data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
//            data += "&username=" + URLEncoder.encode(userName, "UTF-8");
//            data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
//            data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
//            data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");

            link = "http://localhost/api_ct/getLast.php";
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            InputStream is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer jsonData=new StringBuffer();
            while ((line=br.readLine()) != null)
            {
                jsonData.append(line+"n");
            }
            br.close();
            is.close();
            result = jsonData.toString();

//            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            result = bufferedReader.readLine();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String jsonStr = result;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("mensaje");

                Toast.makeText(context, query_result, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }

}
