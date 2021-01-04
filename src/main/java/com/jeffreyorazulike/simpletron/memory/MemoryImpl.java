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
package com.jeffreyorazulike.simpletron.memory;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote A concrete implementation of the {@link Memory} interface having
 * {@code 1000} blocks of memory
 */
public class MemoryImpl implements Memory {

    /**
     * Represents the memory
     */
    private final int memory[];

    private final int MEMORY_SIZE = 1000;
    private final int MAX_WORD = MEMORY_SIZE * 100 - 1;
    private final int MIN_WORD = MAX_WORD * -1;

    public MemoryImpl() {
        memory = new int[MEMORY_SIZE];
    }

    @Override
    public void set(int index, int value) throws ArrayIndexOutOfBoundsException {
        memory[index] = value;
    }

    @Override
    public int get(int index) throws ArrayIndexOutOfBoundsException {
        return memory[index];
    }

    @Override
    public int getConstant(Constant constant) {
        switch (constant) {
            case MEMORY_SIZE:
                return MEMORY_SIZE;
            case MAX_WORD:
                return MAX_WORD;
            case MIN_WORD:
                return MIN_WORD;
            default:
                throw new AssertionError(constant.name());
        }
    }
}
