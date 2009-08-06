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

package org.ancora.MicroblazeInterpreter.Parser;

import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;


/**
 * From a String with a MibroBlaze trace instruction, it can do two things:
 *
 * <p>1)Extract the memory address of the instruction;
 * <br>2)Parse its information, storing the values in his instance variables.
 *
 * <p>The parsing of the instructions are heavily dependent of the format of
 * the instruction (number of spaces, position of characters...). This was a
 * design decision, to speed-up the parsing.
 *
 * @author Joao Bispo
 */
public class InstructionParser {

    /**
     * Parse its information, storing the values in a new TraceData object.
     *
     * <p>This method assumes that the instruction begins that the 13th
     * character (10 chars for instruction address + 2 whitespaces).
     *
     * @param traceInstruction a trace instruction
     * @return a TraceData object with the parsed information
     */
    public static TraceData parseInstruction(String traceInstruction) {
        String instruction;
        String opName = null;
        String[] results = new String[TraceData.MAX_REGISTERS];
        Integer imm = null;

        // Cut the memory address
        instruction = traceInstruction.substring(12);

        // Find first space. This will cut the operation name
        int endIndex = instruction.indexOf(' ');
        opName = instruction.substring(0, endIndex);

        // Cut operation name
        instruction = instruction.substring(endIndex);

        // Extract the other args
        int numArgs = extractsArgs(instruction, results);

        // Fill the variables
        imm = extractImmediate(results, numArgs);

        return new SimpleTraceData(opName, results, imm);
    }
   

   /**
    * Extracts the arguments from an instruction string and stores them in the
    * given String array. Returns the number of arguments found.
    *
    * <p>Assumes that the arguments are separated by commas.
    * @param instruction instruction to parse
    * @param results String array to hold the parsed arguments
    * @return the number of arguments found
    */
   private static int extractsArgs(String instruction, String[] results) {
      int numArgs = 0;

      // Check the first comma
      int beginIndex = 0;
      int indexOfComma = instruction.indexOf(COMMA);

      boolean hasCommas = indexOfComma != -1;
      while (hasCommas) {
         // Extract Argument
         String otherArg = instruction.substring(beginIndex, indexOfComma);
         results[numArgs] = otherArg.trim();
         numArgs++;

         // Update Indexes
         beginIndex = indexOfComma + 1;
         indexOfComma = instruction.indexOf(COMMA, beginIndex);

         hasCommas = indexOfComma != -1;
      }

      // Extract last argument
      String otherArg = instruction.substring(beginIndex);
      results[numArgs] = otherArg.trim();
      numArgs++;

      
      return numArgs;
   }

   /**
    * Extracts the memory address from the given instruction. 
    *
    * <p>The method assumes that the address are at the beginning of the String
    * and that they start with "0x" and have 8 hexadecimal digits.
    *
    * @param traceInstruction a trace instruction
    * @return a string with an hexadecimal representation of the memory
    */
   public static String getMemoryAddress(String traceInstruction) {
      final int beginIndex = "0x".length();
      final int endIndex = "0x".length() + 8;

      return traceInstruction.substring(beginIndex, endIndex);
   }

   /**
    * Modifies the given array so it only constains registers, extracts and
    * returns an Immediate value, if found.
    * Fills this class variables.
    * 
    * @param results a string array with the values of the variables
    * @param numArgs number of valid arguments in the array
    * @return The value of Immediate, or null if not found.
    */
    private static Integer extractImmediate(String[] results, int numArgs) {
        Integer imm = null;

        // Immediates if present in an instruction are always the last variable.
        // Check if the first character of the last variable is a letter.
        // If yes, it's a register. Else, it's an immediate.
      char firstChar = results[numArgs-1].charAt(0);
      boolean firstCharIsLetter = Character.isLetter(firstChar);

      if (!firstCharIsLetter) {
         // Assign immediate value
         imm = Integer.valueOf(results[numArgs - 1]);

         // Invalidate value so the array can be assigned to TraceData object.
         results[numArgs-1] = null;
      }

      return imm;   
   }

    /**
     * Parses a string with a general purpose register. The string should
     * be in the format "r<number>".
     *
     * @param register
     * @return
     */
    public static int parseRegister(String register) {
        //String tempRegister = "";
        //try {
         String   tempRegister = register.substring(BEGIN_INDEX_REG);
        //} catch (NullPointerException ex) {
        //    System.out.println("parserRegister: Could not parse supposed " +
        //            "register string (" + register + ").");
        //    ex.printStackTrace();
        //    System.exit(1);
        //}
        
        int returnReg = 0;

        try {
            returnReg = Integer.parseInt(tempRegister);
        } catch (NumberFormatException ex) {
            console.warn("parseRegister: Could not parse the register ("+
                    register+"). Returning 0.");
        }

        return returnReg;
    }

    //INSTANCE VARIABLES

    // Definitions
    private static final char COMMA = ',';
    private static final int BEGIN_INDEX_REG = 1;
    private static final Console console = DefaultConsole.getConsole();
}
