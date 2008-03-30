/* Copyright (c) 2008 Sascha Kohlmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos.medline;

/**
 * The word list defines terms which structure a medline abstract in something
 * like <em>BACKGROUND:<em>, <em>PURPOSE:</em> or evan a <em>CONCLUSION:</em>.
 * The words in the list are uppercase, like there appearance in medline
 * abstract texts.
 * @author Sascha Kohlmann
 */
public class MedlineAbstractStructureWords {

    /** List of medline structure words. */
    @SuppressWarnings("nls")
    public final static String[] STRUCTURE_WORDS_UPPER =
        {"BACKGROUND:", "PURPOSE:", "OBJECTIVES:", "OBJECTIVE:", "PATIENTS:",
         "INTRODUCTION:", "AIM:", "AIMS:", "METHODS:", "METHOD:", "RESULT:",
         "RESULTS:", "CONCLUSIONS:", "CONCLUSION:"};
}
