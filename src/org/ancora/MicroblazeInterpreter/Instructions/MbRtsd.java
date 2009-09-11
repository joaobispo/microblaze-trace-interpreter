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
 *  Implements the MicroBlaze Return from Subroutine.
 * 
 * <p> Includes the instruction rtsd.
 *
 * @author Joao Bispo
 */
public class MbRtsd implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbRtsd() {
        imm = -1;
        regA = -1;
        regs = null;
        spr = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbRtsd(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbRtsd(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        spr = processor.getSpecialRegisters();

        // Get rA
        regA = InstructionParser.parseRegister(data.getR1());

        // Get imm
        imm = data.getImm();

    }

    /**
     * Executes the instruction
     */
    public void execute() {
        int rA = regs.read(regA);
        int pc = rA + imm;

        spr.writePc(pc);
    }

    public int latency() {

        return LATENCY;

    }

    public boolean isBranch() {
        return IS_BRANCH;
    }
    // INSTANCE VARIABLES
    // State
    private final int regA;
    private final int imm;
    // Hardware Blocks
    private final RegisterFile regs;
    private final SpecialPurposeRegisters spr;
    // Constants
    private final boolean IS_BRANCH = true;
    private final int LATENCY = 2;
}
