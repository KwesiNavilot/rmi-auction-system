package buyer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

public class BuyerServlet {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1099;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        String connectionStr = "rmi://"+host+":"+port+"/auction";
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            ConnectionLayer connection = new ConnectionLayer(connectionStr);
            
            System.out.print("What is your username?: ");
            String username = bufferReader.readLine();
            System.out.println(username);

            AuctionBuyerImpl buyer = new AuctionBuyerImpl(username);

            askForInput();

            boolean end = false;

            while (!end) {
                String response = "";

                try {
                    switch (bufferReader.readLine()) {
                        case "L":
                            System.out.println();
                            response = connection.getServer().getOpenAuctions();
                            break;
                        case "B":
                            try {
                                System.out.print("\nAuction item ID: ");
                                int auctionItemId = Integer.valueOf(bufferReader.readLine());

                                System.out.print("Amount: ");
                                float bidAmount = Float.valueOf(bufferReader.readLine());

                                response = connection.getServer().bid(buyer, auctionItemId, bidAmount);
                            } catch (NumberFormatException nfe) {
                                System.err.println("Incorrect input format. Please try again.");

                                askForInput();
                            }

                            break;
                        case "H":
                            response = connection.getServer().getClosedAuctions();

                            break;
                        case "T":
                            response = "\nAverage turnaround - " + connection.getFailureDetector().determineLoad() + "ms";
                            break;
                        case "Q":
                            System.out.println("Quitting the buyer application....");
                            end = true;
                            break;
                        default:
                            System.out.println("You entered a wrong input.");
                            break;
                    }
                } catch (RemoteException e) {
                    System.out.println(e);
                }

                System.out.println(response);

                askForInput();
            }
            
            System.exit(0);
        } catch (IOException e) {
            System.err.println("\nUnable to parse your input " + e);
            System.exit(2);
        }
    }

    public static void askForInput() {
        System.out.println("\nChoose an option:");
        System.out.println("L - List items");
        System.out.println("B - Make a bid");
        System.out.println("H - History");
        System.out.println("T - Check server load (average turnaround in ms)");
        System.out.println("Q - Quit");
    }
}
