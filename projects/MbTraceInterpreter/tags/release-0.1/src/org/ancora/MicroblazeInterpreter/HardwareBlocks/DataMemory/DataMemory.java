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
     * Reads a word from the memory. If position has not been written,
     * a warning is shown.
     *
     * @param address
     * @return
     */
    public int read(int address);

    /**
     * Writes a word to the memory.
     *
     * @param address
     * @param word
     */
    public void write(int address, int word);

    /**
     *
     * @return the addresses that have been written.
     */
    public int[] getWrittenAddresses();
}
