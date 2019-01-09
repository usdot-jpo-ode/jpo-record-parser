package us.dot.its.jpo.ingest.storage;

import us.dot.its.jpo.ingest.storage.StorageException;

public class StorageFileNotFoundException extends StorageException {

   private static final long serialVersionUID = 1L;

   public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
