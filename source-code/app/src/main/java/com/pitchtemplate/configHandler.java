package com.pitchtemplate;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sid on 1/12/17.
 */

public class configHandler {
    static templateData readConfigFile(Context context){
        try {
            Gson jsonSerializer = new Gson();
            InputStreamReader configStream = new InputStreamReader(context.getAssets().open("config.json"));
            BufferedReader configReader = new BufferedReader(configStream);
            StringBuilder configJson = new StringBuilder();
            String line;
            while ((line = configReader.readLine()) != null) {
                configJson.append(line);
            }
            templateData data =jsonSerializer.fromJson(configJson.toString(), new TypeToken<templateData>() {}.getType());
            return data;
        }
        catch (IOException e){
            //EXCEPTION WILL NEVER BE CAUGHT
        }
        return null;
    }
}
