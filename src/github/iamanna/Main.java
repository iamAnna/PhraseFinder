package github.iamanna;

import java.text.BreakIterator;

public class Main {

    public static void main(String[] args) {
        String myText = "The quick brown fox jumped over the lazy dog." +
                "    The lazy dog, peeved to be labeled lazy, jumped over a snoring turtle." +
                "    In retaliation the quick brown fox jumped over ten snoring turtles." +
                "    Then the quick brown fox refueled with some ice cream.";
        BreakIterator bi = BreakIterator.getSentenceInstance();
        bi.setText(myText);
        int index = 0;
        while (bi.next() != BreakIterator.DONE) {
            String sentence = myText.substring(index, bi.current());
            System.out.println("Sentence: "+sentence);
            index = bi.current();
        }
    }
}
