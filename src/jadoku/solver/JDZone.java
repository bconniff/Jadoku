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

public class JDZone {
   private final JDCell[] cell;
   private final Set<JDCell> unsolved = new HashSet<JDCell>();
   private final Set<Integer> sets = new HashSet<Integer>();
   private final int k, n;

   public JDZone(int k) {
      this.k = k;
      this.n = k*k;
      cell = new JDCell[k*k];
   }

   public JDCell getFirstUns() {
      if (unsolved.size() > 0)
         for (JDCell c: cell)
            if (c.getVal() == -1)
               return c;
      return null;
   }

   public void put(int i, JDCell c) {
      cell[i] = c;
      if (!c.solved())
         unsolved.add(c);
   }

   public void strike(JDCell c) {
      unsolved.remove(c);
      final int v = c.getVal();
      for (JDCell x: cell)
         x.strike(v);
   }

   private boolean findSets(int size) {
      final JDCell[] uns = unsolved.toArray(new JDCell[0]);
      final Combinator c = new Combinator(uns.length, size);
      final Set<Integer> pos = new HashSet<Integer>();
      final Set<JDCell> cs = new HashSet<JDCell>();
      final Set<JDCell> ncs = new HashSet<JDCell>();

      while (c.hasNext()) {
         final int[] idx = c.next();
         pos.clear();
         cs.clear();

         ncs.addAll(unsolved);

         for (int i: idx) {
            pos.addAll(uns[i].getPos());
            cs.add(uns[i]);
            ncs.remove(uns[i]);
         }

         if (cs.size() == pos.size()) {
            final int b = Combinator.toBits(idx);
            if (sets.contains(b))
               continue;
            sets.add(b);

            for (JDCell x: ncs)
               for (int i: pos)
                  x.strike(i);
            return true;
         }
      }
      return false;
   }

   public boolean simplify() {
      if (findSingletons())
         return true;
      for (int i = 2; i < unsolved.size()-1; i++)
         if (findSets(i))
            return true;
      return false;
   }

   public boolean findSingletons() {
      final Map<Integer,List<JDCell>> m = new HashMap<Integer,List<JDCell>>();

      for (JDCell c: unsolved) {
         if (c.getVal() == -1) {
            for (int i: c.getPos()) {
               List<JDCell> l = m.get(i);
               if (l == null) {
                  l = new ArrayList<JDCell>();
                  m.put(i, l);
               }
               l.add(c);
            }
         }
      }

      for (int i: m.keySet()) {
         final List<JDCell> l = m.get(i);
         if (l != null && l.size() == 1) {
            l.get(0).setVal(i);
            return true;
         }
      }

      return false;
   }

   public void setVal(int i, int val) {
      cell[i].setVal(val);
   }

   public boolean solved() {
      return unsolved.isEmpty();
   }

   public boolean validate() {
      final Set<Integer> s = new HashSet<Integer>();

      for (JDCell c: cell) {
         final int v = c.getVal();
         if (v != -1 && s.contains(v))
            return false;
         s.add(v);
      }

      return true;
   }

   public void addSets(Set<Integer> i) { sets.addAll(i); }
   public Set<Integer> getSets() { return sets; }
   public JDCell getCell(int i) { return cell[i]; }

   public String toString() {
      String ret = "";
      int i = 0;

      for (JDCell c: cell) {
         if (i != 0) {
            ret += " ";
         }
         ret += c.toString();
         i++;
         if (i < n && i % k == 0) {
            ret += " |";
         }
      }

      return ret;
   }
}
