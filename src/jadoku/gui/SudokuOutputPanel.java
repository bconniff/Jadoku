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

public class SudokuOutputPanel extends JPanel {
   public final static String READY = "r";
   public final static String MAIN = "m";
   public final static String FAILED = "f";
   public final static String SOLVING = "s";

   private final int n, k;
   private final java.util.List<OutputField> out;
   private final CardLayout cards = new CardLayout();

   private class OutputField extends JTextField {
      public final int x, y;
      public OutputField(int x, int y) {
         final Dimension d = getPreferredSize();
         setPreferredSize(new Dimension(d.height,d.height));
         setEditable(false);
         setBackground(Color.white);
         setHorizontalAlignment(JTextField.CENTER);
         this.x = x;
         this.y = y;
      }
   }

   private class SudokuOutputBox extends JPanel {
      private Border getBorder(int i, int w, Color c) {
         final int t = (i < 3) ? 0 : w;
         final int l = (i % 3 == 0) ? 0 : w;
         final int r = ((i+1) % 3 == 0) ? 0 : w;
         final int b = (i > 5) ? 0 : w;

         return BorderFactory.createMatteBorder(t,l,b,r,c);
      }

      public SudokuOutputBox(int i) {
         super();
         final int imk = (i%k)*k, idk = (i/k)*k;

         setBorder(getBorder(i, 1, Color.black));
         setLayout(new GridLayout(k,k));

         for (int j = 0; j < n; j++) {
            final int x = imk + (j%k), y = idk + (j/k);
            final OutputField field = new OutputField(y, x);
            field.setBorder(getBorder(j, 1, Color.lightGray));
            add(field);
            out.add(field);
         }
      }
   }

   public void setVals(int[][] vals) {
      for (OutputField l: out)
         l.setText(Integer.toString(vals[l.y][l.x]));
   }

   public void show(String s) {
      cards.show(this, s);
   }

   public SudokuOutputPanel(int k) {
      super();
      this.k = k;
      n = k*k;
      out = new ArrayList<OutputField>(n*n);

      final JPanel main = new JPanel();
      final JLabel failed = new JLabel("No solution!", JLabel.CENTER);
      final JLabel ready = new JLabel("Waiting for input", JLabel.CENTER);
      final JLabel solving = new JLabel("Solving...", JLabel.CENTER);

      setBorder(BorderFactory.createTitledBorder("Solution"));

      main.setLayout(new GridLayout(k,k));

      for (int i = 0; i < n; i++)
         main.add(new SudokuOutputBox(i));

      setLayout(cards);
      add(ready, READY);
      add(main, MAIN);
      add(failed, FAILED);
      add(solving, SOLVING);
   }
}
