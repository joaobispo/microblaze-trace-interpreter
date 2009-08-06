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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Unconditional Branch.
 * 
 * <p> Includes the instructions br, bra, brd, brad, brld and brald.
 *
 * @author Joao Bispo
 */
public class MbBr implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbBr() {
        dBit = false;
        aBit = false;
        lBit = false;
        regB = -1;
        regD = -1;
        regs = null;
        //lockReg = null;
        spr = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbBr(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbBr(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        //lockReg = processor.getLockRegister();
        spr = processor.getSpecialRegisters();

        // Check bitD
        final boolean hasD = data.getOpName().contains(D_CHAR);
        if (hasD) {
            dBit = true;
        } else {
            dBit = false;
        }

        // Check bitL
        final boolean hasL = data.getOpName().contains(L_CHAR);
        if (hasL) {
            lBit = true;
            // Get rD
            regD = InstructionParser.parseRegister(data.getR1());
            // Get rB
            regB = InstructionParser.parseRegister(data.getR2());
        } else {
            lBit = false;
            // Get rB
            regB = InstructionParser.parseRegister(data.getR1());
            regD = -1;
        }

        // Check bitA
        final boolean hasA = data.getOpName().contains(A_CHAR);
        if (hasA) {
            aBit = true;
        } else {
            aBit = false;
        }

    }

    /**
     * Executes the instruction
     */
    public void execute() {
        // If L bit, store Program Counter
        if (lBit) {
            regs.write(regD, spr.getPc());
        }

        if (aBit) {
            spr.writePc(regB);
        } else {
            int pc = spr.getPc();
            spr.writePc(pc + regB);
        }
        
    }

    public int latency() {

        if (dBit) {
            return 2;
        } else {
            return 3;
        }

    }

    public boolean isBranch() {
        return IS_BRANCH;
    }
    // INSTANCE VARIABLES
    // State
    private final int regD;
    private final int regB;
    private final boolean dBit;
    private final boolean lBit;
    private final boolean aBit;
    // Hardware Blocks
    private final RegisterFile regs;
    //private final LockRegister lockReg;
    private final SpecialPurposeRegisters spr;
    // Constants
    private final boolean IS_BRANCH = true;
    private final String D_CHAR = "d";
    private final String L_CHAR = "d";
    private final String A_CHAR = "d";
}
