package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import java.util.ArrayList;

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
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        ArrayList<Arc> listeArcs = new ArrayList<Arc>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        Label label;
        int N = graph.size();
        int nbMarqueTrue = 0;
        
        
       
       
        for (Node iNode : graph.getNodes()) {
        	if (iNode != data.getOrigin()) {
        		listeLabel.add(new Label(iNode));
        	}
        }
        
        //reprendre ici
        
        label = new Label(data.getOrigin());
        label.cout = 0;
        tas.insert(label);
        
       
        //iterations
        
        while (nbMarqueTrue < N) { //il existe des sommets non marques
        	label = tas.deleteMin();
            label.marque = true;
            nbMarqueTrue ++;
        	for (Arc successeur : label.sommet.getSuccessors()) { //pour tous les y successeurs de x
        		for (Label iLabel : listeLabel) {
        			if (iLabel.sommet == successeur.getDestination()) {
        				if (iLabel.marque = false) {
        					if (Math.min(iLabel.cout, label.cout + data.getCost(successeur)) != iLabel.cout) {
        						iLabel.cout = Math.min(iLabel.cout, label.cout + data.getCost(successeur));
        						tas.remove(iLabel);
        						tas.insert(iLabel);
        					}
        					
        				}
        			}
        		}
        		listeArcs.add(successeur);
        	}
        }
        
        Path solutionPath = new Path(graph, listeArcs);
        
        
        if (solutionPath.getDestination() != data.getDestination()) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE, solutionPath);
        }
        else {
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, solutionPath);
        }
        
        return solution;
    }

}
