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

/**
 * Represents the individual bits of the Machine Status Register
 *
 * @author Joao Bispo
 */
public enum MsrBit {

   CC(0),
   VMS(17),
   VM(18),
   UMS(19),
   UM(20),
   PVR(21),
   EIP(22),
   EE(23),
   DCE(24),
   DZO(25),
   ICE(26),
   FSL(27),
   BIP(28),
   C(29),
   IE(30),
   BE(31);

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
   private final int bitPosition;
}
