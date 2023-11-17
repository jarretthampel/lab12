package trees;

import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;


public class FamilyTree
{
    
    private static class TreeNode
    {
        private String                    name;
        private TreeNode                parent;
        private ArrayList<TreeNode>        children;
        
        
        TreeNode(String name)
        {
            this.name = name;
            children = new ArrayList<>();
        }
        
        
        String getName()
        {
            return name;
        }
        
        
        void addChild(TreeNode childNode)
        {
            // Add childNode to this node's children list. Also
            // set childNode's parent to this node.
        	children.add(childNode);
            childNode.parent = this;

        }
        
        
        // Searches subtree at this node for a node
        // with the given name. Returns the node, or null if not found.
        TreeNode getNodeWithName(String targetName)
        {
            // Does this node have the target name?
            if (this.name.equals(targetName))
                return this;
                    
            // No, recurse. Check all children of this node.
            for (TreeNode child: children)
            {
                // If child.getNodeWithName(targetName) returns a non-null node,
                // then that's the node we're looking for. Return it.
            	TreeNode foundNode = child.getNodeWithName(targetName);
                if (foundNode != null) {
                    return foundNode;
                }
            }
            
            // Not found anywhere.
            return null;
        }
        
        
        // Returns a list of ancestors of this TreeNode, starting with this node’s parent and
        // ending with the root. Order is from recent to ancient.
        ArrayList<TreeNode> collectAncestorsToList()
        {
            ArrayList<TreeNode> ancestors = new ArrayList<>();
            TreeNode current = this.parent;
            // ?????  Collect ancestors of this TreeNode into the array list. HINT: going up
            // the nodes of a tree is like traversing a linked list. If that isn’t clear,
            // draw a tree, mark any leaf node, and then mark its ancestors in order from
            // recent to ancient. Expect a question about this on the final exam.
            while (current != null) {
                ancestors.add(current);
                current = current.parent;
            }

            return ancestors;
        }
        
        
        public String toString()
        {
            return toStringWithIndent("");
        }
        
        
        private String toStringWithIndent(String indent)
        {
            String s = indent + name + "\n";
            indent += "  ";
            for (TreeNode childNode: children)
                s += childNode.toStringWithIndent(indent);
            return s;
        }
    }

	private TreeNode			root;
	
	
	//
	// Displays a file browser so that user can select the family tree file.
	//
	 public FamilyTree() throws IOException, TreeException {
	        FileNameExtensionFilter filter = new FileNameExtensionFilter("Family tree text files", "txt");
	        File dirf = new File("data");
	        if (!dirf.exists())
	            dirf = new File(".");
	        JFileChooser chooser = new JFileChooser(dirf);
	        chooser.setFileFilter(filter);
	        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
	            System.exit(1);
	        File treeFile = chooser.getSelectedFile();

	        FileReader fr = new FileReader(treeFile);
	        BufferedReader br = new BufferedReader(fr);
	        String line;
	        while ((line = br.readLine()) != null)
	            addLine(line);
	        br.close();
	        fr.close();
	    }

	    private void addLine(String line) throws TreeException {
	        int colonIndex = line.indexOf(':');
	        if (colonIndex < 0)
	            throw new TreeException("Invalid line format: " + line);

	        String parent = line.substring(0, colonIndex);
	        String childrenString = line.substring(colonIndex + 1);
	        String[] childrenArray = childrenString.split(",");

	        TreeNode parentNode;
	        if (root == null)
	            parentNode = root = new TreeNode(parent);
	        else {
	            parentNode = root.getNodeWithName(parent);
	            if (parentNode == null)
	                throw new TreeException("Parent node not found for: " + parent);
	        }

	        for (String child : childrenArray) {
	            TreeNode childNode = new TreeNode(child);
	            parentNode.addChild(childNode);
	        }
	    }

	    TreeNode getMostRecentCommonAncestor(String name1, String name2) throws TreeException {
	        TreeNode node1 = root.getNodeWithName(name1);
	        if (node1 == null)
	            throw new TreeException("Node not found for name: " + name1);

	        TreeNode node2 = root.getNodeWithName(name2);
	        if (node2 == null)
	            throw new TreeException("Node not found for name: " + name2);

	        ArrayList<TreeNode> ancestorsOf1 = node1.collectAncestorsToList();
	        ArrayList<TreeNode> ancestorsOf2 = node2.collectAncestorsToList();

	        for (TreeNode n1 : ancestorsOf1)
	            if (ancestorsOf2.contains(n1))
	                return n1;

	        return null;
	    }

	
	
	public String toString()
	{
		return "Family Tree:\n\n" + root;
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			FamilyTree tree = new FamilyTree();
			System.out.println("Tree:\n" + tree + "\n**************\n");
			TreeNode ancestor = tree.getMostRecentCommonAncestor("Bilbo", "Frodo");
			System.out.println("Most recent common ancestor of Bilbo and Frodo is " + ancestor.getName());
		}
		catch (IOException x)
		{
			System.out.println("IO trouble: " + x.getMessage());
		}
		catch (TreeException x)
		{
			System.out.println("Input file trouble: " + x.getMessage());
		}
	}
}
