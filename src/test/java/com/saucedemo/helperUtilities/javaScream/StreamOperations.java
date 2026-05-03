package com.saucedemo.helperUtilities.javaScream;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamOperations {


    public void printElements(List<String> elements) {
        elements.forEach(System.out::println);
    }

    // 2. Convert a list of strings to uppercase
    public List<String> toUpperCase(List<String> elements) {
        return elements.stream().map(String::toUpperCase).collect(Collectors.toList());
    }

    // 3. Filter elements that start with a specific letter
    public List<String> filterByStartingLetter(List<String> elements, char letter) {
        return elements.stream().filter(e -> e.charAt(0) == letter).collect(Collectors.toList());
    }

    // 4. Count elements in a list
    public long countElements(List<String> elements) {
        return elements.stream().count();
    }

    // 5. Find the first element that matches a condition
    public String findFirstMatchingElement(List<String> elements, String condition) {
        return elements.stream().filter(e -> e.equals(condition)).findFirst().orElse(null);
    }

    // 6. Sort elements in ascending order
    public List<String> sortElements(List<String> elements) {
        return elements.stream().sorted().collect(Collectors.toList());
    }

    // 7. Concatenate all elements into a single string
    public String concatenateElements(List<String> elements) {
        return elements.stream().collect(Collectors.joining(", "));
    }

    // 8. Map elements to their lengths
    public List<Integer> mapToLengths(List<String> elements) {
        return elements.stream().map(String::length).collect(Collectors.toList());
    }

    // 9. Check if any element matches a condition
    public boolean anyMatch(List<String> elements, String condition) {
        return elements.stream().anyMatch(e -> e.equals(condition));
    }

    // 10. Check if all elements match a condition
    public boolean allMatch(List<String> elements, String condition) {
        return elements.stream().allMatch(e -> e.equals(condition));
    }

    // 11. Find the maximum element
    public String findMaxElement(List<String> elements) {
        return elements.stream().max(String::compareTo).orElse(null);
    }

    // 12. Find the minimum element
    public String findMinElement(List<String> elements) {
        return elements.stream().min(String::compareTo).orElse(null);
    }

    // 13. Create a sublist from a list
    public List<String> subList(List<String> elements, int start, int end) {
        return elements.stream().skip(start).limit(end - start).collect(Collectors.toList());
    }

    // 14. Remove duplicates from a list
    public List<String> removeDuplicates(List<String> elements) {
        return elements.stream().distinct().collect(Collectors.toList());
    }

    // 15. Convert a list of integers to their squares
    public List<Integer> squareElements(List<Integer> elements) {
        return elements.stream().map(e -> e * e).collect(Collectors.toList());
    }

    // 16. Create a list of even numbers
    public List<Integer> filterEvenNumbers(List<Integer> elements) {
        return elements.stream().filter(e -> e % 2 == 0).collect(Collectors.toList());
    }

    // 17. Create a list of odd numbers
    public List<Integer> filterOddNumbers(List<Integer> elements) {
        return elements.stream().filter(e -> e % 2 != 0).collect(Collectors.toList());
    }

    // 18. Generate a list of integers from 1 to n
    public List<Integer> generateRange(int n) {
        return IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
    }

    // 19. Find the sum of a list of integers
    public int sumOfElements(List<Integer> elements) {
        return elements.stream().mapToInt(Integer::intValue).sum();
    }

    // 20. Find the average of a list of integers
    public double averageOfElements(List<Integer> elements) {
        return elements.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    // 21. Create a list of products of two lists
    public List<Integer> productOfTwoLists(List<Integer> list1, List<Integer> list2) {
        return IntStream.range(0, Math.min(list1.size(), list2.size())).map(i -> list1.get(i) * list2.get(i)).boxed().collect(Collectors.toList());
    }

    // 22. Create a list of concatenated strings from two lists
    public List<String> concatenateTwoLists(List<String> list1, List<String> list2) {
        return IntStream.range(0, Math.min(list1.size(), list2.size())).mapToObj(i -> list1.get(i) + list2.get(i)).collect(Collectors.toList());
    }

    // 23. Find the longest string in a list
    public String findLongestString(List<String> elements) {
        return elements.stream().max((s1, s2) -> Integer.compare(s1.length(), s2.length())).orElse(null);
    }

    // 24. Find the shortest string in a list
    public String findShortestString(List<String> elements) {
        return elements.stream().min((s1, s2) -> Integer.compare(s1.length(), s2.length())).orElse(null);
    }

    // 25. Group elements by their length
    public Map<Integer, List<String>> groupByLength(List<String> elements) {
        return elements.stream().collect(Collectors.groupingBy(String::length));
    }

    // 26. Create a list of characters from a string
    public List<Character> stringToCharacterList(String str) {
        return str.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    // 27. Create a string from a list of characters
    public String characterListToString(List<Character> characters) {
        return characters.stream().map(String::valueOf).collect(Collectors.joining());
    }

    // 28. Find the frequency of each element in a list
    public Map<String, Long> frequencyOfElements(List<String> elements) {
        return elements.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    // 29. Create a list of unique characters from a string
    public List<Character> uniqueCharacters(String str) {
        return str.chars().distinct().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    // 30. Check if a list contains a specific element
    public boolean containsElement(List<String> elements, String element) {
        return elements.stream().anyMatch(e -> e.equals(element));
    }

    // 31. Find the index of a specific element
    public int indexOfElement(List<String> elements, String element) {
        return IntStream.range(0, elements.size()).filter(i -> elements.get(i).equals(element)).findFirst().orElse(-1);
    }

    // 32. Create a list of squares of even numbers
    public List<Integer> squareOfEvenNumbers(List<Integer> elements) {
        return elements.stream().filter(e -> e % 2 == 0).map(e -> e * e).collect(Collectors.toList());
    }

    // 33. Create a list of cubes of odd numbers
    public List<Integer> cubeOfOddNumbers(List<Integer> elements) {
        return elements.stream().filter(e -> e % 2 != 0).map(e -> e * e * e).collect(Collectors.toList());
    }

    // 34. Create a list of strings with a specific suffix
    public List<String> filterBySuffix(List<String> elements, String suffix) {
        return elements.stream().filter(e -> e.endsWith(suffix)).collect(Collectors.toList());
    }

    // 35. Create a list of strings with a specific prefix
    public List<String> filterByPrefix(List<String> elements, String prefix) {
        return elements.stream().filter(e -> e.startsWith(prefix)).collect(Collectors.toList());
    }

    // 36. Create a list of strings that contain a specific substring
    public List<String> filterBySubstring(List<String> elements, String substring) {
        return elements.stream().filter(e -> e.contains(substring)).collect(Collectors.toList());
    }

    // 37. Create a list of strings sorted by length
    public List<String> sortByLength(List<String> elements) {
        return elements.stream().sorted(Comparator.comparingInt(String::length)).collect(Collectors.toList());
    }

    // 38. Create a list of strings sorted by their first character
    public List<String> sortByFirstCharacter(List<String> elements) {
        return elements.stream().sorted(Comparator.comparing(e -> e.charAt(0))).collect(Collectors.toList());
    }

    // 39. Create a list of strings reversed
    public List<String> reverseList(List<String> elements) {
        return IntStream.rangeClosed(1, elements.size()).mapToObj(i -> elements.get(elements.size() - i)).collect(Collectors.toList());
    }

    // 40. Create a list of integers from a list of strings
    public List<Integer> convertStringsToIntegers(List<String> elements) {
        return elements.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    // 41. Create a list of strings with their lengths
    public List<String> stringsWithLengths(List<String> elements) {
        return elements.stream().map(e -> e + " (" + e.length() + ")").collect(Collectors.toList());
    }

    // 42. Create a list of strings with their indices
    public List<String> stringsWithIndices(List<String> elements) {
        return IntStream.range(0, elements.size()).mapToObj(i -> i + ": " + elements.get(i)).collect(Collectors.toList());
    }

    // 43. Create a list of strings that are palindromes
    public List<String> filterPalindromes(List<String> elements) {
        return elements.stream().filter(e -> e.contentEquals(new StringBuilder(e).reverse())).collect(Collectors.toList());
    }

    // 44. Create a list of strings that are anagrams of a given string
    public List<String> filterAnagrams(List<String> elements, String target) {
        String sortedTarget = sortString(target);
        return elements.stream().filter(e -> sortString(e).equals(sortedTarget)).collect(Collectors.toList());
    }

    private String sortString(String str) {
        return str.chars().sorted().mapToObj(c -> (char) c).map(String::valueOf).collect(Collectors.joining());
    }

    // 45. Create a list of strings that are longer than a given length
    public List<String> filterLongerThan(List<String> elements, int length) {
        return elements.stream().filter(e -> e.length() > length).collect(Collectors.toList());
    }

    // 46. Create a list of strings that are shorter than a given length
    public List<String> filterShorterThan(List<String> elements, int length) {
        return elements.stream().filter(e -> e.length() < length).collect(Collectors.toList());
    }

    // 47. Create a list of strings that are equal to a given length
    public List<String> filterEqualTo(List<String> elements, int length) {
        return elements.stream().filter(e -> e.length() == length).collect(Collectors.toList());
    }

    // 48. Create a list of strings that are not empty
    public List<String> filterNotEmpty(List<String> elements) {
        return elements.stream().filter(e -> !e.isEmpty()).collect(Collectors.toList());
    }

}