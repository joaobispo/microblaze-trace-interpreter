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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 *
 * @author Joao Bispo
 */
public class TraceMemory {

    public TraceMemory(File mbTrace) {
        traceFile = mbTrace;
        hasInitialized = false;
    }

    /**
     * Opens the file and loads it to the BufferedReader
     */
    private void initialize() {
        FileInputStream stream = null;
        InputStreamReader streamReader = null;

        try {
            // Try to read the contents of the file into the StringBuilder
            stream = new FileInputStream(traceFile);
        } catch (FileNotFoundException ex) {
            console.warn("initialize: FileNotFoundException while trying to open " +
                    "file! (" + traceFile.getAbsolutePath() + ")");
            Logger.getLogger(TraceMemory.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            streamReader = new InputStreamReader(stream, charSet);
        } catch (UnsupportedEncodingException ex) {
            console.warn("initialize: UnsupportedEncodingException while " +
                    "trying to use character set ("+charSet+")");
            Logger.getLogger(TraceMemory.class.getName()).log(Level.SEVERE, null, ex);
        }

        reader = new BufferedReader(streamReader);
        hasInitialized = true;
    }

    /**
     * Returns the next line which qualifies as a trace instruction. A String
     * is considered as a trace instruction if it starts with "0x".
     *
     * <p>The trace file is read line by line, instead of being parsed
     * completely (ex.: with JavaCC). This was done to enable to support for
     * execution of very large traces (ex.: > 50Mb), without loading the entire
     * trace in memory.
     * <br>Because this is trace execution, the file is always read as a
     * stream of instructions, and this can be done.
     * 
     * @return the next String which qualifies as an instruction, or null if
     * the end of the stream has been reached
     */
    public String nextInstruction() {
        // Check if memory was initialized
        if(!hasInitialized) {
            initialize();
        }
        
        // While there are lines and a trace instruction was not found, loop.
        //boolean hasLines = true;
        //boolean isTrace = false;
        String line = null;
        while(true) {
            // Read next line
            line = readLine(reader, traceFile);

            // Check if end of stream has arrived.
            if (line == null) {
                return null;
            }

            // Check if current line is a trace instruction
            if (line.startsWith(TRACE_PREFIX)) {
                return line;
            }
        }
    }


    /**
     * Reads a line from a BufferedReader
     *
     * @param reader a buffered reader
     * @param file the file being read by the buffered reader
     * @return A String containing the contents of the line, or null if
     * the end of the stream has been reached or an exception was raised.
     */
    private String readLine(BufferedReader reader, File file) {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException ex) {
            console.warn("readLine: IOException while trying to read " +
                    "buffered file! (" + file.getAbsolutePath() + ")");
            Logger.getLogger(TraceMemory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return line;
    }

    

    // INSTANCE VARIABLES

    // File Input
    private final File traceFile;
    private BufferedReader reader;

    // State
    private boolean hasInitialized;

    // Utilities
    private Console console = DefaultConsole.getConsole();

    // Definitions
    private final String TRACE_PREFIX = "0x";
    private final String charSet = "UTF-8";
}
