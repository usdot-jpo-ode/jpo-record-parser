package us.dot.its.jpo.ingest.codec;

import java.io.BufferedInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ingest.importer.ImporterDirectoryWatcher.ImporterFileType;
import us.dot.its.jpo.ingest.kafka.StringPublisher;
import us.dot.its.jpo.ingest.parsers.LogFileParser;

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
   // TODO - implement or remove serialid
   //protected SerialId serialId;

   // TODO
   public LogFileToAsn1CodecPublisher(StringPublisher dataPub) {
      this.publisher = dataPub;
      //this.serialId = new SerialId();
   }

   // TODO
   @Override
   public void publish(BufferedInputStream bis, String fileName, ImporterFileType fileType)
         throws LogFileToAsn1CodecPublisherException {
      // TODO Auto-generated method stub
      
   }
   
   // TODO - This needs to be redesigned to publish a proprietary schema
//   public void publish(BufferedInputStream bis, String fileName, ImporterFileType fileType) 
//         throws LogFileToAsn1CodecPublisherException {
//      
//      // TODO - should be publishing JSON
//      //XmlUtils xmlUtils = new XmlUtils();
//      ParserStatus status;
//
//      if (fileType == ImporterFileType.OBU_LOG_FILE) {
//         fileParser = LogFileParser.factory(fileName);
//      }
//
//      List<OdeMsgPayload> payloadList = new ArrayList<>();
//      do {
//         try {
//            status = fileParser.parseFile(bis, fileName);
//            if (status == ParserStatus.COMPLETE) {
//               parsePayload(payloadList);
//            } else if (status == ParserStatus.EOF) {
//               publish(xmlUtils, payloadList);
//            } else if (status == ParserStatus.INIT) {
//               logger.error("Failed to parse the header bytes.");
//            } else {
//               logger.error("Failed to decode ASN.1 data");
//            }
//         } catch (Exception e) {
//            throw new LogFileToAsn1CodecPublisherException("Error parsing or publishing data.", e);
//         }
//      } while (status == ParserStatus.COMPLETE);
//   }

   
   // TODO - This needs to be redesigned to publish a proprietary schema
   
//   public void parsePayload(List<OdeMsgPayload> payloadList) {
//
//      OdeMsgPayload msgPayload;
//      
//      if (fileParser instanceof DriverAlertFileParser){
//         msgPayload = new OdeDriverAlertPayload(((DriverAlertFileParser) fileParser).getAlert());
//      } else {
//         msgPayload = new OdeAsn1Payload(fileParser.getPayloadParser().getPayload());
//      }
//
//      payloadList.add(msgPayload);
//   }
//
//   public void publish(XmlUtils xmlUtils, List<OdeMsgPayload> payloadList) throws JsonProcessingException {
//     serialId.setBundleSize(payloadList.size());
//     for (OdeMsgPayload msgPayload : payloadList) {
//       OdeLogMetadata msgMetadata;
//       OdeData msgData;
//       
//       if (fileParser instanceof DriverAlertFileParser){
//          logger.debug("Publishing a driverAlert.");
//
//          msgMetadata = new OdeLogMetadata(msgPayload);
//          msgMetadata.setSerialId(serialId);
//
//          OdeLogMetadataCreatorHelper.updateLogMetadata(msgMetadata, fileParser);
//          
//          msgData = new OdeDriverAlertData(msgMetadata, msgPayload);
//          publisher.publish(JsonUtils.toJson(msgData, false),
//             publisher.getOdeProperties().getKafkaTopicDriverAlertJson());
//          serialId.increment();
//       } else {
//          if (fileParser instanceof BsmLogFileParser || 
//                (fileParser instanceof RxMsgFileParser && ((RxMsgFileParser)fileParser).getRxSource() == RxSource.RV)) {
//             logger.debug("Publishing a BSM");
//             msgMetadata = new OdeBsmMetadata(msgPayload);
//          } else {
//             logger.debug("Publishing a TIM");
//             msgMetadata = new OdeLogMetadata(msgPayload);
//          }
//          msgMetadata.setSerialId(serialId);
//
//          Asn1Encoding msgEncoding = new Asn1Encoding("root", "Ieee1609Dot2Data", EncodingRule.COER);
//          Asn1Encoding unsecuredDataEncoding = new Asn1Encoding("unsecuredData", "MessageFrame",
//                  EncodingRule.UPER);
//          msgMetadata.addEncoding(msgEncoding).addEncoding(unsecuredDataEncoding);
//
//          OdeLogMetadataCreatorHelper.updateLogMetadata(msgMetadata, fileParser);
//          
//          msgData = new OdeAsn1Data(msgMetadata, msgPayload);
//          publisher.publish(xmlUtils.toXml(msgData),
//             publisher.getOdeProperties().getKafkaTopicAsn1DecoderInput());
//          serialId.increment();
//       }
//     }
//   }


}