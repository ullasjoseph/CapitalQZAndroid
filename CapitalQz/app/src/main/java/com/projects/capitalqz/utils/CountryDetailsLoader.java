package com.projects.capitalqz.utils;

import android.content.Context;

import com.projects.capitalqz.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by vaisakhprakash on 19/07/14.
 */
public class CountryDetailsLoader {

    public static  ArrayList<JSONObject> countryDetailsList;
    public static void main(String[] args) {

        getCountryDetails(null);
    }

    public static ArrayList<JSONObject> getCountryDetails(Context context) {

        JSONParser parser = new JSONParser();

        JSONArray obj = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.countries);
            obj = (JSONArray) parser.parse(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(obj == null)
            return null;

        countryDetailsList = new ArrayList<JSONObject>();
        for (Object anObj : obj) {
            JSONObject object = (JSONObject) anObj;
            countryDetailsList.add(object);
            System.out.println(object.get("name"));
        }
        return countryDetailsList;
    }

}
