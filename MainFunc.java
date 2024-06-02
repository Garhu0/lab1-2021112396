import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.*;
import java.io.*;

public class MainFunc {
    public class Graph {
        int numOfVertices;
        private Map<String, Map<String, Integer>> adjList;

        public Graph(){
            numOfVertices = 0;
            adjList = new HashMap<>();
        }

        public void addVertex(String vertex){
            if(!adjList.containsKey(vertex)){
                adjList.put(vertex, new HashMap<>());
                numOfVertices++;
            }
        }

        public void addEdge(String vertex1, String vertex2, int weight){
            if(adjList.containsKey(vertex1) && adjList.containsKey(vertex2)){
                adjList.get(vertex1).put(vertex2, weight);
            }
        }

        // Returns weight of edge if edge exists, 0 otherwise
        public int edgeExists(String vertex1, String vertex2){
            if(adjList.containsKey(vertex1) && adjList.containsKey(vertex2) && adjList.get(vertex1).containsKey(vertex2)){
                return adjList.get(vertex1).get(vertex2);
            }
            return 0;
        }



        public Graph constructGraph(Graph graph){
            System.out.println("Enter file path: ");
            Scanner scanner = new Scanner(System.in);
            String filePath = scanner.nextLine();
            try {
            Scanner fileScanner = new Scanner(new File(filePath));
            List<String> words = new ArrayList<>();
            while(fileScanner.hasNext()) {
                String word = fileScanner.next();
                word = word.replaceAll("[^a-zA-Z]", "");
                if(!word.isEmpty()){
                    words.add(word);
                }
            }
            fileScanner.close();
            Iterator<String> iterator1=words.listIterator();
            Iterator<String> iterator2=words.listIterator();
            graph.addVertex(iterator2.next().toLowerCase());
            while (iterator2.hasNext()) {
                String s1 = iterator1.next().toLowerCase();
                String s2 = iterator2.next().toLowerCase();
                graph.addVertex(s2);
                int weightOfEdge = edgeExists(s1, s2);
                if(weightOfEdge > 0){
                    graph.addEdge(s1, s2, weightOfEdge+1);
                }
                else{
                    graph.addEdge(s1, s2, 1);
                }
            }
            return graph;
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                return null;
            }
        }


        public void showDirectedGraph(){
        System.out.println("Graph: ");
        for(String vertex : adjList.keySet()){
            System.out.print(vertex + " -> ");
            for(String neighbor : adjList.get(vertex).keySet()){
                System.out.print(neighbor + " (" + adjList.get(vertex).get(neighbor) + ") ");
                }
            System.out.println("");
        }
        }

        public void queryBridgeWords(String queryWord1, String queryWord2){
            if(!adjList.containsKey(queryWord1) && !adjList.containsKey(queryWord2)){
                System.out.println("No \""+queryWord1+"\" and \""+queryWord2+"\" in the graph.");
            }else if(!adjList.containsKey(queryWord1)){
                System.out.println("No \""+queryWord1+"\" in the graph.");
            }else if(!adjList.containsKey(queryWord2)){
                System.out.println("No \""+queryWord2+"\" in the graph.");
            }else if(adjList.get(queryWord1).containsKey(queryWord2)){
                System.out.println("No bridge words from \""+queryWord1+"\" to \""+queryWord2+"\"!");
            }else{
                // Perform DFS to find bridge words
                visited.clear();
                bridges.clear();
                boolean hasBridgeWords = dfsQueryBridgeWords(queryWord1, queryWord2);
                if(!hasBridgeWords){
                    System.out.println("Here No bridge words from \""+queryWord1+"\" to \""+queryWord2+"\"!");
                }else{
                    String[] bridgeWords = new String[bridges.size()];
                    int stackSize = bridges.size();
                    for(int i=0; i<stackSize; i++){
                        bridgeWords[i] = bridges.pop();
                    }
                    if(bridgeWords.length == 2){
                        System.out.println("The bridge word from \""+queryWord1+"\" to \""+queryWord2+"\" is: " + bridgeWords[0] + ".");
                    }else{
                    System.out.printf("The bridge words from \""+queryWord1+"\" to \""+queryWord2+"\" are: ");
                    for(int i=bridgeWords.length-2; i>=1; i--){
                        System.out.print(bridgeWords[i] + ", ");
                    }
                    System.out.print("and " + bridgeWords[0] + ".");
                    System.out.println("");
                }
                }
                bridges.clear();
                visited.clear();
            }
    }

    public String queryBridgeWordsReturnStr(String queryWord1, String queryWord2){
        queryWord1 = queryWord1.toLowerCase();
        queryWord2 = queryWord2.toLowerCase();
        // Check if query words are in the graph
        if(!adjList.containsKey(queryWord1) && !adjList.containsKey(queryWord2)){
            return " ";
        }else if(!adjList.containsKey(queryWord1)){
            return " ";
        }else if(!adjList.containsKey(queryWord2)){
            return " ";
        }else if(adjList.get(queryWord1).containsKey(queryWord2)){
            return " ";
        }else{
            // Perform DFS to find bridge words
            visited.clear();
            bridges.clear();
            boolean hasBridgeWords = dfsQueryBridgeWords(queryWord1, queryWord2);
            if(!hasBridgeWords){
                return " ";
            }else{
                String[] bridgeWords = new String[bridges.size()];
                int stackSize = bridges.size();
                for(int i=0; i<stackSize; i++){
                    bridgeWords[i] = bridges.pop();
                }
                if(bridgeWords.length == 2){
                    return " "+bridgeWords[0]+" ";
                }else{
                String bridgeWordsStr = " ";
                for(int i=bridgeWords.length-2; i>=0; i--){
                    bridgeWordsStr += bridgeWords[i] + " ";
                }
                return bridgeWordsStr;
            }
            }
        }
    }

    private Set<String> visited = new HashSet<>();
    private Stack<String> bridges = new Stack<String>();

    public boolean dfsQueryBridgeWords(String queryWord1, String queryWord2){
        visited.add(queryWord1);
        bridges.push(queryWord1);
        try{
            for(String neighbor : adjList.get(queryWord1).keySet()){
                if(neighbor.equals(queryWord2)){
                    return true;
                }else if(!visited.contains(neighbor)){
                    boolean bridgeFound = dfsQueryBridgeWords(neighbor, queryWord2);
                    if(bridgeFound){
                        return true;
                    }
                    bridges.pop();
                }
            }   
        }catch(NullPointerException e){
            return false;
        }
        return false;
    }

    public String generateNewText(String inputText){
        List<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(inputText);
        while(scanner.hasNext()) {
            String word = scanner.next();
            word = word.replaceAll("[^a-zA-Z]", "");
            if(!word.isEmpty()){
                words.add(word);
            }
        }
        Iterator<String> iterator1=words.listIterator();
        Iterator<String> iterator2=words.listIterator();
        String outputText = "";
        outputText += iterator2.next();
        while (iterator2.hasNext()) {
            String s1 = iterator1.next();
            String s2 = iterator2.next();
            String bridgeWords = queryBridgeWordsReturnStr(s1, s2);
            outputText += bridgeWords + s2;
        }
        return outputText;
}
    //using Dijkstra's algorithm to find shortest path
    public String calcShortestPath(String word1, String word2){
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();//word, pre
        Set<String> visited = new HashSet<>();
        visited.add(word1);
        dist.put(word1, 0);
        for(String vertex : adjList.keySet()){
            if(adjList.get(word1).containsKey(vertex)){
                dist.put(vertex, adjList.get(word1).get(vertex));
                prev.put(vertex, word1);
            }else if(vertex.equals(word1)){
                continue;
            }else{
                dist.put(vertex, Integer.MAX_VALUE);
                prev.put(vertex, null);
            }
            }//initialized
        //if not reachable
        visited.clear();
        bridges.clear();
        boolean reachable = dfsQueryBridgeWords(word1, word2);
        if(!reachable){
            return "Not reachable!";
        }
        bridges.clear();
        visited.clear();
        while(!visited.contains(word2)){
            String minVertex = null;
            int minDist = Integer.MAX_VALUE;
            for(String vertex : adjList.keySet()){
                if(!visited.contains(vertex) && dist.get(vertex) < minDist){
                    minVertex = vertex;
                    minDist = dist.get(vertex);
                }
            }
            visited.add(minVertex);
            if(adjList.get(minVertex) != null){
                for(String neighbor : adjList.get(minVertex).keySet()){
                    int newDist = dist.get(minVertex) + adjList.get(minVertex).get(neighbor);
                    if(newDist < dist.get(neighbor)){
                        dist.put(neighbor, newDist);
                        prev.put(neighbor, minVertex);
                    }
                }
            }
        }
        String curr = word2;
        String path = curr;
        curr = prev.get(curr);
        while(curr!= null && !curr.equals(word1)){
            path = curr + " -> " + path;
            curr = prev.get(curr);
        }
        path = word1 + " -> " + path;
        return path;
}

    public String randomWalk(){
        List<String> words = new ArrayList<>(adjList.keySet());
        Random rand = new Random();
        String curr = words.get(rand.nextInt(words.size()));
        String path = curr;
        Map<String, String> visitedEdge = new HashMap<>();//curr, prev
        String prev = null;
        while(true){
            List<String> neighbors = new ArrayList<>(adjList.get(curr).keySet());
            if(neighbors.isEmpty()){
                break;
            }
            prev = curr;
            curr = neighbors.get(rand.nextInt(neighbors.size()));
            path += " -> " + curr;
            if(visitedEdge.containsKey(curr) && visitedEdge.get(curr).equals(prev)){
                break;
            }
            visitedEdge.put(curr, prev);
        }
        //save the path to a file
        try {
            FileWriter writer = new FileWriter("randomWalk.txt");
            writer.write(path);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void main(String[] args) {
        MainFunc outer = new MainFunc();
        Graph graph = outer.new Graph();
        graph = graph.constructGraph(graph);
        while(true){
            System.out.println("Options: ");
            System.out.println("1. Show directed graph");
            System.out.println("2. Query bridge words");
            System.out.println("3. Generate new text");
            System.out.println("4. Calculate shortest path");
            System.out.println("5. Random walk");
            System.out.println("6. Exit");
            System.out.println("Enter option: ");
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            switch(option){
                case 1:
                    graph.showDirectedGraph();
                    break;
                case 2:
                    System.out.println("Enter query word1: ");
                    String queryWord1 = scanner.next().toLowerCase();
                    System.out.println("Enter query word2: ");
                    String queryWord2 = scanner.next().toLowerCase();
                    graph.queryBridgeWords(queryWord1, queryWord2);
                    break;
                case 3:
                    System.out.println("Enter input text: ");
                    scanner.nextLine();
                    String inputText = scanner.nextLine();
                    System.out.println(graph.generateNewText(inputText));
                    break;
                case 4:
                    System.out.println("Enter word1: ");
                    String word1 = scanner.next().toLowerCase();
                    System.out.println("Enter word2: ");
                    String word2 = scanner.next().toLowerCase();
                    System.out.println("Shortest path: ");
                    System.out.println(graph.calcShortestPath(word1, word2));
                    break;
                case 5:
                    System.out.println(graph.randomWalk());
                    break;
                case 6:
                    System.exit(0);
                default:
                    System.out.println("Invalid option!");
                    break;
                }
            }
        }
    }
}