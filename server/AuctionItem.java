package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

import seller.IAuctionSeller;
import buyer.IAuctionBuyer;

public class AuctionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int idCounter = 0;
    private int id;

    private IAuctionBuyer buyer;
    private IAuctionSeller seller;
    private LinkedList<Bid> bids;

    private Set<IAuctionBuyer> observers;
    private String name;
    private final float minBid;
    private final Date startDate, closingDate;

    public AuctionItem(IAuctionSeller seller, String name, float minBid, long closingTime) {
        this.seller = seller;

        this.startDate = new Date(System.currentTimeMillis());
        this.closingDate = new Date(System.currentTimeMillis() + 1000 * closingTime);

        synchronized(this) {
            this.id = idCounter;
            idCounter += 1;
        }

        this.name = name;
        this.bids = new LinkedList<>();
        this.observers = new HashSet<>();
        this.observers.add(buyer);
        this.minBid = minBid;
    }

    /**
     * Notifies all bidders and the seller with a message
     * @param message
     */
    public void notifyObservers(String message) {
        for (IAuctionBuyer client : observers) {
            try {
                client.callback(message);
            } catch (RemoteException e) {
                System.err.println("Unable to access client - " + e);
            }
        }
    }

    /**
     * Thread-safe bidding
     * @param b bid object
     * @return
     */
    public synchronized String makeBid(Bid bid) {
        Bid currentBid = getCurrentBid();

        if (closingDate.getTime() - startDate.getTime() < 0) {
            return ErrorCodes.AUCTION_CLOSED.MESSAGE;
        } else if (bid.getAmount() <= minBid) {
            return ErrorCodes.LOW_BID.MESSAGE;
        } else if (currentBid != null) {
            if (bid.getAmount() <= currentBid.getAmount()) {
                return ErrorCodes.LOW_BID.MESSAGE;
            } else if (bid.getBuyer() == currentBid.getBuyer()) {
                return ErrorCodes.ALREADY_MAX_BIDDER.MESSAGE;
            }
        }

        bids.push(bid);
        observers.add(bid.getBuyer());

        // Notify clients about the new bid
        for (IAuctionBuyer client : observers) {
            try {
                if (client == bid.getBuyer()) {
                    client.callback("You're the max bidder with " + bid.getAmount());
                } else if (client != getBuyer()){
                    client.callback("You've been outbid on " + this.getName());
                }
            } catch (RemoteException e) {
                System.err.println("Unable to access client - " + e);
            }
        }

        return ErrorCodes.SUCCESS_BID.MESSAGE;
    }

    public synchronized Bid getCurrentBid() {
        if (bids.size() > 0) {
            return bids.peek();
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public IAuctionSeller getOwner() {
        return seller;
    }

    public IAuctionBuyer getBuyer() {
        return buyer;
    }

    public LinkedList<Bid> getBids() {
        return bids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMinBid() {
        return minBid;
    }

    public Set<IAuctionBuyer> getObservers() { return observers; }

    public Date getStartDate() { return startDate; }


    public Date getClosingDate() { return closingDate; }


    public long getClosingTime() {
        return this.closingDate.getTime();
    }


    public long getStartTime() {
        return this.startDate.getTime();
    }


    @Override
    public String toString() {
        synchronized(this) {
            Bid currentBid = getCurrentBid();
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

            long timeDiff = closingDate.getTime() - System.currentTimeMillis();
            boolean hasEnded = timeDiff <= 0;
            String timeLeftStr = "";

            if (!hasEnded) {
                if (timeDiff < 60 * 1000) {
                    timeLeftStr = String.valueOf(timeDiff / 1000) + "s";
                } else if (timeDiff >= 60 * 1000 && timeDiff < 60 * 60 * 1000) {
                    timeLeftStr = String.valueOf(timeDiff / 1000 / 60) + "min " + (timeDiff / 1000) % 60 + "s";
                } else if (timeDiff >= 60 * 60 * 1000) {
                    timeLeftStr = String.valueOf(timeDiff / 1000 / 60 / 60) + "h " + (timeDiff / 1000 / 60) % 60 + "min";
                }
            }

            StringBuilder result = new StringBuilder("Auction Item #");
            result.append(id).append(": ").append(name).append("\n");
            result.append("Minimum bid: ").append(minBid).append("\n");

            if (hasEnded && getCurrentBid() != null) {
                result.append("Winning bid: ").append(currentBid)
                        .append(" by ").append(currentBid.getBuyerName()).append("\n");
            } else {
                result.append("Current bid: ").append(currentBid == null ? "none" : currentBid).append("\n");
            }

            result.append("Start date: ").append(dateFormat.format(startDate)).append("\n");
            result.append("Closing date: ").append(dateFormat.format(closingDate)).append("\n");

            if (!hasEnded) {
                result.append("Time left: ").append(timeLeftStr);
            }
            
            return result.append("\n").toString();
        }
    }

}
