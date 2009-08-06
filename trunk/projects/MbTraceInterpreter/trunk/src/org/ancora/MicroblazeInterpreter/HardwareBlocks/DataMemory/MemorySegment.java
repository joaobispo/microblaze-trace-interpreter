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

import java.util.BitSet;
import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Segment of byte-addressed memory. Memory ranges from 0 to 2^sizePower - 1.
 *
 * @author Joao Bispo
 */
public class MemorySegment {

    /**
     * Creates a byte-addressed memory with 2^sizePower 32-bit words.
     *
     * @param sizePower
     */
   public MemorySegment(int sizePower) {
        int totalSize = (int) Math.pow(BASE_2, sizePower);

        words = new int[totalSize];
        isWritten = new BitSet(totalSize);
        this.sizePower = sizePower;
   }

   /**
    * Loads a word (32 bits) from the word aligned memory location indicated
    * by wordAddress. The two least significant bits are ignored. If the address
    * is bigger than the memory segment, the higher bits are ignored.
    *
    * <p>If position has not been written yet, a warning is shown.
    *
    * @param wordAddress a byte-addressed, word-aligned memory location
    * @return
    */
   public int loadWord(int wordAddress) {
        // Truncate higher bits of index
        int offset = INT_SIZE - sizePower;
        int index = wordAddress << offset;
        index = index >>> offset;
        
        // Transform to the index used to address the array
        index = arrayIndex(index);
        

        if(!isWritten.get(index)) {
            String hexAddress = Integer.toHexString(wordAddress);
            hexAddress = BitOperations.padHexString(hexAddress, HEX_STRING_SIZE);
            console.warn("loadWord: Trying to read from a memory position which " +
                    "has not been written ("+hexAddress+")");
            console.more("Writing a zero to prevent more warnings.");

            storeWord(wordAddress, 0);
            return 0;
        }

        return words[index];
   }

   /**
    * Stores the contents of value, into the word aligned memory location
    * indicated by wordAddress. The 2 least significant bits are ignored.
    * If the address is bigger than the memory segment,
    * the higher bits are ignored.
    *
    * @param wordAddress a byte-addressed, word-aligned memory location
    * @param value
    */
   public void storeWord(int wordAddress, int value) {
        // Truncate higher bits of index
        int offset = INT_SIZE - sizePower;
        int index = wordAddress << offset;
        index = index >>> offset;

        // Transform to the index used to address the array
        index = arrayIndex(index);

        words[index] = value;
        isWritten.set(index);
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
      int word = loadWord(halfwordAddress);

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
       int index = arrayIndex(halfwordAddress);
       if(isWritten.get(index)) {
           // Read complete word
           word = loadWord(halfwordAddress);
       }

       // Extract which halfword
       final int offset = (halfwordAddress >>> 1) & MASK_1_BIT;
       final int position = offset * HALFWORD_SIZE;

       final int finalWord = BitOperations.writeBits(position, HALFWORD_SIZE, value, word);

       // Store word
       storeWord(halfwordAddress, finalWord);
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
      int word = loadWord(byteAddress);

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
       int index = arrayIndex(byteAddress);
       if(isWritten.get(index)) {
           // Read complete word
           word = loadWord(byteAddress);
       }

       // Extract which halfword
       final int offset = byteAddress  & MASK_2_BITS;
       final int position = offset * BYTE_SIZE;

       final int finalWord = BitOperations.writeBits(position, BYTE_SIZE, value, word);

       // Store word
       storeWord(byteAddress, finalWord);
   }

   /**
    * @return an array with the word-aligned addresses which have been written.
    */
   public int[] writtenWordAddresses() {
      int numWrittenWords = isWritten.cardinality();
      int[] writtenAddresses = new int[numWrittenWords];

      int index = 0;
      for (int i = isWritten.nextSetBit(0); i >= 0; i = isWritten.nextSetBit(i + 1)) {
         writtenAddresses[index] = i<<2;
         index++;
      }

      return writtenAddresses;
   }

   /**
    * Transforms word-aligned addresses in indexes for accessing the array.
    * 
    * @param wordAddress
    * @return
    */
   private int arrayIndex(int wordAddress) {
       return wordAddress >>> 2;
   }

   /**
    * Transforms array indexes in word-aligned addresses for accessing the
    * memory.
    *
    * @param arrayIndex
    * @return
    */
   private int wordAddress(int arrayIndex) {
       return arrayIndex << 2;
   }

    // INSTANCE VARIABLES
    // State
    private final int[] words;
    private final BitSet isWritten;
    private final int sizePower;

    // Constants
    private final int BASE_2 = 2;
    private final int INT_SIZE = 32;
    private final int HALFWORD_SIZE = 16;
    private final int BYTE_SIZE = 8;
    private final int HEX_STRING_SIZE = 8;
    private final int MASK_16_BITS = 0xFFFF;
    private final int MASK_8_BITS = 0xFF;
    private final int MASK_2_BITS = 0x3;
    private final int MASK_1_BIT = 0x1;

    // Utilities
    private final Console console = DefaultConsole.getConsole();


}
