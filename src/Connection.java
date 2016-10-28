package src;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;
import com.google.gson;

public class Connection{


	public static void main(String[] args) throws IOException{
		// step1();
		// step2();
		step3();

	} 

	private static void step1() throws IOException { 
		Map<String, Object> param = new HashMap<>();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");
		param.put("github","https://github.com/rafael-reyes/code2040_api");

		connect("http://challenge.code2040.org/api/register",param);
	}

	private static void step2() throws IOException { 
		Map<String, Object> param = new HashMap<>();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");

		String res = connect("http://challenge.code2040.org/api/reverse",param);

		param.put("string",(new StringBuilder(res).reverse().toString()));
		connect("http://challenge.code2040.org/api/reverse/validate",param);
	}

	private static void step3() throws IOException { 
		Map<String, Object> param = new HashMap<>();
		param.put("token","6fb2f287ac07a6db27ff145186e252e6");

		String res = connect("http://challenge.code2040.org/api/haystack",param);

		// param.put("needle", index);
		// connect("http://challenge.code2040.org/api/haystack/validate",param);
	}

	private static String connect(String url, Map<String,Object> param) throws IOException{
		URL code2040 = new URL(url);
 		HttpURLConnection con = (HttpURLConnection) code2040.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		// For POST
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(constructJsonObj(param).getBytes("UTF-8"));
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

	private static String constructJsonObj(Map<String,Object> param){
		String json = "{";
		for (Map.Entry<String, Object> entry : param.entrySet()) {
		    String key = entry.getKey();
		    String value = (String) entry.getValue();
		    json += "\"" + key +"\":\"" +value+"\",";
		}
		json = json.substring(0,json.length()-1)+ "}";

		return json;
	}
}

