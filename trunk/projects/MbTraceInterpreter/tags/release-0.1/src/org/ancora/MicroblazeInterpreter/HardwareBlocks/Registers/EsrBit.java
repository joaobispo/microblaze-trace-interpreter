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
package org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers;

import static org.ancora.MicroblazeInterpreter.Configuration.Definitions.MAX_BITS;

/**
 * Represents the individual bits of the Exception Status Register
 *
 * @author Joao Bispo
 */
public enum EsrBit {

   DS(MAX_BITS - 19, 1),
   EC(MAX_BITS - 31, 5),
   W(MAX_BITS - 20, 1),
   S(MAX_BITS - 21, 1),
   Rx(MAX_BITS - 26, 5),
   DEC(MAX_BITS - 20, 1),
   FSL(MAX_BITS - 26, 4),
   DIZ(MAX_BITS - 20, 1);

   private EsrBit(int bitPosition, int bitSize) {
      this.bitPosition = bitPosition;
      this.bitSize = bitSize;
   }

   /**
    * @return the position of this bit.
    */
   public int getPosition() {
      return bitPosition;
   }

   public int getSize() {
      return bitSize;
   }
   // INSTANCE VARIABLES
   // State
   private final int bitPosition;
   private final int bitSize;
}
