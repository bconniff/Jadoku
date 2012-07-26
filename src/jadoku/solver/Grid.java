/* Copyright (c) 2012, Brendan Conniff
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Brendan Conniff nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package jadoku.solver;

import java.util.*;

class Grid {
   private final Zone[] row, col, box;
   private final int k, n;

   private Grid(int k, Grid g) {
      this.k = k;
      n = k*k;

      row = new Zone[n];
      col = new Zone[n];
      box = new Zone[n];

      for (int i = 0; i < n; i++) {
         row[i] = new Zone(k);
         col[i] = new Zone(k);
         box[i] = new Zone(k);

         if (g != null) {
            row[i].addSets(g.row[i].getSets());
            col[i].addSets(g.col[i].getSets());
            box[i].addSets(g.box[i].getSets());
         }
      }

      for (int x = 0; x < n; x++) {
         for (int y = 0; y < n; y++) {
            final int b = x/k + k*(y/k);
            final Cell s = (g == null)
                  ? new Cell(n, row[y], col[x], box[b])
                  : new Cell(g.row[y].getCell(x), row[y], col[x], box[b]);

            row[y].put(x, s);
            col[x].put(y, s);
            box[b].put(x%k + k*(y%k), s);
         }
      }
   }

   public Grid(int k) {
      this(k, null);
   }

   public Grid(Grid g) {
      this(g.k, g);
   }

   public void setVal(int y, int x, int val) {
      col[x].setVal(y, val);
   }

   private boolean validate(Zone[] zone) {
      for (Zone z: zone)
         if (!z.validate())
            return false;
      return true;
   }

   public boolean validate() {
      return validate(row) && validate(col) && validate(box);
   }

   public boolean solved() {
      return getFirstUns() == null;
   }

   public String toString() {
      final String div = getDivider();

      int i = 0;
      String ret = "";
      boolean first = true;

      for (Zone z: row) {
         if (i != 0)
            ret += "\n";
         ret += z.toString();
         i++;
         if (i < n && i % k == 0)
            ret += div;
      }

      return ret;
   }

   public int[][] getVals() {
      final int[][] ret = new int[n][n];

      for (int y = 0; y < n; y++) {
         final Zone r = row[y];
         for (int x = 0; x < n; x++) {
            ret[x][y] = r.getCell(x).getVal();
         }
      }

      return ret;
   }

   private static String stringTimes(String s, int n) {
      String ret = "";
      for (int i = 0; i < n; i++)
         ret += s;
      return ret;
   }

   private String getDivider() {
      final String dashes = stringTimes("-", 2*k);
      return "\n"+dashes+"+-"+dashes+"+"+dashes;
   }

   private boolean simplify(Zone[] zone) {
      for (Zone z: zone)
         if (z.simplify())
            return true;
      return false;
   }

   public void simplify() {
      while (simplify(box) || simplify(row) || simplify(col));
   }

   public Cell getFirstUns() {
      for (Zone z: row) {
         final Cell ret = z.getFirstUns();
         if (ret != null)
            return ret;
      }

      return null;
   }
}
