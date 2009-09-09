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

package org.ancora.MicroblazeInterpreter.HardwareBlocks;

import org.ancora.MicroblazeInterpreter.HardwareBlocks.InstructionMemory.TraceMemory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ancora.jCommons.Disk;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrador
 */
public class TraceMemoryTest {

    public TraceMemoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        disk = Disk.getDisk();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nextInstruction method, of class TraceMemory.
     */
    @Test
    public void testNextInstruction() {
        System.out.println("nextInstruction");
        File testFile = disk.safeFile(test1filename);
        TraceMemory instance = new TraceMemory(testFile);
        List<String> instructions = new ArrayList<String>();

        String inst = instance.nextInstruction();
        while(inst != null) {
            instructions.add(inst);
            inst =instance.nextInstruction();
        }

        // Check if new call returns null again without raising exceptions
        String nullString = instance.nextInstruction();
        assertTrue(nullString == null);

        // Check if 3 instructions where read
        boolean threeInst = instructions.size() == 3;
        assertTrue(threeInst);

        // Check if all 3 instructions start with "0x"
        for(String line : instructions) {
            assertTrue(line.startsWith("0x"));
            System.out.println(line);
        }
        System.out.println("--------------------------");
    }


    // Definitions
    private Disk disk;
    private String test1filename = "./test/test_trace1.txt";

}