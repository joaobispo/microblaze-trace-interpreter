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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor;

import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialRegister;
import org.ancora.MicroblazeInterpreter.Instructions.Instruction;

/**
 * Implementation of the Clock interface.
 * 
 * <p>This class takes care of LockRegister and Program Counter, and counts
 * the number of cycles.
 *
 * @author Joao
 */
public class CycleClock implements Clock {

    public CycleClock(LockRegister lockReg, SpecialPurposeRegisters spr) {
        this.lockReg = lockReg;
        this.spr = spr;
        this.latency = 0;
    }


    /**
     * Advances a step in the processor state.
     * 
     * <p>This method takes care of LockRegister and Program Counter, and counts
     * the number of cycles.
     * 
     * @param instruction
     */
    public void step(Instruction instruction) {
        // Advance Lock Register
        lockReg.step();

        // Advance Program Counter if instruction is not branch
        if (!instruction.isBranch()) {
            spr.incrementPc();
            //int pc = spr.read(SpecialRegister.rpc);
            //spr.write(SpecialRegister.rpc, pc + 4);
        }

        // Update latency
        latency += instruction.latency();
    }

    public int getLatency() {
        return latency;
    }

    // INSTANCE VARIABLES
    // State
    private final LockRegister lockReg;
    private final SpecialPurposeRegisters spr;
    private int latency;




}
