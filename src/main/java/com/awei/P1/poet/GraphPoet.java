package com.awei.P1.poet;

import com.awei.P1.graph.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A graph-based poetry generator.
 *
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 *
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 *
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 *
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 *
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    //   TODO
    // GraphPoet represents a word affinity graph which is generated with a corpus
    // and vertices of is are case-insensitive words
    // and edge weights of it are in-order adjacency counts.

    // Representation invariant:
    //   TODO
    // graph is a non-null graph.

    // Safety from rep exposure:
    //   TODO
    // All fields are modified by private and final.
    // Clients can not access the graph reference outside the class


    /**
     * Create a new poet with the graph from corpus (as described above).
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader in = null;
        in = new BufferedReader(new FileReader(corpus));
        List<String> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        String string;
        while ((string = in.readLine()) != null) {
            list.addAll(Arrays.asList(string.split(" ")));
        }
        in.close();
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            // remove empty string.
            if(iterator.next().length() == 0)
                iterator.remove();
        }
        for (int i = 0; i < list.size() - 1; i++) {
            String source = list.get(i).toLowerCase();
            String target = list.get(i+1).toLowerCase();
            int preweight = 0;
            if(map.containsKey(source+target)){
                preweight = map.get(source+target);
            }
            map.put(source+target, preweight+1);
            graph.set(source, target, preweight+1);
        }
        checkRep();
    }

    // TODO checkRep
    private void checkRep(){
        assert graph != null;
    }
    /**
     * Generate a poem.
     *
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = new ArrayList<>(Arrays.asList(input.split(" ")));
        Map<String, Integer> sourceMap;
        Map<String, Integer> targetMap;
        for (int i = 0; i < list.size() - 1; i++) {
            String source = list.get(i).toLowerCase();
            String target = list.get(i + 1).toLowerCase();
            stringBuilder.append(list.get(i)).append(" ");
            targetMap = graph.targets(source);
            sourceMap = graph.sources(target);
            int maxWeight = 0;
            String bridgeWord = "";
            for (String string : targetMap.keySet()) {
                if (sourceMap.containsKey(string)) {
                    if (sourceMap.get(string) + targetMap.get(string) > maxWeight) {
                        maxWeight = sourceMap.get(string) + targetMap.get(string);
                        bridgeWord = string;
                    }
                }
            }
            if (maxWeight > 0) {
                stringBuilder.append(bridgeWord + " ");
            }
        }
        stringBuilder.append(list.get(list.size() - 1));
        return stringBuilder.toString();
    }

    // TODO toString()
    @Override
    public String toString() {
        return graph.toString();
    }
}
