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
//public class InstructionParser implements TraceData {

    /*
   public InstructionParser() {
      opName = null;
      registers = emptyRegisters;
      //rD = null;
      //rA = null;
      //rB = null;
      imm = null;
   }
     */

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
        Short imm = null;

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
    * Parse its information, storing the values in his instance variables.
    *
    * <p>This method assumes that the instruction begins that the 13th
    * character (10 chars for instruction address + 2 whitespaces).
    *
    * @param traceInstruction a trace instruction
    */
    /*
   public void parseInstructionLocal(String traceInstruction) {
      cleanVariables();

      String instruction;
      // Cut the memory address
      instruction = traceInstruction.substring(12);
      
      // Find first space. This will cut the operation name
      int endIndex = instruction.indexOf(' ');
      opName = instruction.substring(0, endIndex);

      // Cut operation name
      instruction = instruction.substring(endIndex);

      // Extract the other args
      String[] results = new String[MAX_INST_ARGS];
      int numArgs = extractsArgs(instruction, results);

      // Fill the variables
      extractImmediate(results, numArgs);

      // DEBUG
      // Test if parsing is correct
      //if(!instruction.trim().equals(this.toString().trim())) {
      //   System.out.println("parseInstruction: Problem. Instruction ("+
      //           instruction.trim()+") different from parsing ("+
      //           this.toStringVariables().trim()+")");
      //}

      //System.out.println("opName:"+opName);
      //System.out.println(instruction);
   }
     */

   /**
    * Extracts the arguments from an instruction string.
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

      // Old Assumptions
      //    * <p>The method assumes that the trace instruction:
      //    * <br>Starts with "0x", with the address immediately following;
      //    * <br>Has a whitespace immediately after the address;

      // If no address is      * found,      returns           null.
      //, or null * if an address could not be extracted

      /*
      final String prefix = "0x";

      String workString = traceInstruction.substring(prefix.length());
      // Find the first whitespace
      int whitespaceIndex = 0;
      for(int i=0; i<workString.length(); i++) {
         if(workString.charAt(i))
      }
       */
      final int beginIndex = "0x".length();
      final int endIndex = "0x".length() + 8;

      return traceInstruction.substring(beginIndex, endIndex);
   }

   /**
    * Extracts the memory address from the given instruction. If the address is
    * not valid, returns -1.
    * 
    * <p>A long is used because Java as no support for unsigned types. 
    * Therefore, a long is used to store an unsigned integer.
    * 
    * 
    * @param traceInstruction a trace instruction
    * @return a long with the value of the memory address, or -1 if an address
    * is not valid
    */
   /*
   public static long getMemoryAddressDecoded(String traceInstruction) {

      String address = getMemoryAddress(traceInstruction);
      long decodedAddress;
      try{
         decodedAddress = Long.valueOf(address, 16);
      } catch(NumberFormatException ex) {
         console.warn("getMemoryAddressDecoded: Could not decode hexadecimal " +
                 "string ("+address+")");
         decodedAddress = -1;
      }

      return decodedAddress;
   }
    */

   /**
    * Modifies the given array so it only constains registers, extracts and
    * returns an Immediate value, if found.
    * Fills this class variables.
    * 
    * @param results a string array with the values of the variables
    * @param numArgs number of valid arguments in the array
    * @return The value of Immediate, or null if not found.
    */
    private static Short extractImmediate(String[] results, int numArgs) {
        Short imm = null;

        // Immediates if present in an instruction are always the last variable.
        // Check if the first character of the last variable is a letter.
        // If yes, it's a register. Else, it's an immediate.
      char firstChar = results[numArgs-1].charAt(0);
      boolean firstCharIsLetter = Character.isLetter(firstChar);

      if (!firstCharIsLetter) {
         // Assign immediate value
         imm = Short.valueOf(results[numArgs - 1]);

         // Invalidate value so the array can be assigned to TraceData object.
         results[numArgs-1] = null;
      }

      return imm;   
   }

   /**
    * Cleans the variables
    */
    /*
   private void cleanVariables() {
      opName = null;
      registers = emptyRegisters;
      //rD = null;
      //rA = null;
      //rB = null;
      imm = null;
   }
     */

    /*
   public String getOpName() {
      return opName;
   }

   public String getRd() {
      return registers[RD_INDEX];
   }

   public String getRa() {
      return registers[RA_INDEX];
   }

   public String getRb() {
      return registers[RB_INDEX];
   }

   public Short getImm() {
      return imm;
   }
*/
    /*
   public String toStringVariables() {
      int capacity = 100;
      StringBuilder builder = new StringBuilder(capacity);
*/
      //builder.append(opName+" ");
      //int currentIndex = 0;
      //String register = registers[0];

      /*
      while(register != null) {
         System.out.println(registers[currentIndex]);
         currentIndex++;

         if(currentIndex < registers.length) {
            register = registers[currentIndex];
         }
      }
       */

/*
      // Print registers until a null is found
      for(int i=0; i<registers.length; i++) {
         if(registers[i] == null) {
            break;
         }

         // Check if its not the last argument
         if(i < MAX_INST_ARGS-1) {
            // Check if this is the last argument
            if(registers[i+1] == null && imm == null) {
               builder.append(registers[i]);
            }
            else {
               builder.append(registers[i]+", ");
            }
         }
         else {
            builder.append(registers[i]);
         }

      }

      if(imm != null) {
          builder.append(imm);
         
      }

      return builder.toString();
   }

*/

   //INSTANCE VARIABLES
   
   // Definitions
   //private static final int MAX_INST_ARGS = 3;
   private static final char COMMA = ',';

   //private static final int RD_INDEX = 0;
   //private static final int RA_INDEX = 1;
   //private static final int RB_INDEX = 2;


   // State
   //private String opName;
   //private String[] registers;
   //String rD;
   //String rA;
   //String rB;
   //private Short imm;
   private static final String[] emptyRegisters = new String[TraceData.MAX_REGISTERS];





   //private static Console console = DefaultConsole.getConsole();

}
