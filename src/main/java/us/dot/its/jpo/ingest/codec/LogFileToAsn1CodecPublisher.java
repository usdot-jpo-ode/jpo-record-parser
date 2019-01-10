package us.dot.its.jpo.ingest.codec;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ingest.importer.ImporterDirectoryWatcher.ImporterFileType;
import us.dot.its.jpo.ingest.kafka.StringPublisher;
import us.dot.its.jpo.ingest.parsers.FileParser.ParserStatus;
import us.dot.its.jpo.ingest.parsers.LogFileParser;
import us.dot.its.jpo.ingest.pojos.Record;

public class LogFileToAsn1CodecPublisher implements Asn1CodecPublisher {

   public class LogFileToAsn1CodecPublisherException extends Exception {

      private static final long serialVersionUID = 1L;

      public LogFileToAsn1CodecPublisherException(String string, Exception e) {
         super (string, e);
      }

   }

   protected static final Logger logger = LoggerFactory.getLogger(LogFileToAsn1CodecPublisher.class);

   protected StringPublisher publisher;
   protected LogFileParser fileParser;

   // TODO
   public LogFileToAsn1CodecPublisher(StringPublisher dataPub) {
      this.publisher = dataPub;
   }
   
//    TODO - This needs to be redesigned to publish a proprietary schema
   public void publish(BufferedInputStream bis, String fileName, ImporterFileType fileType) 
         throws LogFileToAsn1CodecPublisherException {
      
      ParserStatus status;

      if (fileType == ImporterFileType.OBU_LOG_FILE) {
         fileParser = LogFileParser.factory(fileName);
      }

      List<Record> recordList = new ArrayList<>();
      do {
         try {
            status = fileParser.parseFile(bis, fileName);
            if (status == ParserStatus.COMPLETE) {
               recordList.add(fileParser.getCurrentRecord());
            } else if (status == ParserStatus.EOF) {
               publishRecordList(recordList);
            } else if (status == ParserStatus.INIT) {
               logger.error("Failed to parse the header bytes.");
            } else {
               logger.error("Failed to decode ASN.1 data");
            }
         } catch (Exception e) {
            throw new LogFileToAsn1CodecPublisherException("Error parsing or publishing data.", e);
         }
      } while (status == ParserStatus.COMPLETE);
   }
   
   public void publishRecordList(List<Record> recordList) {
      for (Record record: recordList) {
         publisher.publish(record);
      }
   }
}