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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory.DataMemoryPlus;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Load Halfword Unsigned Immediate.
 * 
 * <p> Includes the instruction lhui.
 *
 * @author Joao Bispo
 */
public class MbLhui implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbLhui() {
        regA = -1;
        imm = -1;
        regD = -1;
        regs = null;
        config = null;
        memory = null;
        lockReg = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbLhui(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbLhui(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        config = processor.getConfiguration();
        memory = processor.getDataMemory();
        lockReg = processor.getLockRegister();

        // Get rA
        regA = InstructionParser.parseRegister(data.getR2());

        // Get rB
        imm = data.getImm();

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());

    }

    /**
     * Executes the instruction
     */
    public void execute() {
        // Get rA and rB from register file
        int rA = regs.read(regA); // rA <- RF
        int immediate = lockReg.processImmediate(imm);

        int halfwordAddress = rA + immediate;

        int rD;

        int bit0 = BitOperations.getBit(0, halfwordAddress);
        if(bit0 != 0) {
            System.out.println("lhui: address is not halfword aligned ("+
                    halfwordAddress+"). Returning 0.");
            rD = 0;
        } else {
            rD = memory.loadHalfword(halfwordAddress);
        }

        // Store result in register file
        regs.write(regD, rD);// RD <- rD

    }

    public int latency() {
        if(config.C_AREA_OPTIMIZED() == 0) {
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

    // Hardware Blocks
    private final RegisterFile regs;
    private final LockRegister lockReg;
    private final MbConfiguration config;
    private final DataMemoryPlus memory;
    
    // Constants
    private final boolean IS_BRANCH = false;
}
