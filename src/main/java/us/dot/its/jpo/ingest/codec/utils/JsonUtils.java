package us.dot.its.jpo.ingest.codec.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
   
   private static Gson gsonCompact;
   private static Gson gsonVerbose;
   private static ObjectMapper mapper;
   
   static {
      gsonCompact = new GsonBuilder().create();
      gsonVerbose = new GsonBuilder().serializeNulls().create();
      setMapper(new ObjectMapper());
   }
   
   public static String toJson(Object o, boolean verbose) {

      // convert java object to JSON format,
      // and returned as JSON formatted string
      return verbose ? gsonVerbose.toJson(o) : gsonCompact.toJson(o);
   }

   public static ObjectMapper getMapper() {
      return mapper;
   }

   public static void setMapper(ObjectMapper mapper) {
      JsonUtils.mapper = mapper;
   }

}
