archetype archetype
// NB: all fixes marked with comments made by JG Sept-Nov 2016 (1) to allow for the
// new dsl convention: default _child edge labels
// (2) to enable properties on edges, which were apparently omitted before.

    exclusive

    hasNode
        reference: "archetype:"
        multiplicity: 1..*
//        hasEdge hasNode  // now the label would be "_child"
        hasEdge
            toNode: "archetype:/hasNode:" 
            multiplicity: 1..*

    hasNode
        reference: "archetype:/exclusive:"
        multiplicity: 0..1

    hasNode
        reference: "archetype:/hasNode:/mustSatisfyQuery:"
        multiplicity: 0..*
        hasProperty className
            type: String 
            multiplicity: 1..1
    
    hasNode
        reference: "archetype:/hasNode:/hasEdge:/mustSatisfyQuery:"
        multiplicity: 0..*
        hasProperty className
            type: String 
            multiplicity: 1..1
    
    hasNode
//        reference: "archetype:/hasNode:/hasProperty:/mustSatisfyQuery:"
        reference: "hasProperty:/mustSatisfyQuery:"
        multiplicity: 0..*
        hasProperty className
            type: String 
            multiplicity: 1..1
        
    hasNode
        reference: "archetype:/hasNode:"
        multiplicity: 0..*
        hasProperty reference
            type: String 
            multiplicity: 1..1
        hasProperty multiplicity
            type: IntegerRange
            multiplicity: 0..1
//        hasEdge hasEdge
        hasEdge _child
            toNode: "hasEdge:"
            multiplicity: 0..*

    hasNode
        reference: "archetype:/hasNode:/hasEdge:"
        hasProperty toNode
            type: String
            multiplicity: 1..1
        hasProperty multiplicity
            type: IntegerRange 
            multiplicity: 0..1

    hasNode
        reference: "archetype:/hasNode:/hasProperty:"
        hasProperty type
            type: String
            multiplicity: 1..1
        hasProperty multiplicity
            type: IntegerRange 
            multiplicity: 0..1
    
// this added by JG 4/11/2016 to allow for the definition of properties on edges            
    hasNode
        reference: "archetype:/hasNode:/hasEdge:/hasProperty:"
        hasProperty type
            type: String
            multiplicity: 1..1
        hasProperty multiplicity
            type: IntegerRange 
            multiplicity: 0..1
     
            
      