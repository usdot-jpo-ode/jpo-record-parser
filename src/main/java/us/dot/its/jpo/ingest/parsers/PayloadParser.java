/*******************************************************************************
 * Copyright 2018 572682
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package us.dot.its.jpo.ingest.parsers;

import java.io.BufferedInputStream;
import java.nio.ByteOrder;
import java.util.Arrays;

import us.dot.its.jpo.ingest.codec.utils.CodecUtils;
import us.dot.its.jpo.ingest.pojos.Record;

public class PayloadParser extends LogFileParser {

   public static final int PAYLOAD_LENGTH_LENGTH = 2;

   protected short payloadLength;
   protected byte[] payload;

   public PayloadParser() {
      super();
   }

   @Override
   public ParserStatus parseFile(BufferedInputStream bis, String fileName) throws FileParserException {

      ParserStatus status = ParserStatus.INIT;
      try {
         // parse payload length
         if (getStep() == 0) {
            status = parseStep(bis, PAYLOAD_LENGTH_LENGTH);
            if (status != ParserStatus.COMPLETE)
               return status;
            setPayloadLength(CodecUtils.bytesToShort(readBuffer, 0, PAYLOAD_LENGTH_LENGTH, ByteOrder.LITTLE_ENDIAN));
         }

         // Step 10 - copy payload bytes
         if (getStep() == 1) {
            status = parseStep(bis, getPayloadLength());
            if (status != ParserStatus.COMPLETE)
               return status;
            setPayload(Arrays.copyOf(readBuffer, getPayloadLength()));
         }
         
         resetStep();
         status = ParserStatus.COMPLETE;

      } catch (Exception e) {
         throw new FileParserException(String.format("Error parsing %s on step %d", fileName, getStep()), e);
      }

      return status;

   }
   
   public short getPayloadLength() {
      return payloadLength;
   }

   public LogFileParser setPayloadLength(short length) {
      this.payloadLength = length;
      return this;
   }

   public byte[] getPayload() {
      return payload;
   }

   public LogFileParser setPayload(byte[] payload) {
      this.payload = payload;
      return this;
   }

}
