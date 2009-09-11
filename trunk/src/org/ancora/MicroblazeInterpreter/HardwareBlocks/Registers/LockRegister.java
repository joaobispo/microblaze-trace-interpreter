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

package org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers;

import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Register for holding values of the instruction IMM.
 *
 * <p>Everytime a Imm instruction is executed, this register takes the value
 * of Imm and goes to the “Imm” state. In the next instruction, it goes to the
 * “Locked” state. If the instruction is of type B, it will use the value of
 * LockRegister. In the next instructions, the state goes to “Unlocked”
 * and the value is not used.
 * 
 * @author Joao
 */
public class LockRegister {

    public LockRegister() {
        state = lockState.unlocked;
    }



    /**
     * Sets the value for a TypeB instruction.
     *
     * @param immediate
     */
    public void setImediate(int immediate) {
        this.immediate = immediate;
        state = lockState.imm;
    }

    /**
     * @return true, if register is in state locked.
     */
    public boolean isLocked() {
        if (state.equals(lockState.locked)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * If the register is locked, returns a word with the given immediate as the
     * bottom-half, and the locked immediate as the upper-half. Else, returns
     * the given immediate unaltered.
     * 
     * @param imm
     * @return
     */
    public int processImmediate(int imm) {
        if(isLocked()) {
            int upperHalf = this.immediate;
            return BitOperations.writeBits(16, 16, upperHalf, imm);
        }
        else {
            return imm;
        }
    }

    /**
     * Returns the stored value, but only if is in locked state.
     * @return the stored value if is in locked state, 0 otherwise.
     */
    /*
    public int getImmediate() {
        if(state == lockState.locked) {
            return immediate;
        }
        else {
            console.warn("getImmediate: lockRegisters is not in locked state" +
                    "("+state+")! Returning 0.");
            return 0;
        }
    }
     */

    /**
     * Advances the state machine.
     */
    public void step() {
        switch(state) {
            case imm:
                state = lockState.locked;
                break;
            case locked:
                state = lockState.unlocked;
                break;
            case unlocked:
                break;
        }
    }

    enum lockState {
        imm,
        locked,
        unlocked;
    }

    // INSTANCE VARIABLES
    // State
    private lockState state;
    private int immediate;

    // Utilities
    private final Console console = DefaultConsole.getConsole();
}
