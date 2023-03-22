import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class DB {
    public String url_main="https://studev.groept.be/api/a22ib2d02/";
    public String makeGETRequest(String urlName){
        BufferedReader rd;
        StringBuilder sb;
        String line;
        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                sb.append(line + '\n');
            }
            conn.disconnect();
            return sb.toString();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (ProtocolException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "";

    }

    public int get_mode(){
        String response= makeGETRequest(  url_main+"getMode");
       int mode=0;
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                //System.out.println("ID: " + curObject.getString("card_number") + " Name: " + curObject.getString("name"));
                mode= curObject.getInt("mode");
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return mode;
    }
    public ArrayList<String> get_RFID_info(){
        String response = makeGETRequest(url_main+"RFID_list" );
        ArrayList<String> list=new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                //System.out.println("ID: " + curObject.getString("card_number") + " Name: " + curObject.getString("name"));
                String s="";
                s+="ID: ";

                int id=curObject.getInt("id");
                if(String.valueOf(id).length()<3)
                {
                    for(int j=0;j<3-String.valueOf(id).length();j++){
                        s+="0";
                    }
                }
                s+=id;
                s+="     Name: ";
                s+=curObject.getString("name");
                list.add(s);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<String> get_scan_history(int cardNumber){
        String response;
        if(cardNumber==0)
            response = makeGETRequest(url_main+"get_scan_history_all");
        else
            response = makeGETRequest(url_main+"get_scan_history/"+cardNumber);
        ArrayList<String> list=new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                String s="ID:";
                int id=curObject.getInt("cardID");
                if(String.valueOf(id).length()<3)
                {
                    for(int j=0;j<3-String.valueOf(id).length();j++){
                        s+="0";
                    }
                }
                s+=id;
                s+="    |    Name: ";
                String name=curObject.optString("name","Unknown");
                s+=name;
                if(name.length()<8){
                    for(int j=0;j<8-name.length();j++){
                        s+="  ";
                    }
                }

                switch (curObject.getInt("action")){

                    case 0:
                        s+="  |  Denied  |  ";
                                break;
                    case 1:
                        s+="  |  Unlock  |   ";
                        break;
                    case 2:
                        s+="  |  Lock     |   ";
                        break;
                    default:
                        s+="  | Unknown  ";
                }
                s+="     ";
                long time_int = curObject.getLong("time");
                Instant instant = Instant.ofEpochSecond(time_int);
                ZoneId belgiumZoneId = ZoneId.of("Europe/Brussels");
                LocalDateTime time = LocalDateTime.ofInstant(instant,belgiumZoneId);
                s+=time;
                list.add(s);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    //get the history of alarming with the state and time
    public ArrayList<String> get_alarm_history(){
        String response = makeGETRequest(url_main+"get_alarm_history" );
        ArrayList<String> list=new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                String s="";
                long time_int = curObject.getLong("time");
                Instant instant = Instant.ofEpochSecond(time_int);
                ZoneId belgiumZoneId = ZoneId.of("Europe/Brussels");
                LocalDateTime time = LocalDateTime.ofInstant(instant,belgiumZoneId);
                s+=time;
                list.add(s);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    //return the state of the lock and alarm
    public int[] get_lock_state(){
        String scan_response = makeGETRequest(url_main+"getMode");
        int[] mode={0,0};
        try {
            JSONArray array = new JSONArray(scan_response);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                mode[0]=curObject.getInt("isLocked");
                mode[1]=curObject.getInt("isAlarming");
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return mode;
    }
}


