package com.jacks205.spots;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.jacks205.spots.constants.Constants;
import com.jacks205.spots.listener.OnSpotsDataRetrievedListener;
import com.jacks205.spots.model.ParkingLevel;
import com.jacks205.spots.model.ParkingStructure;

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

    private Spots() {

    }


    public void getParkingData(OnSpotsDataRetrievedListener listener){
        RetrieveJsonTask task = new RetrieveJsonTask(listener);
        task.execute();
    }


    class RetrieveJsonTask extends AsyncTask {

        OnSpotsDataRetrievedListener listener;

        public RetrieveJsonTask(OnSpotsDataRetrievedListener listener){
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            InputStream in;
            try{
                URL url = new URL(Constants.URL);
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
