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

import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.MicroblazeInterpreter.Configuration.MbConfiguration;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.MsrBit;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialRegister;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Unconditional Branch Immediate.
 * 
 * <p> Includes the instructions bri, brai, brid, braid, brlid and bralid.
 *
 * @author Joao Bispo
 */
public class MbBri implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbBri() {
        dBit = false;
        aBit = false;
        lBit = false;
        imm = -1;
        regD = -1;
        regs = null;
        lockReg = null;
        spr = null;
        config = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbBri(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbBri(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        lockReg = processor.getLockRegister();
        spr = processor.getSpecialRegisters();
        config = processor.getConfiguration();

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());

        // Get imm
        imm = data.getImm();

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
        } else {
            lBit = false;
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

        int immediate = lockReg.processImmediate(imm);
        if (aBit) {
            spr.writePc(immediate);
        } else {
            int pc = spr.getPc();
            spr.writePc(pc + immediate);
        }

        // Special case
        if(config.C_USE_MMU() >= 1) {
            if(dBit & aBit & lBit & immediate == 0x8) {
                int bit;
                int msr = spr.read(SpecialRegister.rmsr);
                bit = BitOperations.getBit(MsrBit.UM.getPosition(), msr);
                msr = BitOperations.writeBit(MsrBit.UMS.getPosition(), bit, msr);
                bit = BitOperations.getBit(MsrBit.VM.getPosition(), msr);
                msr = BitOperations.writeBit(MsrBit.VMS.getPosition(), bit, msr);
                msr = BitOperations.clearBit(MsrBit.UM.getPosition(), msr);
                msr = BitOperations.clearBit(MsrBit.VM.getPosition(), msr);
            }
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
    private final int imm;
    private final boolean dBit;
    private final boolean lBit;
    private final boolean aBit;
    // Hardware Blocks
    private final RegisterFile regs;
    private final LockRegister lockReg;
    private final SpecialPurposeRegisters spr;
    private final MbConfiguration config;
    // Constants
    private final boolean IS_BRANCH = true;
    private final String D_CHAR = "d";
    private final String L_CHAR = "d";
    private final String A_CHAR = "d";
}
