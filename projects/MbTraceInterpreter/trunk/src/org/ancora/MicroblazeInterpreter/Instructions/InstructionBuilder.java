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
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 * Contaisn the instructions supported by this interpreter.
 *
 * <p>To add a new instruction, after creating a class which implements the
 * MbInstruction and the MbBuilder interface, add a new enum which initializes
 * with a default constructor of that class.
 * 
 * 
 * @author Joao Bispo
 */
public enum InstructionBuilder implements MbBuilder {

   add(new MbAdd()),
   addc(new MbAdd()),
   addk(new MbAdd()),
   addkc(new MbAdd());

   /**
    * Constructor
    *
    * @param builder
    */
   private InstructionBuilder(MbBuilder builder) {
      this.builder = builder;
   }



   public MbInstruction build(TraceData data, MicroBlazeProcessor processor) {
      return builder.build(data, processor);
   }

   // INSTANCE VARIABLES
   private final MbBuilder builder;
}
