
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author asanapple
 */


public class MergeTree {
    Node root;
    ArrayList<Double> functlist;
    ArrayList<Node> nodelist;
    ArrayList<ArrayList<String>> lastlayeroft1;
    ArrayList<ArrayList<Integer>> allpartition;
    ArrayList<Node> lastlayeroft2;
    ArrayList<Boolean> amount;
    
    public class Node
    {
        double function;
        Node father; 
        ArrayList<Node> children;
        double[] coordinate;
        
        public Node()
        {
            this.coordinate = new double[2];
            this.function = Double.POSITIVE_INFINITY;
            this.father = null;
            this.children = new ArrayList<>();
        }
        
        public Node(double[] coordinate, double function, Node father, ArrayList<Node> children)
        {
            this.coordinate = coordinate;
            this.function = function; 
            this.father = father;
            this.children = children;
        }
        
        public void set_father(Node u)
        {
            this.father = u;
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
        
        public void set_child(ArrayList<Node> child)
        {
            this.children = child;
            for (Node i:child) 
            {
                i.set_father(this);
            }
        }
        
        public void set_function(double delta)
        {
            this.function = delta;
        }
        
        public void add_node_up(double delta)
        {
            Node u = new Node();
            u.set_function(delta);
            u.set_father(this.father);
            this.father.remove_child(this);
            this.set_father(u);
        }
        
        private double d(Node u)
        {
            double s = 0;
            s = s + Math.pow(this.coordinate[0] - u.coordinate[0],2);
            s = s + Math.pow(this.coordinate[1] - u.coordinate[1],2);
            return s;
        }
        
    }
    
    public MergeTree(Node u)
    {
        this.functlist = new ArrayList<Double>();
        this.root = u;
        u.set_father(new Node());
    }
    
    public void insert_function()
    {
        this.root.set_function(0);
        this.nodelist.add(root);
        if (!root.children.isEmpty())
        {
            insert_function(this.root);
            this.functlist.add(this.root.function);
        }
    }
    
    private void insert_function(Node u)
    {
        for (Node child : u.children)
        {
            child.set_function(-(u.function + u.d(child)));
            this.nodelist.add(child);
            if (!child.children.isEmpty())
            {
                insert_function(child);
                this.functlist.add(child.function);
            }
        }
    }
    
    private void add_nodes(ArrayList<Double> fun)
    {
        for (double f: fun)
        {
            for (Node child:this.nodelist)
            {
                if (child.function<f && child.father.function>f)
                {
                    this.insert_node(child, f);
                }
            }
        }
        
    }
    
//    inserts a node between the node and its father
    private void insert_node(Node child, double f)
    {
        Node u = new Node();
        u.set_function(f);
        Node z = child.father;
        child.father.add_child(u);
        child.set_father(u);
        z.remove_child(child);
    }
    
    private ArrayList<Double> func_eps(double eps)
    {
        ArrayList<Double> funcplus = new ArrayList<Double>();
        for (double i : this.functlist)
        {
            funcplus.add(i + eps);
        }
        return funcplus;
    }
    
    public double interleaving_distance(MergeTree t2, double epsilon)
    {
        this.insert_function();
        t2.insert_function();
        ArrayList<Double> funcaddto1 = new ArrayList<Double>();
        ArrayList<Double> funcaddto2 = new ArrayList<Double>();
        
        for(double d: this.functlist)
        {
            funcaddto1.add(d);
            funcaddto2.add(d+ epsilon);
        }
        
        for(double d: t2.functlist)
        {
            funcaddto2.add(d);
            funcaddto1.add(d- epsilon);
        }
        
        this.add_nodes(funcaddto2);
        this.add_nodes(funcaddto1);
        
        Collections.sort(this.functlist);
        Collections.sort(t2.functlist);
        
    }
    
    
    private boolean nodesetid(ArrayList<Node> nodeset1, Node w)
    {
        boolean answer = true;
        ArrayList<Node> ww = new ArrayList<Node>();
        ww.add(w);
        if(hassameancestor(nodeset1))
        {
            ArrayList<Node> setofchild = setofchildren(nodeset1);
            ArrayList<Node> setofchildw = setofchildren(ww);
            if(setofchildw.isEmpty())
            {
                if(setofchild.isEmpty())
                {
                    answer = true;
                }else
                {
                    answer = false;
                }
            }else
            {
                answer = partition(setofchild, setofchildw);
            }
        }else
        {
            answer = false;
        }
        
        this.lastlayeroft1.add(nodeset1);
        this.lastlayeroft2.add(w);
        this.amount.add(answer);
        
        return answer;
    }
    
    private boolean nodepartition(ArrayList<Node> setofchild, ArrayList<Node> setofchildw)
    {
        boolean answer = false;
        ArrayList<Integer> first = new ArrayList<Integer>();
        ArrayList<Integer> second = new ArrayList<Integer>();
        for (int i=1; i<= setofchild.size();i++)
        {
            first.add(i);
        }
        for (int i=1; i<= setofchildw.size();i++)
        {
            second.add(i);
        }
        partition(first, second, setofchild.size());
        
        
        return answer;
    }
    
    private ArrayList<Node> setofchildren(ArrayList<Node> nodeset)
    {
        ArrayList<Node> setnode = new ArrayList<Node>();
        for(Node node: nodeset)
        {
            for(Node child:node.children)
            {
                setnode.add(child);
            }
        }
        return setnode;
    }
    
    private boolean hassameancestor(ArrayList<Node> nodseset)
    {
        
    }

    
    private void partition(ArrayList<Integer> first, ArrayList<Integer>second, int n)
    {
        if(first.size() ==n)
        {
            this.allpartition.add(first);
        }else
        {
            for(int i: second)
            {
                int index = second.indexOf(i);
                first.add(i);
                second.remove(i);
                partition(first,second,n);
            }
        }
    }
    
}
