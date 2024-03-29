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
public enum InstructionBuilder implements Builder {

   add(new MbAdd()),
   addc(new MbAdd()),
   addk(new MbAdd()),
   addkc(new MbAdd()),
   addi(new MbAddi()),
   addic(new MbAddi()),
   addik(new MbAddi()),
   addikc(new MbAddi()),
   imm(new MbImm()),
   and(new MbAnd()),
   andi(new MbAndi()),
   beqi(new MbBeqi()),
   beqid(new MbBeqi()),
   bgei(new MbBgei()),
   bgeid(new MbBgei()),
   bgti(new MbBgti()),
   bgtid(new MbBgti()),
   blei(new MbBlei()),
   bleid(new MbBlei()),
   blti(new MbBlti()),
   bltid(new MbBlti()),
   bnei(new MbBnei()),
   bneid(new MbBnei()),
   br(new MbBr()),
   bra(new MbBr()),
   brd(new MbBr()),
   brad(new MbBr()),
   brld(new MbBr()),
   brald(new MbBr()),
   bri(new MbBri()),
   brai(new MbBri()),
   brid(new MbBri()),
   braid(new MbBri()),
   brlid(new MbBri()),
   bralid(new MbBri()),
   bsrli(new MbBsi()),
   bsrai(new MbBsi()),
   bslli(new MbBsi()),
   cmp(new MbCmp()),
   cmpu(new MbCmp()),
   idiv(new MbIdiv()),
   idivu(new MbIdiv()),
   lbu(new MbLbu()),
   lbui(new MbLbui()),
   lhu(new MbLhu()),
   lhui(new MbLhui()),
   lw(new MbLw()),
   lwi(new MbLwi()),
   mul(new MbMul()),
   or(new MbOr()),
   ori(new MbOri()),
   rsub(new MbRsub()),
   rsubk(new MbRsub()),
   rtsd(new MbRtsd()),
   sb(new MbSb()),
   sbi(new MbSbi()),
   sext16(new MbSext16()),
   sext8(new MbSext8()),
   sh(new MbSh()),
   shi(new MbShi()),
   sra(new MbSra()),
   srl(new MbSrl()),
   sw(new MbSw()),
   swi(new MbSwi()),
   xori(new MbXori());


   /**
    * Constructor
    *
    * @param builder
    */
   private InstructionBuilder(Builder builder) {
      this.builder = builder;
   }



   public Instruction build(TraceData data, MicroBlazeProcessor processor) {
      return builder.build(data, processor);
   }

   // INSTANCE VARIABLES
   private final Builder builder;
}
