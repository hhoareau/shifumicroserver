package org.shelajev.webframeworks;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.List;


public class Tools {
    static File[] oldListRoot = File.listRoots();

    public static InetAddress getMyAddress() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        return InetAddress.getByName(in.readLine()); //you get the IP as a String
    }

    /**
     *
     * @param port
     * @param localAddr
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws InterruptedException
     */
    public static Boolean openPort(int port,InetAddress localAddr) throws ParserConfigurationException, SAXException, IOException, InterruptedException {
        GatewayDevice d=null;
        try{
            GatewayDiscover discover = new GatewayDiscover();
            discover.discover();
            d= discover.getValidGateway();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null != d) {
            if (d.addPortMapping(port, port, localAddr.getHostAddress(), "TCP", "ShifuMusicServer")){
                d.addPortMapping(port, port, localAddr.getHostAddress(), "UDP", "ShifuMusicServer");
                return true;
            }

        }

     /*
      PortMappingEntry portMapping = new PortMappingEntry();

      if (!d.getSpecificPortMappingEntry(port,"TCP",portMapping)) {
        logger.info("Port was already mapped. Aborting test.");
        return false;
      } else {
        logger.info("Sending port mapping request");
        if (!d.addPortMapping(port, port ,localAddr.getHostAddress(),"TCP","ShifuMusicServer")) {
          logger.info("Port mapping attempt failed");
          logger.info("Test FAILED");
        }else{
          Thread.sleep(1000*WAIT_TIME);
          d.deletePortMapping(port ,"TCP");
          logger.info("Port mapping removed");
          logger.info("Test SUCCESSFUL");
        }
      }
    }else{
      logger.info("No valid gateway device found.");
    }
    */

        return false;
    }


    public static String getMacAddress(){
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            if(network==null)network = NetworkInterface.getByName("eth0");
            if(network==null)return "";
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();

        } catch (UnknownHostException e) {e.printStackTrace();
        } catch (SocketException e){e.printStackTrace();
        }
        return null;
    }



    public static InetAddress getLocalAddress() throws IOException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if(i.getHostAddress().indexOf("192.")>-1)return i;
            }
        }
        return InetAddress.getLocalHost();
    }


    public static void list_directory(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        if(fList!=null){
            for (File file : fList) {
                if (file.isFile()) {
                    String s=file.getName().toLowerCase();
                    if(s.contains(".mp3") || s.contains(".ogg") || s.contains(".flac"))files.add(file);
                } else if (file.isDirectory()) {
                    list_directory(file.getAbsolutePath(), files);
                }
            }
        }
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


}
