package main.utils;
import javax.swing.text.*;

public class NumberFilter extends DocumentFilter {
    private final int maxLength = 4;
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String newText = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()))
                .insert(offset, string).toString();
        if (newText.matches("\\d{0," + maxLength + "}")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = new StringBuilder(currentText).replace(offset, offset + length, text).toString();
        if (newText.matches("\\d{0," + maxLength + "}")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
