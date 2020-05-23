package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import java.util.ArrayList;
import java.util.Collections;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
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
        	listeLabel[iNode.getId()] = new Label(iNode);
        }

        listeLabel[data.getOrigin().getId()].cout = 0;
        tas.insert(listeLabel[data.getOrigin().getId()]);
        listeLabel[data.getOrigin().getId()].presentTas = true;
        

        while ((!tas.isEmpty()) && (listeLabel[data.getDestination().getId()].marque != true)) {
        	labelX = tas.deleteMin();
        	listeLabel[labelX.getSommet().getId()].marque = true;
        	for (Arc successeur : labelX.getSommet().getSuccessors()) {
        		if (!data.isAllowed(successeur)) {
                    continue;
                }
        		labelY = listeLabel[successeur.getDestination().getId()];
        		if (labelY.marque != true) {
        			double costX = labelX.cout;
        			double w = data.getCost(successeur);
        			if (labelY.cout > (costX + w)) {
        				labelY.cout = costX+w;
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
        
        ArrayList<Arc> arcList = new ArrayList<>();
        Arc arc = listeArcs[data.getDestination().getId()];
        while (arc != null) {
            arcList.add(arc);
            arc = listeArcs[arc.getOrigin().getId()];
        }
        
        Collections.reverse(arcList);
        
    
        Path solutionPath = new Path(graph, arcList);
        
        
        solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        

        return solution;
    }

}
