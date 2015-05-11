package tk.michael.project;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;

public class SyntaxTextPane extends JFrame {
	//http://stackoverflow.com/questions/14400946/how-to-change-the-color-of-specific-words-in-a-jtextpane
	private int findLastNonWordChar (String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar (String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	public SyntaxTextPane() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);

		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final AttributeSet attrRed = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
		final AttributeSet attrOrange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.ORANGE );
		final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if (before < 0) before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
						if (text.substring(wordL, wordR).matches("(\\W)*(private|public|protected)"))
							setCharacterAttributes(wordL, wordR - wordL, attrRed, false);
						else if( text.substring( wordL, wordR ).matches( "(\\W)*(select|from|where)" ) )
							setCharacterAttributes(wordL, wordR - wordL, attrOrange, false);
						else
							setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove (int offs, int len) throws BadLocationException {
				super.remove(offs, len);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0) before = 0;
				int after = findFirstNonWordChar(text, offs);

				if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
					setCharacterAttributes(before, after - before, attrRed, false);
				} else {
					setCharacterAttributes(before, after - before, attrBlack, false);
				}
			}
		};
		JTextPane txt = new JTextPane(doc);
		txt.setText("public class Hi {}");
		add(new JScrollPane(txt));
		setVisible(true);
	}
}