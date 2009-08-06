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

import org.ancora.MicroblazeInterpreter.Configuration.MbConfiguration;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Barrel Shift Immediate.
 * 
 * <p> Includes the instructions bsrli, bsrai and bslli.
 *
 * @author Joao Bispo
 */
public class MbBsi implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbBsi() {
        regA = -1;
        imm = -1;
        regD = -1;
        sBit = false;
        tBit = false;
        regs = null;
        lockReg = null;
        config = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbBsi(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbBsi(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        lockReg = processor.getLockRegister();
        config = processor.getConfiguration();

        // Get rA
        regA = InstructionParser.parseRegister(data.getR2());

        // Get rB
        imm = data.getImm();

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());

        // Check bitS
        final boolean hasS = !data.getOpName().contains(R_CHAR);
        if(hasS) {
            sBit = true;
        }
        else {
            sBit = false;
        }

        // Check bitT
        final boolean hasT = data.getOpName().contains(A_CHAR);
        if(hasT) {
            tBit = true;
        }
        else {
            tBit = false;
        }

    }

    /**
     * Executes the instruction
     */
    public void execute() {

        // Get rA from register file
        int rA = regs.read(regA); // rA <- RF
        int result;

        if(sBit) {
            result = rA << imm;
        } else {
            if(tBit) {
                result = rA >> imm;
            } else {
                result = rA >>> imm;
            }
        }

        // Store result in register file
        regs.write(regD, result); // RD <- rD

    }

    public int latency() {
        if (config.C_AREA_OPTIMIZED() == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean isBranch() {
        return IS_BRANCH;
    }
    // INSTANCE VARIABLES
    // State
    private final int regA;
    private final int imm;
    private final int regD;
    private final boolean sBit;
    private final boolean tBit;


    // Hardware Blocks
    private final RegisterFile regs;
    private final LockRegister lockReg;
    private final MbConfiguration config;
    
    // Constants
    private final boolean IS_BRANCH = false;
    private final String A_CHAR = "a";
    private final String R_CHAR = "r";
}
