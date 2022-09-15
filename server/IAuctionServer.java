package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import seller.IAuctionSeller;
import buyer.IAuctionBuyer;

public interface IAuctionServer extends Remote {
    /**
     * Create an auction item
     * @param owner client object
     * @param name item name
     * @param minimumValue minimum bid
     * @param closingTime closing time in seconds
     * @return success/error message
     * @throws RemoteException
     */
    String createAuctionItem(IAuctionSeller owner, String name, float minimumValue, long closingTime) throws RemoteException;

    /**
     * Make a bid
     * @param buyer buyer object
     * @param auctionItemID item id
     * @param amount bid amount
     * @return success/error message
     * @throws RemoteException
     */
    String bid(IAuctionBuyer buyer, int auctionItemID, float amount) throws RemoteException;

    /**
     * Returns a nicely formatted string that contains a list of open auctions
     * @return list of open auctions
     * @throws RemoteException
     */
    String getOpenAuctions() throws RemoteException;

    /**
     * Returns a set of item IDs instead of a user-friendly string
     * Mainly used by AuctionClientWorker
     * @return set of auction item ids
     * @throws RemoteException
     */
    ArrayList<Integer> getOpenAuctionIDs() throws RemoteException;
    /**
     * Returns a nicely formatted string that contains a list of closed auctions
     * @return list of closed auctions
     * @throws RemoteException
     */
    String getClosedAuctions() throws RemoteException;

    /**
     * Probes the server to check if alive
     * @throws RemoteException
     */
    void probe() throws RemoteException;
}
