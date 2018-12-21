/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package au.edu.anu.rscs.aot.graph;

import java.util.Collection;
import java.util.List;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.GraphElementFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.ResizeablePropertyList;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;
import fr.ens.biologie.generic.NamedAndLabelled;

/**
 * reimplementation of AOTEdge
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
public class AotEdge implements Edge, ResizeablePropertyList, NamedAndLabelled {

	@Override
	public ResizeablePropertyList addProperties(List<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList addProperties(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList addProperty(Property arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList addProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList addProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList removeAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResizeablePropertyList removeProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element addConnectionsLike(Element arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element disconnect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphElementFactory factory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Node> traversal(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends Node> traversal(int arg0, Direction arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String instanceId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node endNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node otherNode(Node arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge setEndNode(Node arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge setStartNode(Node arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node startNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Named setName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sameName(Named item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasName(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Labelled setLabel(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sameLabel(Labelled item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasLabel(String label) {
		// TODO Auto-generated method stub
		return false;
	}

}
