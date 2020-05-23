package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import java.util.ArrayList;
import java.util.Collections;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public Label createLabel(Node node, double dist) {
    	return new Label(node);
    }
   

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // TODO:
        
        //initialisation
        
        Graph graph = data.getGraph();
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        int N = graph.size();
        Label labelX;
        Label labelY;
        Label[] listeLabel = new Label[N];
        Arc[] listeArcs = new Arc[N];
       
        for (Node iNode : graph.getNodes()) {
        	//listeLabel[iNode.getId()] = new Label(iNode);
        	if (data.getMode() == AbstractInputData.Mode.LENGTH) {
        		double dist = iNode.getPoint().distanceTo(data.getDestination().getPoint());
        		listeLabel[iNode.getId()] = createLabel(iNode, dist);
        	}
        	else if (data.getMode() == AbstractInputData.Mode.TIME){
        		double dist = ((iNode.getPoint().distanceTo(data.getDestination().getPoint()))/graph.getGraphInformation().getMaximumSpeed());
        		System.out.println(data.getMaximumSpeed());
        		listeLabel[iNode.getId()] = createLabel(iNode, dist);
        	}
        }

        listeLabel[data.getOrigin().getId()].cout = 0;
        tas.insert(listeLabel[data.getOrigin().getId()]);
        listeLabel[data.getOrigin().getId()].presentTas = true;
        
        //notification node origine atteint
        notifyOriginProcessed(data.getOrigin());
        

        while ((!tas.isEmpty()) && (listeLabel[data.getDestination().getId()].marque != true)) {
        	labelX = tas.deleteMin();
        	listeLabel[labelX.getSommet().getId()].marque = true;
        	//verification cout croissant
        	//System.out.println(labelX.cout);
        	//notification node marque
        	notifyNodeMarked(labelX.getSommet());
        	for (Arc successeur : labelX.getSommet().getSuccessors()) {
        		if (!data.isAllowed(successeur)) {
                    continue;
                }
        		labelY = listeLabel[successeur.getDestination().getId()];
        		if (labelY.marque != true) {
        			double costX = labelX.cout;
        			double w = data.getCost(successeur);
        			double oldCost = labelY.cout;
        			if (labelY.cout > (costX + w)) {
        				labelY.cout = costX+w;
        				//notification node atteint premiere fois
        				if (Double.isInfinite(oldCost) && Double.isFinite(labelY.cout)) {
                            notifyNodeReached(successeur.getDestination());
                        }
        				if (labelY.presentTas) {
        					tas.remove(labelY);
        					tas.insert(labelY);
        				}
        				else {
        					tas.insert(labelY);
        					listeLabel[labelY.getSommet().getId()].presentTas = true;
        				}
        				listeArcs[labelY.getSommet().getId()] = successeur;
        			}
        		}
        	}
        	
        }
        
        if (listeArcs[data.getDestination().getId()] == null) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	//notification destination atteinte
        	notifyDestinationReached(data.getDestination());
        	
        	ArrayList<Arc> arcList = new ArrayList<>();
        	Arc arc = listeArcs[data.getDestination().getId()];
        	while (arc != null) {
        		arcList.add(arc);
        		arc = listeArcs[arc.getOrigin().getId()];
        	}
        
        	Collections.reverse(arcList);
        
    
        	Path solutionPath = new Path(graph, arcList);
        
        
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        }
        
        if (solution.getPath().isValid()) {
        	System.out.println("Chemin valide");
        }
        else {
        	System.out.println("Chemin invalide");
        }

        
        return solution;
    }

}
