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

package org.ancora.MicroblazeInterpreter.Commons;

import org.ancora.MicroblazeInterpreter.HardwareBlocks.MsrBit;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Methods for reading and writing information from the MSR register.
 *
 * @author Joao Bispo
 */
public class MsrTools {

   /**
    * Reads a specific bit from the Machine Status Register.
    *
    * @param bit the MSR bit to read
    * @param msr the contents of the MSR register
    * @return 1 if the bit is set, 0 if not.
    */
   public static int readBit(MsrBit bit, int msr) {
      // Amount to shift
      int offset = (MAX_BITS - bit.getPosition());
      // Create mask
      int mask = 1 << offset;

      // Read bit
      int readValue = (msr & mask) >>> offset;

      return readValue;
   }

   /**
    * Writes a value to a specific bit of the Machine Status Register.
    *
    * @param bit the MSR bit to write
    * @param value 0 for clearing the bit, 1 for setting the bit
    * @param msr the contents of the MSR register
    * @return the updated value of msr
    */
   public static int writeBit(MsrBit bit, int value, int msr) {
      if(value == 0) {
         return clearBit(bit, msr);
      }
      else if(value == 1) {
         return setBit(bit, msr);
      }
      else {
         console.warn("writeBit: Value is not 1 or 0 ("+value+")");
         return msr;
      }
   }

   /**
    * Sets a specific bit of the Machine Status Register.
    * 
    * @param bit
    * @param msr
    * @return the updated value of msr
    */
   public static int setBit(MsrBit bit, int msr) {
      // Amount to shift
      int offset = (MAX_BITS - bit.getPosition());
      // Create mask
      int mask = 1 << offset;
      // Set bit
      return msr | mask;
   }

   /**
    * Clears a specific bit of the Machine Status Register.
    *
    * @param bit
    * @param msr
    * @return the updated value of msr
    */
   public static int clearBit(MsrBit bit, int msr) {
       // Amount to shift
      int offset = (MAX_BITS - bit.getPosition());
      // Create mask
      int mask = 1 << offset;
      // Clear bit
      return msr & ~mask;
   }

   // INSTANCE VARIABLES

   // Definitions
   private static final int MAX_BITS = 31;

   // Utilities
   private static final Console console = DefaultConsole.getConsole();
}
