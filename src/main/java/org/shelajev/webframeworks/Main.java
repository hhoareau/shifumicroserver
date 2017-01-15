package org.shelajev.webframeworks;

import spark.Request;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;


public class Main {
    private static final String charset = "UTF-8";
    private static final String API_ROOT = "_ah/api/ficarbar/v1";

    private static final String DOMAIN = "https://shifumixweb.appspot.com";
    private static final String DIRECTORY = "/home/pi/workspace/Mails";

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String getPath(Request request){
        String rc=request.params(":path");
        for(int i=0;i<10;i++)rc=rc.replace("_","/");
        return rc;
    }


    public static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }


    public static void sendMail(String dest, String from, String subject, String body) throws IOException {
        System.out.print("Mail pour "+dest+", from "+from+" de subject:"+subject);
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.debug", "true");

        Session session = Session.getDefaultInstance(props, null);

        if(dest==null || dest.length()==0)return;
        if(from==null || from.length()==0)return;
        if(body==null || body.length()==0)return;

        try {
            javax.mail.Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(dest))   ;
            msg.setSubject(subject);
            msg.setContent(body, "text/html; charset=utf-8");

            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static String api(String api_name, String params, String domain) throws IOException {
        if(params==null)params="";
        if(params.length()>0)params="&"+params;
        URL url = new URL(domain+"/"+API_ROOT+"/"+api_name+"?password=hh4271"+params);

        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Accept-Charset", charset);
        InputStream response = connection.getInputStream();

        return getStringFromInputStream(response);
    }
}
