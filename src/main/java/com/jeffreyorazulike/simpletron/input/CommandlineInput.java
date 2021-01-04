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

import java.util.Scanner;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote Defines a way an input is gotten to the simpletron system
 */
public class CommandlineInput implements Input {

    private static final Scanner keyboard = new Scanner(System.in);

    private String last;

    @Override
    public String next() {
        String value;
        do
            value = keyboard.nextLine();
        while (value.isBlank());
        return last = value;
    }

    @Override
    public String last() {
        return last;
    }

    @Override
    public boolean isEOL() {
        return false;
    }

    @Override
    public boolean isEOF() {
        return false;
    }

    @Override
    public int nextToken() {
        return (int) Math.random() * 10;
    }

    @Override
    public boolean onDemand() {
        return false;
    }
}
