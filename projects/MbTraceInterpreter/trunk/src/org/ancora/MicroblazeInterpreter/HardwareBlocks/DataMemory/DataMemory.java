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

/**
 * Represents data memory which can be written and read.
 *
 * @author Joao Bispo
 */
public interface DataMemory {

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
    public int loadWord(int wordAddress);

   /**
    * Stores the contents of value, into the word aligned memory location
    * indicated by wordAddress. The 2 least significant bits are ignored.
    * If the address is bigger than the memory segment,
    * the higher bits are ignored.
    *
    * @param wordAddress a byte-addressed, word-aligned memory location
    * @param value
    */
   public void storeWord(int wordAddress, int value);


    /**
     * Reads a word from the memory. If position has not been written,
     * a warning is shown.
     *
     * @param address
     * @return
     */
    //public int read(int address);

    /**
     * Writes a word to the memory.
     *
     * @param address
     * @param word
     */
    //public void write(int address, int word);

   /**
    * @return an array with the word-aligned addresses which have been written.
    */
    public int[] writtenWordAddresses();

    /**
     * Checks if a specific word has been written.
     *
     * @param wordAddress
     * @return
     */
    public boolean isWordWritten(int wordAddress);
    
}
