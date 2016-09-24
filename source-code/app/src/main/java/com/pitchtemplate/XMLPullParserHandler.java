package com.pitchtemplate;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLPullParserHandler {

    private List<Data> datas = new ArrayList<Data>();
    private Data data;
    private String text;

    public List<Data> getDatas() {
        return datas;
    }

    public List<Data> parse(InputStream is) {

        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("data")) {
                            // create a new instance of data
                            data = new Data();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("data")) {
                            // add employee object to list
                            datas.add(data);
                        }else if (tagname.equalsIgnoreCase("id")) {
                            data.setId(Integer.parseInt(text));
                        }  else if (tagname.equalsIgnoreCase("name")) {
                            data.setName(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        return datas;
    }
}
