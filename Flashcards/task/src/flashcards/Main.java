package flashcards;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {

        String cardTerm ;//Map key
        String cardDefinition = null;//Map value
        Integer error;
        String answer;//user answer
        boolean termExists; //to check if the term exists in Map
        boolean definitionExists; // to check the definition exists in Map
        boolean exit = false; // to exit from loop
        boolean exitExport = false;
        String exitFile = null;
        Random generator;
        Object[] keys;
        String randomKey;
        Integer timesToAsk;
        int loadedCards = 0;
        Map<String,String> cardMap = new LinkedHashMap<>();
        Map<String,Integer> failuresCardMap = new LinkedHashMap<>();
        ArrayList<String> logArrayList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        Scanner fileScanner;
        //Using run arguments -import to read data from file and -export to write
        for (int i = 0; i < args.length; i++)
        {
            if  (args[i].equals("-import"))
            {
                try(FileReader fileReader = new FileReader(args[i + 1]))
                {
                    fileScanner = new Scanner(new BufferedReader(fileReader));
                    fileScanner.useDelimiter(":");
                    while (fileScanner.hasNextLine())
                    {
                        String input = fileScanner.nextLine();
                        String[] data = input.split(":");
                        cardTerm = data[0];
                        cardDefinition = data[1];
                        error = Integer.parseInt(data[2]);
//                        fileScanner.skip(fileScanner.delimiter());
//                        fileScanner.nextLine();
                        //System.out.println("Term:"+cardTerm+" Definition:"+cardDefinition);
                        cardMap.put(cardTerm,cardDefinition);
                        failuresCardMap.put(cardTerm,error);
                        loadedCards++;
                    }
                    System.out.println(loadedCards+" cards have been loaded.");
                    logArrayList.add(loadedCards+" cards have been loaded.");
                    loadedCards=0;
                    //System.out.println("The map has "+cardMap.size()+" cards");
                }
                catch (IOException ex)
                {
                    System.out.println("File not found.");
                    logArrayList.add("File not found.");

                }
            }
            else if (args[i].equals("-export"))
            {
                exitExport = true;
                exitFile = args[i + 1];
            }
        }
        do
        {
            termExists = false;
            definitionExists = false;
            System.out.println("Input action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            logArrayList.add("Input action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            answer = scanner.nextLine();
            logArrayList.add(answer);
            //ADD
            if (answer.equals("add"))
            {
                System.out.println("The card:");
                logArrayList.add("The card:");
                cardTerm = scanner.nextLine();
                logArrayList.add(cardTerm);

                //if the Map isn't empty AND the term exists
                if(!cardMap.isEmpty() && cardMap.containsKey(cardTerm) )
                {

                    System.out.println("The card \""+cardTerm+"\" already exists.");
                    logArrayList.add("The card \""+cardTerm+"\" already exists.");
                    termExists = true;

                }
                if ( !termExists)
                {
                    System.out.println("The definition of the card:");
                    logArrayList.add("The definition of the card:");
                    cardDefinition = scanner.nextLine();
                    logArrayList.add(cardDefinition);
                    //if the Map isn't empty AND the definition exists
                    if(!cardMap.isEmpty() && cardMap.containsValue(cardDefinition))
                    {
                        System.out.println("The definition \"" + cardDefinition + "\" already exists.");
                        logArrayList.add("The definition \"" + cardDefinition + "\" already exists.");
                        definitionExists = true;
                    }

                }
                //if term AND definition NOT exists
                if (!termExists && !definitionExists)
                {
                    cardMap.put(cardTerm,cardDefinition);
                    failuresCardMap.put(cardTerm,0);
                    System.out.println("The pair (\""+cardTerm+"\":\""+cardDefinition+"\") has been added.");
                    logArrayList.add("The pair (\""+cardTerm+"\":\""+cardDefinition+"\") has been added.");
                }
            }
            //REMOVE
            else if (answer.equals("remove"))
            {
                System.out.println("The card:");
                logArrayList.add("The card:");
                cardTerm = scanner.nextLine();
                logArrayList.add(cardTerm);

                if (cardMap.remove(cardTerm,cardMap.get(cardTerm)))
                {
                    System.out.println("The card has been removed.");
                    logArrayList.add("The card has been removed.");
                }
                else
                {
                    System.out.println("Can't remove \"" + cardTerm + "\": there is no such card.");
                    logArrayList.add("Can't remove \"" + cardTerm + "\": there is no such card.");
                }
                //Remove failure from Map
                if (failuresCardMap.containsKey(cardTerm))
                    failuresCardMap.remove(cardTerm,failuresCardMap.get(cardTerm));
            }
            //IMPORT
            else if (answer.equals("import"))
            {
                System.out.println("File Name:");
                logArrayList.add("File Name:");
                answer = scanner.nextLine();
                logArrayList.add(answer);
                try(FileReader fileReader = new FileReader(answer))
                {
                    fileScanner = new Scanner(new BufferedReader(fileReader));
                    fileScanner.useDelimiter(":");
                    while (fileScanner.hasNextLine())
                    {
                        String input = fileScanner.nextLine();
                        String[] data = input.split(":");
                        cardTerm = data[0];
                        cardDefinition = data[1];
                        error = Integer.parseInt(data[2]);
//                        fileScanner.skip(fileScanner.delimiter());
//                        fileScanner.nextLine();
                        //System.out.println("Term:"+cardTerm+" Definition:"+cardDefinition);
                        cardMap.put(cardTerm,cardDefinition);
                        failuresCardMap.put(cardTerm,error);
                        loadedCards++;
                    }
                    System.out.println(loadedCards+" cards have been loaded.");
                    logArrayList.add(loadedCards+" cards have been loaded.");
                    loadedCards=0;
                    //System.out.println("The map has "+cardMap.size()+" cards");
                }
                catch (IOException ex)
                {
                    System.out.println("File not found.");
                    logArrayList.add("File not found.");

                }
            }
            //EXPORT
            else if (answer.equals("export"))
            {
                System.out.println("File Name:");
                logArrayList.add("File Name:");
                answer = scanner.nextLine();
                logArrayList.add(answer);
                try(FileWriter fileWriter = new FileWriter(answer))
                {
                    for (Map.Entry<String,String> entry : cardMap.entrySet())
                    {
                        fileWriter.write(entry.getKey()+":"+entry.getValue()+":");
                        if (failuresCardMap.containsKey(entry.getKey()))
                            fileWriter.write(failuresCardMap.get(entry.getKey()).toString()+"\n");
                        else
                            fileWriter.write("0\n");
                    }
                    System.out.println(cardMap.size()+" cards have been saved.");
                    logArrayList.add(cardMap.size()+" cards have been saved.");
                }
                catch (IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            //ASK
            else if (answer.equals("ask"))
            {
                generator = new Random();
                //return the keys of Map
                keys = cardMap.keySet().toArray();
                System.out.println("How many times to ask?");
                logArrayList.add("How many times to ask?");
                timesToAsk = scanner.nextInt();
                logArrayList.add(timesToAsk.toString());
                scanner.nextLine();
                for (int i=0; i<timesToAsk; i++)
                {

                    randomKey = (String) keys[generator.nextInt(keys.length)];
                    System.out.println("Print the definition of \""+randomKey+"\":");
                    logArrayList.add("Print the definition of \""+randomKey+"\":");
                    answer = scanner.nextLine();
                    logArrayList.add(answer);
                    if (answer.equals(cardMap.get(randomKey)))
                    {
                        System.out.print("Correct answer.\n");
                        logArrayList.add("Correct answer.");
                    }
                    else
                    {
                        System.out.print("Wrong answer. (The correct one is \""+cardMap.get(randomKey)+"\"");
                        String stringForLogArray = "Wrong answer. (The correct one is \""+cardMap.get(randomKey)+"\"";
                        if (failuresCardMap.containsKey(randomKey))
                        {
                            int failureNum = failuresCardMap.get(randomKey);
                            failureNum++;
                            failuresCardMap.put(randomKey,failureNum);
                        }
                        else
                            failuresCardMap.put(randomKey,1);
                        //if user answer exists in map
                        if (cardMap.containsValue(answer))
                        {
                            for (Map.Entry<String,String> entry : cardMap.entrySet())
                            {
                                //print the term of user definition
                                if (answer.equals(entry.getValue()))
                                {
                                    System.out.print(", you've just written the definition of \"" + entry.getKey() + "\".)\n");
                                    stringForLogArray += ", you've just written the definition of \""+entry.getKey()+"\".)";
                                }
                            }
                        }
                        else
                        {
                            System.out.print(".)\n");
                            stringForLogArray += ".)";
                        }
                        logArrayList.add(stringForLogArray);
                    }
                }
            }
            //EXIT
            else if (answer.equals("exit"))
            {


                System.out.println("Bye bye!");
                if (exitExport)
                {
                    try(FileWriter fileWriter = new FileWriter(exitFile))
                    {
                        for (Map.Entry<String,String> entry : cardMap.entrySet())
                        {
                            fileWriter.write(entry.getKey()+":"+entry.getValue()+":");
                            if (failuresCardMap.containsKey(entry.getKey()))
                                fileWriter.write(failuresCardMap.get(entry.getKey()).toString()+"\n");
                            else
                                fileWriter.write("0\n");
                        }
                        System.out.println(cardMap.size()+" cards have been saved.");
                        logArrayList.add(cardMap.size()+" cards have been saved.");
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                }
                //logArrayList.add("Bye bye!");
                exit = true;
            }
            //LOG
            else if (answer.equals("log"))
            {
                System.out.println("File name:");
                logArrayList.add("File name:");
                answer = scanner.nextLine();
                logArrayList.add(answer);
                File logFile = new File(answer);
                try(PrintWriter pw = new PrintWriter(new FileOutputStream(logFile)))
                {
                    for (String logString : logArrayList)
                        pw.println(logString);
                    pw.println("The log has been saved.");
                    System.out.println("The log has been saved.");
                }
                catch (FileNotFoundException ex)
                {
                    System.out.println("File not found");
                }
            }
            //HARDEST CARD
            else if (answer.equals("hardest card"))
            {
                if (failuresCardMap.isEmpty())
                {
                    System.out.println("There are no cards with errors.");
                    logArrayList.add("There are no cards with errors.");
                }
                else
                {
                    ArrayList<String> maxFailures = new ArrayList<>();
                    int counter = 0;
                    int maxValueInMap = Collections.max(failuresCardMap.values());
                    if (maxValueInMap == 0) {
                        System.out.println("There are no cards with errors.");
                        logArrayList.add("There are no cards with errors.");
                    } else {

                        for (Map.Entry<String, Integer> entry : failuresCardMap.entrySet()) {
                            if (entry.getValue() == maxValueInMap)
                                maxFailures.add(entry.getKey());
                        }

                        if (maxFailures.size() == 1) {
                            System.out.println("The hardest card is \"" + maxFailures.get(0) + "\".You have " + maxValueInMap + " errors answering it.");
                            logArrayList.add("The hardest card is \"" + maxFailures.get(0) + "\".You have " + maxValueInMap + " errors answering it.");
                        } else {
                            System.out.print("The hardest cards are");
                            String failuresForLogArray = "The hardest cards are";
                            for (int i = 0; i < (maxFailures.size() - 1); i++) {
                                System.out.print(" \"" + maxFailures.get(i) + "\",");
                                failuresForLogArray += " \"" + maxFailures.get(i) + "\",";
                                counter = i;
                            }
                            System.out.println(" \"" + maxFailures.get(counter + 1) + "\". You have " + maxValueInMap + " errors answering them.");
                            failuresForLogArray += " \"" + maxFailures.get(counter + 1) + "\". You have " + maxValueInMap + " errors answering them.";
                            logArrayList.add(failuresForLogArray);
                        }
                    }
                }
            }
            //RESET STATS
            else if (answer.equals("reset stats"))
            {
                for (Map.Entry<String,Integer> entry : failuresCardMap.entrySet())
                {
                    if (failuresCardMap.containsKey(entry.getKey()))
                        failuresCardMap.put(entry.getKey(), 0);
                }
//                failuresCardMap.clear();
                System.out.println("Card statistics has been reset.");
                logArrayList.add("Card statistics has been reset.");
            }

        }while (!exit);

    }
}
