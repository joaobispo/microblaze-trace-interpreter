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

import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;


/**
 * Contains static methods used in MicroBlaze instructions.
 *
 * @author Joao Bispo
 */
public class BitOperations {

    /**
     * Calculates the carryOut of the sum of rA with rB and carry.
     *
     * @param rA
     * @param rB
     * @param carry the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static int getCarryOut(int rA, int rB, int carry) {
        if(carry != 0 && carry != 1) {
            console.warn("getCarryOut: Carry is different than 0 or 1 ("+
                    carry+")");
        }

        //System.out.println("rA:"+Integer.toBinaryString(rA));
        //System.out.println("rB:"+Integer.toBinaryString(rB));

        // Extend operands to long and mask them
        long lRa = rA & MASK_32_BITS;
        long lRb = rB & MASK_32_BITS;
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;


        //System.out.println("lRa:"+Long.toBinaryString(lRa));
        //System.out.println("lRb:"+Long.toBinaryString(lRb));

        // Do the summation
        long result = lRa + lRb + lCarry;

        //System.out.println("Result:"+Long.toBinaryString(result));

        // Get the carry bit
        int carryOut = (int) ((result & MASK_BIT_33) >>> 32);
        return carryOut;
    }

    /**
     * Performs a 32-bit unsigned division.
     * 
     * @param a
     * @param b
     * @return
     */
    public static int unsignedDiv(int a, int b) {
         final long la = a & MASK_32_BITS;
         final long lb = b & MASK_32_BITS;

         return (int) (la / lb);
    }

   /**
    * Sets a specific bit of an int.
    *
    * @param bit the bit to set. The least significant bit is bit 0
    * @param target the integer where the bit will be set
    * @return the updated value of the target
    */
   public static int setBit(int bit, int target) {;
      // Create mask
      int mask = 1 << bit;
      // Set bit
      return target | mask;
   }

   /**
    * Clears a specific bit of an int.
    *
    * @param bit the bit to clear. The least significant bit is bit 0
    * @param target the integer where the bit will be cleared
    * @return the updated value of the target
    */
   public static int clearBit(int bit, int target) {
      // Create mask
      int mask = 1 << bit;
      // Clear bit
      return target & ~mask;
   }

   /**
    * Writes a value to a specific bit of an int.
    *
    * @param bit the bit to write
    * @param value 0 for clearing the bit, 1 for setting the bit
    * @param target the integer where the bit will be written
    * @return the updated value of the integer
    */
   public static int writeBit(int bit, int value, int target) {
      if(value == 0) {
         return clearBit(bit, target);
      }
      else if(value == 1) {
         return setBit(bit, target);
      }
      else {
         console.warn("writeBit: Value is not 1 or 0 ("+value+")");
         return target;
      }
   }
   
   /**
    * Gets the a single bit of the target.
    * 
    * @param position
    * @param target
    * @return
    */
   public static int getBit(int position, int target) {
      return (target >>> position) & MASK_BIT_1;  
   }

   /**
    * Writes a set of bits to an intenger.
    *
    * @param position the least significat bit that will be written
    * @param size how many bits of the value, from the least significant bit,
    * will be used to write
    * @param value the bits to write
    * @param target the integer where the bits will be written
    * @return the updated value of the integer
    */
   public static int writeBits(int position, int size, int value, int target) {
      for(int i=0; i<size; i++) {
         // Get bit to write
         int bitToWrite = getBit(i, value);
         target = writeBit(i+position, bitToWrite, target);
      }

      return target;
   }

    private static final long MASK_32_BITS = 0xFFFFFFFFL;
    private static final long MASK_BIT_33 = 0x100000000L;
    private static final int MASK_BIT_1 = 0x1;
    private static final Console console = DefaultConsole.getConsole();

}
