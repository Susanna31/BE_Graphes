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
        ArrayList<Arc> arcList = new ArrayList<>();
        int compteur_iterations = 0;
       
        for (Node iNode : graph.getNodes()) {
        	//listeLabel[iNode.getId()] = new Label(iNode);
        	
        	if (data.getMode() == AbstractInputData.Mode.LENGTH) {
        		double dist = (double)iNode.getPoint().distanceTo(data.getDestination().getPoint());
        		listeLabel[iNode.getId()] = createLabel(iNode, dist);
        	}
        	else if (data.getMode() == AbstractInputData.Mode.TIME){
        		if (((data.getMaximumSpeed() == -1) || (data.getMaximumSpeed() == GraphStatistics.NO_MAXIMUM_SPEED)) && ((graph.getGraphInformation().getMaximumSpeed() != -1)) && ((graph.getGraphInformation().getMaximumSpeed()) != (GraphStatistics.NO_MAXIMUM_SPEED))) {
        			double dist = ((iNode.getPoint().distanceTo(data.getDestination().getPoint()))/graph.getGraphInformation().getMaximumSpeed());
        			listeLabel[iNode.getId()] = createLabel(iNode, dist);
        		}
        		else if (((graph.getGraphInformation().getMaximumSpeed() == -1) || (graph.getGraphInformation().getMaximumSpeed() == GraphStatistics.NO_MAXIMUM_SPEED)) && ((data.getMaximumSpeed() != -1) && (data.getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED))) {
        			double dist = ((iNode.getPoint().distanceTo(data.getDestination().getPoint()))/data.getMaximumSpeed());
        			listeLabel[iNode.getId()] = createLabel(iNode, dist);
        		}
        		else if ((data.getMaximumSpeed() != -1) && (graph.getGraphInformation().getMaximumSpeed() != -1) && (data.getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED) && (graph.getGraphInformation().getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED)){
        			double dist = ((iNode.getPoint().distanceTo(data.getDestination().getPoint()))/Math.min(data.getMaximumSpeed(),graph.getGraphInformation().getMaximumSpeed()));
        			listeLabel[iNode.getId()] = createLabel(iNode, dist);
        		}
        		else {
        			double dist = ((iNode.getPoint().distanceTo(data.getDestination().getPoint()))/((130.0*1000.0)/(3600.0)));
        			listeLabel[iNode.getId()] = createLabel(iNode, dist);
        		}
        	}
        }

        listeLabel[data.getOrigin().getId()].cout = 0.0;
        tas.insert(listeLabel[data.getOrigin().getId()]);
        listeLabel[data.getOrigin().getId()].presentTas = true;
        
        //notification node origine atteint
        notifyOriginProcessed(data.getOrigin());
        

        while ((!tas.isEmpty()) && (listeLabel[data.getDestination().getId()].marque != true)) {
        	compteur_iterations++;
        	//on extrait le minimum du tas
        	labelX = tas.deleteMin();
        	//on verifie que le cout des labels marques est croissant au cours du temps
        	System.out.println("Cout label : " + labelX.getCout());
        	listeLabel[labelX.getSommet().getId()].marque = true;
        	notifyNodeMarked(labelX.getSommet());
        	for (Arc successeur : labelX.getSommet().getSuccessors()) {
        		if (!data.isAllowed(successeur)) {
                    continue;
                }
        		labelY = listeLabel[successeur.getDestination().getId()];
        		if (labelY.marque != true) {
        			double costX = (labelX.getTotalCost());
        			System.out.println("costX " + labelX.getTotalCost());
        			System.out.println("coutEstiX " + labelX.getCoutEsti());
        			System.out.println("costX " + labelX.getCout());
        			double w = data.getCost(successeur);
        			System.out.println("w " + w);
        			double oldCost = (labelY.getTotalCost());
        			System.out.println("oldCost " + labelY.getTotalCost());
        			if (oldCost > (costX + w)) {
        				labelY.cout = labelX.getCout() + w;
        				//notification node atteint premiere fois
        				//if (Double.isInfinite(oldCost) && Double.isFinite(labelY.cout)) {
                            //notifyNodeReached(successeur.getDestination());
                        //}
        				if (labelY.presentTas) {
        					tas.remove(labelY);
        					tas.insert(labelY);
        				}
        				else {
        					tas.insert(labelY);
        					listeLabel[labelY.getSommet().getId()].presentTas = true;
        				}
        				listeArcs[labelY.getSommet().getId()] = successeur;
        				if (Double.isInfinite(oldCost) && Double.isFinite(labelY.cout)) {
                            notifyNodeReached(successeur.getDestination());
                        }
        			}
        		}
        	}
        	//evolution taille du tas
        	System.out.println("Taille tas : " + tas.size());
        }
        
        if (listeArcs[data.getDestination().getId()] == null) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	//notification destination atteinte
        	notifyDestinationReached(data.getDestination());
        	
        	//ArrayList<Arc> arcList = new ArrayList<>();
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

        //afficher nombre iterations et arcs du chemin
        System.out.println("Iterations : " + compteur_iterations + "Arcs : " + arcList.size());
        
        return solution;
    

    }
}
