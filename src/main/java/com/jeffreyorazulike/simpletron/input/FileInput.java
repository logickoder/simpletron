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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 *
 * @author Jeffrey Orazulike
 * @apiNote Receives input from a file to the simpletron system
 */
public class FileInput implements Input {

    private final StreamTokenizer tokens;
    private int nextToken;
    private String last;
    private boolean eolIsSignificant = true;

    public FileInput(String path) throws IOException {
        tokens = new StreamTokenizer(new BufferedReader(new FileReader(path)));
        tokens.eolIsSignificant(eolIsSignificant);
        tokens.wordChars(32, 255);
    }

    @Override
    public String next() {
        try {
            nextToken = tokens.nextToken();
        } catch (IOException ignoreIOException) {
        }

        switch (nextToken) {
            case StreamTokenizer.TT_WORD:
                return last = tokens.sval;
            case StreamTokenizer.TT_NUMBER:
                return last = String.valueOf(Math.round(tokens.nval));
            default:
                return "";
        }
    }

    @Override
    public String nextLine() {
        tokens.eolIsSignificant(true);

        StringBuilder string = new StringBuilder();
        do
            string.append(next()).append(" ");
        while (!(isEOL() || isEOF()));
        returnEOL(eolIsSignificant);

        return last = string.toString();
    }

    @Override
    public String last() {
        return last;
    }

    @Override
    public void returnEOL(boolean toReturn) {
        tokens.eolIsSignificant(eolIsSignificant = toReturn);
    }

    @Override
    public boolean isEOL() {
        return nextToken == StreamTokenizer.TT_EOL;
    }

    @Override
    public boolean isEOF() {
        return nextToken == StreamTokenizer.TT_EOF;
    }

    @Override
    public int nextToken() {
        return nextToken;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
