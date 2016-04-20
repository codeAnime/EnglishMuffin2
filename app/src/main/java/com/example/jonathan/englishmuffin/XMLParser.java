package com.example.jonathan.englishmuffin;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jonathan on 9/8/2015.
 */
public class XMLParser {

    private String ew = "ew";
    private String def = "loading...";
    private String wav = "loading...";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public XMLParser(String url){this.urlString = url;}

    public String getEW() {return ew;}

    public String getWav() { return wav;}

    public String getDef() {return def;}

    public void parseXMLAndStore(XmlPullParser myParser){
        int event;
        int tri;
        String text = null;
        String part = null;

        try {
            event = myParser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT){
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if(myParser.getName().equals("dt")){
                                tri = myParser.next();
                                part = myParser.getText();
                                def = part;
                        }
                        break;
                    case XmlPullParser.TEXT:

                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("ew")) {
                            ew = text;

                        }
                        else if(name.equals("dt")) {
                            def = part;
                        } else if (name.equals("wav")) {
                          wav = text;
                        } else {

                        }
                        break;
                }
                if(event == XmlPullParser.END_TAG && myParser.getName().contains("dt")) {
                    break;
                } else {

                    event = myParser.next();
                }
            }
            parsingComplete = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setReadTimeout(10000);//10000
                    conn.setConnectTimeout(15000);//15000
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStore(myparser);
                    stream.close();
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
