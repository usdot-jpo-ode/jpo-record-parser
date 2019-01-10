package us.dot.its.jpo.ingest.kafka;

import us.dot.its.jpo.ingest.codec.utils.JsonUtils;
import us.dot.its.jpo.ingest.pojos.Record;

public class StringPublisher {

   public void publish(Record record) {
      // TODO - implement

      String jsonRecord = JsonUtils.toJson(record, false);
   }
}
