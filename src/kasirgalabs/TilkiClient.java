/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasirgalabs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

/**
 *
 * @author Kerim
 */
public class TilkiClient {

    /**
     * @param args the command line arguments
     */
    public Exam[] listExams() {
        Exam[] examList = null;
        
        try {
            //This url will be changed after heroku deploy.
            String url = "https://examwebserver.herokuapp.com/tilki/list_exams.json";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String sourceString = response.toString();

            JSONArray jsonArr = new JSONArray(sourceString);
            
            examList = new Exam[jsonArr.length()];
            
            for(int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String name = jsonObj.getString("name");
                Exam exam = new Exam(name,"default description");
                examList[i] = exam;                
            }
        }
        catch(Exception e) {

        };
        
        return examList;        
    }

    public int checkIn(String name, String surname, int number, String exam) {
        int loggedIn = 0;

        try {

            String url = "https://examwebserver.herokuapp.com/tilki/check_in.json";

            url += "?name=" + name;
            url += "&surname=" + surname;
            url += "&number=" + number;
            url += "&exam=" + exam;
            url = replaceSpaceInString(url);
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String serverResponse = response.toString();

            if(serverResponse.equalsIgnoreCase("Record successfully found")) {
                loggedIn = 1;
            }

        }
        catch(Exception e) {
            System.out.println("Exception in login action");
        };

        return loggedIn;
    }

    public int verifyInstructorKey(String exam, String key) {
        int verifyStatus = 0;

        try {

            String url = "https://examwebserver.herokuapp.com/tilki/verify_key.json";

            url += "?exam=" + exam;
            url += "&key=" + key;
            url = replaceSpaceInString(url);
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String serverResponse = response.toString();

            if(serverResponse.equalsIgnoreCase("Instructor verified")) {
                verifyStatus = 1;
            }

        }
        catch(Exception e) {
            System.out.println("Exception in verify Instructor action");
        };

        return verifyStatus;
    }
    
    
    public String replaceSpaceInString(String s){
    int i;
    for (i=0;i<s.length();i++){
        System.out.println("i is "+i);
        if (s.charAt(i)==(int)32){
            s=s.substring(0, i)+"%20"+s.substring(i+1, s.length());
            i=i+2;              
            }
    }
    return s;
    }
    

}
