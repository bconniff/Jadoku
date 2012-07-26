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

import jadoku.solver.Solver;
import jadoku.solver.Simplifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class SudokuControlPanel extends JPanel {
   private final JLabel time = new JLabel("", JLabel.LEFT);
   private final JLabel guesses = new JLabel("", JLabel.CENTER);
   private final JLabel status = new JLabel("Ready", JLabel.RIGHT);
   private final JButton simplify = new JButton("Simplify");
   private final JButton solve = new JButton("Solve");
   private final JButton reset = new JButton("Reset");
   private final SudokuInputPanel in;
   private final SudokuOutputPanel out;
   private final int k;

   public SudokuControlPanel(int k, SudokuInputPanel i, SudokuOutputPanel o) {
      in = i;
      out = o;
      this.k = k;

      final JPanel action = new JPanel();
      action.setLayout(new GridLayout(1,2));
      action.add(simplify);
      action.add(solve);
      action.add(reset);

      final JPanel stat = new JPanel();
      stat.setLayout(new GridLayout(1,3));
      stat.add(time);
      stat.add(guesses);
      stat.add(status);

      setLayout(new BorderLayout());
      add(action, BorderLayout.NORTH);
      add(stat, BorderLayout.SOUTH);

      solve.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { runSolver(); }
      });

      simplify.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { runSimplifier(); }
      });

      reset.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            in.clear();
            out.show(out.READY);
            time.setText("");
            guesses.setText("");
            status.setText("Ready");
         }
      });
   }

   private void begin() {
      in.setEnabled(false);
      simplify.setEnabled(false);
      solve.setEnabled(false);
      reset.setEnabled(false);

      time.setText("");
      guesses.setText("");
      out.show(out.SOLVING);
      status.setText("Solving");
   }

   private void finish(Simplifier j) {
      final int[][] v = j.getVals();
      final long t = j.time();
      time.setText("Time: "+t+" ms");

      if (v == null) {
         out.show(out.FAILED);
         status.setText("Failed");
      } else {
         out.setVals(v);
         out.show(out.MAIN);
         status.setText("Success");
      }

      in.setEnabled(true);
      simplify.setEnabled(true);
      solve.setEnabled(true);
      reset.setEnabled(true);
   }

   private void finish(Solver j) {
      final int[][] v = j.getVals();
      final long t = j.time();
      final int g = j.guesses(), b = j.badGuesses();
      time.setText("Time: "+t+" ms");
      guesses.setText((g>0?"Guesses: "+g+(b>0?" ("+b+" wrong)":""):""));

      if (v == null) {
         out.show(out.FAILED);
         status.setText("Failed");
      } else {
         out.setVals(v);
         out.show(out.MAIN);
         status.setText("Success");
      }

      in.setEnabled(true);
      solve.setEnabled(true);
      reset.setEnabled(true);
   }

   private void runSimplifier() {
      begin();
      new Thread(new Runnable() {
         public void run() {
            final Simplifier j = new Simplifier(k, in.getVals());
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  finish(j);
               }
            });
         }
      }).start();
   }

   private void runSolver() {
      begin();
      new Thread(new Runnable() {
         public void run() {
            final Solver j = new Solver(k, in.getVals());
            EventQueue.invokeLater(new Runnable() {
               public void run() {
                  finish(j);
               }
            });
         }
      }).start();
   }
}
