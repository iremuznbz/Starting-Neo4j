package com.irem.neo4jutils;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class HelloNeo4j {

	private static final String DB_PATH = "/Users/Irem/Documents/Neo4j/neo4j-community-2.1.7";

	String myString;
	GraphDatabaseService graphDB;
	Node firstNode;
	Node secondNode;
	Relationship relationship;

	private static enum RelTypes implements RelationshipType {
		KNOWS
	}

	public static void main(final String args[]) {

		HelloNeo4j myNeoInstance = new HelloNeo4j();
		myNeoInstance.createDB();
		myNeoInstance.removeDB();
		myNeoInstance.shutDown();
	}

	void createDB() {
		graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		Transaction tx = graphDB.beginTx();

		try {
			firstNode = graphDB.createNode();
			firstNode.setProperty("name", "xyz");
			secondNode = graphDB.createNode();
			secondNode.setProperty("name", "abc");
			relationship = firstNode.createRelationshipTo(secondNode,
					RelTypes.KNOWS);
			relationship.setProperty("relationship-type", "knows");

			myString = (firstNode.getProperty("name").toString())
					+ " "
					+ (relationship.getProperty("relationship-type").toString())
					+ " " + (secondNode.getProperty("name").toString());
			System.out.println(myString);

			tx.success();
		} finally {
			tx.finish();
		}

	}

	void removeDB() {

		Transaction tx = graphDB.beginTx();
		try {

			firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING)
					.delete();
			System.out.println("Removing nodes..");
			firstNode.delete();
			secondNode.delete();

			tx.success();
		} finally {
			tx.finish();
		}
	}

	void shutDown() {
		graphDB.shutdown();
		System.out.println("shutting down..");
	}
}
