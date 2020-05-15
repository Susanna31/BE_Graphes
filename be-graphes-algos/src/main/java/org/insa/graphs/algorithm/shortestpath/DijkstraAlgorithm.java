package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
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
        
        ArrayList<Label> listeLabel = new ArrayList<Label>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        Label label;
        Label successeur; //a enlever et definir dans le for
        //successeurs
        for (Node iNode : this.data.getGraph().getNodes()) {
        	
        }
        
        for (Node iNode : this.data.getGraph().getNodes()) {
        	if (iNode != this.data.getOrigin()) {
        		Label iLabel = new Label(iNode);
        		listeLabel.add(iLabel);
        	}
        }
        
        Label labelOrigine = new Label(this.data.getOrigin());
        labelOrigine.cout = 0;
        tas.insert(labelOrigine);
        
        
        //iterations
        
        while (1) { //il existe des sommets non marqus
        	label = tas.findMin();
        	label.marque = true;
        	for (Label iLabel : listeLabel) { //pour tous les y successeurs de x
        		if (successeur.marque = false) {
        			successeur.cout = min(successeur.cout, label.cout); //+cout trajet x a y )
        			if () { //cout success a ete mis a jour
        				tas.insert(successeur);
        				//mettre a jour pere successeur
        			}
        		}
        	}
        }
        
        return solution;
    }

}
