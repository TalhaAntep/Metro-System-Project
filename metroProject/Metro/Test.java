package Metro;

import java.io.*;
import java.util.Scanner;

import ADTPackage.*;
import GraphPackage.DirectedGraph;
import GraphPackage.StationInfo;

public class Test {
    static DirectedGraph metroGraph = new DirectedGraph<>();
   public static DictionaryInterface<String, StationInfo> StationDict = new UnsortedLinkedDictionary<>();

   static String fileDirectory;
    static String testFile;
    public static void main(String[] args) {

        metro();

    }
    static void metro() {
        try {
            fileDirectory = "Paris_RER_Metro_v2.csv";
            testFile="Test100.csv";
            readMetro();
            createEdge();
            //For average query time
           /* try {
                long start=System.nanoTime();

                File file = new File(testFile);
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                String[] tempArr1;

                br.readLine();
                String line1= "";
                while ((line1 = br.readLine()) != null) {

                    tempArr1 = line1.split(",");

                    String originStation =tempArr1[0];

                    String destinationStation=tempArr1[1];

                    String preference = tempArr1[2];
                    while(true) {
                        if (preference.equals("0")) {
                            //Shortest path
                            System.out.println("\n- Fewer Stops");
                            metroGraph.getShortestPath(originStation, destinationStation,new LinkedStack<String>());

                            break;
                        } else if (preference.equals("1")) {
                            //Cheapest Path
                            System.out.println("\n- Minimum Time");

                            metroGraph.getCheapestPath(originStation, destinationStation,
                                    new LinkedStack<String>());


                            break;

                        } else
                            System.out.println("Invalid Value!");
                    }

                }
                br.close();
                long end=System.nanoTime();
                long time=(end-start)/1000000;
                System.out.println(time+" ms");

            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }*/

            Scanner scanner=new Scanner(System.in);
           System.out.println("Enter Origin Station: ");

            String originStation =scanner.nextLine();
            System.out.println("Enter Destination Station: ");
            String destinationStation=scanner.nextLine();
            System.out.println("0) Fewer Stops or 1) Minimumm Time");
            String preference = scanner.nextLine();
            while(true) {
                if (preference.equals("0")) {
                    //Shortest path
                    System.out.println("\n- Shortest Path");
                    metroGraph.getShortestPath(originStation, destinationStation,new LinkedStack<String>());

                    break;
                } else if (preference.equals("1")) {
                    //Cheapest Path
                    System.out.println("\n- Cheapest Path");

                    metroGraph.getCheapestPath(originStation, destinationStation,
                            new LinkedStack<String>());


                    break;

                } else
                    System.out.println("Invalid Value!");
            }


        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
    }


    //to convert metro to graph
    static void readMetro() {

        try {
            File file = new File(fileDirectory);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String[] tempArr1;

            br.readLine();
            String line1= "";


            while ((line1 = br.readLine()) != null) {

                tempArr1 = line1.split(",");

                metroGraph.addVertex(tempArr1[1]);

                StationInfo stationInfo=new StationInfo(Double.parseDouble(tempArr1[2]),tempArr1[4],tempArr1[5],tempArr1[6]);
                StationDict.add(tempArr1[1],stationInfo);


            }
            br.close();

        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    static void createEdge(){
        try {
            File file = new File(fileDirectory);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String[] tempArr1;
            String[] tempArr2;
            br.readLine();
            String line1= br.readLine();
            String line2;

            while ((line2 = br.readLine()) != null) {

                tempArr1 = line1.split(",");
                tempArr2 = line2.split(",");

                double arrivalTime1 = Double.parseDouble(tempArr1[2]);
                double arrivalTime2 = Double.parseDouble(tempArr2[2]);
                double weight = Math.abs(arrivalTime1 - arrivalTime2);
                String directionID1 = tempArr1[4];
                String directionID2 = tempArr2[4];
                if (directionID2.equals(directionID1)&&tempArr1[5].equals(tempArr2[5])) {
                    metroGraph.addEdge(tempArr1[1], tempArr2[1], weight,tempArr2[5]);
                }
                line1 = line2;
            }

            br.close();


        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }




    }
}