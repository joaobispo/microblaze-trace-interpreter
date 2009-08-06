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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Branch Immediate if Greater or Equal.
 * 
 * <p> Includes the instructions bgei and bgeid.
 *
 * @author Joao Bispo
 */
public class MbBgei implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbBgei() {
        dBit = false;
        regA = -1;
        imm = -1;
        regs = null;
        lockReg = null;
        spr = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbBgei(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbBgei(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        lockReg = processor.getLockRegister();
        spr = processor.getSpecialRegisters();

        // Get rD
        regA = InstructionParser.parseRegister(data.getR1());

        // Get rB
        imm = data.getImm();

        // Check bitC
        final boolean hasD = data.getOpName().contains(D_CHAR);
        if(hasD) {
            dBit = true;
        }
        else {
            dBit = false;
        }

    }

    /**
     * Executes the instruction
     */
    public void execute() {
        // Get rA from register file
        int rA = regs.read(regA); // rA <- RF

        if(rA >= 0) {
            int immediate = lockReg.processImmediate(imm);
            int pc = spr.getPc();
            spr.writePc(pc + immediate);
            branchTaken = true;
        } else {
            spr.incrementPc();
            branchTaken = false;
        }

    }

    public int latency() {
        if(!branchTaken) {
            return 1;
        } else {
            if(dBit) {
                return 2;
            }
            else {
                return 3;
            }
        }
    }

    public boolean isBranch() {
        return IS_BRANCH;
    }
    // INSTANCE VARIABLES
    // State
    private final int regA;
    private final int imm;
    private final boolean dBit;
    private boolean branchTaken;

    // Hardware Blocks
    private final RegisterFile regs;
    private final LockRegister lockReg;
    private final SpecialPurposeRegisters spr;
    
    // Constants
    private final boolean IS_BRANCH = true;
    private final String D_CHAR = "d";
}
