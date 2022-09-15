package server;

import java.io.Serializable;
import java.text.SimpleDateFormat;

// import seller.IAuctionSeller;
import buyer.IAuctionBuyer;

public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;

    // private final IAuctionSeller seller;
    // private final String sellerName;
    private final IAuctionBuyer buyer;
    private final String buyerName;
    private final float amount;
    private final long timestamp;

    // public Bid(IAuctionSeller seller, String sellerName, float amount) {
    //     this.seller = seller;
    //     // In case the seller disconnects - maintain record of his name
    //     // Would be replaced by a proper authentication system
    //     this.sellerName = sellerName;
    //     this.amount = amount;
    //     this.timestamp = System.currentTimeMillis();
    // }

    public Bid(IAuctionBuyer buyer, String buyerName, float amount) {
        this.buyer = buyer;
        this.buyerName = buyerName;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    // public String getSellerName() { return sellerName; }

    // public IAuctionSeller getSeller() {
    //     return seller;
    // }

    public String getBuyerName() { return buyerName; }

    public IAuctionBuyer getBuyer() {
        return buyer;
    }

    public float getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GHC ").append(amount).append(" @ ").append(dateFormat.format(timestamp));
        
        return stringBuilder.toString();
    }
}
