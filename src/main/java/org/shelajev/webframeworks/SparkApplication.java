package org.shelajev.webframeworks;

import com.google.gson.Gson;
import spark.Spark;

/**
 * Created by shelajev on 17/02/16.
 */

//test :http://localhost:9090/mail={
public class SparkApplication {
  public static void main(String[] args) {
    Spark.port(4271);
    Spark.post("/sendmail","application/json", (request, response) -> {
      String s=request.body();
      Mail m=new Gson().fromJson(s,Mail.class);
      if(m!=null){
        Main.sendMail(m.getTo(),m.getFrom(),m.getSubject(),m.getBody());
        return "sended";
      } else {
        return "ko";
      }


    });
  }
}

