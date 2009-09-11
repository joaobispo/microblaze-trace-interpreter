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
public class MemorySegment implements DataMemory {

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


   public int loadWord(int wordAddress) {     
        // Transform to the index used to address the array
        final int index = arrayIndex(wordAddress);
        

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
        // Transform to the index used to address the array
        final int index = arrayIndex(wordAddress);

        words[index] = value;
        isWritten.set(index);
   }




   /**
    * @return an array with the word-aligned addresses which have been written.
    */
   public int[] writtenWordAddresses() {
      int numWrittenWords = isWritten.cardinality();
      int[] writtenAddresses = new int[numWrittenWords];

      int index = 0;
      for (int i = isWritten.nextSetBit(0); i >= 0; i = isWritten.nextSetBit(i + 1)) {
         writtenAddresses[index] = wordAddress(i);
         index++;
      }

      return writtenAddresses;
   }

    public boolean isWordWritten(int wordAddress) {
        final int index = arrayIndex(wordAddress);
        return isWritten.get(index);
    }

   /**
    * Transforms word-aligned addresses in indexes for accessing the array.
    * 
    * @param wordAddress
    * @return
    */
   private int arrayIndex(int wordAddress) {
        // Truncate higher bits of address
        int offset = INT_SIZE - sizePower - WORD_POWER;
        int index = wordAddress << offset;
        index = index >>> offset;
        // Transform from byte-addressed to word-addressed.
        return index >>> WORD_POWER;
   }

   /**
    * Transforms array indexes in word-aligned addresses for accessing the
    * memory.
    *
    * @param arrayIndex
    * @return
    */
   private int wordAddress(int arrayIndex) {
       return arrayIndex << WORD_POWER;
   }

    // INSTANCE VARIABLES
    // State
    private final int[] words;
    private final BitSet isWritten;
    private final int sizePower;

    // Constants
    /**
     * Exponent of a base two power. Size of the word, in bytes. 
     */
    public static final int WORD_POWER = 2;

    private final int BASE_2 = 2;
    private final int INT_SIZE = 32;
    private final int HEX_STRING_SIZE = 8;

    // Utilities
    private final Console console = DefaultConsole.getConsole();






}
