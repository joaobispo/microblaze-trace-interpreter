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
 * Stores a segment of Data Memory
 *
 * @author Joao
 */
public interface Bucket {

    /**
     * Reads a word from the bucket. Higher bits of the wordIndex are truncated.
     *  If position has not been written, a warning is shown.
     *
     * @param wordIndex
     * @return
     */
    public int read(int wordIndex);

    /**
     * Writes a word to the bucket. Higher bits of the wordIndex are truncated.
     *
     *
     * @param wordIndex
     * @param word
     */
    public void write(int wordIndex, int word);

    /**
     * Get the exponent of the power of 2 which corresponds to the size of this
     * bucket.
     *
     * @return
     */
    public int getBucketSizePow();

    /**
     * @return the indexes that have been written.
     */
    public int[] writtenPositions();
}
