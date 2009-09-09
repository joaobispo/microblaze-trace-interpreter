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
 * Implementation of Bucket.
 *
 * @author Joao
 */
public class DataBucket implements Bucket {

    /**
     * Creats a new DataBucket.
     *
     * @param bucketSizePower exponent of a power of 2. The result will be the
     * size of the bucket.
     */
    public DataBucket(int bucketSizePower) {
        this.bucketSizePower = bucketSizePower;

        int totalSize = (int) Math.pow(BASE_2, bucketSizePower);
        words = new int[totalSize];
        isWritten = new BitSet(totalSize);
    }



    /**
     * Reads a word from the bucket. Higher bits of the wordIndex are truncated.
     *  If position has not been written, a warning is shown.
     *
     * @param wordIndex
     * @return
     */
    public int read(int wordIndex) {
        // Truncate higher bits of index
        int offset = INT_SIZE - bucketSizePower;
        int correctIndex = wordIndex << offset;
        correctIndex = correctIndex >>> offset;

        if(!isWritten.get(correctIndex)) {
            String hexAddress = Integer.toHexString(wordIndex);
            hexAddress = BitOperations.padHexString(hexAddress, HEX_STRING_SIZE);
            console.warn("read: Trying to read from memory position which has not " +
                    "been written ("+hexAddress+")");
            console.more("Writing a zero to prevent more warnings.");

            write(wordIndex, 0);
        }

        return words[correctIndex];
    }

    /**
     * Writes a word to the bucket. Higher bits of the wordIndex are truncated.
     *
     *
     * @param wordIndex
     * @param word
     */
    public void write(int wordIndex, int word) {
        // Truncate higher bits of index
        int offset = INT_SIZE - bucketSizePower;
        int correctIndex = wordIndex << offset;
        correctIndex = correctIndex >>> offset;

        words[correctIndex] = word;
        isWritten.set(correctIndex);
    }

    public int getBucketSizePow() {
        return bucketSizePower;
    }

    public int[] writtenPositions() {
        int numWrittenWords = isWritten.cardinality();
        int[] writtenWords = new int[numWrittenWords];

        int index = 0;
        for (int i = isWritten.nextSetBit(0); i >= 0; i = isWritten.nextSetBit(i + 1)) {
            writtenWords[index] = i;
            index++;
        }

        return writtenWords;
    }

    // INSTANCE VARIABLES
    // State
    private final int[] words;
    private final BitSet isWritten;
    private final int bucketSizePower;

    // Constants
    private final int BASE_2 = 2;
    private final int INT_SIZE = 32;
    private final int HEX_STRING_SIZE = 8;

    // Utilities
    private final Console console = DefaultConsole.getConsole();

}
