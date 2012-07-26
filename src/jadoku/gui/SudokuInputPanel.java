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

package jadoku.gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

class SudokuInputPanel extends JPanel {
   private final int n, k;
   private final int[][] vals;
   private final NumberField[][] nums;

   private class SudokuInputBox extends JPanel {
      private Border getBorder(int i, int w, Color c) {
         final int t = (i < k) ? 0 : w;
         final int l = (i % k == 0) ? 0 : w;
         final int r = ((i+1) % k == 0) ? 0 : w;
         final int b = (i > k*(k-1)-1) ? 0 : w;

         return BorderFactory.createMatteBorder(t,l,b,r,c);
      }

      public SudokuInputBox(int i) {
         super();
         final int imk = (i%k)*k, idk = (i/k)*k;

         setLayout(new GridLayout(k,k));
         setBorder(getBorder(i, 1, Color.black));

         for (int j = 0; j < n; j++) {
            final NumberField f = new NumberField(n);
            final int x = imk + (j%k), y = idk + (j/k);

            f.setBorder(getBorder(j, 1, Color.lightGray));
            f.setBackground(Color.white);

            f.getDocument().addDocumentListener(new DocumentListener() {
               public void insertUpdate(DocumentEvent e) { update(); }
               public void removeUpdate(DocumentEvent e) { update(); }
               public void changedUpdate(DocumentEvent e) { update(); }
               public void update() { vals[x][y] = f.getVal(); }
            });

            add(f);
            nums[x][y] = f;
         }
      }
   }

   public int[][] getVals() {
      return vals;
   }

   public void clear() {
      for (int x = 0; x < n; x++)
         for (int y = 0; y < n; y++)
            nums[x][y].setText("");
   }

   public void setVals(int[][] v) {
      if (v == null) {
         clear();
      } else {
         for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
               final int i = v[x][y];
               final String s = (i == 0) ? "" : Integer.toString(i);
               nums[x][y].setText(s);
            }
         }
      }
   }

   public void setEnabled(boolean v) {
      for (int x = 0; x < n; x++)
         for (int y = 0; y < n; y++)
            nums[x][y].setEnabled(v);
   }

   public SudokuInputPanel(int k) {
      super();
      this.k = k;
      n = k*k;
      vals = new int[n][n];
      nums = new NumberField[n][n];

      setLayout(new GridLayout(k,k));
      setBorder(BorderFactory.createTitledBorder("Input"));

      for (int i = 0; i < n; i++)
         add(new SudokuInputBox(i));
   }

   public SudokuInputPanel(int k, int[][] vals) {
      this(k);
      if (vals != null)
         setVals(vals);
   }
}
