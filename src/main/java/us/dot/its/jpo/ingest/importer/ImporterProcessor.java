package us.dot.its.jpo.ingest.importer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ingest.IngestProperties;
import us.dot.its.jpo.ingest.codec.FileAsn1CodecPublisher;
import us.dot.its.jpo.ingest.codec.FileAsn1CodecPublisher.FileAsn1CodecPublisherException;
import us.dot.its.jpo.ingest.importer.ImporterDirectoryWatcher.ImporterFileType;
import us.dot.its.jpo.ingest.storage.IngestFileUtils;

public class ImporterProcessor {

   private static final Logger logger = LoggerFactory.getLogger(ImporterProcessor.class);
   private FileAsn1CodecPublisher codecPublisher;
   private IngestProperties ingestProperties;
   private ImporterFileType fileType;
   private Pattern gZipPattern = Pattern.compile("application/.*gzip");
   private Pattern zipPattern = Pattern.compile("application/.*zip.*");

   public ImporterProcessor(IngestProperties ingestProperties, ImporterFileType fileType) {
      this.codecPublisher = new FileAsn1CodecPublisher(ingestProperties);
      this.ingestProperties = ingestProperties;
      this.fileType = fileType;
   }

   public int processDirectory(Path dir, Path backupDir, Path failureDir) {
      int count = 0;
      // Process files already in the directory
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

         for (Path entry : stream) {
            if (entry.toFile().isDirectory()) {
               processDirectory(entry, backupDir, failureDir);
            } else {
               logger.debug("Found a file to process: {}", entry.getFileName());
               processAndBackupFile(entry, backupDir, failureDir);
               count++;
            }
         }

      } catch (Exception e) {
         logger.error("Error processing files.", e);
      }
      return count;
   }

   public void processAndBackupFile(Path filePath, Path backupDir, Path failureDir) {

      // ODE-559
      boolean success = true;
      InputStream inputStream = null;
      BufferedInputStream bis = null;

      try {
         inputStream = new FileInputStream(filePath.toFile());
         String probeContentType = Files.probeContentType(filePath);
         if ((probeContentType != null && gZipPattern.matcher(probeContentType).matches()) || filePath.toString().toLowerCase().endsWith("gz")) {
            logger.info("Treating as gzip file");
            inputStream = new GZIPInputStream(inputStream);
            bis = publishFile(filePath, inputStream);
         } else if ((probeContentType != null && zipPattern.matcher(probeContentType).matches()) || filePath.toString().endsWith("zip")) {
            logger.info("Treating as zip file");
            inputStream = new ZipInputStream(inputStream);
            ZipInputStream zis = (ZipInputStream)inputStream;
            while (zis.getNextEntry() != null) {
               bis = publishFile(filePath, inputStream);
            }
         } else {
            logger.info("Treating as unknown file");
            bis = publishFile(filePath, inputStream);
         }
      } catch (Exception e) {
         success = false;
         logger.error("Failed to open or process file: " + filePath, e);
         //EventLogger.logger.error("Failed to open or process file: " + filePath, e);  
      } finally {
         try {
            if (bis != null) {
               bis.close();
            }
            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException e) {
            logger.error("Failed to close file stream: {}", e);
         }
      }

      try {
         if (success) {
            IngestFileUtils.backupFile(filePath, backupDir);
            logger.info("File moved to backup: {}", backupDir);
            //EventLogger.logger.info("File moved to backup: {}", backupDir);  
         } else {
            IngestFileUtils.moveFile(filePath, failureDir);
            logger.info("File moved to failure directory: {}", failureDir);  
            //EventLogger.logger.info("File moved to failure directory: {}", failureDir);
         }
      } catch (IOException e) {
         logger.error("Unable to backup file: " + filePath, e);
      }
   }

   private BufferedInputStream publishFile(Path filePath, InputStream inputStream)
         throws FileAsn1CodecPublisherException {
      BufferedInputStream bis;
      bis = new BufferedInputStream(inputStream, ingestProperties.getImportProcessorBufferSize());
      codecPublisher.publishFile(filePath, bis, fileType);
      return bis;
   }
}

