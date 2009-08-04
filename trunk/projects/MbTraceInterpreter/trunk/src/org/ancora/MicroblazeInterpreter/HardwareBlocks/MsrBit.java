/*
 *  Copyright 2009 Ancora Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.ancora.MicroblazeInterpreter.HardwareBlocks;

import static org.ancora.MicroblazeInterpreter.Configuration.Definitions.MAX_BITS;

/**
 * Represents the individual bits of the Machine Status Register
 *
 * @author Joao Bispo
 */
public enum MsrBit {

   CC(MAX_BITS - 0),
   VMS(MAX_BITS - 17),
   VM(MAX_BITS - 18),
   UMS(MAX_BITS - 19),
   UM(MAX_BITS - 20),
   PVR(MAX_BITS - 21),
   EIP(MAX_BITS - 22),
   EE(MAX_BITS - 23),
   DCE(MAX_BITS - 24),
   DZO(MAX_BITS - 25),
   ICE(MAX_BITS - 26),
   FSL(MAX_BITS - 27),
   BIP(MAX_BITS - 28),
   C(MAX_BITS - 29),
   IE(MAX_BITS - 30),
   BE(MAX_BITS - 31);



   private MsrBit(int bitPosition) {
      this.bitPosition = bitPosition;
   }

   /**
    * @return the position of this bit.
    */
   public int getPosition() {
      return bitPosition;
   }



   // INSTANCE VARIABLES
   // State
   private final int bitPosition;


}
