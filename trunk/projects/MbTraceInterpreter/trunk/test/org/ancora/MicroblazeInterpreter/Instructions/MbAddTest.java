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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.ancora.MicroblazeInterpreter.Parser.TraceData;
import org.ancora.MicroblazeInterpreter.Parser.SimpleTraceData;

/**
 *
 * @author Administrador
 */
public class MbAddTest {

    public MbAddTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class MbAdd.
     */
    @Test
    public void testRun() {
        //System.out.println("run");
        //MbAdd instance = null;
        //instance.run();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    @Test
    public void testConstructorAddck() {
        String opName;
        String[] regs = {"r3", "r1", "r2"};
        Integer imm = null;

        opName = "addck";
        TraceData data = new SimpleTraceData(opName, regs, imm);

        MbAdd add = new MbAdd(data);

        assertTrue(add.iscBit());
        assertTrue(add.iskBit());
        assertTrue(add.getRegA() == 1);
        assertTrue(add.getRegB() == 2);
        assertTrue(add.getRegD() == 3);
    }

    @Test
    public void testConstructorAddc() {
        String opName;
        String[] regs = {"r3", "r1", "r2"};
        Integer imm = null;

        opName = "addc";
        TraceData data = new SimpleTraceData(opName, regs, imm);

        MbAdd add = new MbAdd(data);

        assertTrue(add.iscBit());
        assertTrue(!add.iskBit());
        assertTrue(add.getRegA() == 1);
        assertTrue(add.getRegB() == 2);
        assertTrue(add.getRegD() == 3);
    }

    @Test
    public void testConstructorAddk() {
        String opName;
        String[] regs = {"r3", "r1", "r2"};
        Integer imm = null;

        opName = "addk";
        TraceData data = new SimpleTraceData(opName, regs, imm);

        MbAdd add = new MbAdd(data);

        assertTrue(!add.iscBit());
        assertTrue(add.iskBit());
        assertTrue(add.getRegA() == 1);
        assertTrue(add.getRegB() == 2);
        assertTrue(add.getRegD() == 3);
    }

    @Test
    public void testConstructorAdd() {
        String opName;
        String[] regs = {"r3", "r1", "r2"};
        Integer imm = null;

        opName = "add";
        TraceData data = new SimpleTraceData(opName, regs, imm);

        MbAdd add = new MbAdd(data);

        assertTrue(!add.iscBit());
        assertTrue(!add.iskBit());
        assertTrue(add.getRegA() == 1);
        assertTrue(add.getRegB() == 2);
        assertTrue(add.getRegD() == 3);
    }


}