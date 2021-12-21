/*
 * Copyright (C) 2021 Jeffrey Orazulike
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jeffreyorazulike.simpletron;

import com.jeffreyorazulike.simpletron.cpu.CPU;
import com.jeffreyorazulike.simpletron.cpu.CPUImpl;
import com.jeffreyorazulike.simpletron.display.CommandlineDisplay;
import com.jeffreyorazulike.simpletron.display.Display;
import com.jeffreyorazulike.simpletron.display.FileDisplay;
import com.jeffreyorazulike.simpletron.input.CommandlineInput;
import com.jeffreyorazulike.simpletron.input.FileInput;
import com.jeffreyorazulike.simpletron.input.Input;
import com.jeffreyorazulike.simpletron.memory.Memory;
import com.jeffreyorazulike.simpletron.memory.MemoryImpl;
import com.jeffreyorazulike.simpletron.translator.Compiler;
import com.jeffreyorazulike.simpletron.translator.Interpreter;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Jeffrey Orazulike
 */
public class Launch {

    /**
     * Starts the simpletron system
     *
     * @param args the command line arguments
     *
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Input command = new CommandlineInput();
        Display display = new CommandlineDisplay();
        Memory memory = new MemoryImpl();
        CPU cpu = new CPUImpl();
        Simpletron simpletron = new Simpletron(display, command, cpu, memory);

        while (true) {
            display.line("1\tTo launch Simpletron");
            display.line("2\tTo compile a Simpletron Basic Language Program");
            display.line("3\tTo interprete a Simpletron Basic Language Program");
            display.line("4\tExit");
            switch (Integer.parseInt(command.next())) {
                case 1:
                    display.line("\t1\tTo enter instruction by yourself");
                    display.line("\t2\tTo enter instruction from a file");
                    switch (Integer.parseInt(command.next())) {
                        case 1:
                            simpletron.runSystem(command);
                            break;
                        default:
                            display.show("\t\t\tEnter the path to the file to load the instructions from %s ",
                                    Display.Decorator.INPUT);
                            checkFileAndExecute(new File(command.next()), file -> {
                                try {
                                    simpletron.runSystem(new FileInput(file.getCanonicalPath()));
                                } catch (IOException iOException) {
                                    iOException.printStackTrace();
                                }
                            });
                            break;
                    }
                    break;
                case 2:
                    display.show("\t\tEnter the path to the sbl program %s ", Display.Decorator.INPUT);
                    checkFileAndExecute(new File(command.next()), (file) -> {
                        try {
                            String path = file.getCanonicalPath();
                            StringBuilder newPath = new StringBuilder(path.length());
                            String splits[] = path.split("\\.");
                            newPath.append(splits[0]);
                            if (splits.length > 1)
                                for (int i = 1; i < splits.length - 1; i++)
                                    newPath.append(splits[i]);

                            new Compiler(simpletron).run(path,
                                    Optional.of(new FileDisplay(newPath.append(".sml").toString())));
                            display.line("%s %s %s %s", Display.Decorator.MESSAGE,
                                    "Program compiled successfully, the file is at", newPath,
                                    Display.Decorator.MESSAGE);
                        } catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                    });
                    break;
                case 3:
                    display.show("\t\tEnter the path to the sbl program %s ", Display.Decorator.INPUT);
                    checkFileAndExecute(new File(command.next()), (file) -> {
                        try {
                            simpletron.clean();
                            new Interpreter(simpletron).run(file.getCanonicalPath(), Optional.empty());
                            display.line("%s %s %s", Display.Decorator.MESSAGE, "Program interpreted successfully",
                                    Display.Decorator.MESSAGE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case 4:
                    simpletron.dump();
                    return;
            }
            display.line("");
        }
    }

    private static void checkFileAndExecute(File file, Consumer<File> executor) {
        if (file.exists() & file.isFile() & file.canExecute() & file.canRead() & file.canWrite())
            executor.accept(file);
    }
}
