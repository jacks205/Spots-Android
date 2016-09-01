package com.jacks205.spots;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.jacks205.spots.constants.Constants;
import com.jacks205.spots.listener.OnSpotsDataRetrievedListener;
import com.jacks205.spots.model.ParkingLevel;
import com.jacks205.spots.model.ParkingStructure;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ian on 11/4/2015.
 */
public class Spots {
    private static Spots ourInstance = new Spots();

    public static Spots getInstance() {
        return ourInstance;
    }

    public SpotsSchool selectedSchool;

    private Spots() {

    }

    public void getParkingData(OnSpotsDataRetrievedListener listener){
        AsyncTask task;
        switch (selectedSchool) {
            case CSUF:
                task = new CSUFRetrieveTask(listener);
                break;
            default:
                task = new ChapmanRetrieveJsonTask(listener);
        }
        task.execute();
    }

    class CSUFRetrieveTask extends AsyncTask {
        OnSpotsDataRetrievedListener listener;

        public CSUFRetrieveTask(OnSpotsDataRetrievedListener listener){
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            InputStream in;
            try{
                HTMLResponse res = parseHTML(Constants.CSUF_URL);
                return res;
            }catch (Exception e){
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(o instanceof Exception){
                listener.onDataError((Exception)o);
                return;
            }

            boolean type = o instanceof HTMLResponse;
            if (!type){
                listener.onDataError(new Exception(("Parsing error occurred.")));
                return;
            }
            HTMLResponse resp = (HTMLResponse)o;
            //We can return the parking structures and lastUpdated time
            listener.onDataReceived(resp.structures);
        }

        private HTMLResponse parseHTML(String url) throws IOException {
            Document doc = Jsoup.connect(url).get();
            Elements tableRows = doc.getElementsByTag("tr");
            List<ParkingStructure> structureList = new ArrayList<ParkingStructure>();
            for (Element tr: tableRows) {
                Elements tableDatas = tr.getElementsByTag("td");

                String name = tableDatas.first().getElementsByClass("LocationName").first().text();
                int available = Integer.parseInt(tableDatas.get(1).child(0).text());
                int total = Integer.parseInt(tableDatas.first().getElementsByClass("TotalSpaces").first().child(1).text());
                String lastUpdated = tableDatas.first().getElementsByClass("LastUpdated").first().text();
                Date lastUpdatedDate = parseDate(lastUpdated);
                ParkingLevel level = new ParkingLevel(name, available, total);
                ParkingLevel[] levels = new ParkingLevel[] { level };
                ParkingStructure structure = new ParkingStructure(name, available, total, lastUpdatedDate, levels);
                structureList.add(structure);
            }
            HTMLResponse res = new HTMLResponse();
            res.structures = structureList.toArray(new ParkingStructure[structureList.size()]);
            return res;
        }

        private Date parseDate(String dateStr) throws IOException {
            //Date with Java 7: http://stackoverflow.com/a/18217193/4684652
            try {
                DateFormat df1 = new SimpleDateFormat("M/dd/yyyy h:mm:s a");
                return df1.parse(dateStr);
            }catch(ParseException e) {
                return new Date();
            }
        }

        class HTMLResponse {
            ParkingStructure[] structures;
        }
    }


    class ChapmanRetrieveJsonTask extends AsyncTask {

        OnSpotsDataRetrievedListener listener;

        public ChapmanRetrieveJsonTask(OnSpotsDataRetrievedListener listener){
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            InputStream in;
            try{
                URL url = new URL(Constants.CU_URL);
                in = url.openStream();
                JsonResponse response = parseJson(in);
                return response;
            }catch (Exception e){
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(o instanceof Exception){
                listener.onDataError((Exception)o);
                return;
            }

            boolean type = o instanceof JsonResponse;
            if (!type){
                listener.onDataError(new Exception(("Parsing error occurred.")));
                return;
            }
            JsonResponse resp = (JsonResponse)o;
            //We can return the parking structures and lastUpdated time
            listener.onDataReceived(resp.structures);
        }

        private JsonResponse parseJson(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            JsonResponse response = new JsonResponse();
            List<ParkingStructure> structureList = new ArrayList<ParkingStructure>();

            reader.beginObject();
            while(reader.hasNext()) {
                String key = reader.nextName();
                if(key.equals("Structures")) {
                    parseParkingStructures(reader, structureList);
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();

            ParkingStructure[] structures = structureList.toArray(new ParkingStructure[structureList.size()]);
            response.structures = structures;
            return response;
        }

        private void parseParkingStructures(JsonReader reader, List<ParkingStructure> structureList) throws IOException {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                ParkingStructure structure = parseParkingStructure(reader);
                structureList.add(structure);
                reader.endObject();
            }
            reader.endArray();
        }

        private ParkingStructure parseParkingStructure(JsonReader reader) throws IOException {
            //Structures
            int structureAvailable = 0;
            String structureName = "";
            int structureTotal = 0;
            List<ParkingLevel> levelList = new ArrayList<ParkingLevel>();
            while (reader.hasNext()) {
                //Levels
                String levelKey = reader.nextName();
                if(levelKey.equals("Levels")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        ParkingLevel level = parseParkingLevel(reader);
                        levelList.add(level);
                    }
                    reader.endArray();
                }else if(levelKey.equals("CurrentCount")){
                    structureAvailable = reader.nextInt();
                }else if(levelKey.equals("Name")){
                    structureName = reader.nextString();
                }else if(levelKey.equals("Capacity")){
                    structureTotal = reader.nextInt();
                }else {
                    reader.skipValue();
                }
            }
            ParkingLevel[] levels = levelList.toArray(new ParkingLevel[levelList.size()]);
            return new ParkingStructure(structureName, structureAvailable, structureTotal, levels);
        }

        private ParkingLevel parseParkingLevel(JsonReader reader) throws IOException {
            int available = 0;
            int total = 0;
            String levelName = "";
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "CurrentCount":
                        available = reader.nextInt();
                        break;
                    case "FriendlyName":
                        levelName = reader.nextString();
                        break;
                    case "Capacity":
                        total = reader.nextInt();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return new ParkingLevel(levelName, available, total);
        }

        private Date parseDate(JsonReader reader) throws IOException {
            //Date with Java 7: http://stackoverflow.com/a/18217193/4684652
            try {
                String dateStr = reader.nextString();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                return df1.parse(dateStr);
            }catch(ParseException e) {
                return new Date();
            }
        }

        class JsonResponse {
            public ParkingStructure[] structures;
        }


    }


}
