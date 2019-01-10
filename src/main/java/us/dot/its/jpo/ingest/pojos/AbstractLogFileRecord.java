package us.dot.its.jpo.ingest.pojos;

public abstract class AbstractLogFileRecord implements Record {

   private LogLocation curLocation;
   private long utcTimeInSec;
   private int msec;
   private int length;
   private byte[] payload;

   public LogLocation getLogLocation() {
      return curLocation;
   }

   public void setLogLocation(LogLocation logLocation) {
      this.curLocation = logLocation;
   }

   public long getUtcTimeInSec() {
      return utcTimeInSec;
   }

   public void setUtcTimeInSec(long utcTimeInSec) {
      this.utcTimeInSec = utcTimeInSec;
   }

   public int getMsec() {
      return msec;
   }

   public void setMsec(int msec) {
      this.msec = msec;
   }

   public int getLength() {
      return length;
   }

   public void setLength(int length) {
      this.length = length;
   }

   public byte[] getPayload() {
      return payload;
   }

   public void setPayload(byte[] payload) {
      this.payload = payload;
   }
}
