package org.shelajev.webframeworks;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * Created by u016272 on 11/01/2017.
 */
public class Flyer {

    label date=new label();
    String fontname="Arial";
    label title=new label();
    label address=new label();
    label teaser=new label();
    Font font=null;
    BufferedImage image=null;
    Graphics graph =null;

    public String getCode64() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( this.image, "jpg", baos );
        baos.flush();
        return Base64.encode(baos.toByteArray());
    }

    public byte[] getFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( this.image, "png", baos );
        return baos.toByteArray();
    }

    public class label {
        Integer x=0;
        Integer y=0;
        String color="#FFFFFF";
        Integer size=12;
        String text="";

        public label() {
        }

        public label(String text,Integer x, Integer y,Integer size) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.size=size;
        }

        public Integer getWitdh(Font f){
            return 0;//TODO: produire la taille d'une font
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

    public void save(String path) throws IOException {
        File f=new File(path);
        ImageIO.write(this.image, "png", f);
    }

    public String getFontname() {
        return fontname;
    }

    public void setFontname(String fontname) throws IOException, FontFormatException {
        this.fontname = fontname;
        this.font=Font.createFont(Font.TRUETYPE_FONT, new File(fontname));
    }

    private static final int IMG_WIDTH = 200;
    private static final int IMG_HEIGHT = 300;

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, double target_w, double target_h){

        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();;

        BufferedImage resizedImage = new BufferedImage((int) target_w, (int) target_h, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, (int) target_w, (int) target_h, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    private BufferedImage crop(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    public Flyer(String title, String url_image, String address, String date,String teaser,Integer offset,Integer title_size,Integer label_size) throws IOException, FontFormatException {
        if(url_image.startsWith("http")) this.image=ImageIO.read(new URL(url_image));
        if(url_image.indexOf(":/")==1){
            File f=new File(url_image);
            if(f.exists())
                this.image=ImageIO.read(f);
        }

        Integer offset_w=0;
        Integer offset_h=0;

        Double ratio= Math.max(IMG_WIDTH/(double) this.image.getWidth(),IMG_HEIGHT/(double) this.image.getHeight());

        this.image=resizeImageWithHint(this.image,this.image.getWidth()*ratio,this.image.getHeight()*ratio);
        //this.image=resizeImageWithHint(this.image,200,300);

        if(this.image.getHeight()>this.image.getWidth())
            offset_h=Math.min(offset,this.image.getHeight());
        else
            offset_w=Math.min(offset,this.image.getWidth());

        this.image=crop(this.image,new Rectangle(offset_w,offset_h,IMG_WIDTH,IMG_HEIGHT));
        this.graph = this.image.getGraphics();

        //this.graph.fillRect(0,0,200, (int) (400*ratio));
        //this.graph.clipRect(0,0,200,400);

        this.title = new label(title,10,25,title_size);
        this.address = new label(address,10,title_size+10,label_size);
        this.teaser = new label(teaser,10,title_size+label_size+10,label_size);
        this.date=new label(date,10,IMG_HEIGHT-label_size-20,label_size);

        drawText("Arial");
        this.graph.dispose();
    }


    private void drawText(String fontname) throws IOException, FontFormatException {
        //this.setFontname(fontname);
        this.draw(this.title);
        this.draw(this.address);
        this.draw(this.teaser);
        this.draw(this.date);
    }

    private void draw(label title) {
        Font f=new Font("Arial",Font.BOLD,title.size);
        this.graph.setFont(f);
        this.graph.drawString(title.text, title.x, title.y);
    }
}
