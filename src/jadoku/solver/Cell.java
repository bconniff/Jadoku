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

class Cell {
   private final List<Zone> zones = new ArrayList<Zone>();
   private Set<Integer> pos = new HashSet<Integer>();
   private int val = -1;

   private Cell(Zone... zon) {
      for (Zone z: zon)
         zones.add(z);
   }

   public Cell(Cell in, Zone... zon) {
      this(zon);

      if (in.pos == null) {
         val = in.val;
         pos = null;
      } else {
         for (int i: in.pos)
            pos.add(i);
      }
   }

   public Cell(int n, Zone... zon) {
      this(zon);

      for (int i = 1; i <= n; i++)
         pos.add(i);
   }

   public boolean solved() {
      return val != -1;
   }

   public void setVal(int v) {
      val = v;
      pos = null;

      for (Zone z: zones)
         z.strike(this);
   }

   public int getVal() {
      return val;
   }

   public void strike(int v) {
      if (pos != null) {
         pos.remove(v);

         if (pos.size() == 1)
            setVal(pos.iterator().next());
      }
   }

   public Set<Integer> getPos() {
      return pos;
   }

   public int numPos() {
      return (pos == null)
         ? 0
         : pos.size();
   }

   public Integer getFirstPos() {
      Integer ret = null;
      for (int i: pos)
         if (ret == null || i < ret)
            ret = i;
      return ret;
   }

   public String toString() {
      return (val == -1) ? "." : Integer.toString(val);
   }
}
