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

/* Translates a sudoku puzzle into an instance of the exact cover problem,
 * then uses Dancing Links to solve it */
public class Solver {
   private int[][] solution = null;
   private final long time;
   private final int n, k, xOff, yOff, bOff;

   private class Row {
      public final int x, y, val;

      public Row(int x, int y, int val) {
         this.x = x;
         this.y = y;
         this.val = val;
      }

      public String toString() {
         return "("+x+", "+y+") = "+val;
      }
   }

   public Solver(int k, int[][] vals) {
      final long start = System.currentTimeMillis();

      this.k = k;
      n = k*k;

      final int n2 = n*n;

      xOff = n2;
      yOff = xOff + n2;
      bOff = yOff + n2;

      final DancingLinks<Row> solver = new DancingLinks<Row>(bOff+n2);

      for (int x = 0; x < n; x++) {
         for (int y = 0; y < n; y++) {
            if (0 < vals[x][y] && vals[x][y] <= n)
               addVal(solver, x, y, vals[x][y]-1);
            else
               for (int i = 0; i < n; i++)
                  addVal(solver, x, y, i);
         }
      }

      final List<Row> result = solver.solve();

      if (result != null) {
         solution = new int[n][n];
         for (Row r: result)
            solution[r.x][r.y] = r.val;
      }

      final long stop = System.currentTimeMillis();
      time = stop - start;
   }

   private int bNum(int x, int y) {
      return k*(y/k) + (x/k);
   }

   private void addVal(DancingLinks<Row> s, int x, int y, int val) {
      final int[] data = new int[4];
      data[0] = n*y + x;
      data[1] = xOff + (n*x + val);
      data[2] = yOff + (n*y + val);
      data[3] = bOff + (n*bNum(x,y) + val);
      s.addRow(new Row(x,y,val+1), data);
   }

   public long time() { return time; }
   public int[][] getVals() { return solution; }
   public boolean solved() { return solution != null; }
}
