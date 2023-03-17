import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.*;

public class DB {
   // String url_main="https://studev.groept.be/api/";

    public String makeGETRequest(String urlName){
        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;
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

    public void parseJSON(String jsonString){
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                System.out.println("The coach for the " + curObject.getString("Date") + " session is " + curObject.getString("Coach"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int get_mode(String jsonString){
       int mode=0;
        try {
            JSONArray array = new JSONArray(jsonString);
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
    public ArrayList<String> get_RFID_info(String jsonString){
        ArrayList<String> list=new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                //System.out.println("ID: " + curObject.getString("card_number") + " Name: " + curObject.getString("name"));
             list.add("ID: " + String.valueOf(curObject.getInt("id")) + "     Name: " + curObject.getString("name"));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    /*public Map<String,Integer> get_RFID_by_map(String jsonString){
        Map<String,Integer> map=new HashMap<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                //System.out.println("ID: " + curObject.getString("card_number") + " Name: " + curObject.getString("name"));
               map.put( curObject.getString("name"),curObject.getInt("cardNumber") );
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return map;
    }*/
    public ArrayList<String> get_scan_history(String jsonString){
        ArrayList<String> list=new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                String s="ID:";

                int id=curObject.getInt("cardID");
                    s+=id;
                    s+=" | Name: ";
                    String name=curObject.optString("name","Unknown");
                   // if(name.isEmpty())
                     //   name="unknown";
                   // System.out.println("name");
                 s+=name;
                    if(name.length()<10){
                        for(int j=0;j<10-name.length();j++){
                            s+=" ";
                        }
                    }


                switch (curObject.getInt("action")){

                    case 0:
                        s+=" | Denied  |  ";
                                break;
                    case 1:
                        s+=" | Unlock  |   ";
                        break;
                    case 2:
                        s+=" | Lock     |   ";
                        break;
                    default:
                        s+=" |Unknown  ";
                }

                long time_int = curObject.getLong("time");
            //    System.out.println("time:"+time_int);
                Instant instant = Instant.ofEpochSecond(time_int);
                ZoneId belgiumZoneId = ZoneId.of("Europe/Brussels");
                LocalDateTime time = LocalDateTime.ofInstant(instant,belgiumZoneId);

               /* long time_int=curObject.getInt("time");
                Instant instant=Instant.ofEpochSecond(time_int);
                //   ZoneId zoneId=ZoneId.of("Euro/Belgium");
                LocalDateTime time=LocalDateTime.ofInstant(instant,ZoneId.systemDefault());*/
                //  Date time= Date.from(instant);
                s+=time;
               // System.out.println("a: " + String.valueOf(curObject.getInt("action")) + "     t: " + curObject.getString("time"));
                list.add(s);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    //get the history of alarming with the state and time
    public ArrayList<String> get_alarm_history(String jsonString){
        ArrayList<String> list=new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                String s="";
                long time_int = curObject.getLong("time");
             //   System.out.println(curObject.getLong("time"));
                Instant instant = Instant.ofEpochSecond(time_int);
                ZoneId belgiumZoneId = ZoneId.of("Europe/Brussels");
                LocalDateTime time = LocalDateTime.ofInstant(instant,belgiumZoneId);
              //  Date time= Date.from(instant);
                s+=time;
                list.add(s);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    public void erase_alarm_history(String jsonString){

    }
    //return the state of the lock and alarm
    public int[] get_lock_state(String jsonString){
        int[] mode={0,0};
        try {
            JSONArray array = new JSONArray(jsonString);
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
    public int get_mode_lock(String jsonString){
        int mode=0;
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
               mode=curObject.getInt("isLocked");
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return mode;
    }
    /*public static void main(String[] args) {
        DB rc = new DB();
        String response = rc.makeGETRequest("https://studev.groept.be/api/a21ib2demo/all" );
        rc.parseJSON(response);
    }*/
}


