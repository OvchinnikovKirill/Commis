 package Comm4;

class Bheap {
    HeapElement Heap[];
    int heapSize;
    int watch[] = new int[TspAlg.cityNum];


    void watch_init() {
        for (int i=0; i<TspAlg.cityNum; i++)
            watch[i]=i+1;
    }


    int Parent(int i) {
        return ((int)i/2);
    }

    int Left(int i) {
        return (i*2);
    }

    int Right(int i) {
        return (i*2 + 1);
    }


    public Bheap(int num_of_elem, double key0, double keyall) {
        Heap = new HeapElement[num_of_elem+1];
        Heap[1]=new HeapElement(0,key0);
        for (int i=2;i<=num_of_elem;i++)
            Heap[i]=new HeapElement(i-1,keyall);

        heapSize=num_of_elem;
        watch_init();
    }

    void Heapify(int i) {
        int smallest = i;
        int l = Left(i);
        int r = Right(i);
        if ( (l <= heapSize) && (Heap[l].key < Heap[i].key) )
            smallest=l;
        if ( (r <= heapSize) && (Heap[r].key < Heap[smallest].key) )
            smallest=r;
        if (smallest!=i)
        {
            Exchange(i,smallest);
            Heapify(smallest);
        }
    }


    void Exchange(int ind1,int ind2) {
        watch[Heap[ind1].index]=ind2;
        watch[Heap[ind2].index]=ind1;
        HeapElement temp = new HeapElement(Heap[ind1].index,Heap[ind1].key);
        Heap[ind1].index=Heap[ind2].index;
        Heap[ind1].key=Heap[ind2].key;
        Heap[ind2].index=temp.index;
        Heap[ind2].key=temp.key;
    }

    void DoMove(int dest, int source) {
        Heap[dest].index= Heap[source].index;
        Heap[dest].key=Heap[source].key;
        watch[Heap[source].index]=dest;
    }



    int ExtractMin() {
        int ind = Heap[1].index;
        DoMove(1,heapSize);
        heapSize--;
        Heapify(1);
        return ind;
    }


    void DecreaseKey(int ind, double key) {
        int i=watch[ind];
        int p=Parent(i);
        while ( (i > 1) && (Heap[p].key > key) )
        {
            DoMove(i,p);
            i=p;
            p=Parent(i);
        }
        Heap[i].key=key;
        Heap[i].index=ind;
        watch[ind]=i;
    }

    boolean NotEmpty()
    {
        if (heapSize==0)
            return false;
        else
            return true;
    }

}
