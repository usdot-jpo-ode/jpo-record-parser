package us.dot.its.jpo.ingest.pojos;

public abstract class AbstractVerifiedMsgRecord extends AbstractLogFileRecord {
   
   private int verficationStatus;

   public int getVerficationStatus() {
      return verficationStatus;
   }

   public void setVerficationStatus(int verficationStatus) {
      this.verficationStatus = verficationStatus;
   }

}
