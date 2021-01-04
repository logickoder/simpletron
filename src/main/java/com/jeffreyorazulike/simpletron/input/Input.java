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
package com.jeffreyorazulike.simpletron.input;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote Defines the way the simpletron system receives input frnm a source
 */
public interface Input {

    /**
     *
     * @return the last string returned by {@link #next()}
     */
    String last();

    /**
     *
     * @return the next value from the input source
     */
    String next();

    /**
     *
     * @return the next value from the input source
     */
    default String nextLine() {
        return next();
    }

    ;

    /**
     *
     * @param toReturn should {@link #next} return an end of line if it's reached or
     *                 it should skip it, the default for an {@link #Input} instance
     *                 is true
     */
    default void returnEOL(boolean toReturn) {
    }

    /**
     *
     * @return {@code true} if the end of the current line has been reached
     */
    boolean isEOL();

    /**
     *
     * @return {@code true} if the end of the input has been reached or the
     *         input is closed
     */
    boolean isEOF();

    /**
     *
     * @return the value gotten when trying to retrieve the next value, if the
     *         input doesn't return {@code int} values when called, a random
     *         number is generated
     */
    int nextToken();

    /**
     *
     * @return <p>
     * {@code true} if the input is gotten as it is needed (e.g from a file)
     * </p>
     * <p>
     * {@code false} if not (e.g users input)
     * </p>
     */
    boolean onDemand();
}
