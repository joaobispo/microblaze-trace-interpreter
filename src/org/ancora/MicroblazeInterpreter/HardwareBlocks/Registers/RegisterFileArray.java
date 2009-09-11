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
 *  Implementation of the MicroBlaze RegisterFile. It has 32 registers.
 *
 * @author Joao
 */
public class RegisterFileArray implements RegisterFile {

    public RegisterFileArray() {
        registers = new int[NUM_REGS];
    }


    public int read(int address) {
        return registers[address];
    }

    public void write(int address, int value) {
        // Discard writes to R0
        if(address == 0) {
            return;
        }
        
        registers[address] = value;
    }

    public int numRegisters() {
        return NUM_REGS;
    }

    // INSTANCE VARIABLES
    // State
    private final int[] registers;

    // Definitions
    private static final int NUM_REGS = 32;




}
