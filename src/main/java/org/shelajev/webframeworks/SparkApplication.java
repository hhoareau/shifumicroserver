package org.shelajev.webframeworks;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import spark.Spark;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static spark.Spark.before;
import static spark.Spark.options;

/**
 * Created by shelajev on 17/02/16.
 */

//test :/usr/bin/java -DrunDir=/home/pi/workspace -cp /home/pi/workspace/ShifuMicroServer.jar org.shelajev.webframeworks.SparkApplication v1

public class SparkApplication {

  private static void enableCORS(final String origin, final String methods, final String headers) {

    options("/*", (request, response) -> {

      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    before((request, response) -> {
      response.header("Access-Control-Allow-Origin", origin);
      response.header("Access-Control-Request-Method", methods);
      response.header("Access-Control-Allow-Headers", headers);
      // Note: this may or may not be necessary in your particular application
      response.type("application/json");
    });
  }

  public static void main(String[] args) throws IOException {
    String port="4271";
    String server="https://shifumixweb.appspot.com";
    String myaddress=Tools.getMyAddress().getHostAddress();
    String version="v1";

    //enableCORS("http://localhost:8080/","makeflyer","");

    if(args.length>0)version=args[0];
    if(args.length>1)server=args[1];
    if(args.length>2)port=args[2];
    if(args.length>3)myaddress=args[3];

    Spark.port(Integer.parseInt(port));
    //Enregistrement du serveur
    Main.api(server+"/_ah/api/shifumix/"+version+"/addserver?server="+myaddress+":"+port);

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
    Spark.get("/preview/:path/:title", (request, response) -> {
      String url=Main.getPath(request);
      String title=request.params("title");

      Integer offset=Integer.valueOf(request.params("offset"));

      Flyer f=new Flyer(title,url,"address","4/2/1971","teaser",0,30,15);

      response.raw().setContentType("image/png");
      ImageIO.write(f.image, "png", response.raw().getOutputStream());
      return response;
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

      //response.raw().setContentType("image/jpg");
      //response.status(200);

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ImageIO.write(f.image, "png", os);
      String rc= Base64.encode(os.toByteArray());
      return "data:image/png;base64,"+rc;
    });

    Spark.get("/join", (request, response) -> {
      return "Delay:"+System.currentTimeMillis();
    });


  }
}

