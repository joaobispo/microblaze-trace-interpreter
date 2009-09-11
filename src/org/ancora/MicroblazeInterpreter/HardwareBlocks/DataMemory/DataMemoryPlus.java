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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory;

import org.ancora.MicroblazeInterpreter.Commons.BitOperations;

/**
 * Adds byte and halfword operations to a DataMemory object.
 *
 * @author Joao Bispo
 */
public class DataMemoryPlus implements DataMemory {

    public DataMemoryPlus(DataMemory dataMemory) {
        this.dataMemory = dataMemory;
    }

   /**
    * Loads a halfword (16 bits) from the halfword aligned memory location
    * indicated by halfwordAddress. The least significant bit is ignored.
    * If the address is bigger than the memory segment, the higher bits
    * are ignored.
    *
    * <p>If position has not been written yet, a warning is shown.
    *
    * @param halfWord
    * @return
    */
   public int loadHalfword(int halfwordAddress) {
      // Read complete word
      int word = dataMemory.loadWord(halfwordAddress);

      // Extract which halfword
      final int offset = (halfwordAddress >>> 1) & MASK_1_BIT;
      // Put requested halfword at position 0
      int halfword = word >>> (offset * HALFWORD_SIZE);

      return halfword & MASK_16_BITS;
   }

   /**
    * Stores the contents of value, into the halfword aligned memory location
    * indicated by halfwordAddress.
    * The least significant bit is ignored.
    * If the address is bigger than the memory segment, the higher bits
    * are ignored.
    *
    * @param wordAddress a byte-addressed, word-aligned memory location
    *
    * @param halfwordAddress
    * @param value
    */
   public void storeHalfword(int halfwordAddress, int value) {
       int word = 0;

       // Check if word has been written
       if(dataMemory.isWordWritten(halfwordAddress)) {
           word = dataMemory.loadWord(halfwordAddress);
       }

       // Extract which halfword
       final int offset = (halfwordAddress >>> 1) & MASK_1_BIT;
       final int position = offset * HALFWORD_SIZE;

       final int finalWord = BitOperations.writeBits(position, HALFWORD_SIZE, value, word);

       // Store word
       dataMemory.storeWord(halfwordAddress, finalWord);
   }

   /**
    * Loads a byte (8 bits) from the byte aligned memory location
    * indicated by halfwordAddress.
    * If the address is bigger than the memory segment, the higher bits
    * are ignored.
    *
    * <p>If position has not been written yet, a warning is shown.
    *
    * @param halfWord
    * @return
    */
   public int loadByte(int byteAddress) {
      // Read complete word
      int word = dataMemory.loadWord(byteAddress);

      // Extract which byte
      final int offset = byteAddress & MASK_2_BITS;
      // Put requested byte at position 0
      int byt = word >>> (offset * BYTE_SIZE);

      return byt & MASK_8_BITS;
   }

   /**
    * Stores the contents of value, into the byte aligned memory location
    * indicated by byteAddress.
    * If the address is bigger than the memory segment, the higher bits
    * are ignored.
    *
    * @param wordAddress a byte-addressed, word-aligned memory location
    *
    * @param halfwordAddress
    * @param value
    */
   public void storeByte(int byteAddress, int value) {
       int word = 0;

       // Check if word has been written
       if(dataMemory.isWordWritten(byteAddress)) {
           word = dataMemory.loadWord(byteAddress);
       }

       // Extract which halfword
       final int offset = byteAddress  & MASK_2_BITS;
       final int position = offset * BYTE_SIZE;

       final int finalWord = BitOperations.writeBits(position, BYTE_SIZE, value, word);

       // Store word
        dataMemory.storeWord(byteAddress, finalWord);
    }

       public int loadWord(int wordAddress) {
        return dataMemory.loadWord(wordAddress);
    }

    public void storeWord(int wordAddress, int value) {
        dataMemory.storeWord(wordAddress, value);
    }

    public int[] writtenWordAddresses() {
        return dataMemory.writtenWordAddresses();
    }

    public boolean isWordWritten(int wordAddress) {
        return dataMemory.isWordWritten(wordAddress);
    }

    // INSTANCE VARIABLES
    // State
    private final DataMemory dataMemory;

    // Constants
    private final int HALFWORD_SIZE = 16;
    private final int BYTE_SIZE = 8;
    private final int MASK_16_BITS = 0xFFFF;
    private final int MASK_8_BITS = 0xFF;
    private final int MASK_2_BITS = 0x3;
    private final int MASK_1_BIT = 0x1;


}
