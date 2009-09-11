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
 *  Implements the MicroBlaze Integer Divide.
 * 
 * <p> Includes the instructions idiv and idivu.
 *
 * @author Joao Bispo
 */
public class MbIdiv implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbIdiv() {
        regA = -1;
        regB = -1;
        regD = -1;
        spr = null;
        config = null;
        uBit = false;
        regs = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbIdiv(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbIdiv(TraceData data, MicroBlazeProcessor processor) {
        // Assign Hardware Blocks
        regs = processor.getRegisterFile();
        spr = processor.getSpecialRegisters();
        config = processor.getConfiguration();

        // Get rA
        regA = InstructionParser.parseRegister(data.getR2());

        // Get rB
        regB = InstructionParser.parseRegister(data.getR3());

        // Get rD
        regD = InstructionParser.parseRegister(data.getR1());

        // Check bitU
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

        // Result
        int rD;

        if(rA == 0) {
            aEqualsZero = true;
        } else {
            aEqualsZero = false;
        }

        // Division by 0
        if(aEqualsZero) {
            rD = 0;
            spr.writeDzoBit(1);
            spr.writeEcBits(5);
            spr.writeDecBit(0);
        } else if(!uBit & rA == -1 & rB == -2147483648) {
            rD = rB;
            spr.writeDzoBit(1);
            spr.writeEcBits(5);
            spr.writeDecBit(1);
        } else {
            if(uBit) {
                rD = BitOperations.unsignedDiv(rB, rA);
            } else {
                rD = rB / rA;
            }
        }



        // Store result in register file
        regs.write(regD, rD);// RD <- rD

    }

    public int latency() {
        if(aEqualsZero) {
            return 1;
        } else if(config.C_AREA_OPTIMIZED() == 0) {
            return 32;
        } else {
            return 34;
        }
        
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
    private boolean aEqualsZero;
    // Hardware Blocks
    private final RegisterFile regs;
    private final SpecialPurposeRegisters spr;
    private final MbConfiguration config;
    // Constants
    private final boolean IS_BRANCH = false;
    private final String U_CHAR = "u";
}
