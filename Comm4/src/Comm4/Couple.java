package Comm4;

class Couple  {
    int node1;
    int node2;
    double distance;

    public Couple(int ind1,int ind2) {
        node1=ind1;
        node2=ind2;
        distance=TspAlg.distMatr[ind1][ind2];
    }
}
