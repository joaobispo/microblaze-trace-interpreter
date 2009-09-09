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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers;

/**
 * General Purpose Register File with 32 bits registers.
 * 
 * @author Joao
 */
public interface RegisterFile {

    /**
     * Read a value from the Register File
     * 
     * @param address
     * @return
     */
    public int read(int address);

    /**
     * Write a value to the register file
     * 
     * @param address
     * @param value
     */
    public void write(int address, int value);

    /**
     * @return the number of registers of this register file
     */
    public int numRegisters();
}
