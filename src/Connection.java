package src;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.lang.StringBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Connection{


	public static void main(String[] args) throws IOException, JSONException, ParseException{
		step1();
		step2();
		step3();
		step4();
		step5();
	} 

	private static void step1() throws IOException, JSONException { 
		JSONObject param = new JSONObject();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");
		param.put("github","https://github.com/rafael-reyes/code2040_api");

		connect("http://challenge.code2040.org/api/register",param);
	}

	private static void step2() throws IOException, JSONException { 
		JSONObject param = new JSONObject();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");

		String res = connect("http://challenge.code2040.org/api/reverse",param);
		
		param.put("string",(new StringBuilder(res).reverse().toString()));//reversed string
		connect("http://challenge.code2040.org/api/reverse/validate",param);
	}

	private static void step3() throws IOException, JSONException { 
		JSONObject param = new JSONObject();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");

		String res = connect("http://challenge.code2040.org/api/haystack",param);
		JSONObject obj = new JSONObject(res);
		
		String needle = obj.getString("needle");
		JSONArray arr = obj.getJSONArray("haystack");
		for (int i = 0; i < arr.length(); i++){
		    if ((arr.getString(i)).equals(needle)) {//when true we find the index of the "needle"
		    	param.put("needle", i);
		    	break;
		    }
		}
		connect("http://challenge.code2040.org/api/haystack/validate",param);
	}
	
	private static void step4() throws IOException, JSONException { 
		JSONObject param = new JSONObject();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");

		String res = connect("http://challenge.code2040.org/api/prefix",param);
		JSONObject obj = new JSONObject(res);
		
		String prefix = obj.getString("prefix");
		JSONArray arr = obj.getJSONArray("array");
		JSONArray arr2 = new JSONArray();
		for (int i = 0; i < arr.length(); i++){
			//creating new array only including words that do not start with prefix
			if (!(arr.getString(i)).startsWith(prefix)) {
		    	arr2.put(arr.getString(i));
		    }
		}

		param.put("array", arr2);
		connect("http://challenge.code2040.org/api/prefix/validate",param);
	}
	
	private static void step5() throws IOException, JSONException, ParseException { 
		JSONObject param = new JSONObject();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");

		String res = connect("http://challenge.code2040.org/api/dating",param);
		JSONObject obj = new JSONObject(res);
		
		String dateStamp = obj.getString("datestamp");
		int secs = obj.getInt("interval");
		//format for dateStamp to comply with ISO 8601
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		Date date = df.parse(dateStamp);
		//adding interval to given date
		long dateTime = ((date.getTime() / 1000)+secs)*1000;
		Date date2 = new Date(dateTime);
		String formattedDate = df.format(date2);
		
		param.put("datestamp", formattedDate);
		connect("http://challenge.code2040.org/api/dating/validate",param);
	}
	
	private static String connect(String url, JSONObject param) throws IOException{
		URL code2040 = new URL(url);
 		HttpURLConnection con = (HttpURLConnection) code2040.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		// For POST
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(param.toString().getBytes("UTF-8"));
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                con.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	        	response.append(inputLine);
	        }
	        in.close();

			// print result
			System.out.println("Response: "+response.toString());
			return response.toString();
		} else {
			System.out.println("Http POST request failed!");
			return null;
		}
	}

}

