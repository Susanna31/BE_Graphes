package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class Label implements Comparable<Label> {
	
	protected Node sommet;
	protected boolean marque;
	protected double cout;
	protected Arc pere;
	
	public Label(Node sommet) {
		this.sommet = sommet;
		this.marque = false;
		this.cout = Double.POSITIVE_INFINITY;
		this.pere = null;
	}
	
	public double getCout() {
		return this.cout;
	}
	
	public int compareTo(Label compareTo) {
		
		if (this.cout == compareTo.cout) {
			return 0;
		}
		
		else if (this.cout <= compareTo.cout) {
			return -1;
		}
		
		else {
			return 1;
		}
	}
}
