package com.example.neerex.mytutapp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neerex on 26/02/16.
 */
public class Parser {
    private static final String ns = null;
    private XmlPullParserFactory xmlFactoryObject;
    private InputStream Is = null;
    public volatile boolean parsingComplete = true;



public  void parser() {


}

   public List<Item> getparseddata(InputStream stream) throws XmlPullParserException, IOException {

       xmlFactoryObject = XmlPullParserFactory.newInstance();
        XmlPullParser myparser = xmlFactoryObject.newPullParser();
        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        myparser.setInput(stream, null);
      List<Item>  items= parseXMLAndStoreIt(myparser);
       if(stream != null) {
           stream.close();
       }
       return items;

    }
    public List<Item> parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        List<Item> lstitem = new ArrayList<>();
        Item objitem =null ;

        try {
              event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {

                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("title")){
                            event = myParser.next();
                            if(event==4)
                            {
                                objitem = new Item() ;
                                text = myParser.getText();
                                objitem.setTitle(text);
                                break;
                            }
                        }

                        if(name.equals("link")){
                            event = myParser.next();
                            if(event==4)
                            {
                                text = myParser.getText();
                                objitem.setLink(text);
                                break;
                            }
                        }

                        if(name.equals("enclosure")){
                               text = myParser.getAttributeValue(null,"url");
                                objitem.setEnclosure(text);
                                break;

                        }

                        if(name.equals("description")){
                            event = myParser.next();
                            if(event==4)
                            {
                                text = myParser.getText();
                                objitem.setDescription(text);
                                break;
                            }
                        }

                        if(name.equals("pubDate")){
                            event = myParser.next();
                            if(event==4)
                            {
                                text = myParser.getText();
                                objitem.setPubDate(text);
                                lstitem.add(objitem);
                                break;
                            }
                        }

                        if(name.equals("docs")){
                            event = myParser.next();
                            if(event==4)
                            {
                                lstitem.add(objitem);
                                break;
                            }
                        }


                        break;


                    case XmlPullParser.END_TAG:

                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return lstitem;
    }




    public List parse(InputStream in) throws IOException, XmlPullParserException {
        XmlPullParser myparser = Xml.newPullParser();
        try {

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(in, null);
            myparser.nextTag();


        } catch (Exception e) {

        } finally {
            in.close();
        }
        return readfeed(myparser);

    }

    public List readfeed(XmlPullParser myparser) throws IOException, XmlPullParserException {
        List entries = new ArrayList();
        int event;
        try
        {
            event = myparser.getEventType();
            while(event !=XmlPullParser.END_DOCUMENT)
            {
                String name = myparser.getName();
                switch (event)
                {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("item")) {
                            entries.add(readItem(myparser));
                        } else {

                        }

                        break;
                }
                event = myparser.next();
            }

        }
        catch(Exception ex)
        {
            Log.e("tag", "readfeed: ",ex );
        }


           /* myparser.require(XmlPullParser.START_TAG, ns, "rss");
            while (myparser.next() != XmlPullParser.END_TAG) {
                if (myparser.getEventType() != XmlPullParser.START_TAG) {
                    // String name1 = myparser.getName();
                    continue;
                }
                String name = myparser.getName();

                if (name !=null) {
                    if (name.equals("item")) {
                        entries.add(readItem(myparser));
                    } else {
                        skip(myparser);
                    }
                }
            }*/



        return entries;
    }


    private Item readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        String title = null;
        String description = null;
        String link = null;
        String enclosure = null;
        String pubdate = null;

        while  (parser.next()!= XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name =parser.getName();
            if(name.equals("title"))
            {
                title= readTitle(parser);
            }
            if(name.equals("description"))
            {
                description= readDescription(parser);
            }
            if(name.equals("link"))
            {
                link= readLink(parser);
            }
            if(name.equals("enclosure"))
            {
                enclosure= readEnclosure(parser);
            }
            if(name.equals("pubdate"))
            {
                pubdate= readDate(parser);
            }
            else
            {
                skip(parser);
            }
        }

        return  new Item();
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,ns,"title");
        String title= readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"title");
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link="";
        parser.require(XmlPullParser.START_TAG,ns,"link");
        link=readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"link");
        return link;
    }


    private String readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {
        String enclosure="";
        parser.require(XmlPullParser.START_TAG,ns,"enclosure");
        String tag =parser.getName();
        enclosure = parser.getAttributeValue(ns, "url");
        parser.require(XmlPullParser.END_TAG,ns,"enclosure");
        return enclosure;

    }
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,ns,"description");
        String description= readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"description");
        return description;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,ns,"pubDate");
        String date= readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"pubDate");
        return date;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result="";
        if(parser.next()==XmlPullParser.TEXT)
        {
            result=parser.getText();
        }
        return  result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
