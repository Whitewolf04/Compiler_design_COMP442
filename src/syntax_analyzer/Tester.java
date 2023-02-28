package syntax_analyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tester {
    public static void FirstSetTest(){
        Scanner followScan = null;
        Pattern nonTermPattern = Pattern.compile("<(.*?)>");
        Pattern followSetPattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = null;
        String[] followSet;
        String line = "";
        String nonTermName = "";
        String followSetMatch = "";
        NonTerminal nonTerm = null;


        try{
            followScan = new Scanner(new File("./syntax_analyzer/LL1grammar.grm.first"));
        }  catch(FileNotFoundException e){
            System.out.println("File not found!");
        }

        while(followScan.hasNextLine()){
            line = followScan.nextLine();

            // Pattern match to find non-terminal name
            m = nonTermPattern.matcher(line);
            if(m.find()){
                nonTermName = m.group(1);
            } else {
                System.out.println("There is no non-terminal pattern match for line: " + line);
            }

            nonTerm = ParsingTable.get(nonTermName);
            if(nonTerm == null){
                System.out.println("Missing non-terminal " + nonTermName + " in the parsing table");
                continue;
            }

            // Pattern match to find first set
            m = followSetPattern.matcher(line);
            if(m.find()){
                followSetMatch = m.group(1);
                followSetMatch = followSetMatch.replaceAll("\'", "");
            } else {
                System.out.println("There is no first set pattern match for line: " + line);
            }

            followSet = followSetMatch.split(", ");
            if(nonTerm.first.length > followSet.length){
                for(Terminal term : nonTerm.first){
                    boolean missing = true;
                    for(String token : followSet){
                        if(!term.compareToString(token)){
                            missing = false;
                            break;
                        }
                    }
                    if(missing){
                        System.out.println("Redundant token: " + term.toString() + " in " + nonTerm.toString() + " first set.");
                    }
                }
            } else {
                for(String token : followSet){
                    boolean missing = true;
                    for(Terminal term : nonTerm.first){
                        if(term.compareToString(token)){
                            missing = false;
                            break;
                        }
                    }
                    if(missing){
                        System.out.println("Missing token: " + token + " in " + nonTerm.toString() + " first set.");
                    }
                }
            }

        }
    }

    public static void FollowSetTest(){
        Scanner followScan = null;
        Pattern nonTermPattern = Pattern.compile("<(.*?)>");
        Pattern followSetPattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = null;
        String[] followSet;
        String line = "";
        String nonTermName = "";
        String followSetMatch = "";
        NonTerminal nonTerm = null;


        try{
            followScan = new Scanner(new File("./syntax_analyzer/LL1grammar.grm.follow"));
        }  catch(FileNotFoundException e){
            System.out.println("File not found!");
        }

        while(followScan.hasNextLine()){
            line = followScan.nextLine();

            // Pattern match to find non-terminal name
            m = nonTermPattern.matcher(line);
            if(m.find()){
                nonTermName = m.group(1);
            } else {
                System.out.println("There is no non-terminal pattern match for line: " + line);
            }

            nonTerm = ParsingTable.get(nonTermName);
            if(nonTerm == null){
                System.out.println("Missing non-terminal " + nonTermName + " in the parsing table");
                continue;
            }

            // Pattern match to find first set
            m = followSetPattern.matcher(line);
            if(m.find()){
                followSetMatch = m.group(1);
                followSetMatch = followSetMatch.replaceAll("\'", "");
            } else {
                System.out.println("There is no first set pattern match for line: " + line);
            }

            followSet = followSetMatch.split(", ");
            if(nonTerm.follow.length > followSet.length){
                for(Terminal term : nonTerm.follow){
                    boolean missing = true;
                    for(String token : followSet){
                        if(!term.compareToString(token)){
                            missing = false;
                            break;
                        }
                    }
                    if(missing){
                        System.out.println("Redundant token: " + term.toString() + " in " + nonTerm.toString() + " follow set.");
                    }
                }
            } else {
                for(String token : followSet){
                    boolean missing = true;
                    for(Terminal term : nonTerm.follow){
                        if(term.compareToString(token)){
                            missing = false;
                            break;
                        }
                    }
                    if(missing){
                        System.out.println("Missing token: " + token + " in " + nonTerm.toString() + " follow set.");
                    }
                }
            }

        }
    }

    public static List<List<String>> tableExtract() {
        Document document = null;

        try{
            document = Jsoup.parse(new File("./syntax_analyzer/LL1grammar_table.html"), "UTF-8", "/");
        } catch(IOException e){
            System.out.println(e);
        }

        Elements rows = document.select("tr");

        List<List<String>> table = new ArrayList<List<String>>();
        Pattern prodPattern = Pattern.compile(".*\\→\\s*(.*?)\\Z");
        Matcher m = null;

        for(int i = 0; i < rows.size(); i++)
        {
            Elements columns = rows.get(i).select("td");
            List<String> row = new ArrayList<String>();
            for (org.jsoup.nodes.Element column:columns)
            {
                m = prodPattern.matcher(column.text());
                if(m.find()){
                    String temp = m.group(1);
                    temp = temp.replaceAll("&epsilon", "EPSILON");
                    row.add(temp);
                    // System.out.print(temp + "|");
                } else{
                    row.add(column.text());
                    // System.out.print(column.text() + "|");
                }
            }
            table.add(row);
        }

        return table;
    }

    public static void TableTest(){
        List<List<String>> extractedTable = tableExtract();
        int rowDif = extractedTable.size() - 1 - ParsingTable.size();
        // if rowDif > 0 then parsing table missing entry, if rowDif < 0 then parsing table has redundant entry
        String rowName = "";
        String colName = "";
        String[] tokens = null;
        int rowSize = 0;
        int prodDif = 1111;
        NonTerminal nonTerm = null;

        for(int i = 1; i < extractedTable.size(); i++){
            rowName = extractedTable.get(i).get(0);

            // Check if entry exist in parsing table
            nonTerm = ParsingTable.get(rowName);
            if(nonTerm == null){
                rowDif++;
                System.out.println("Parsing table missing " + rowName + "!");
                continue;
            }

            // Loop for each row
            for(int j = 1; j < extractedTable.get(i).size(); j++){
                // Skip through all empty entries
                if(extractedTable.get(i).get(j).isEmpty()){
                    continue;
                }
                rowSize++;  // Use row size to check for redundant or missing productions on each nonTerm

                colName = extractedTable.get(0).get(j);
                tokens = extractedTable.get(i).get(j).split(" ");
                int numProduct = 0;

                if(nonTerm.tableEntry.get(colName) == null){
                    System.out.println("Non-terminal " + nonTerm.name + " is missing productions for " + colName);
                    continue;
                }

                while(!nonTerm.tableEntry.get(colName).empty()){
                    GrammarToken temp = nonTerm.tableEntry.get(colName).pop();
                    numProduct++;
                    boolean found = false;
                    for(String token : tokens){
                        if(temp.compareToString(token)){
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("Non-terminal " + nonTerm.name + " has redundant " + temp.toString());
                    }
                }

                if(numProduct > tokens.length){
                    System.out.println("Non-terminal " + nonTerm.name + " has " + (numProduct-tokens.length) + " redundant productions");
                } else if(numProduct < tokens.length){
                    System.out.println("Non-terminal " + nonTerm.name + " has " + (tokens.length+numProduct) + " missing productions");
                }
            }

            // Check if there is any missing or redundant productions on each nonTerm
            prodDif = rowSize - nonTerm.tableEntry.size();
            if(prodDif < 0){
                prodDif *= -1;
                System.out.println("Non-terminal " + nonTerm.name + " is missing " + prodDif + " productions");
            } else if(prodDif > 0){
                System.out.println("Non-terminal " + nonTerm.name + " has extra " + prodDif + " productions");
            }
            rowSize = 0;
            prodDif = 1111;
        }

        if(rowDif > 0){
            System.out.println("Parsing table is missing non-terminals!");
        } else if(rowDif < 0){
            System.out.println("Parsing table has redundant non-terminals!");
        }
    }

    public static void main(String[] args) {
        ParsingTable.loadTable();
        FirstSetTest();
        FollowSetTest();
        TableTest();
    }
}
