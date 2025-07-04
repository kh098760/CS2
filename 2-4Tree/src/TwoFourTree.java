public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
        	if(values == 1)
        		return true;
        	else
        		return false;
        }

        public boolean isThreeNode() {
        	if(values == 2)
        		return true;
        	else
        		return false;
        }

        public boolean isFourNode() {
        	if(values == 3)
        		return true;
        	else
        		return false;
        }

        public boolean isRoot() {
        	if(parent == null)
        		return true;
        	else
        		return false;
        }
        
        public boolean isLeaf() {
        	if(isFourNode() == true) 
        		return leftChild == null && centerLeftChild == null && centerRightChild == null && rightChild == null;
        	if(isThreeNode() == true) 
        		return leftChild == null && centerChild == null && rightChild == null;
        	else
        		return leftChild == null && rightChild == null;
        }

        public TwoFourTreeItem(int value1) {
        	this.values = 1;
        	this.value1 = value1; 
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.values = 3;
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
        
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~MAJOR_HELPERFUNCTIONS(SPLIT/MERGE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

        //helper function to split four node on the way down
        public void split(TwoFourTreeItem node) {
        	
        	if(node == root) {
        		//creating new parent to be root
        		TwoFourTreeItem newParent = new TwoFourTreeItem(node.value2);
        		//creating new right node to be parent's right child and take node's right-side children
        		TwoFourTreeItem newRight = new TwoFourTreeItem(node.value3);
        		newRight.parent = newParent;
        		newParent.rightChild = newRight;
        		if(node.rightChild != null) {
    	    		node.rightChild.parent = newRight;
    	    		newRight.rightChild = node.rightChild;
        		}
        		if(node.centerRightChild != null) {
    	    		node.centerRightChild.parent = newRight;
    	    		newRight.leftChild = node.centerRightChild;
        		}
        		//turning node into parent's left child node with left-half children
        		node.value2 = 0;
        		node.value3 = 0;
        		node.values = 1;
        		node.rightChild = node.centerLeftChild;
        		node.centerChild = null;
        		node.centerLeftChild = null;
        		node.centerRightChild = null;
        		node.parent = newParent;
        		newParent.leftChild = node;
        		root = newParent;
      
        		//setting leaf with isLeaf()
        		root.isLeaf = root.isLeaf();
        		root.leftChild.isLeaf = root.leftChild.isLeaf();
        		root.rightChild.isLeaf = root.rightChild.isLeaf();
        	}		
        	
        	else if(node != root) {
        		if(node.parent.isThreeNode()) {
        			if(node.parent.leftChild == node) {
        				//updating Three-Node parent values to Four-Node
        				node.parent.values++;
        				node.parent.value3 = node.parent.value2;
        				node.parent.value2 = node.parent.value1;
        				node.parent.value1 = node.value2;
        				//creating parent's new center left child with node's right value and taking node right-side children
        				TwoFourTreeItem newCenterLeft = new TwoFourTreeItem(node.value3);
        				newCenterLeft.parent = node.parent;
        				node.parent.centerLeftChild = newCenterLeft; 
        				if(node.rightChild != null) {
    	    				node.rightChild.parent = newCenterLeft;
    	    				newCenterLeft.rightChild = node.rightChild;
        				}
        				if(node.centerRightChild != null) {
    	    				node.centerRightChild.parent = newCenterLeft;
    	    				newCenterLeft.leftChild = node.centerRightChild;
        				}
        				//turning node into parent's left child with updated values and children
        				node.value3 = 0;
        				node.value2 = 0;
        				node.values = 1;
        				node.rightChild = node.centerLeftChild;
        				node.centerLeftChild = null;
        				node.centerRightChild = null;
        				node.centerChild = null;
        				//updating parent children
        				node.parent.centerRightChild = node.parent.centerChild;
        				node.parent.centerChild = null;
        				//setting leaf values
        				node.parent.isLeaf = node.parent.isLeaf();
        				node.isLeaf = node.isLeaf();
        				newCenterLeft.isLeaf = newCenterLeft.isLeaf();
        				/*
        				node.leftChild.isLeaf = node.leftChild.isLeaf();
        				node.rightChild.isLeaf = node.rightChild.isLeaf();
        				newRightNode.leftChild.isLeaf = newRightNode.leftChild.isLeaf();
        				newRightNode.rightChild.isLeaf = newRightNode.rightChild.isLeaf();
        				*/
        			}
        			
        			if(node.parent.centerChild == node) {
        				//updating Three-Node parent values to Four-Node
        				node.parent.values++;
        				node.parent.value3 = node.parent.value2;
        				node.parent.value2 = node.value2;
        				//creating parent's new center left child with node's right value and taking node right-side children
        				TwoFourTreeItem newCenterRight = new TwoFourTreeItem(node.value3);
        				newCenterRight.parent = node.parent;
        				node.parent.centerRightChild = newCenterRight;
        				if(node.rightChild != null) {
        					node.rightChild.parent = newCenterRight;
            				newCenterRight.rightChild = node.rightChild;
        				}
        				if(node.centerRightChild != null) {
    	    				node.centerRightChild.parent = newCenterRight;
    	    				newCenterRight.leftChild = node.centerRightChild;
        				}
        				//turning node into parent's center left child with updated values and children
        				node.value2 = 0;
        				node.value3 = 0;
        				node.values = 1;
        				node.rightChild = node.centerLeftChild;
        				node.centerLeftChild = null;
        				node.centerRightChild = null;
        				node.centerChild = null;
        				//updating parent children
        				node.parent.centerLeftChild = node;
        				node.parent.centerChild = null;
        				//setting leaf values
        				node.parent.isLeaf = node.parent.isLeaf();
        				node.isLeaf = node.isLeaf();
        				newCenterRight.isLeaf = newCenterRight.isLeaf();

        			}
        			
        			if(node.parent.rightChild == node) {
        				//updating Three-Node parent values to Four-Node
        				node.parent.values++;
        				node.parent.value3 = node.value2;
        				//creating parent's new right child with node's right value and taking node right-side children
        				TwoFourTreeItem newRight = new TwoFourTreeItem(node.value3);
        				newRight.parent = node.parent;
        				node.parent.rightChild = newRight;
        				if(node.rightChild != null) {
    	    				node.rightChild.parent = newRight;
    	    				newRight.rightChild = node.rightChild;
        				}
        				if(node.centerRightChild != null) {
    	    				node.centerRightChild.parent = newRight;
    	    				newRight.leftChild = node.centerRightChild;
        				}
        				//turning node into parent's center right child with updated values and children
        				node.value2 = 0;
        				node.value3 = 0;
        				node.values = 1;
        				node.rightChild = node.centerLeftChild;
        				node.centerLeftChild = null;
        				node.centerRightChild = null;
        				node.centerChild = null;
        				//updating parent children
        				node.parent.centerLeftChild = node.parent.centerChild;
        				node.parent.centerChild = null;
        				node.parent.centerRightChild = node;
        				//setting leaf values
        				node.parent.isLeaf = node.parent.isLeaf();
        				node.isLeaf = node.isLeaf();
        				newRight.isLeaf = newRight.isLeaf();
        			}
        		}
        		
        		else {
        			if(node.parent.leftChild == node) {
        				//updating Two-Node parent values to Three-Node
        				node.parent.values++;
        				node.parent.value2 = node.parent.value1;
        				node.parent.value1 = node.value2;
        				//creating parent's new center child to take right half of node
        				TwoFourTreeItem newCenter = new TwoFourTreeItem(node.value3);
        				node.parent.centerChild = newCenter;
        				newCenter.parent = node.parent;
        				if(node.rightChild != null) {
        					newCenter.rightChild = node.rightChild;
        					node.rightChild.parent = newCenter;
        				}
        				if(node.centerRightChild != null) {
        					newCenter.leftChild = node.centerRightChild;
        					node.centerRightChild.parent = newCenter;
        				}
        				//turning node into Two-Node as parent's left child
        				node.value3 = 0;
        				node.value2 = 0;
        				node.values = 1;
        				node.rightChild = node.centerLeftChild;
        				node.centerRightChild = null;
        				node.centerChild = null;
        				node.centerLeftChild = null;
        				//updating leaf nodes
        				node.parent.isLeaf = node.parent.isLeaf();
        				node.isLeaf = node.isLeaf();
        				newCenter.isLeaf = newCenter.isLeaf();		
        			}
        			
        			else if(node.parent.rightChild == node) {
        				//updating Two-Node parent values to Three-Node
        				node.parent.values++;
        				node.parent.value2 = node.value2;
        				//creating parent's new right child to take right half of node
        				TwoFourTreeItem newRight = new TwoFourTreeItem(node.value3);
        				node.parent.rightChild = newRight;
        				newRight.parent = node.parent;
        				if(node.rightChild != null) {
        					newRight.rightChild = node.rightChild;
        					node.rightChild.parent = newRight;
        				}
        				if(node.centerRightChild != null) {
        					newRight.leftChild = node.centerRightChild;
        					node.centerRightChild.parent = newRight;
        				}
        				//turning node into Two-Node as parent's center child
        				node.value3 = 0;
        				node.value2 = 0;
        				node.values = 1;
        				node.parent.centerChild = node;
        				node.rightChild = node.centerLeftChild;
        				node.centerRightChild = null;
        				node.centerChild = null;
        				node.centerLeftChild = null;
        				//updating leaf nodes
        				node.parent.isLeaf = node.parent.isLeaf();
        				node.isLeaf = node.isLeaf();
        				newRight.isLeaf = newRight.isLeaf();	

        			}
        		}
        			
        	}
        }
    }
    

    
    //helper function to merge two node on the way down
    public void merge(TwoFourTreeItem node) {    	
    	//merging two-node root with two-node children
    	if (node.isRoot() && node.isTwoNode() && node.leftChild != null && node.rightChild != null) {
    	    if (node.leftChild.isTwoNode() && node.rightChild.isTwoNode()) {
    	        // turning new root into four-node
    	        TwoFourTreeItem newRoot = new TwoFourTreeItem(node.leftChild.value1, node.value1,node.rightChild.value1);
    	        if (node.leftChild.leftChild != null) {
    	            newRoot.leftChild = node.leftChild.leftChild;
    	            node.leftChild.leftChild.parent = newRoot;
    	        }
    	        if (node.leftChild.rightChild != null) {
    	            newRoot.centerLeftChild = node.leftChild.rightChild;
    	            node.leftChild.rightChild.parent = newRoot;
    	        }
    	        if (node.rightChild.leftChild != null) {
    	            newRoot.centerRightChild = node.rightChild.leftChild;
    	            node.rightChild.leftChild.parent = newRoot;
    	        }
    	        if (node.rightChild.rightChild != null) {
    	            newRoot.rightChild = node.rightChild.rightChild;
    	            node.rightChild.rightChild.parent = newRoot;
    	        }

    	        // setting new root and leaf nodes
    	        root = newRoot;
    	        node = root;
    	        root.isLeaf = root.isLeaf();
    	        }
    	}
		
		
		else if(node.parent.isRoot() && node.parent.isTwoNode()) {
			if(root.leftChild == node) {
				if(root.rightChild.isThreeNode()) {
					//rotate values to the left from right sibling to left node
					node.values = 2;
					node.value2 = root.value1;
					root.value1 = root.rightChild.value1;
					root.rightChild.value1 = root.rightChild.value2;
					root.rightChild.value2 = 0;
					root.rightChild.values = 1;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(root.rightChild.leftChild != null) {
						node.rightChild = root.rightChild.leftChild;
						root.rightChild.leftChild.parent = node;
					}
					root.rightChild.leftChild = root.rightChild.centerChild;
					root.rightChild.centerChild = null;
				}
				else if(root.rightChild.isFourNode()) {
					//rotate values to the left from right sibling to left node
					node.values = 2;
					node.value2 = root.value1;
					root.value1 = root.rightChild.value1;
					root.rightChild.value1 = root.rightChild.value2;
					root.rightChild.value2 = root.rightChild.value3;
					root.rightChild.value3 = 0;
					root.rightChild.values = 2;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(root.rightChild.leftChild != null) {
						node.rightChild = root.rightChild.leftChild;
						root.rightChild.leftChild.parent = node;
					}
					root.rightChild.leftChild = root.rightChild.centerLeftChild;
					root.rightChild.centerChild = root.rightChild.centerRightChild;
					root.rightChild.centerLeftChild = null;
					root.rightChild.centerRightChild = null;
				}
			}
			
			else if(root.rightChild == node) {
				if(root.leftChild.isThreeNode()) {
					//rotate values to the right from left sibling to left node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = root.value1;
					root.value1 = root.leftChild.value2;
					root.leftChild.value2 = 0;
					root.leftChild.values = 1;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(root.leftChild.rightChild != null) {
						node.leftChild = root.leftChild.rightChild;
						root.leftChild.rightChild.parent = node;
					}
					root.leftChild.rightChild = root.leftChild.centerChild;
					root.leftChild.centerChild = null;
				}
				else if(root.leftChild.isFourNode()) {
					//rotate values to the right from left sibling to left node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = root.value1;
					root.value1 = root.leftChild.value3;
					root.leftChild.value3 = 0;
					root.leftChild.values = 2;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(root.leftChild.rightChild != null) {
						node.leftChild = root.leftChild.rightChild;
						root.leftChild.rightChild.parent = node;
					}
					root.leftChild.rightChild = root.leftChild.centerRightChild;
					root.leftChild.centerChild = root.leftChild.centerLeftChild;
					root.leftChild.centerLeftChild = null;
					root.leftChild.centerRightChild = null;
				}
			}
		}
		
		
		else if(node.parent.isThreeNode()) {
			if(node.parent.leftChild == node) {
				if(node.parent.centerChild.isThreeNode()) {
					//rotate values to the left from right sibling to left node
					node.values = 2;
					node.value2 = node.parent.value1;
					node.parent.value1 = node.parent.centerChild.value1;
					node.parent.centerChild.value1 = node.parent.centerChild.value2;
					node.parent.centerChild.value2 = 0;
					node.parent.centerChild.values = 1;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.centerChild.leftChild != null) {
						node.rightChild = node.parent.centerChild.leftChild;
						node.parent.centerChild.leftChild.parent = node;
					}
					node.parent.centerChild.leftChild = node.parent.centerChild.centerChild;
					node.parent.centerChild.centerChild = null;
				}
				else if(node.parent.centerChild.isFourNode()) {
					//rotate values to the left from right sibling to left node
					node.values = 2;
					node.value2 = node.parent.value1;
					node.parent.value1 = node.parent.centerChild.value1;
					node.parent.centerChild.value1 = node.parent.centerChild.value2;
					node.parent.centerChild.value2 = node.parent.centerChild.value3;
					node.parent.centerChild.value3 = 0;
					node.parent.centerChild.values = 2;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.centerChild.leftChild != null) {
						node.rightChild = node.parent.centerChild.leftChild;
						node.parent.centerChild.leftChild.parent = node;
					}
					node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
					node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
					node.parent.centerChild.centerLeftChild = null;
					node.parent.centerChild.centerRightChild = null;
				}
				else if(node.parent.centerChild.isTwoNode()) {
					//take the parent value and center sibling value to left node
					node.values = 3;
					node.value2 = node.parent.value1;
					node.value3 = node.parent.centerChild.value1;
					node.parent.value1 = node.parent.value2;
					node.parent.value2 = 0;
					node.parent.values = 1;
					//take center sibling children and update parent left and right children
					node.centerLeftChild = node.rightChild;
					if(node.parent.centerChild.leftChild != null) {
						node.centerRightChild = node.parent.centerChild.leftChild;
						node.parent.centerChild.leftChild.parent = node;
					}
					if(node.parent.centerChild.rightChild != null) {
						node.rightChild = node.parent.centerChild.rightChild;
						node.parent.centerChild.rightChild.parent = node;
					}
					//fix parent children
					node.parent.centerChild = null;
				}
			}
			
			else if(node.parent.centerChild == node) {
				if(node.parent.leftChild.isThreeNode()) {
					//rotate values to the right from left sibling to center node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value1;
					node.parent.value1 = node.parent.leftChild.value2;
					node.parent.leftChild.value2 = 0;
					node.parent.leftChild.values = 1;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.leftChild.rightChild != null) {
						node.leftChild = node.parent.leftChild.rightChild;
						node.parent.leftChild.rightChild.parent = node;
					}
					node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
					node.parent.leftChild.centerChild = null;
				}
				else if(node.parent.leftChild.isFourNode()) {
					//rotate values to the right from left sibling to center node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value1;
					node.parent.value1 = node.parent.leftChild.value3;
					node.parent.leftChild.value3 = 0;
					node.parent.leftChild.values = 2;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.leftChild.rightChild != null) {
						node.leftChild = node.parent.leftChild.rightChild;
						node.parent.leftChild.rightChild.parent = node;
					}
					node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
					node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
					node.parent.leftChild.centerRightChild = null;
					node.parent.leftChild.centerLeftChild = null;
				}
				else if(node.parent.rightChild.isThreeNode()) {
					//rotate values to the left from right sibling to center node
					node.values = 2;
					node.value2 = node.parent.value2;
					node.parent.value2 = node.parent.rightChild.value1;
					node.parent.rightChild.value1 = node.parent.rightChild.value2;
					node.parent.rightChild.value2 = 0;
					node.parent.rightChild.values = 1;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.rightChild.leftChild != null) {
						node.rightChild = node.parent.rightChild.leftChild;
						node.parent.rightChild.leftChild.parent = node;
					}
					node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
					node.parent.rightChild.centerChild = null;
				}
				else if(node.parent.rightChild.isFourNode()) {
					//rotate values to the left from right sibling to center node
					node.values = 2;
					node.value2 = node.parent.value2;
					node.parent.value2 = node.parent.rightChild.value1;
					node.parent.rightChild.value1 = node.parent.rightChild.value2;
					node.parent.rightChild.value2 = node.parent.rightChild.value3;
					node.parent.rightChild.value3 = 0;
					node.parent.rightChild.values = 2;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.rightChild.leftChild != null) {
						node.rightChild = node.parent.rightChild.leftChild;
						node.parent.rightChild.leftChild.parent = node;
					}
					node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
					node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
					node.parent.rightChild.centerLeftChild = null;
					node.parent.rightChild.centerRightChild = null;
				}
				else if(node.parent.rightChild.isTwoNode()) {
					//take the parent value and right sibling value to center node
					node.values = 3;
					node.value2 = node.parent.value2;
					node.value3 = node.parent.rightChild.value1;
					node.parent.value2 = 0;
					node.parent.values = 1;
					//take right sibling children and update parent left and right children
					node.centerLeftChild = node.rightChild;
					if(node.parent.rightChild.leftChild != null) {
						node.centerRightChild = node.parent.rightChild.leftChild;
						node.parent.rightChild.leftChild.parent = node;
					}
					if(node.parent.rightChild.rightChild != null) {
						node.rightChild = node.parent.rightChild.rightChild;
						node.parent.rightChild.rightChild.parent = node;
					}
					//fix parent children
					node.parent.rightChild = node.parent.centerChild;
					node.parent.centerChild = null;
				}
			}
			
			else if(node.parent.rightChild == node) {
				if(node.parent.centerChild.isThreeNode()) {
					//rotate values to the right from left sibling to right node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value2;
					node.parent.value2 = node.parent.centerChild.value2;
					node.parent.centerChild.value2 = 0;
					node.parent.centerChild.values = 1;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.centerChild.rightChild != null) {
						node.leftChild = node.parent.centerChild.rightChild;
						node.parent.centerChild.rightChild.parent = node;
					}
					node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
					node.parent.centerChild.centerChild = null;
				}
				else if(node.parent.centerChild.isFourNode()) {
					//rotate values to the right from left sibling to right node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value2;
					node.parent.value2 = node.parent.centerChild.value3;
					node.parent.centerChild.value3 = 0;
					node.parent.centerChild.values = 2;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.centerChild.rightChild != null) {
						node.leftChild = node.parent.centerChild.rightChild;
						node.parent.centerChild.rightChild.parent = node;
					}
					node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
					node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
					node.parent.centerChild.centerRightChild = null;
					node.parent.centerChild.centerLeftChild = null;
				}
				else if(node.parent.centerChild.isTwoNode()) {
					//take the parent value and center sibling value to right node
					node.values = 3;
					node.value3 = node.value1;
					node.value2 = node.parent.value2;
					node.value1 = node.parent.centerChild.value1;
					node.parent.value2 = 0;
					node.parent.values = 1;
					//take center sibling children and update parent left and right children
					node.centerRightChild = node.leftChild;
					if(node.parent.centerChild.rightChild != null) {
						node.centerLeftChild = node.parent.centerChild.rightChild;
						node.parent.centerChild.rightChild.parent = node;
					}
					if(node.parent.centerChild.leftChild != null) {
						node.leftChild = node.parent.centerChild.leftChild;
						node.parent.centerChild.leftChild.parent = node;
					}
					//fix parent children
					node.parent.centerChild = null;
				}
			}
		}
		
		
		else if(node.parent.isFourNode()) {
			if(node.parent.leftChild == node) {
				if(node.parent.centerLeftChild.isThreeNode()) {
					//rotate values to the left from leftcenter sibling to left node
					node.values = 2;
					node.value2 = node.parent.value1;
					node.parent.value1 = node.parent.centerLeftChild.value1;
					node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
					node.parent.centerLeftChild.value2 = 0;
					node.parent.centerLeftChild.values = 1;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.centerLeftChild.leftChild != null) {
						node.rightChild = node.parent.centerLeftChild.leftChild;
						node.parent.centerLeftChild.leftChild.parent = node;
					}
					node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
					node.parent.centerLeftChild.centerChild = null;
				}
				else if(node.parent.centerLeftChild.isFourNode()) {
					//rotate values to the left from right sibling to left node
					node.values = 2;
					node.value2 = node.parent.value1;
					node.parent.value1 = node.parent.centerLeftChild.value1;
					node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
					node.parent.centerLeftChild.value2 = node.parent.centerLeftChild.value3;
					node.parent.centerLeftChild.value3 = 0;
					node.parent.centerLeftChild.values = 2;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.centerLeftChild.leftChild != null) {
						node.rightChild = node.parent.centerLeftChild.leftChild;
						node.parent.centerLeftChild.leftChild.parent = node;
					}
					node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
					node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerRightChild;
					node.parent.centerLeftChild.centerLeftChild = null;
					node.parent.centerLeftChild.centerRightChild = null;
				}
				else if(node.parent.centerLeftChild.isTwoNode()) {
					//take the parent value and centerleft sibling value to left node
					node.values = 3;
					node.value2 = node.parent.value1;
					node.value3 = node.parent.centerLeftChild.value1;
					node.parent.value1 = node.parent.value2;
					node.parent.value2 = node.parent.value3;
					node.parent.value3 = 0;
					node.parent.values = 2;
					//take center sibling children and update parent left and right children
					node.centerLeftChild = node.rightChild;
					if(node.parent.centerLeftChild.leftChild != null) {
						node.centerRightChild = node.parent.centerLeftChild.leftChild;
						node.parent.centerLeftChild.leftChild.parent = node;
					}
					if(node.parent.centerLeftChild.rightChild != null) {
						node.rightChild = node.parent.centerLeftChild.rightChild;
						node.parent.centerLeftChild.rightChild.parent = node;
					}
					//fix parent children 
					node.parent.centerLeftChild = null;
					node.parent.centerChild = node.parent.centerRightChild;
					node.parent.centerRightChild = null;
				}
			}
			
			else if(node.parent.centerLeftChild == node) {
				if(node.parent.leftChild.isThreeNode()) {
					//rotate values to the right from left sibling to centerleft node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value1;
					node.parent.value1 = node.parent.leftChild.value2;
					node.parent.leftChild.value2 = 0;
					node.parent.leftChild.values = 1;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.leftChild.rightChild != null) {
						node.leftChild = node.parent.leftChild.rightChild;
						node.parent.leftChild.rightChild.parent = node;
					}
					node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
					node.parent.leftChild.centerChild = null;
				}
				else if(node.parent.leftChild.isFourNode()) {
					//rotate values to the right from left sibling to centerleft node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value1;
					node.parent.value1 = node.parent.leftChild.value3;
					node.parent.leftChild.value3 = 0;
					node.parent.leftChild.values = 2;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.leftChild.rightChild != null) {
						node.leftChild = node.parent.leftChild.rightChild;
						node.parent.leftChild.rightChild.parent = node;
					}
					node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
					node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
					node.parent.leftChild.centerRightChild = null;
					node.parent.leftChild.centerLeftChild = null;
				}
				else if(node.parent.centerRightChild.isThreeNode()) {
					//rotate values to the left from centerright sibling to centerleft node
					node.values = 2;
					node.value2 = node.parent.value2;
					node.parent.value2 = node.parent.centerRightChild.value1;
					node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
					node.parent.centerRightChild.value2 = 0;
					node.parent.centerRightChild.values = 1;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.centerRightChild.leftChild != null) {
						node.rightChild = node.parent.centerRightChild.leftChild;
						node.parent.centerRightChild.leftChild.parent = node;
					}
					node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
					node.parent.centerRightChild.centerChild = null;
				}
				else if(node.parent.centerRightChild.isFourNode()) {
					//rotate values to the left from right sibling to center node
					node.values = 2;
					node.value2 = node.parent.value2;
					node.parent.value2 = node.parent.centerRightChild.value1;
					node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
					node.parent.centerRightChild.value2 = node.parent.centerRightChild.value3;
					node.parent.centerRightChild.value3 = 0;
					node.parent.centerRightChild.values = 2;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.centerRightChild.leftChild != null) {
						node.rightChild = node.parent.centerRightChild.leftChild;
						node.parent.centerRightChild.leftChild.parent = node;
					}
					node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerLeftChild;
					node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerRightChild;
					node.parent.centerRightChild.centerLeftChild = null;
					node.parent.centerRightChild.centerRightChild = null;
				}
				else if(node.parent.centerRightChild.isTwoNode()) {
					//take the parent value and right sibling value to center node
					node.values = 3;
					node.value2 = node.parent.value2;
					node.value3 = node.parent.centerRightChild.value1;
					node.parent.value2 = node.parent.value3;
					node.parent.value3 = 0;
					node.parent.values = 2;
					//take right sibling children and update parent left and right children
					node.centerLeftChild = node.rightChild;
					if(node.parent.centerRightChild.leftChild != null) {
						node.centerRightChild = node.parent.centerRightChild.leftChild;
						node.parent.centerRightChild.leftChild.parent = node;
					}
					if(node.parent.centerRightChild.rightChild != null) {
						node.rightChild = node.parent.centerRightChild.rightChild;
						node.parent.centerRightChild.rightChild.parent = node;
					}
					//fix parent children
					node.parent.centerChild = node.parent.centerLeftChild;
					node = node.parent.centerChild;
					node.parent.centerLeftChild = null;
					node.parent.centerRightChild = null;
				}
			}
			
			else if(node.parent.centerRightChild == node) {
				if(node.parent.centerLeftChild.isThreeNode()) {
					//rotate values to the right from leftcenter sibling to centerright node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value2;
					node.parent.value2 = node.parent.centerLeftChild.value2;
					node.parent.centerLeftChild.value2 = 0;
					node.parent.centerLeftChild.values = 1;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.centerLeftChild.rightChild != null) {
						node.leftChild = node.parent.centerLeftChild.rightChild;
						node.parent.centerLeftChild.rightChild.parent = node;
					}
					node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerChild;
					node.parent.centerLeftChild.centerChild = null;
				}
				else if(node.parent.centerLeftChild.isFourNode()) {
					//rotate values to the right from leftcenter sibling to centerright node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value2;
					node.parent.value2 = node.parent.centerLeftChild.value3;
					node.parent.centerLeftChild.value3 = 0;
					node.parent.centerLeftChild.values = 2;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.centerLeftChild.rightChild != null) {
						node.leftChild = node.parent.centerLeftChild.rightChild;
						node.parent.centerLeftChild.rightChild.parent = node;
					}
					node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerRightChild;
					node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerLeftChild;
					node.parent.centerLeftChild.centerRightChild = null;
					node.parent.centerLeftChild.centerLeftChild = null;
				}				
				else if(node.parent.rightChild.isThreeNode()) {
					//rotate values to the left from right sibling to centerright node
					node.values = 2;
					node.value2 = node.parent.value3;
					node.parent.value3 = node.parent.rightChild.value1;
					node.parent.rightChild.value1 = node.parent.rightChild.value2;
					node.parent.rightChild.value2 = 0;
					node.parent.rightChild.values = 1;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.rightChild.leftChild != null) {
						node.rightChild = node.parent.rightChild.leftChild;
						node.parent.rightChild.leftChild.parent = node;
					}
					node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
					node.parent.rightChild.centerChild = null;
				}
				else if(node.parent.rightChild.isFourNode()) {
					//rotate values to the left from right sibling to centerright node
					node.values = 2;
					node.value2 = node.parent.value3;
					node.parent.value3 = node.parent.rightChild.value1;
					node.parent.rightChild.value1 = node.parent.rightChild.value2;
					node.parent.rightChild.value2 = node.parent.rightChild.value3;
					node.parent.rightChild.value3 = 0;
					node.parent.rightChild.values = 2;
					//update node and sibling children
					node.centerChild = node.rightChild;
					if(node.parent.rightChild.leftChild != null) {
						node.rightChild = node.parent.rightChild.leftChild;
						node.parent.rightChild.leftChild.parent = node;
					}
					node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
					node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
					node.parent.rightChild.centerLeftChild = null;
					node.parent.rightChild.centerRightChild = null;
				}
				else if(node.parent.rightChild.isTwoNode()) {
					//take the parent value and right sibling value to center node
					node.values = 3;
					node.value2 = node.parent.value3;
					node.value3 = node.parent.rightChild.value1;
					node.parent.value3 = 0;
					node.parent.values = 2;
					//take right sibling children and update parent left and right children
					node.centerLeftChild = node.rightChild;
					if(node.parent.rightChild.leftChild != null) {
						node.centerRightChild = node.parent.rightChild.leftChild;
						node.parent.rightChild.leftChild.parent = node;
					}
					if(node.parent.rightChild.rightChild != null) {
						node.rightChild = node.parent.rightChild.rightChild;
						node.parent.rightChild.rightChild.parent = node;
					}
					//fix parent children
					node.parent.centerChild = node.parent.centerLeftChild;
					node.parent.rightChild = node.parent.centerRightChild;
					node = node.parent.rightChild;
					node.parent.centerLeftChild = null;
					node.parent.centerRightChild = null;
				}
			}
			
			else if(node.parent.rightChild == node) {
				if(node.parent.centerRightChild.isThreeNode()) {
					//rotate values to the right from rightcenter sibling to right node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value3;
					node.parent.value3 = node.parent.centerRightChild.value2;
					node.parent.centerRightChild.value2 = 0;
					node.parent.centerRightChild.values = 1;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.centerRightChild.rightChild != null) {
						node.leftChild = node.parent.centerRightChild.rightChild;
						node.parent.centerRightChild.rightChild.parent = node;
					}
					node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerChild;
					node.parent.centerRightChild.centerChild = null;
				}
				else if(node.parent.centerRightChild.isFourNode()) {
					//rotate values to the right from rightcenter sibling to right node
					node.values = 2;
					node.value2 = node.value1;
					node.value1 = node.parent.value3;
					node.parent.value3 = node.parent.centerRightChild.value3;
					node.parent.centerRightChild.value3 = 0;
					node.parent.centerRightChild.values = 2;
					//update node and sibling children
					node.centerChild = node.leftChild;
					if(node.parent.centerRightChild.rightChild != null) {
						node.leftChild = node.parent.centerRightChild.rightChild;
						node.parent.centerRightChild.rightChild.parent = node;
					}
					node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerRightChild;
					node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerLeftChild;
					node.parent.centerRightChild.centerRightChild = null;
					node.parent.centerRightChild.centerLeftChild = null;
				}
				else if(node.parent.centerRightChild.isTwoNode()) {
					//take the parent value and centerright sibling value to right node
					node.values = 3;
					node.value3 = node.value1;
					node.value2 = node.parent.value3;
					node.value1 = node.parent.centerRightChild.value1;
					node.parent.value3 = 0;
					node.parent.values = 2;
					//take center sibling children and update parent left and right children
					node.centerRightChild = node.leftChild;
					if(node.parent.centerRightChild.rightChild != null) {
						node.centerLeftChild = node.parent.centerRightChild.rightChild;
						node.parent.centerRightChild.rightChild.parent = node;
					}
					if(node.parent.centerRightChild.leftChild != null) {
						node.leftChild = node.parent.centerRightChild.leftChild;
						node.parent.centerRightChild.leftChild.parent = node;
					}
					//fix parent children 
					node.parent.centerRightChild = null;
					node.parent.centerChild = node.parent.centerLeftChild;
					node.parent.centerLeftChild = null;
				}
			}
		}
	}
    
     
//~~~~~~~~~~~~~~~~~~~~~~~~~~INSERTION&DELETION&SEARCH&NON-MERGE/SPLIT_HELPERFUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
    //a wrapper class if I want to reference a node to update across multiple function
    public class NodeRef {
        TwoFourTreeItem node;
        NodeRef(TwoFourTreeItem n) { 
            System.out.print("1");
        	this.node = n; 
        }
    }
 
    TwoFourTreeItem root = null;
    //reference object for updating delete node (since object cannot be updated multiple functions)
    NodeRef deleteRef = new NodeRef(null);
    
    public boolean addValue(int value) {
    	//if tree empty, create first node/root
    	if(root == null) {
    		root = new TwoFourTreeItem(value);
    		return true;
    	}
    	//split if root is a four node
    	if(root.isFourNode()) {
    		root.split(root);
    	}
    	//set current to traverse from root
    	TwoFourTreeItem current = root;
    	
    	//traverse from root to the leaf nodes, while splitting four nodes on the way down
    	while(!current.isLeaf()) {
    		//traverse from two node
    		if(current.isTwoNode()){
    			if(value > current.value1)
    				current = current.rightChild;
    			else
    				current = current.leftChild;
    		}
    		//traverse from three node
    		else if(current.isThreeNode()){
    			if(value > current.value2)
    				current = current.rightChild;
    			else if(value > current.value1 && value < current.value2)
    				current = current.centerChild;
    			else if(value < current.value1)
    				current = current.leftChild;
    		}
    		//split if hit four node
    		else{
    			current.split(current);
    			current = current.parent;
    		}
    		
    	}
    	
    	//once leaf node is reached, add the value
    	//add to two node
    	if(current.isTwoNode()) {
    		if(value > current.value1)
    			current.value2 = value;
    		else {
    			current.value2 = current.value1;
    			current.value1 = value;
    		}
    		current.values++; 
    		return true;
    	}
    	//add to three node
    	else if(current.isThreeNode()) {
    		if(value > current.value2)
				current.value3 = value;
			if(value > current.value1 && value < current.value2) {
				current.value3 = current.value2;
				current.value2 = value;
			}
			if(value < current.value1) {
				current.value3 = current.value2;
				current.value2 = current.value1;
				current.value1 = value;
			}
			current.values++;
			return true;
    	}
    	//split four node leaf then add
    	else{
    		//set current to parent of split node and traverse if three-node or four-node
    		current.split(current);
    		current = current.parent;
    		//traverse Three node
    		if(current.isThreeNode()){
    			if(value > current.value2)
    				current = current.rightChild;
    			else if(value > current.value1 && value < current.value2)
    				current = current.centerChild;
    			else if(value < current.value1)
    				current = current.leftChild;
    		}
    		//traverse four node
    		else if(current.isFourNode()){
    			if(value > current.value3)
    				current = current.rightChild;
    			else if(value > current.value2 && value < current.value3)
    				current = current.centerRightChild;
    			else if(value > current.value1 && value < current.value2)
    				current = current.centerLeftChild;
    			else if(value < current.value1)
    				current = current.leftChild;
    		}
    		//after traverse add value to two-node
    		if(value > current.value1)
    			current.value2 = value;
    		else {
    			current.value2 = current.value1;
    			current.value1 = value;
    		}
    		current.values++; 	
    		return true;	
    	}
    }
    
    public boolean hasValue(int value) {
    	//set current to root and found to 0
        TwoFourTreeItem current = root; 
        int found = 0;
        //traverse to the bottom
        while(current != null) {
        	//if current node contains the value return true
        	if(current.isTwoNode()) {
        		if(current.value1 == value) {
	        		found = 1;
	        		return true;
        		}
        	}
        	else if(current.isThreeNode()) {
        		if(current.value1 == value || current.value2 == value) {
	        		found = 1;
	        		return true;
        		}
        	}
        	else if(current.isFourNode()) {
        		if(current.value1 == value || current.value2 == value || current.value3 == value) {
	        		found = 1;
	        		return true;
        		}
        	}
        	//if it doesn't contain value, then go towards direction of value
        	if(found == 0) {
	        	if(current.isTwoNode()) {
	        		if(value < current.value1)
	        			current = current.leftChild;
	        		else if(value > current.value1) 
	        			current = current.rightChild;
	        	}
	        	else if(current.isThreeNode()) {
	        		if(value < current.value1) 
	        			current = current.leftChild;
	        		else if(value > current.value1 && value < current.value2) 
	        			current = current.centerChild;
	        		else if(value > current.value2) 
	        			current = current.rightChild;
	        	}
	        	else if(current.isFourNode()) {
	        		if(value < current.value1) 
	        			current = current.leftChild;
	        		else if(value > current.value1 && value < current.value2) 
	        			current = current.centerLeftChild;
	        		else if(value > current.value2 && value < current.value3) 
	        			current = current.centerRightChild;
	        		else if(value > current.value3) 
	        			current = current.rightChild;
	        	}
        	}
        }
        //if we reach the bottom and no value return false
        return false;
    }
    
    //helper function to get the node containing value to delete
    public TwoFourTreeItem getDeleteNode(int value) {
        TwoFourTreeItem current = root; 
        int found = 0;
        //starting at root go down to find delete value
        while(current != null) {
        	//if current is a two node merge it and if root contains value return it
        	if(current.isTwoNode()) {
        		merge(current);
        		if(current.value1 == value && current.isRoot() && current.isTwoNode()) {
	        		found = 1;
	        		return current;
        		}
        		else if(current.value2 == value && current.isRoot() && current.isFourNode()) {
	        		found = 1;
	        		return current;
        		}
        	}
        	//if non-two node and contains value return it
        	if(current.isThreeNode()) {
        		if(current.value1 == value || current.value2 == value) {
	        		found = 1;
	        		return current;
        		}
        	}
        	else if(current.isFourNode()) {
        		if(current.value1 == value || current.value2 == value || current.value3 == value) {
	        		found = 1;
	        		return current;
        		}
        	}
        	//if not found then go down towards value (found is not needed but for better visualization)
        	if(found == 0) {
	        	if(current.isTwoNode()) {
	        		if(value < current.value1) {
	        			current = current.leftChild;
	        		}
	        		else if(value > current.value1) {
	        			current = current.rightChild;
	        		}
	        		else {
	        			return current;
	        		}
	        	}
	        	else if(current.isThreeNode()) {
	        		if(value < current.value1) {
	        			current = current.leftChild;
	        		}
	        		else if(value > current.value1 && value < current.value2) {
	        			current = current.centerChild;
	        		}
	        		else if(value > current.value2) {
	        			current = current.rightChild;
	        		}
	        		else {
	        			return current;
	        		}
	        	}
	        	else if(current.isFourNode()) {
	        		if(value < current.value1) {
	        			current = current.leftChild;
	        		}
	        		else if(value > current.value1 && value < current.value2) { 
	        			current = current.centerLeftChild;
	        		}
	        		else if(value > current.value2 && value < current.value3) {
	        			current = current.centerRightChild;
	        		}
	        		else if(value > current.value3) {
	        			current = current.rightChild;
	        		}
	        		else {
	        			return current;
	        		}
	        	}
        	}
        	//outside while loop cannot find delete node containing value (shouldn't reach)
        	else
        		return null;
        }
        //shouldn't reach as it will be checked if tree contains value first
        return null;
    }
    
    //helper function to get successor and updater delete node
    public TwoFourTreeItem getSuccessorNode(TwoFourTreeItem deleteNode, int value) {
    	TwoFourTreeItem current = deleteNode; 
    	deleteRef.node = deleteNode;
    	//starting at delete node return if it is leaf
    	if(current.isLeaf()) {
    		return current;
    	}
    	//getting the left subtree of delete value in delete node if it is not leaf
    	if(current.isTwoNode() && current.isRoot()) {
    		current = current.leftChild;
    	}
    	else if(current.isThreeNode()) {
    		if(current.value1 == value)
    			current = current.leftChild;
    		else if(current.value2 == value)
    			current = current.centerChild;
    	}
    	
    	else if(current.isFourNode()) {
    		if(current.value1 == value) {
    			current = current.leftChild;
    		}
    		else if(current.value2 == value) {
    			current = current.centerLeftChild;
    		}
    		else if(current.value3 == value) {
    			current = current.centerRightChild;
    		}
    	}
    	
    	//updating two node successor and delete node if successor steals value from delete node
    	while(current != null && current.isTwoNode()) {
    		//if left subtree of delete node starts with two node merge
    		merge(current);
    		//if successor steals value update delete node reference to successor and get new successor
    		if(current.value1 == value || current.value2 == value || current.value3 == value) {
    				deleteRef.node = current;
    		}
    		//if left subtree starts as leaf then return
    		if(current.isLeaf()) {
    			return current;
    		}
    		//if left subtree is not leaf then go to left subtree of value
    		else if(current.isTwoNode()) {
	    		if(current.value1 == value && current.leftChild != null) {
	    			current = current.leftChild;
	    		}
	    	}
			else if(current.isThreeNode()) {
	    		if(current.value1 == value && current.leftChild != null) {
	    			current = current.leftChild;
	    		}
	    		else if(current.value2 == value && current.centerChild != null) {
	    			current = current.centerChild;
	    		}
	    	}
	    	
	    	else if(current.isFourNode()) {
	    		if(current.value1 == value && current.leftChild != null) {
	    			current = current.leftChild;
	    		}
	    		else if(current.value2 == value && current.centerLeftChild != null) {
	    			current = current.centerLeftChild;
	    		}
	    		else if(current.value3 == value && current.centerRightChild != null) {
	    			current = current.centerRightChild;
	    		}
	    	}
    	}
    	
    	//after getting successor and delete node reference go to the max of left subtree and merge two nodes
        while(current.rightChild != null) {
        	current = current.rightChild;
        	if(current.isTwoNode())
        		merge(current);
        }
        //return successor
        return current;	
    }
    
    
    public boolean deleteValue(int value) {
    	//stop deletion if value isn't in the tree or if root is null
    	if(!hasValue(value) || root == null)
    		return false;
    	TwoFourTreeItem deleteNode;
    	TwoFourTreeItem successor;
    	//get deleteNode and successor through helper functions (all two nodes merged)
    	deleteNode = getDeleteNode(value);
    	successor = getSuccessorNode(deleteNode, value);
    	//if the successor contains the value to delete, this means successor = delete node which only occurs at leaf
    	//delete value and shift other values
    	if(successor.value1 == value|| successor.value2 == value || successor.value3 == value) {
    		if(successor.isThreeNode()) {
        		if(successor.value1 == value) {
        			successor.value1 = successor.value2;
        			successor.value2 = 0;
        			successor.values--;
        		}
        		else if(successor.value2 == value) {
        			successor.value2 = 0;
        			successor.values--;
        		}
        	}
        	else if(successor.isFourNode()) {
        		if(successor.value1 == value) {
        			successor.value1 = successor.value2;
        			successor.value2 = successor.value3;
        			successor.value3 = 0;
        			successor.values--;
        		}
        		else if(successor.value2 == value) {
        			successor.value2 = successor.value3;
        			successor.value3 = 0;
        			successor.values--;
        		}
        		else if(successor.value3 == value) {
        			successor.value3 = 0;
        			successor.values--;
        		}
        	}
    	}
    	//here successor and delete node are seperate, so just replace value in delete node and shift over values in successor
    	else {
        	deleteNode = deleteRef.node;
        	//two node (root) deleteNode with three and four node successor
    		if(deleteNode.isTwoNode()) {
    			if(successor.isThreeNode()) {
		    		if(deleteNode.value1 == value) {
		    			deleteNode.value1 = successor.value2;
		    			successor.value2 = 0;
		    			successor.values--;
		    		}
	    		}
		    	else if(successor.isFourNode()) {
		    		if(deleteNode.value1 == value) {
		    			deleteNode.value1 = successor.value3;
		    			successor.value3 = 0;
		    			successor.values--;
		    		}
		    	}
    		}
        	//three node deleteNode with three and four node successor
    		else if(deleteNode.isThreeNode()) {
	    		if(successor.isThreeNode()) {
		    		if(deleteNode.value1 == value) {
		    			deleteNode.value1 = successor.value2;
		    			successor.value2 = 0;
		    			successor.values--;
		    		}
		    		else if(deleteNode.value2 == value) {
		    			deleteNode.value2 = successor.value2;
		    			successor.value2 = 0;
		    			successor.values--;
		    		}
	    		}
		    	else if(successor.isFourNode()) {
		    		if(deleteNode.value1 == value) {
		    			deleteNode.value1 = successor.value3;
		    			successor.value3 = 0;
		    			successor.values--;
		    		}
		    		else if(deleteNode.value2 == value) {
		    			deleteNode.value2 = successor.value3;
		    			successor.value3 = 0;
		    			successor.values--;
		    		}
		    	}
	    	}
        	//four node deleteNode with three and four node successor
	    	else if(deleteNode.isFourNode()) {
	    		if(successor.isThreeNode()) {
		    		if(deleteNode.value1 == value) {
		    			deleteNode.value1 = successor.value2;
		    			successor.value2 = 0;
		    			successor.values--;
		    		}
		    		else if(deleteNode.value2 == value) {
		    			deleteNode.value2 = successor.value2;
		    			successor.value2 = 0;
		    			successor.values--;
		    		}
		    		else if(deleteNode.value3 == value) {
		    			deleteNode.value3 = successor.value2;
		    			successor.value2 = 0;
		    			successor.values--;
		    		}
	    		}
		    	else if(successor.isFourNode()) {
		    		if(deleteNode.value1 == value) {
		    			deleteNode.value1 = successor.value3;
		    			successor.value3 = 0;
		    			successor.values--;
		    		}
		    		else if(deleteNode.value2 == value) {
		    			deleteNode.value2 = successor.value3;
		    			successor.value3 = 0;
		    			successor.values--;
		    		}
		    		else if(deleteNode.value3 == value) {
		    			deleteNode.value3 = successor.value3;
		    			successor.value3 = 0;
		    			successor.values--;
		    		}
		    	}
	    	}	
    	}
    	
    	return true;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }
}
