package server;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionServlet {

    private Timer timer = new Timer();
    private static final String DEFAULT_FILENAME = "database";
    private static final String FILE_EXTENSION = ".txt";
    private static final long SAVE_DELAY = 1000 * 60 * 5;

    public class SaveTask extends TimerTask {
        private IAuctionServer auction;
        private String fileName;

        SaveTask(IAuctionServer auction, String fileName) {
            this.auction = auction;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            saveState(auction, fileName);
        }
    }


    public static void main(String args[]) {
        AuctionServlet servlet = new AuctionServlet();

        String host = "localhost";
        int port = 1099;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = "";

        try {
            IAuctionServer auction = null;

            System.out.println("Starting a new server....");
            System.out.print("How should the server state be saved? (default: " + DEFAULT_FILENAME + "): ");

            fileName = bufferReader.readLine();

            if (fileName.equals("")) {
                fileName = DEFAULT_FILENAME + FILE_EXTENSION;
            } else {
                fileName = fileName + FILE_EXTENSION;
            }

            auction = new AuctionServerImpl();

            LocateRegistry.createRegistry(port);
            Registry registry = LocateRegistry.getRegistry(host, port);
            registry.rebind("auction", auction);

            servlet.getTimer().schedule(servlet.new SaveTask(auction, fileName), SAVE_DELAY);

            System.out.println("\nServer state will be saved as " + fileName);
            System.out.println("Server state will be saved every "+ (float)SAVE_DELAY / 1000 / 60 +"mins");
            System.out.println("Server ready...");

            askForInput();

            while (true) {
                String input = bufferReader.readLine();

                if (input.equals("S")) {
                    System.out.println("\nSaving the current server state...");
                    saveState(auction, fileName);

                    askForInput();
                } else if (input.equals("Q")) {
                    System.out.println("\nQuitting the server....");
                    System.exit(0);
                } else {
                    System.out.println("You entered a wrong input.");

                    askForInput();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void saveState(IAuctionServer auction, String fileName) {
        try {
            FileOutputStream fOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream oOutputStream = new ObjectOutputStream(fOutputStream);

            oOutputStream.writeObject(auction);
            oOutputStream.close();

            System.out.println("Successfully saved server state to " + fileName);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find file " + e);
        } catch (IOException e) {
            System.err.println("Unable to write to file " + e);
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public static void askForInput() {
        System.out.println("\nChoose an option when ready (UPPERCASE only):\n"
                            + "S to save\n"
                            + "Q to quit");
    }
}