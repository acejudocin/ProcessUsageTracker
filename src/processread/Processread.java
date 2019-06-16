/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author CEJCINAN
 */
public class Processread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            String process;
            int i = 0;
            Date initDate;
            // getRuntime: Returns the runtime object associated with the current Java application.
            // exec: Executes the specified string command in a separate process.
            // en PowerShell = Get-Process | select name, starttime
            //Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe /V");
            Process p = Runtime.getRuntime().exec("powershell Get-Process Teams | select name, starttime");
            
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((process = input.readLine()) != null) {
                if((++i)>3){
                    try{
                        process = process.replace("Teams", "").replaceAll("^\\s+", "");
                        System.out.println("Hora de inicio: "+ process);
                        String pattern = "dd/MM/yyyy HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        Date date = simpleDateFormat.parse(process);
                        System.out.println(date);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
