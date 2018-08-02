package github.iamanna;

import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final BreakIterator biWord = BreakIterator.getWordInstance();

    // keeping public for testing and/or will pull out into another class (to decide: Functional or OOP)
    public static void getWordListFromSentence (String sentence, List<String> wordList) {
        biWord.setText(sentence);
        wordList.clear();
        int wordIndex = 0;
        while (biWord.next() != BreakIterator.DONE) {
            String word = sentence.substring(wordIndex, biWord.current());
            // Ignore any 'words' that are punctuation or white space
            if(Character.isLetterOrDigit(sentence.codePointAt(wordIndex))) {
                wordList.add(word);
            }
            wordIndex = biWord.current();
        }
    }
    public static Map<String, Integer> getFrequentPhraseMap (int max, int min, String text) {
        Map<String, Integer> phraseCountMap = new HashMap<>();
        BreakIterator bi = BreakIterator.getSentenceInstance();
        List<String> wordList = Collections.synchronizedList(new ArrayList<>());
        StringBuilder builder = new StringBuilder();
        bi.setText(text);
        int sentenceIndex = 0;
        while (bi.next() != BreakIterator.DONE) {
            int startIdx = 0;
            String sentence = text.substring(sentenceIndex, bi.current()).toLowerCase();
            // build list of words, need to know the total word count for each sentence
            //TODO: Either make OOP or functional. Should I mutate an existing list or should I return new instance?
            getWordListFromSentence(sentence, wordList);
            int sentenceWordTotal = wordList.size();
            // if there aren't enough words in the sentence to make the smallest phrase, then skip.
            if (sentenceWordTotal < min) {
                continue;
            }

            // building phrases from sentence
            while (startIdx <= (sentenceWordTotal-min)) {
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

                // building phrase from words
                while (phraseWordCounter<=max && phraseEndIdx<sentenceWordTotal) {
                    builder.append(" ").append(wordList.get(phraseEndIdx));
                    phraseCountMap.merge(builder.toString(), 1, Integer::sum);
                    phraseEndIdx++;
                    phraseWordCounter++;
                }

                startIdx++; // new starting point for phrase building
            }
            sentenceIndex = bi.current();
        }
        // Done with text and we have our hashmap - need to sort top 10 and confirm none are substrings
        LinkedHashMap<String, Integer> sortedByPhraseSize = phraseCountMap.entrySet()
                .stream()
                .filter(map -> map.getValue()>1)
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(String::length)))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String [] temp = sortedByPhraseSize.keySet().toArray(new String [0]);
        int phraseIdx = 0;
        // only check the top 10 and any offset?
        while (phraseIdx < temp.length) {
            boolean allTheSameLength = false;
            int nextPhraseIdx = phraseIdx+1;
            int phraseLength = temp[phraseIdx].length();

            while (nextPhraseIdx <temp.length) {
                // iterate until you find phrase that's longer than current phrase
                if (phraseLength>=temp[nextPhraseIdx].length()) {
                    // if you're at the end of the list and haven't found a longer phrase to compare then you can stop looking for substrings
                    if (nextPhraseIdx == temp.length-1) {
                        allTheSameLength = true;
                        break;
                    }
                    // TODO: Find a way to keep it DRY
                    nextPhraseIdx++;
                    continue;
                }
                // if a substring is found, remove the smaller string from the map of phrases, and move on to next phrase
                if (temp[nextPhraseIdx].contains(temp[phraseIdx])) {
                    sortedByPhraseSize.remove(temp[phraseIdx]);
                    break;
                }
                // TODO: Find a way to keep it DRY
                nextPhraseIdx++;
            }
            if (allTheSameLength) {
                break;
            }
            phraseIdx++;
        }

        return sortedByPhraseSize;
    }

    public static void main(String[] args) {
        String myText = "The quick brown fox jumped over the lazy dog. " +
                "The lazy dog, peeved to be labeled lazy, jumped over a snoring turtle. " +
                "In retaliation the quick brown fox jumped over ten snoring turtles. " +
                "Then the quick brown fox refueled with some ice cream.";
        Map<String, Integer> topPhrasesMap = getFrequentPhraseMap(10, 3, myText);
        System.out.println(topPhrasesMap.entrySet());
    }
}
