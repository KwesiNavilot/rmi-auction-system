package server;

public enum ErrorCodes {
    AUCTION_CLOSED(-1, "The auction is closed"),
    ALREADY_MAX_BIDDER(-2, "You are already the max bidder"),
    OWNER_NULL(-3, "The owner cannot be null"),
    ITEM_NAME_NULL(-4, "The item's name cannot be null"),
    ITEM_NAME_EMPTY(-5, "Item name cannot be an empty string"),
    NEGATIVE_MINIMUM_VAL(-6, "Starting bid can't be less than 0"),
    NEGATIVE_CLOSING_TIME(-7, "Closing time must be positive"),
    AUCTION_DOES_NOT_EXIST(-8, "Auction does not exist"),
    BID_ON_OWN_ITEM(-9, "You can't bid on your own item"),
    LOW_BID(0, "The bid amount is too low"),
    SUCCESS_BID(1, "The bid was successful"),
    ITEM_CREATED(2, "Item created successfully");

    public final int ID;
    public final String MESSAGE;
    ErrorCodes(int id, String msg) { this.ID = id; this.MESSAGE = msg; }

    @Override
    public String toString() {
        return "Error " + ID + ": " + MESSAGE;
    }
}
