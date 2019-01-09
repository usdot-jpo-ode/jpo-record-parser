package us.dot.its.jpo.ingest.importer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ingest.IngestProperties;
import us.dot.its.jpo.ingest.storage.IngestFileUtils;

public class ImporterDirectoryWatcher implements Runnable {
   
   public enum ImporterFileType {
      OBU_LOG_FILE
   }

   private static final Logger logger = LoggerFactory.getLogger(ImporterDirectoryWatcher.class);

   private boolean watching;

   private ImporterProcessor importerProcessor;

   private Path inbox;
   private Path backup;
   private Path failed;

   private ScheduledExecutorService executor;

   private Integer timePeriod;

   public ImporterDirectoryWatcher(IngestProperties ingestProperties, Path dir, Path backupDir, Path failureDir, ImporterFileType fileType, Integer timePeriod) {
      this.inbox = dir;
      this.backup = backupDir;
      this.failed = failureDir;
      this.watching = true;
      this.timePeriod = timePeriod;

      try {
         IngestFileUtils.createDirectoryRecursively(inbox);
         String msg = "Created directory {}";
         logger.debug(msg, inbox);
         IngestFileUtils.createDirectoryRecursively(failed);
         logger.debug(msg, failed);
         IngestFileUtils.createDirectoryRecursively(backup);
         logger.debug(msg, backup);
      } catch (IOException e) {
         logger.error("Error creating directory: " + inbox, e);
      }

      this.importerProcessor = new ImporterProcessor(ingestProperties, fileType);
      
      executor = Executors.newScheduledThreadPool(1);
   }

   @Override
   public void run() {

      logger.info("Processing inbox directory {} every {} seconds.", inbox, timePeriod);

      // ODE-646: the old method of watching the directory used file
      // event notifications and was unreliable for large quantities of files
      // Watch directory for file events
      executor.scheduleWithFixedDelay(() -> importerProcessor.processDirectory(inbox, backup, failed),
          0, timePeriod, TimeUnit.SECONDS);
      
      try {
         // This line will only execute in the event that .scheduleWithFixedDelay() throws an error
         executor.awaitTermination(timePeriod, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         logger.error("Directory watcher polling loop interrupted!", e);
      }
   }

   public boolean isWatching() {
      return watching;
   }

   public void setWatching(boolean watching) {
      this.watching = watching;
   }

}
