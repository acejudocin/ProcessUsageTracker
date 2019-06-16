package ProcessUsageTracker;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CEJCINAN
 */
public class ProcessUsageTracker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            String process;
            int i = 0;
            Process p = Runtime.getRuntime().exec("powershell Get-Process Teams | select name, starttime");
            
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while ((process = input.readLine()) != null) {
                    if((++i)>3){
                        try{
                            process = process.replace("Teams", "").replaceAll("^\\s+", "");
                            if(process.length() > 0){ // Avoid process without start time
                                System.out.println("Hora de inicio: "+ process);
                                String pattern = "dd/MM/yyyy HH:mm:ss";
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                Date date = simpleDateFormat.parse(process);
                                System.out.println(date);
                                insertProcessUsage(date,"Teams");
                            }
                        }catch(Exception e){
                            System.out.println(e);
                        }
                    }
                }
                
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    
    static void insertProcessUsage(Date startDate, String processName) throws MalformedURLException, ProtocolException{
        
        try {
            
            URL url = new URL("http://localhost:3003/hello");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "apikey");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            
            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
              content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println(content);
        } catch (IOException ex) {
            Logger.getLogger(ProcessUsageTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
