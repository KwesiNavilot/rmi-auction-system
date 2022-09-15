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