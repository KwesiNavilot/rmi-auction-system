# Java Distributed Auction System
This is a Java Remote Method Invocation (RMI) implementation for an Auctioning System. The program was written for a Distributed Systems assignment.

The goal and required of the assignment was to create an auction system that had one of the clients named as ‘Seller’ who could create a new auction, preferably with an ‘Auction ID’. The Seller should be able to quot a starting price and the reserve price (i.e., the
minimum price of the item expected). The reserve price should be kept secret. The auction should be time
bounded, which means that, the Seller should be able to close the auction after a specified
time or when the deadline is reached. When an auction is closed, the Seller should either
display the details of the winner or should be informed that the reserve price is not attained.

The second client is called ‘Buyer’. This client is meant for placing a bid against the items
under auction. The Buyer should be able to fetch the list of active auctions and bid for a selected item using the details of the buyer like name and mobile number. 

There is also a Server that should deal with requests from both Seller and Buyer maintaining the appropriate
details of items, auctions, sellers and buyers.

# Current Implementation
The implementation covers all of the requirements that were specified in the assignment document. There is a server that lets the users create auction items, view auctions and make bids. The server also notifies bidders when they are outbid or the auction closes, maintains historical records, has the ability save and load its state from file storage. There are two client implementations for Seller and Buyer. The auction system is thread safe to the best of my understanding.

## The Serverside
The auction’s serverside contains an IAuctionServer interface that extends Remote, an enum ErrorCodes containing error/success codes and four classes.
- **AuctionServerImpl** implements the core functionality of the server.
- **AuctionItem** represents auction listings, maintains a list of bids and observers for that item and contains some bidding logic.
- **Bid** a data model with no logic besides the overloaded toString() method.
- **AuctionServlet** runs the whole thing, manages standard IO and loading/saving auction state

## The Clientside
The clientside contains the IAuctionClient interface and four classes.
- **AuctionClientImpl** a remote class that has a name and a callback.
- **FailureDetector** responsible for failure detection, correction and performance measurement.
- **ConnectionLayer** a wrapper that maintains the server object and an instance of the FailureDetector
- **ClientServlet** runs an interactive client

# How To Run The System
Run the AuctionServlet.java in a terminal
1. Enter a name for the database file
2. Run the SellerServlet.java in a terminal
3. Use any of the features, add an auction items
4. Run the BuyerServlet.java in a terminal
5. Use any of the features, bid for an item
6. Enter Q on the AuctionServlet terminal to close the server
