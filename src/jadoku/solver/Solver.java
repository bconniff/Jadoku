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

public class Solver {
   private final long time;
   private Grid solution;
   private int guesses = 0;
   private int badGuesses = 0;

   public Solver(int k, int[][] in) {
      final long startTime = System.currentTimeMillis();

      final int n = k*k;
      final Grid g = new Grid(k);

      for (int x = 0; x < in.length; x++) {
         for (int y = 0; y < in[x].length; y++) {
            int i = in[x][y];
            if (i > 0 && i <= n)
               g.setVal(y, x, i);
         }
      }

      g.simplify();
      solution = solve(g);

      final long endTime = System.currentTimeMillis();
      
      time = endTime - startTime;
   }

   private Grid solve(Grid in) {
      if (in.validate()) {
         if (in.solved()) {
            return in;
         } else {
            final Grid g = new Grid(in);
            final Cell c = g.getFirstUns();
            final int guess = c.getFirstPos();

            guesses++;

            c.setVal(guess);
            g.simplify();

            final Grid ng = solve(g);

            if (ng == null) {
               badGuesses++;

               in.getFirstUns().strike(guess);
               in.simplify();

               return solve(in);
            } else {
               return ng;
            }
         }
      } else {
         return null;
      }
   }

   public int guesses() { return guesses; }
   public int badGuesses() { return badGuesses; }
   public long time() { return time; }

   public boolean solved() {
      return solution != null;
   }

   public int[][] getVals() {
      return (solution == null)
         ? null
         : solution.getVals();
   }

   public String toString() {
      return (solution == null)
         ? "No solution"
         : solution.toString();
   }
}
