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
import javax.swing.text.*;
import java.awt.*;

class NumberField extends JTextField {
   private final int max;

   public NumberField(int max) {
      /*super();
      final Dimension d = getPreferredSize();
      setPreferredSize(new Dimension(d.height,d.height));*/
      this.max = max;
      setHorizontalAlignment(JTextField.CENTER);
   }

   public int getVal() {
      final String text = getText();
      if (text.length() == 0)
         return -1;
      return Integer.parseInt(text);
   }

   protected Document createDefaultModel() {
      return new NumberDocument();
   }

   private class NumberDocument extends PlainDocument {
      public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException {
         if (str == null)
            return;

         char[] s = str.toCharArray();
         int len = getLength();

         String pre = getText(0, offs);
         String post = getText(offs, getLength()-offs);
         String result = "";

         for (char c: s) {
            if (!(len == 0 && c == '0')
                  && Character.isDigit(c)
                  && Integer.parseInt(pre+c+post) <= max) {
               result += c;
               pre += c;
            }
         }

         super.insertString(offs, result, a);
      }
   }
}
