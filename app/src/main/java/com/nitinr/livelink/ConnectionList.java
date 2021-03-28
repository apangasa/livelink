package com.nitinr.livelink;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConnectionList { // stores connections that the user has made
    private static ArrayList<JSONObject> connections;

   static {
       connections = new ArrayList<>();
   }

   public static void addConnection(JSONObject user) {
       connections.add(user);
   }

   public static String getName(int index) throws JSONException {
        return connections.get(index).getString("name");
   }

   public static String getEmail(int index) throws JSONException  {
       return connections.get(index).getString("email");
   }

   public static String getBio(int index) throws JSONException {
       return connections.get(index).getString("bio");
   }

   public static String getNumLinks(int index) throws JSONException {
       return connections.get(index).getString("numLinkConnections");
   }

   public static String getProfilePic(int index) throws JSONException {
       return connections.get(index).getString("profilePic");
   }
}
