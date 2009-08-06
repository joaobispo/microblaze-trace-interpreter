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
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Integer Compare.
 * 
 * <p> Includes the instructions cmp and cmpu.
 *
 * @author Joao Bispo
 */
public class MbCmp implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbCmp() {
        regA = -1;
        regB = -1;
        regD = -1;
        uBit = false;
        regs = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbCmp(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbCmp(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();

        // Get rA
        regA = InstructionParser.parseRegister(data.getR2());

        // Get rB
        regB = InstructionParser.parseRegister(data.getR3());

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());

        // Check bitC
        final boolean hasU = data.getOpName().contains(U_CHAR);
        if(hasU) {
            uBit = true;
        }
        else {
            uBit = false;
        }

    }

    /**
     * Executes the instruction
     */
    public void execute() {

        // Get rA and rB from register file
        int rA = regs.read(regA); // rA <- RF
        int rB = regs.read(regB); // rB <- RF

        // Do operation
        int rD = rB + ~rA + 1;

        // Determine which one is bigger
        boolean aBiggerThanB;
        if(uBit) {
            aBiggerThanB = BitOperations.unsignedComp(rA, rB);
        } else {
            aBiggerThanB = rA > rB;
        }

        // Change MSB to reflect relation
        if(aBiggerThanB) {
            rD = BitOperations.setBit(31, rD);
        } else {
            rD = BitOperations.clearBit(31, rD);
        }

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
    private final boolean uBit;
    // Hardware Blocks
    private final RegisterFile regs;
    private final int LATENCY = 1;
    private final boolean IS_BRANCH = false;
    // Constants
    private final String U_CHAR = "u";
}
