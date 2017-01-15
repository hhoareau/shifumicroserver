package org.shelajev.webframeworks;

import com.google.gson.Gson;
import spark.Spark;

import javax.imageio.ImageIO;
import java.io.OutputStream;

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


    //http://localhost:4444/makeflyer/D:_Users_U016272_Pictures_IMG7635.jpg/titre/martel/mardi/coucou/30
    Spark.get("/makeflyer/:path/:title/:address/:date/:teaser/:offset", (request, response) -> {
      String url=Main.getPath(request);
      String address=request.params("address");
      String teaser=request.params("teaser");
      String title=request.params("title");
      String date=request.params("date");

      Integer offset=Integer.valueOf(request.params("offset"));

      Flyer f=new Flyer(title,url,address,date,teaser,offset,30,15);
      //f.save("c:/temp/flyer.png");

      //return f.getCode64();

      response.raw().setContentType("image/png");
      OutputStream os=response.raw().getOutputStream();
      ImageIO.write(f.image,"jpg",os);

      return response;
    });

  }
}

