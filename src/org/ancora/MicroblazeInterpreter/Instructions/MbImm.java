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

package org.ancora.MicroblazeInterpreter.Instructions;

import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 *  Implements the MicroBlaze Immediate.
 *
 * <p> Includes the instruction imm.
 *
 * @author Joao
 */
public class MbImm implements Instruction, Builder {

        /**
     * Constructor for using this object as a MbBuilder
     */
    public MbImm() {
        imm = -1;
        lockReg = null;
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbImm(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        lockReg = processor.getLockRegister();

        // Parse the data
        imm = data.getImm();
    }



    public void execute() {
        lockReg.setImediate(imm);
    }

    public int latency() {
        return LATENCY;
    }

    public boolean isBranch() {
        return IS_BRANCH;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbImm(data, processor);
    }

    // INSTANCE VARIABLES

    // State
    private final int imm;
    private final LockRegister lockReg;

    // Constants
    private final int LATENCY = 1;
    private final boolean IS_BRANCH = false;
}
