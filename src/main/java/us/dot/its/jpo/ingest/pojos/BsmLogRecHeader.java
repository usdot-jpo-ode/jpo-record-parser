package us.dot.its.jpo.ingest.pojos;

public class BsmLogRecHeader extends AbstractLogFileRecord {
   
   private int signStatus;
   
   public BsmLogRecHeader () {
      super();
   }

   public int getSignStatus() {
      return signStatus;
   }

   public void setSignStatus(int signStatus) {
      this.signStatus = signStatus;
   }
   
}
