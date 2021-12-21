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

import java.lang.ref.Cleaner.Cleanable;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote An interface defining the way
 * {@link com.jeffreyorazulike.simpletron.Simpletron} communicates with the
 * memory
 *
 */
public interface Memory extends Cleanable {

    /**
     * Refers to the non changing values of the memory, such as the memory size
     * and min and max words a block in the memory can accept
     */
    enum Constant {
        /**
         * The size of the current memory's implementation
         */
        MEMORY_SIZE,
        /**
         * The maximum word that can be inserted into a memory location
         */
        MAX_WORD,
        /**
         * The minimum word that can be inserted into a memory location
         */
        MIN_WORD;
    }

    /**
     *
     * @param index the position to store the value in the underlying data
     *              structure
     * @param value the value to be stored in the position
     *
     * @throws ArrayIndexOutOfBoundsException if an index less than 0 or greater
     *                                        than the size of the underlying
     *                                        data structure is passed
     */
    void set(int index, int value) throws ArrayIndexOutOfBoundsException;

    /**
     *
     * @param index the index of the value to be returned
     *
     * @return the value at the index
     *
     * @throws ArrayIndexOutOfBoundsException if the index is invalid
     */
    int get(int index) throws ArrayIndexOutOfBoundsException;

    /**
     *
     * @param constant the constant to return
     *
     * @return the value of the constant
     */
    int getConstant(Constant constant);

    @Override
    default void clean() {
        int memory_size = getConstant(Constant.MEMORY_SIZE);
        for (int i = 0; i < memory_size; ++i)
            set(i, 0);
    }
}
