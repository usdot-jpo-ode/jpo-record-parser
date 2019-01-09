package us.dot.its.jpo.ingest.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import us.dot.its.jpo.ingest.IngestProperties;
import us.dot.its.jpo.ingest.importer.ImporterDirectoryWatcher;
import us.dot.its.jpo.ingest.importer.ImporterDirectoryWatcher.ImporterFileType;
import us.dot.its.jpo.ingest.storage.StorageFileNotFoundException;
import us.dot.its.jpo.ingest.storage.StorageService;

@RestController
public class FileUploadController {
   // TODO - These should be moved into ODE
   //private static final String FILTERED_OUTPUT_TOPIC = "/topic/filtered_messages";
   //private static final String UNFILTERED_OUTPUT_TOPIC = "/topic/unfiltered_messages";

   private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

   private final StorageService storageService;

   @Autowired
   public FileUploadController(StorageService storageService, IngestProperties ingestProperties) {
      super();
      this.storageService = storageService;

      ExecutorService threadPool = Executors.newCachedThreadPool();

      Path logPath = Paths.get(ingestProperties.getUploadLocationRoot(), ingestProperties.getUploadLocationObuLog());
      logger.debug("UPLOADER - BSM log file upload directory: {}", logPath);
      Path failurePath = Paths.get(ingestProperties.getUploadLocationRoot(), "failed");
      logger.debug("UPLOADER - Failure directory: {}", failurePath);
      Path backupPath = Paths.get(ingestProperties.getUploadLocationRoot(), "backup");
      logger.debug("UPLOADER - Backup directory: {}", backupPath);

      // Create the importers that watch folders for new/modified files
      threadPool.submit(new ImporterDirectoryWatcher(ingestProperties, logPath, backupPath, failurePath,
            ImporterFileType.OBU_LOG_FILE, ingestProperties.getFileWatcherPeriod()));

      // TODO - These should be moved into ODE

      // Create unfiltered exporters
//      threadPool.submit(new StompStringExporter(odeProperties, UNFILTERED_OUTPUT_TOPIC, template, odeProperties.getKafkaTopicOdeBsmJson()));
//      threadPool.submit(new StompStringExporter(odeProperties, UNFILTERED_OUTPUT_TOPIC, template, odeProperties.getKafkaTopicOdeTimJson()));
//      threadPool.submit(new StompStringExporter(odeProperties, UNFILTERED_OUTPUT_TOPIC, template, odeProperties.getKafkaTopicDriverAlertJson()));
//      threadPool.submit(new StompStringExporter(odeProperties, UNFILTERED_OUTPUT_TOPIC, template, odeProperties.getKafkaTopicOdeTimBroadcastJson()));

      // Create filtered exporters
//      threadPool.submit(new StompStringExporter(odeProperties, FILTERED_OUTPUT_TOPIC, template, odeProperties.getKafkaTopicFilteredOdeBsmJson()));
//      threadPool.submit(new StompStringExporter(odeProperties, FILTERED_OUTPUT_TOPIC, template, odeProperties.getKafkaTopicFilteredOdeTimJson()));
   }

   @PostMapping("/upload/{type}")
   public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
         @PathVariable("type") String type) {

      logger.debug("File received at endpoint: /upload/{}, name={}", type, file.getOriginalFilename());
      try {
         storageService.store(file, type);
      } catch (Exception e) {
         logger.error("File storage error", e);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Error\": \"File storage error.\"}");
         // do not return exception, XSS vulnerable
      }

      return ResponseEntity.status(HttpStatus.OK).body("{\"Success\": \"True\"}");
   }

   @ExceptionHandler(StorageFileNotFoundException.class)
   public ResponseEntity<Void> handleStorageFileNotFound(StorageFileNotFoundException exc) {
      return ResponseEntity.notFound().build();
   }

}
