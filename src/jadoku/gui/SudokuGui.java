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
import java.awt.*;

public class SudokuGui extends JFrame {
   public SudokuGui(int k) { this(k, null); }

   public SudokuGui(int k, int[][] vals) {
      super("Sudoku Solver (DLX)");
      final JPanel main = new JPanel();
      final JPanel io = new JPanel();
      final SudokuInputPanel in = new SudokuInputPanel(k, vals);
      final SudokuOutputPanel out = new SudokuOutputPanel(k);
      final SudokuControlPanel stat = new SudokuControlPanel(k, in, out);

      io.setLayout(new GridLayout(1,2));
      io.add(in);
      io.add(out);

      main.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
      main.setLayout(new BorderLayout());
      main.add(io, BorderLayout.CENTER);
      main.add(stat, BorderLayout.SOUTH);

      setContentPane(main);

      pack();
      setResizable(false);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      final Point p = MouseInfo.getPointerInfo().getLocation();
      final Dimension d = getSize();
      setLocation(p.x-(d.width/2), p.y-(d.height/2));
   }
}
