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

import jadoku.solver.SudokuReader;
import jadoku.solver.Solver;

public class Cli {
   public static void main(String[] args) {
      final int k = 3;
      final int n = k*k;
      boolean first = true;
      long time = 0;
      for (String a: args) {
         try {
            System.out.println("Reading from "+a);
            int i = 1;
            SudokuReader r = new SudokuReader(k, a);
            while (r.hasNext()) {
               System.out.print("   Puzzle #"+(i++)+": ");
               Solver s = new Solver(k, r.next());
               final long t = s.time();
               time += t;
               System.out.print(t+" ms");
               if (s.solved()) {
                  final int[][] vals = s.getVals();
                  System.out.println();
                  for (int y = 0; y < n; y++) {
                     System.out.print("      ");
                     for (int x = 0; x < n; x++)
                        System.out.print(vals[x][y]);
                     System.out.println();
                  }
               } else {
                  System.out.println(", no solution!");
               }
            }
            r.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      System.out.println("Total time: "+time+" ms");
   }
}
