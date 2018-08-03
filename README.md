# Phrase Finder & Counter

This project is based on a take-home problem I was assigned and wanted to keep working on.

Any and all grammatical errors are mine and mine alone.

### Instructions

Given a string representing a document, write a function which returns the top 10 most frequent repeated phrases. 
* A phrase is a stretch of three to ten consecutive words 
    * cannot span sentences
    * 3 <= wordsInPhrase <= 10
* Omit a phrase if it is a subset of another, longer phrase, even if the shorter phrase occurs more frequently 
    * For example, if “cool and collected” and “calm cool and collected” are repeated, do not include “cool and collected” in the returned set. 
* Do not include any phrases in returned set that were only used once
    * phraseCount >= 2
    * If there are only 7 phrases that are used more than once, only return the set of 7.

Example input:

    The quick brown fox jumped over the lazy dog.
    The lazy dog, peeved to be labeled lazy, jumped over a snoring turtle.
    In retaliation the quick brown fox jumped over ten snoring turtles.
    Then the quick brown fox refueled with some ice cream.

Example output:

    ['the lazy dog', 'the quick brown fox jumped over']
    
#### Work Left to Do
* Add arguments in Main class (see Future Features below)
* Add tests for `buildFrequentPhraseMap` method in the Main class **AND / OR** break into smaller methods and relocate to PhraseUtils class
* Add larger range of Strings to test with
    
#### My Initial Approach
1. Separate text into sentences with REGEX and iterate through each
1. Word count of current sentence >= the minimum? If yes, continue.  If not, skip current sentence and continue iteration.
1. Remove all punctuation
1. Map phrases from current sentence using a HashMap with < String, Integer > key-value pairs (key = phrase, value = !count ? 1 : count++ )
    * increment phrase count if it already exists
1. Validate each phrase
    1. Word count within min / max? If no, remove
    1. Phrase count >= 2.  If no, remove
1. Sort in descending order and check the top 10 against all other phrases
    1. Are any of the top 10 phrases a subset of another?  If yes, remove
        * confirm all new phrases that bubble up through the removal process
    1. Return top 10
    
#### Potential Problems
* **Question**: How to determine a sentence ending / sentence beginning?
    * REGEX for period (.), exclamation point (!), question mark (?)
        * Cons:
            1. Poor performance
            1. Delicate and easy to break
            1. Tons edge cases to consider (encoding type, see quote example below, etc)
            1. Difficult to read, debug, and maintain (see _b_ above)
    * However what if there is a block quote or a quote within a quote: 

             When I was there the waiter said, "Well let me tell ya, my boss was givin' me the business and 
             hollering, 'Two dollar bills aren't real! Who in their right mind would think that?!?!', and 
             it took all my power not to bust up laughing."
    * Looking at the example above, what if "`think that?!?!', and it`" is a repeated phrase? It's technically part of the same sentence, but contains the sentence ending delimiters that were listed previously
* **Answer**: Java's [BreakIterator](https://docs.oracle.com/javase/8/docs/api/index.html?java/text/BreakIterator.html).getSentenceInstance()
    * Note: a sentence containing a quote will break the encompassing sentence into 2 or more separate strings.
    For example: `I said to Mable, "That's too bad!" and I think she believed it.` will break it into three sentences:
    
            I said to Mable,
            "That's too bad."
            and I think she believed it.
        

#### Future Features
* Add arguments (with defaults and checks) to let user modify the phrase word min and max as well as number of phrases to return
    * Defaults:  min >= 3, max <= 10, total number of phrases = 10
    * Check: confirm min <= max, total number of phrases > 0
    
* Output: include count with phrase, perhaps as map entries
* Make input more flexible and realistic - not just a String
    * Eventually allow different file types / file upload
    
* Try same problem in JavaScript, Go, etc.

