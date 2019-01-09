package us.dot.its.jpo.ingest.codec;

import java.io.BufferedInputStream;

import us.dot.its.jpo.ingest.codec.LogFileToAsn1CodecPublisher.LogFileToAsn1CodecPublisherException;
import us.dot.its.jpo.ingest.importer.ImporterDirectoryWatcher.ImporterFileType;

public interface Asn1CodecPublisher {

   public void publish(BufferedInputStream bis, String fileName, ImporterFileType fileType)
         throws LogFileToAsn1CodecPublisherException;
}