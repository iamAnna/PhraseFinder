package github.iamanna;

import java.text.BreakIterator;
import java.util.*;

public class Main {

    public static Map<String, Integer> buildFrequentPhraseMap (int max, int min, String text) {
        Map<String, Integer> phraseCountMap = new HashMap<>();
        BreakIterator bi = BreakIterator.getSentenceInstance();
        List<String> wordList = Collections.synchronizedList(new ArrayList<>());
        StringBuilder builder = new StringBuilder();
        bi.setText(text);
        int sentenceIndex = 0;
        while (bi.next() != BreakIterator.DONE) {
            // clear words from previous sentence
            wordList.clear();
            int startIdx = 0;
            String sentence = text.substring(sentenceIndex, bi.current()).toLowerCase();
            sentenceIndex = bi.current();
            // build list of words, need to know the total word count for each sentence
            PhraseUtils.getWordListFromSentence(sentence, wordList);
            int sentenceWordCount = wordList.size();
            // if there aren't enough words in the sentence to make the smallest phrase, then skip.
            if (sentenceWordCount < min) {
                continue;
            }

            // building phrases from words in a sentence
            while (startIdx <= (sentenceWordCount-min)) {
                // clear previous phrase (if any)
                builder.delete(0, builder.length());
                // tracks how many words in a phrase
                int phraseWordCounter = min;
                // the index of the last word in a phrase
                int phraseEndIdx = startIdx+min;
                // build the first phrase with min number of words
                builder.append(String.join(" ", wordList.subList(startIdx, phraseEndIdx)));
                // Map initial out here in case you never enter last while loop
                phraseCountMap.merge(builder.toString(), 1, Integer::sum);

                // map all phrases up to max word count
                while (phraseWordCounter<=max && phraseEndIdx<sentenceWordCount) {
                    builder.append(" ").append(wordList.get(phraseEndIdx));
                    phraseCountMap.merge(builder.toString(), 1, Integer::sum);
                    phraseEndIdx++;
                    phraseWordCounter++;
                }
                startIdx++; // new starting point for phrase building
            }
        }
        // Done with text and we have our hashmap - need to sort top 10 and confirm none are substrings
        LinkedHashMap<String, Integer> phraseCountMapSortedBySize = PhraseUtils.sortRepeatedPhrasesBySize(phraseCountMap);

        PhraseUtils.removeAllSubstringPhrasesFromMap(phraseCountMapSortedBySize);
        return phraseCountMapSortedBySize;
    }

    public static void main(String[] args) {
        String myText = "The quick brown fox jumped over the lazy dog. " +
                "The lazy dog, peeved to be labeled lazy, jumped over a snoring turtle. " +
                "In retaliation the quick brown fox jumped over ten snoring turtles. " +
                "Then the quick brown fox refueled with some ice cream.";
        Map<String, Integer> topPhrasesMap = buildFrequentPhraseMap(10, 3, myText);
        System.out.println(topPhrasesMap.entrySet());
    }
}
