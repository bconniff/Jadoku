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

/* A Knuthian Dancing Links matrix implementing Algorithm X */
public class DancingLinks<T> {
   private List<Node> row;
   private Node root;
   private Stack<T> result = new Stack<T>();
   private List<T> solution;

   private class Node {
      public final T r;
      public Node prev, next, up, down;
      public Node head;

      public Node(T r) { this.r = r; }
      public Node() { this(null); }
   }

   @SuppressWarnings("unchecked")
   public DancingLinks(int w) {
      row = new ArrayList<Node>(w);

      final Node first = new Node();
      Node curr = first;
      Node prev = curr;

      for (int i = 0; i < w; i++) {
         curr.prev = prev;
         prev.next = curr;
         curr.up = curr;
         curr.down = curr;
         curr.head = curr;
         row.add(curr);
         prev = curr;
         curr = new Node();
      }

      root = curr;

      prev.next = root;
      root.prev = prev;
      root.next = first;
      first.prev = root;
   }

   private Node nextCol() {
      return root.next;
   }

   public void addRow(T r, int[] pos) {
      final Node first = new Node(r);
      Node curr = first;
      Node prev = curr;
      for (int i: pos) {
         final Node x = row.get(i);
         curr.prev = prev;
         prev.next = curr;
         curr.up = x;
         curr.down = x.down;
         curr.head = x.head;
         x.down = curr;
         curr.down.up = curr;
         row.set(i, curr);
         prev = curr;
         curr = new Node(r);
      }
      prev.next = first;
      first.prev = prev;
   }

   private Node getCol() {
      Node result = null;
      int count = -1;
      for (Node head = root.next; head != root; head = head.next) {
         // count nodes in this column
         int c = 0;
         for (Node n = head.down; n != head; n = n.down, c++);

         if (c == 0)
            return head;
         if (count == -1 || c < count) {
            count = c;
            result = head;
         }
      }
      return result;
   }

   private boolean doSolve() {
      final Node head = getCol();

      if (head == null) {
         solution = getSolution();
         return true;
      }

      coverColumn(head);

      for (Node n = head.down; n != head; n = n.down) {
         for (Node x = n.next; x != n; x = x.next)
            coverColumn(x);

         result.push(n.r);

         if (doSolve())
            return true;

         result.pop();

         for (Node x = n.next; x != n; x = x.next)
            uncoverColumn(x);
      }

      uncoverColumn(head);

      return false;
   }

   private void coverColumn(Node n) {
      final Node head = n.head;
      head.prev.next = head.next;
      head.next.prev = head.prev;
      for (Node y = head.down; y != head; y = y.down) {
         for (Node x = y.next; x != y; x = x.next) {
            x.up.down = x.down;
            x.down.up = x.up;
         }
      }
   }

   private void uncoverColumn(Node n) {
      final Node head = n.head;
      head.prev.next = head;
      head.next.prev = head;
      for (Node y = head.down; y != head; y = y.down) {
         for (Node x = y.next; x != y; x = x.next) {
            x.up.down = x;
            x.down.up = x;
         }
      }
   }

   @SuppressWarnings("unchecked")
   private List<T> getSolution() {
      return new ArrayList<T>(result);
   }

   public List<T> solve() {
      row = null;
      doSolve();
      result = null;
      root = null;
      return solution;
   }
}
