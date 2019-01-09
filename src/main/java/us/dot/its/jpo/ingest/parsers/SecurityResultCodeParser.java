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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityResultCodeParser extends LogFileParser {
   
   // TODO - ripped from OdeLogMetadata
   public enum SecurityResultCode {
      success,
      unknown,
      inconsistentInputParameters,
      spduParsingInvalidInput,
      spduParsingUnsupportedCriticalInformationField,
      spduParsingCertificateNotFound,
      spduParsingGenerationTimeNotAvailable,
      spduParsingGenerationLocationNotAvailable,
      spduCertificateChainNotEnoughInformationToConstructChain,
      spduCertificateChainChainEndedAtUntrustedRoot,
      spduCertificateChainChainWasTooLongForImplementation,
      spduCertificateChainCertificateRevoked,
      spduCertificateChainOverdueCRL,
      spduCertificateChainInconsistentExpiryTimes,
      spduCertificateChainInconsistentStartTimes,
      spduCertificateChainInconsistentChainPermissions,
      spduCryptoVerificationFailure,
      spduConsistencyFutureCertificateAtGenerationTime,
      spduConsistencyExpiredCertificateAtGenerationTime,
      spduConsistencyExpiryDateTooEarly,
      spduConsistencyExpiryDateTooLate,
      spduConsistencyGenerationLocationOutsideValidityRegion,
      spduConsistencyNoGenerationLocation,
      spduConsistencyUnauthorizedPSID,
      spduInternalConsistencyExpiryTimeBeforeGenerationTime,
      spduInternalConsistencyextDataHashDoesntMatch,
      spduInternalConsistencynoExtDataHashProvided,
      spduInternalConsistencynoExtDataHashPresent,
      spduLocalConsistencyPSIDsDontMatch,
      spduLocalConsistencyChainWasTooLongForSDEE,
      spduRelevanceGenerationTimeTooFarInPast,
      spduRelevanceGenerationTimeTooFarInFuture,
      spduRelevanceExpiryTimeInPast,
      spduRelevanceGenerationLocationTooDistant,
      spduRelevanceReplayedSpdu,
      spduCertificateExpired
   }

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   public static final int SECURITY_RESULT_CODE_LENGTH = 1;

   protected SecurityResultCode securityResultCode;

   public SecurityResultCodeParser() {
      super();
   }

   @Override
   public ParserStatus parseFile(BufferedInputStream bis, String fileName) throws FileParserException {

      ParserStatus status = ParserStatus.INIT;
      try {
         // parse SecurityResultCode
         if (getStep() == 0) {
            status = parseStep(bis, SECURITY_RESULT_CODE_LENGTH);
            if (status != ParserStatus.COMPLETE)
               return status;
            setSecurityResultCode(readBuffer[0]);
         }
         
         resetStep();
         status = ParserStatus.COMPLETE;

      } catch (Exception e) {
         throw new FileParserException(String.format("Error parsing %s on step %d", fileName, getStep()), e);
      }

      return status;

   }

   public SecurityResultCode getSecurityResultCode() {
      return securityResultCode;
   }

   public void setSecurityResultCode(SecurityResultCode securityResultCode) {
      this.securityResultCode = securityResultCode;
   }

   public void setSecurityResultCode(byte code) {
      try {
         setSecurityResultCode(SecurityResultCode.values()[code]);
      } catch (Exception e) {
         logger.error("Invalid SecurityResultCode: {}. Valid values are {}: ", 
            code, SecurityResultCode.values());
         setSecurityResultCode(SecurityResultCode.unknown);
      }
   }

}
