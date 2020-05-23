package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label implements Comparable<Label>{
	
	private Node sommet;
	protected boolean marque;
	protected double cout;
	protected Arc pere;
	protected boolean presentTas;
	protected double coutEsti;
	
	
	public LabelStar(Node sommet, double dist) {
		super(sommet);
		this.marque = false;
		this.cout = Double.POSITIVE_INFINITY;
		this.pere = null;
		this.presentTas = false;
		this.coutEsti = dist;
	}
	
	public double getCoutEsti() {
		return this.coutEsti;
	}
	
	public double getTotalCost() {
		return (this.cout + this.coutEsti);
	}
	
	@Override
	public int compareTo(Label compareTo) {
		
		if (this.getTotalCost() == compareTo.getTotalCost()) {
			if (this.getCoutEsti() < compareTo.getCoutEsti()) {
				return -1;
			}
			if (this.getCoutEsti() > compareTo.getCoutEsti()) {
				return 1;
			}
			else {
				return 0;
			}
		}
		
		else if (this.getTotalCost() < compareTo.getTotalCost()) {
			return -1;
		}
		
		else {
			return 1;
		}
	}
		
}
