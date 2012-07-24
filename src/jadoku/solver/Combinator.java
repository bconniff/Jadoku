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

/* Combination algorithm by Donald Knuth.
 * Adapted from C by Brendan Conniff. */

public class Combinator {
   private final int n, k;
   private final int[] c, a;
   private int j, x;
   private boolean cont;

   public Combinator(int n, int k) {
      this.n = n;
      this.k = k;

      c = new int[k+3];
      a = new int[k];

      reset();
   }

   public void reset() {
      j = 1;
      x = 0;

      cont = true;

      for (int i = 1; i <= k; i++)
         c[i] = i;

      c[k+1] = n+1;
      c[k+2] = 0;

      j = k;
   }

   /* Same logic as Knuth's original C, but Java-ified.
    * Now without gotos, multiple returns, breaks, or continues! */
   public int[] next() {
      for (int i = k; i >= 1; i--)
         a[i-1] = c[i]-1;

      if (j > 0) {
         x = j + 1;
         c[j--] = x;
      } else if (c[1] + 1 < c[2]) {
         c[1]++;
      } else {
         j = 1;

         do {
            c[j] = j;
            x = c[++j] + 1;
         } while (x == c[j+1]);

         if (j > k)
            cont = false;
         else
            c[j--] = x;
      }

      return a;
   }

   public static int toBits(int[] comb) {
      int v = 0;
      for (int i: comb)
         v += 1 << i-1;
      return v;
   }

   public boolean hasNext() {
      return cont;
   }
}
