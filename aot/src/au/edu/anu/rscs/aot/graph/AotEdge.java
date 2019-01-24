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

import java.util.List;
import java.util.Objects;
import java.util.Set;

import au.edu.anu.rscs.aot.graph.property.Property;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.SimpleEdgeImpl;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.PropertyListSetters;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.ResizeablePropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;
import fr.ens.biologie.generic.Labelled;
import fr.ens.biologie.generic.Named;
import fr.ens.biologie.generic.NamedAndLabelled;
import fr.ens.biologie.generic.Sealable;


/**
 * reimplementation of AOTEdge
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
public class AotEdge extends SimpleEdgeImpl 
		implements ExtendablePropertyList, NamedAndLabelled {

	private static String defaultLabel = "AOTEdge";
	
	// this holds the properties
	private ExtendablePropertyList properties;
	// the name
	private String name = null;
	// the label - remember that label+name = uniqueID within the graph context
	private String label = null;

	// ----------------------------- Constructors - all protected
	
	/**
	 * Constructor with no properties
	 * @param start the start node
	 * @param end the end node
	 * @param factory the AotGraph
	 */
	protected AotEdge(Node start, Node end, AotGraph factory) {
		super(start,end,factory);
		properties = new ExtendablePropertyListImpl();
	}
	
	/**
	 * Constructor with properties
	 * @param start the start node
	 * @param end the end node
	 * @param properties the property list (copied)
	 * @param factory the AotGraph
	 */
	protected AotEdge(Node start, Node end, ReadOnlyPropertyList properties, AotGraph factory) {
		this(start,end,factory);
		this.properties.addProperties(properties);
	}

	// -----------------------ExtendablePropertyList
	@Override
	public ResizeablePropertyList addProperties(List<String> keys) {
		properties.addProperties(keys);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperties(String... keys) {
		properties.addProperties(keys);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperties(ReadOnlyPropertyList list) {
		properties.addProperties(list);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(Property property) {
		properties.addProperty(property);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(String key) {
		properties.addProperty(key);
		return properties;
	}

	@Override
	public ResizeablePropertyList addProperty(String key, Object value) {
		properties.addProperty(key, value);
		return properties;
	}

	@Override
	public Object getPropertyValue(String key, Object defaultValue) {
		return properties.getPropertyValue(key, defaultValue);
	}

	@Override
	public ResizeablePropertyList removeAllProperties() {
		properties.removeAllProperties();
		return properties;
	}

	@Override
	public ResizeablePropertyList removeProperty(String key) {
		properties.removeProperty(key);
		return properties;
	}
	
	@Override
	public SimplePropertyList clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyListSetters setProperty(String arg0, Object arg1) {
		return properties.setProperty(arg0, arg1);
	}

	@Override
	public Set<String> getKeysAsSet() {
		return properties.getKeysAsSet();
	}

	@Override
	public Object getPropertyValue(String arg0) {
		return properties.getPropertyValue(arg0);
	}

	@Override
	public boolean hasProperty(String arg0) {
		return properties.hasProperty(arg0);
	}

	@Override
	public int size() {
		return properties.size();
	}

	@Override
	public Sealable seal() {
		return properties.seal();
	}

	@Override
	public boolean isSealed() {
		return properties.isSealed();
	}

	// -----------------------------NamedAndLabelled
	@Override
	public String getName() {
		return name;
	}

	/**
	 * NOTE: name can only be set once, since it is used as unique ID in equality tests,
	 * on which sets base their unicity of element constraint.
	 */
	@Override
	public Named setName(String name) {	
		if (this.name==null)
			this.name = name;
		return this;
	}

	@Override
	public boolean sameName(Named item) {
		return hasName(item.getName());
	}

	@Override
	public boolean hasName(String name) {
		return Objects.equals(this.name, name);
	}

	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * NOTE: label can only be set once, since it is used as unique ID in equality tests,
	 * on which sets base their unicity of element constraint.
	 */
	@Override
	public Labelled setLabel(String label) {
		if (this.label==null)
			this.label = label;
		return this;
	}

	@Override
	public boolean sameLabel(Labelled item) {
		return hasLabel(item.getLabel());
	}

	@Override
	public boolean hasLabel(String label) {
		return Objects.equals(this.label, label);
	}

	// ---------------------------Identifiable (from both Node and TreeNode). 
	@Override
	public String classId() {
		if (label==null)
			return defaultLabel;
		return label;
	}

	@Override
	public String instanceId() {
		if (name==null)
			return "";
		return name;
	}

}
