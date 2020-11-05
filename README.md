# ChatTree_v5.0

Application for the site of a "reliable" network for sending text messages. The nodes are logically united into a tree, each node can send UDP messages only to its immediate neighbors. The application accepts as parameters its own hostname, percentage of losses, its own port, as well as optionally the IP address and port of the host to which you want to connect at the start (make it your neighbor in the tree). An application that has not been given the IP address and port of the parent node becomes a free-standing node in the tree. Launch examples:

* $ chat_node Vasya 2001 30 # Node named "Vasya", loss of 30%, detached
* $ chat_node Petya 2002 40 127.0.0.1 2001 # Node named "Petya", loss 40%, connects to Vasya

A message entered into standard input at any of the nodes on the network is sent to all other nodes in the tree and printed to standard output exactly once at each node. All messages are identified using a GUID. To ensure "reliability", message delivery is confirmed.

Each node keeps a record of messages sent and received.

Re-sending messages due to losses does not lead to delays in the delivery of other messages, and does not block the operation of other program functions.

When any incoming message arrives, the node generates a random number from 0 to 99 inclusive. If this number is strictly less than the percentage of losses specified in the parameters, the message is ignored completely, simulating a network packet loss. This is necessary to test the reliability of message delivery.

At any time, any node can shutdown or become unavailable, then its neighbors, after a timeout specified in the program, stop sending messages to it and stop considering it a neighbor. Moreover, the tree does not fall apart: the remaining nodes rebuild the connections between themselves so that a tree is again obtained. To do this, each node chooses a "proxy" among its neighbors and communicates its IP address and port to other neighbors. The rebuilding of the tree occurs regardless of whether there was a transfer of text messages between these nodes at that moment.

Nodes can fail only one by one, so that the tree has time to recover. It is also allowed to lose some messages when the tree is rebuilt.
