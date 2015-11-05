package com.pickle.pickleproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Unused_goods extends AppCompatActivity {

    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unused_goods);

        /*
        String url = "http://private-22976-pickleapi.apiary-mock.com/trash";
        String str = getJSON(url,2000);
        */
        new JSONTask().execute("http://localhost:8080/trash/");


        //Log.d(str);


        Button GeneralButton = (Button) findViewById(R.id.Generalbtn);
        Button RecycledButton = (Button) findViewById(R.id.Recycledbtn);
        Button GreenButton = (Button) findViewById(R.id.Greenbtn);

        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        GeneralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGeneral();
            }
        });

        RecycledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRecycled();
            }
        });

        GreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGreen();
            }
        });
    }

    private void changeGeneral(){
        Intent intent = new Intent(this, pickGeneral.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void changeRecycled(){
        Intent intent = new Intent(this, pickRecycled.class);
        startActivity(intent);
    }

    private void changeGreen(){
        Intent intent = new Intent(this, pickGreen.class);
        startActivity(intent);
    }
    private void changeHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private void changeIndividual(){
        Intent intent = new Intent(this, individual_trash_info.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_unused_goods, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Slide gestures */

    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        changeGeneral();
    }

    private void onRightSwipe() {
        changeHome();
    }

    // To pull the JSON request
    public class JSONTask extends AsyncTask<String,String,List<Trash>>{

        @Override
        protected List<Trash> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finalJson = buffer.toString();


                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("Result");

                List<Trash> Trashlist = new ArrayList<Trash>();

                for(int i=0 ; i<parentArray.length();i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    Trash trashObj;
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    //Gson gsonBuilder = new GsonBuilder().registerTypeAdapterFactory(new TrashCategoriesDeserializer())
                    //        .registerTypeAdapterFactory(new UnusedConditionDeserializer())
                    //        .create();
                    //gsonBuilder.registerTypeAdapter(TrashCategories.class, new TrashCategoriesDeserialize());
                    gsonBuilder.registerTypeAdapter(UnusedCondition.class, new UnusedConditionDeserialize());
                    Gson gson = gsonBuilder.create();

                    if(finalObject.getString("categories").equals("Unused Goods")){
                        trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);

                        trashObj.setCategories(TrashCategories.UNUSED);
                        //trashObj.setDistance(finalObject.getInt("distance"));
                        //trashObj.setTitle(finalObject.getString("title"));
                        /*
                        if(finalObject.getString("condition").equals("Good" )){
                            trashObj.setCondition(UnusedCondition.GOOD);
                        } else if(finalObject.getString("condition").equals("Bad")){
                            trashObj.setCondition(UnusedCondition.BAD);
                        } else if (finalObject.getString("condition").equals("New")){
                            trashObj.setCondition(UnusedCondition.NEW);
                        }
                        */
                        /*
                        Log.d("id:", Integer.toString(trashObj.id));
                        Log.d("description:", trashObj.description);
                        Log.d("title:", trashObj.title);
                        Log.d("status:", Integer.toString(trashObj.status));
                        Log.d("latitude:", Double.toString(trashObj.latitude));
                        Log.d("longitude:", Double.toString(trashObj.longitude));
                        Log.d("timestamp:", Integer.toString(trashObj.timestamp));
                        Log.d("distance:", Integer.toString(trashObj.distance));
                        Log.d("size:", Integer.toString(trashObj.size));
                        Log.d("categories", trashObj.categories.toString());
                        Log.d("condition", trashObj.condition.toString());
                        */
                        Trashlist.add(trashObj);
                    }


                }


                return Trashlist;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trash> Result) {
            super.onPostExecute(Result);

            //Log.d("IDOBJECT",Result.get(0).getDesc());

            Trash[] trashArray = Result.toArray(new Trash[0]);
            ListAdapter myAdapter=new ListAdapter(Unused_goods.this ,R.layout.rowlayout, trashArray);
            ListView myList = (ListView)
                    findViewById(R.id.listView4);
            myList.setAdapter(myAdapter);

        }
    }




    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Unused_goods.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Unused_goods.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }

    private class TrashCategoriesDeserialize implements JsonDeserializer<TrashCategories> {
        @Override
        public TrashCategories deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.getAsString().equals("Unused Goods")){
                return TrashCategories.UNUSED;
            } else if (json.getAsString().equals("General Waste")){
                return TrashCategories.GENERAL;
            } else if (json.getAsString().equals("Recycleable Waste")){
                return TrashCategories.RECYCLED;
            } else if (json.getAsString().equals("Green Waste")){
                return TrashCategories.GREEN;
            } else {
                return TrashCategories.UNSPECIFIED;
            }
        }
    }

    private class UnusedConditionDeserialize implements JsonDeserializer<UnusedCondition>{
        @Override
        public UnusedCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.getAsString().equals("Good")){
                return UnusedCondition.GOOD;
            } else if(json.getAsString().equals("Bad")){
                return UnusedCondition.BAD;
            } else if(json.getAsString().equals("New")){
                return UnusedCondition.NEW;
            } else{
                return UnusedCondition.UNSPECIFIED;
            }
        }
    }

/*
    public abstract class CustomizedTypeAdapterFactory<C> implements TypeAdapterFactory {

        private final Class<C> customizedClass;

        public CustomizedTypeAdapterFactory(Class<C> customizedClass) {
            this.customizedClass = customizedClass;
        }

        @SuppressWarnings("unchecked") // we use a runtime check to guarantee that 'C' and 'T' are equal
        public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            return type.getRawType() == customizedClass
                    ? (TypeAdapter<T>) customizeMyClassAdapter(gson, (TypeToken<C>) type)
                    : null;
        }

        private TypeAdapter<C> customizeMyClassAdapter(Gson gson, TypeToken<C> type) {
            final TypeAdapter<C> delegate = gson.getDelegateAdapter(this, type);
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

            return new TypeAdapter<C>() {

                @Override public void write(JsonWriter out, C value) throws IOException {
                    JsonElement tree = delegate.toJsonTree(value);
                    beforeWrite(value, tree);
                    elementAdapter.write(out, tree);
                }


                @Override public C read(JsonReader in) throws IOException {
                    JsonElement tree = elementAdapter.read(in);
                    afterRead(tree);
                    return delegate.fromJsonTree(tree);
                }
            };
        }


        protected void beforeWrite(C source, JsonElement toSerialize) {

        }


        protected void afterRead(JsonElement deserialized) {
            //return null;
        }

    }

    private class TrashCategoriesDeserializer extends CustomizedTypeAdapterFactory<TrashCategories>{

        TrashCategoriesDeserializer(){
            super(TrashCategories.class);
        }

        @Override
        protected void afterRead( JsonElement deserialized) {
            if(deserialized.getAsString().equals("Unused Goods")){
                TrashCategories.UNUSED;
            } else if (deserialized.getAsString().equals("General Waste")){
                return TrashCategories.GENERAL;
            } else if (deserialized.getAsString().equals("Recycleable Waste")){
                return TrashCategories.RECYCLED;
            } else if (deserialized.getAsString().equals("Green Waste")){
                return TrashCategories.GREEN;
            } else {
                return TrashCategories.UNSPECIFIED;
            }
        }

    }

    private class UnusedConditionDeserializer extends CustomizedTypeAdapterFactory<UnusedCondition>{
        UnusedConditionDeserializer(){
            super(UnusedCondition.class);
        }

        @Override
        protected UnusedCondition afterRead2(JsonElement deserialized) {
            if(deserialized.getAsString().equals("Good")){
                return UnusedCondition.GOOD;
            } else if(deserialized.getAsString().equals("Bad")){
                return UnusedCondition.BAD;
            } else if(deserialized.getAsString().equals("New")){
                return UnusedCondition.NEW;
            } else{
                return UnusedCondition.UNSPECIFIED;
            }
        }
    }
*/
}
