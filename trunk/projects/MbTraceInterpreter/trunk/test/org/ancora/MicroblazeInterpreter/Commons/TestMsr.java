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

/**
 *
 * @author Joao Bispo
 */
public class TestMsr {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //testLoad();
        testBitOperations();
    }

    /*
   private static void testLoad() {
      int msr;

      //msr = 0x2; // Bit 30
      msr = 0x4; // Bit 29


      System.out.println("Before:"+Integer.toBinaryString(msr));
      //System.out.println("Bit:"+MsrTools.readBit(MsrBit.C, msr));

      //System.out.println("Bit:"+Integer.toBinaryString(MsrTools.clearBit(MsrBit.C, msr)));
      //System.out.println("Bit:"+Integer.toBinaryString(MsrTools.setBit(MsrBit.C, msr)));

      System.out.println("Bit:"+Integer.toBinaryString(MsrTools.writeBit(MsrBit.C, 0, msr)));
      //System.out.println("Bit:"+Integer.toBinaryString(MsrTools.writeBit(MsrBit.C, 1, msr)));
      //System.out.println("Bit:"+Integer.toBinaryString(MsrTools.writeBit(MsrBit.C, 2, msr)));
   }
     */

   private static void testBitOperations() {
      int target;

      target = 1;
      System.out.println("Before:"+Integer.toBinaryString(target));
      System.out.println(Integer.toBinaryString(BitOperations.writeBits(1, 3, 4, target)));
   }

}
