package us.dot.its.jpo.ingest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@ConfigurationProperties("ingest")
@PropertySource("classpath:application.properties")
public class IngestProperties implements EnvironmentAware {

   @Autowired
   private Environment environment;

   // File import properties
   private String uploadLocationRoot = "uploads";
   private String uploadLocationObuLog = "bsmlog";
   private Integer fileWatcherPeriod = 5; // time to wait between processing inbox directory for new files
   
   private int importProcessorBufferSize = 8192;

   @Override
   public void setEnvironment(Environment environment) {
      this.environment = environment;
   }

   public Environment getEnvironment() {
      return environment;
   }

   public String getUploadLocationRoot() {
      return uploadLocationRoot;
   }

   public void setUploadLocationRoot(String uploadLocationRoot) {
      this.uploadLocationRoot = uploadLocationRoot;
   }

   public Integer getFileWatcherPeriod() {
      return fileWatcherPeriod;
   }

   public void setFileWatcherPeriod(Integer fileWatcherPeriod) {
      this.fileWatcherPeriod = fileWatcherPeriod;
   }

   public String getUploadLocationObuLog() {
      return uploadLocationObuLog;
   }

   public void setUploadLocationObuLog(String uploadLocationObuLog) {
      this.uploadLocationObuLog = uploadLocationObuLog;
   }

   public int getImportProcessorBufferSize() {
      return importProcessorBufferSize;
   }

   public void setImportProcessorBufferSize(int importProcessorBufferSize) {
      this.importProcessorBufferSize = importProcessorBufferSize;
   }
}
