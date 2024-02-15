package GraphPackage;
import java.util.*;

import ADTPackage.*; // Classes that implement various ADTs
import Metro.Test;

public class DirectedGraph<T> implements GraphInterface<T>
{
   private DictionaryInterface<T, VertexInterface<T>> vertices;
   private int edgeCount;
   
   public DirectedGraph()
   {
      vertices = new UnsortedLinkedDictionary<>();
      edgeCount = 0;
   } // end default constructor

   public boolean addVertex(T vertexLabel)
   {
      VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
      return addOutcome == null; // Was addition to dictionary successful?
   } // end addVertex
   
   public boolean addEdge(T begin, T end, double edgeWeight,String routeName)
   {
      boolean result = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
         result = beginVertex.connect(endVertex, edgeWeight,routeName);
      if (result)
         edgeCount++;
      
      return result;
   } // end addEdge
   

   public boolean hasEdge(T begin, T end)
   {
      boolean found = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
      {
         Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
         while (!found && neighbors.hasNext())
         {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (endVertex.equals(nextNeighbor))
               found = true;
         } // end while
      } // end if
      
      return found;
   } // end hasEdge

	public boolean isEmpty()
	{
	  return vertices.isEmpty();
	} // end isEmpty

	public void clear()
	{
	  vertices.clear();
	  edgeCount = 0;
	} // end clear

	public int getNumberOfVertices()
	{
	  return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges()
	{
	  return edgeCount;
	} // end getNumberOfEdges

	protected void resetVertices()
	{
	   Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	   while (vertexIterator.hasNext())
	   {
	      VertexInterface<T> nextVertex = vertexIterator.next();
	      nextVertex.unvisit();
	      nextVertex.setCost(0);
	      nextVertex.setPredecessor(null);
	   } // end while
	} // end resetVertices
	


	//Breadth first search traversal
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public QueueInterface<T> getBreadthFirstSearch(T origin, T end) {
        resetVertices(); //reset all vertices before starting
        boolean isMazeExitFound = false; // to stop searching if exit spotted
        // necessary variables to store traversal order information and vertex order.
		QueueInterface<T> traversalOrder = new LinkedQueue();
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue();// using queue for bfs
        // converting start and end position those user gave as parameters to vertex
        VertexInterface<T> originVertex = vertices.getValue(origin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        //mark origin vertex visited
        originVertex.visit();
        traversalOrder.enqueue(origin);
        vertexQueue.enqueue(originVertex);
        //Start traversal
        while (!isMazeExitFound && !vertexQueue.isEmpty()) {
            VertexInterface<T> currentVertex = vertexQueue.dequeue();
            //creating iterator for current neighbors
            Iterator<VertexInterface<T>> neighbors = currentVertex.getNeighborIterator();
            // Walking in neighbors
            while (!isMazeExitFound && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();// mark the vertex visited
                    traversalOrder.enqueue(nextNeighbor.getLabel());
                    vertexQueue.enqueue(nextNeighbor);
                }
                // if exit founded
                if (nextNeighbor.equals(endVertex))
                	isMazeExitFound = true;
            }
        }
        return traversalOrder;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public QueueInterface<T> getDepthFirstSearch(T origin, T end) {
        resetVertices(); // reset all vertices before starting
        boolean isMazeExitFound = false; // to stop searching if exit spotted
        // necessary variables to store traversal order information and vertex order.
		QueueInterface<T> traversalOrder = new LinkedQueue();
		StackInterface<VertexInterface<T>> vertexStack = new LinkedStack();// using stack for stack
        // converting start and end position those user gave as parameters to vertex
        VertexInterface<T> originVertex = vertices.getValue(origin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        //mark origin vertex visited
        originVertex.visit();
        traversalOrder.enqueue(end);
        vertexStack.push(originVertex);
        // Start traversal
        while (!isMazeExitFound && !vertexStack.isEmpty()) {
            VertexInterface<T> currentVertex = vertexStack.pop();
            //creating iterator for current neighbors
            Iterator<VertexInterface<T>> neighbors = currentVertex.getNeighborIterator();
            traversalOrder.enqueue(currentVertex.getLabel());
            // Walking in neighbors
            while (!isMazeExitFound && neighbors.hasNext()) {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit(); // mark the vertex visited
                    vertexStack.push(nextNeighbor);
                } 
                // if exit founded
                if (nextNeighbor.equals(endVertex))
                	isMazeExitFound = true;
            } 
        } 
        return traversalOrder;
    }

	public StackInterface<T> getShortestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();


		// take begin station and destination station
		VertexInterface<T> originVertex = vertices.getValue(begin);
		VertexInterface<T> endVertex = vertices.getValue(end);

		// use BFS for shortest path between stations
		QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();
		originVertex.visit();
		vertexQueue.enqueue(originVertex);

		while (!vertexQueue.isEmpty()) {
			VertexInterface<T> frontVertex = vertexQueue.dequeue();
			Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

			while (neighbors.hasNext()) {
				VertexInterface<T> nextNeighbor = neighbors.next();

				if (!nextNeighbor.isVisited()) {
					nextNeighbor.visit();
					nextNeighbor.setCost(1 + frontVertex.getCost());
					nextNeighbor.setPredecessor(frontVertex);
					vertexQueue.enqueue(nextNeighbor);
				}
			}
		}


		// find shortest path and create path
		int shortestPathLength = (int) endVertex.getCost();
		VertexInterface<T> vertex = endVertex;

		while (vertex.hasPredecessor()) {
			path.push(vertex.getLabel());
			vertex = vertex.getPredecessor();
		}
		path.push(begin); // add to begin station


		StackInterface<T> tempPath=new LinkedStack<>();

		String controlLine =null;
		Object[][] arr;
		String tempBeginStation=null;

		VertexInterface<T> origin=vertices.getValue(path.peek());

		arr=((Vertex<T>)origin).display();
		tempPath.push(path.pop());
		for (int row = 0; row < arr.length; row++) {
		if (!(arr[row][0]==null)) {
			String a=arr[row][0].toString();
			if (a.equals(path.peek())) {
				controlLine = arr[row][2].toString();
			}
		}


		}
		Boolean flag=false;
		String Line=null;
		int tourCounter=0;
		while(path.size()>1) {
			origin=vertices.getValue(path.peek());
			arr=((Vertex<T>)origin).display();
			tempPath.push(path.pop());

			for (int row = 0; row < arr.length; row++) {
				if (!(arr[row][0]==null) ) {
					String b=arr[row][0].toString();
					if (b.equals(path.peek())) {
						if (!arr[row][2].toString().equals(controlLine)) {
							Line = controlLine;
							controlLine = arr[row][2].toString();
							flag = true;
						}
					}
				}

			}
			if (flag){
				System.out.println("Line: "+Line);
				String endStation;
				String beginStation;

				if (tourCounter==0){
					int size= tempPath.size()-1;
					endStation=tempPath.pop().toString();
					while(tempPath.size()>1){
						tempPath.pop();
					}
					beginStation=tempPath.pop().toString();
					tourCounter++;
					System.out.println(beginStation+" - "+endStation+" ("+size+" stations) ");
					tempBeginStation=endStation;
				}
				else {
					int size = tempPath.size();
					endStation = tempPath.pop().toString();
					while (!tempPath.isEmpty()) {
						tempPath.pop();
					}
					System.out.println(tempBeginStation+" - "+endStation+" ("+size+" stations) ");
					tempBeginStation=endStation;

				}

				flag=false;

			}
		}
		tempPath.push(path.pop());
		if (!flag){
			System.out.println("Line: "+controlLine);
			String endStation;
			int size= tempPath.size();
			endStation=tempPath.pop().toString();
			while(!tempPath.isEmpty()){
				tempPath.pop();
			}

			System.out.println(tempBeginStation+" - "+endStation+" ("+size+" stations) ");
		}

		return path;
	}


   public StackInterface<T> getCheapestPath(T begin, T end, StackInterface<T> path) {
	   resetVertices();
	   boolean done;
	   done = false;
	   PriorityQueueInterface<EntryPQ> priorityQueue= new HeapPriorityQueue<>();
	   VertexInterface<T> originVertex = vertices.getValue(begin);
	   VertexInterface<T> endVertex = vertices.getValue(end);
	   priorityQueue.add(new EntryPQ(originVertex, 0, null));
	   while (!done && !priorityQueue.isEmpty())
	   {

		   EntryPQ frontEntry = priorityQueue.remove();
		   VertexInterface<T> frontVertex = frontEntry.vertex;
		   if (!frontVertex.isVisited())
		   {
			   frontVertex.visit();
			   frontVertex.setCost(frontEntry.getCost()); // sum weights
			   frontVertex.setPredecessor(frontEntry.getPredecessor());
			   // if exit founded
			   if (frontVertex.equals(endVertex)) {
				   done = true;
			   }
			   else
			   {
				   //creating iterator for current neighbors and weights
				   Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
				   Iterator<Double> weights = frontVertex.getWeightIterator();
				   frontVertex.getCost();
				   // walking in neighbors
				   while (neighbors.hasNext()) {
					   VertexInterface<T> nextNeighbor = neighbors.next();
					   double weightOfEdgeToNeighbor = weights.next();
					   if (!nextNeighbor.isVisited()) {
						   double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
						   priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
					   }
				   }

			   }
		   }
	   }
	   double pathCost = endVertex.getCost();
	   path.push(endVertex.getLabel());
	   VertexInterface<T> vertex = endVertex;
	   int vertexCounter = 0;
	   while (vertex.hasPredecessor()) {
		   vertex = vertex.getPredecessor();
		   path.push(vertex.getLabel());
		   vertexCounter++;
	   }
	   StackInterface<T> tempPath=new LinkedStack<>();

	   String controlLine =null;
	   Object[][] arr;
	   String tempBeginStation=null;

	   VertexInterface<T> origin=vertices.getValue(path.peek());

	   arr=((Vertex<T>)origin).display();
	   tempPath.push(path.pop());
	   for (int row = 0; row < arr.length; row++) {
		   if (!(arr[row][0]==null)) {
			   String a=arr[row][0].toString();
			   if (a.equals(path.peek())) {
				   controlLine = arr[row][2].toString();
			   }
		   }


	   }
	   Boolean flag=false;
	   String Line=null;
	   int tourCounter=0;
	   while(path.size()>1) {
		   origin=vertices.getValue(path.peek());
		   arr=((Vertex<T>)origin).display();
		   tempPath.push(path.pop());

		   for (int row = 0; row < arr.length; row++) {
			   if (!(arr[row][0]==null) ) {
				   String b=arr[row][0].toString();
				   if (b.equals(path.peek())) {
					   if (!arr[row][2].toString().equals(controlLine)) {
						   Line = controlLine;
						   controlLine = arr[row][2].toString();
						   flag = true;
					   }
				   }
			   }

		   }
		   if (flag){
			   System.out.println("Line: "+Line);
			   String endStation;
			   String beginStation;

			   if (tourCounter==0){
				   int size= tempPath.size()-1;
				   endStation=tempPath.pop().toString();
				   while(tempPath.size()>1){
					   tempPath.pop();
				   }
				   beginStation=tempPath.pop().toString();
				   tourCounter++;
				   System.out.println(beginStation+" - "+endStation+" ("+size+" stations) ");
				   tempBeginStation=endStation;
			   }
			   else {
				   int size = tempPath.size();
				   endStation = tempPath.pop().toString();
				   while (!tempPath.isEmpty()) {
					   tempPath.pop();
				   }
				   System.out.println(tempBeginStation+" - "+endStation+" ("+size+" stations) ");
				   tempBeginStation=endStation;

			   }

			   flag=false;

		   }
	   }
	   tempPath.push(path.pop());
	   if (!flag){
		   System.out.println("Line: "+controlLine);
		   String endStation;
		   int size= tempPath.size();
		   endStation=tempPath.pop().toString();
		   while(!tempPath.isEmpty()){
			   tempPath.pop();
		   }

		   System.out.println(tempBeginStation+" - "+endStation+" ("+size+" stations) ");
	   }
	   pathCost=(double)(pathCost/60);
	   System.out.println(pathCost+" min");
	   return path;
   }

	
   private class EntryPQ implements Comparable<EntryPQ>
	{
		private VertexInterface<T> vertex; 	
		private VertexInterface<T> previousVertex; 
		private double cost; // cost to nextVertex
		
		private EntryPQ(VertexInterface<T> vertex, double cost, VertexInterface<T> previousVertex)
		{
			this.vertex = vertex;
			this.previousVertex = previousVertex;
			this.cost = cost;
		} // end constructor
		
		@SuppressWarnings("unused")
		public VertexInterface<T> getVertex()
		{
			return vertex;
		} // end getVertex
		
		public VertexInterface<T> getPredecessor()
		{
			return previousVertex;
		} // end getPredecessor

		public double getCost()
		{
			return cost;
		} // end getCost
		
		public int compareTo(EntryPQ otherEntry)
		{
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int)Math.signum(otherEntry.cost - cost);
		} // end compareTo
		
		public String toString()
		{
			return vertex.toString() + " " + cost;
		} // end toString 
	} // end EntryPQ

} // end DirectedGraph
