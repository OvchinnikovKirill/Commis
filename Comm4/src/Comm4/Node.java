package Comm4;

import java.util.Vector;


class Node  {
    int index;
    int degree;
    Vector edgeList;
    int circleNum;

    public Node(int ind)  {
        index=ind;
        degree=0;
        edgeList = new Vector();
        circleNum=-1;
    }

    void addEdge(int endpoint) {
        edgeList.addElement(new Integer(endpoint));
        degree++;
    }

    int getEdge()  {
        if (degree!=0)
        {
            int size = edgeList.size();
            Integer N = (Integer) (edgeList.firstElement());
            int n = N.intValue();

            edgeList.removeElementAt(0);
            degree--;

            TspAlg.Graph[n].edgeList.removeElement(new Integer(index));
            TspAlg.Graph[n].degree--;

            return n;
        }
        else return (-1);
    }
}
