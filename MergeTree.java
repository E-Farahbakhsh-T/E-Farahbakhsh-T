
import java.util.ArrayList;

/**
 *
 * @author asanapple
 */


public class MergeTree {
    Node root;
    
    public class Node{
        double function;
        Node father; 
        ArrayList<Node> children;
        
        public Node()
        {
            this.function = Double.POSITIVE_INFINITY;
            this.father = null;
            this.children = new ArrayList<>();
        }
        
        public Node(double function, Node father, ArrayList<Node> children)
        {
            this.function = function; 
            this.father = father;
            this.children = children;
        }
        
        public void set_father(Node u)
        {
            this.father = u;
            u.add_child(this);
        }
        
        public void add_child(Node u)
        {
            this.children.add(u);
            u.set_father(this);
        }
        
        public void remove_child(Node u)
        {
            this.children.remove(u);
            u.set_father(null);
        }
    }
    
    public MergeTree(Node u)
    {
        this.root = u;
        u.set_father(new Node());
        
    }
    
}
