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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Logical OR.
 * 
 * <p> Includes the instruction or.
 *
 * @author Joao Bispo
 */
public class MbOr implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbOr() {
        regA = -1;
        regB = -1;
        regD = -1;
        regs = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbOr(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbOr(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();

        // Get rA
        regA = InstructionParser.parseRegister(data.getR2());

        // Get rB
        regB = InstructionParser.parseRegister(data.getR3());

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());


    }

    /**
     * Executes the instruction
     */
    public void execute() {

        // Get rA and rB from register file
        int rA = regs.read(regA); // rA <- RF
        int rB = regs.read(regB); // rB <- RF

        // Result
        int rD = rA | rB;

        // Store result in register file
        regs.write(regD, rD);// RD <- rD

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
    private final int regB;
    private final int regD;
    // Hardware Blocks
    private final RegisterFile regs;
    // Constants
    private final boolean IS_BRANCH = false;
    private final int LATENCY = 1;
}
