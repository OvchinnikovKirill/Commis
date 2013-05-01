 package Comm4;

import java.applet.Applet;
import java.awt.*;
import java.util.Vector;


public class TspAlg extends Applet
{
    public static final int STOP=1,ALG2=4,FINAL=5,CHANGED=7,SKIP=8,BEST=9,INTERSEC=20,WAIT=21,TRIANG=99;
    public static final int MST=10,MATCH=13,TSP2=14,ODDS=15,EULER=16,BOTH=17,IMPROVE=18,INITSOL=19;
    public static final int GLOBALMATCH=25;
    int Mode = 0,AlgMode=ALG2;
    public static final int BOTTON=30,UPPER=31,CENTER=32;
    double MAX=100000;
    boolean exhastMode=false;

    static int xarr[]=new int[100];
    static int yarr[]=new int[100];
    int Order[];


    int xfinal2[],yfinal2[];
    double  alg2Weight=MAX;
    double  alg2Wimproved=MAX;
    double alg2WnoIntersec=MAX;
    double Oldcost  =    0;
    double Newcost	=    0;
    int odd[];
    Couple Match[],globalMatch[];
    static int cityNum = 0;
    int oddNum=0,matchNum=0;
    int numOfEdges=0;

    Vector Circles;
    int outerIndex;
    int eulertour[];
    int tspPath[],tsp2NoIntersec[];
    int tsp2Improved[];
    static double distMatr[][];
    int edgesMatr[][];
    boolean tsp2Matr[][];
    int parent[], preoderList[];
    int list_index=0;
    static Node Graph[];
    int Iterations 	= 1000;

    Button stopButton,cleanButton,gostepButton,originalButton;
    Button pathButton,nextButton,improveButton;
    Button beginButton,intersecButton,bestButton,triangButton,MSTButton;
    Label lab1,lab2,lab3,lab4;
    Button algorithms;
    MyChoice iterations;
    Color BackGround = Color.green;
    int x00		=  200;
    int y00		=   30;
    int window_height = 300;
    int window_width  = 400;


    void Triang(Graphics g)
    {
       Delone N =  new Delone();
       // N.update(g);
       N.Delonea(xfinal2,yfinal2,g);
       /*// distMatr = new double[cityNum][cityNum];
       for (int i=0;i<cityNum;i++){
           for(int j=0;j<cityNum;j++){

           }
       }  */
    }




    void calculateDist()
    {
        distMatr=new double[cityNum][cityNum];
        for (int i=0;i< cityNum;i++)
            for (int j=0;j< cityNum;j++)
            {
                if (i<j)
                    distMatr[i][j]=Math.sqrt((xarr[i]-xarr[j])*(xarr[i]-xarr[j])
                            +(yarr[i]-yarr[j])*(yarr[i]-yarr[j]));
                else if (i>j)
                    distMatr[i][j]=distMatr[j][i];
                else
                    distMatr[i][j]=0;
            }
    }

    void MSTcreate() {

        Bheap Q = new Bheap(cityNum,0,1000);

        parent = new int[cityNum];
        parent[0]=-1;



        double Key[] = new double[cityNum];
        Key[0]=0;

        for (int i=1; i<cityNum;Key[i++]=1000);



        boolean bitvector[] = new boolean[cityNum];
        for (int i=0;i < cityNum;bitvector[i++]=true);
        if (distMatr==null)
            calculateDist();

        int u;
        while (Q.NotEmpty())
        {
            u=Q.ExtractMin();
            bitvector[u]=false;
            for (int v=0;v < cityNum;v++)
            {
                if (bitvector[v] && (distMatr[v][u] < Key[v]) )
                {
                    parent[v]=u;
                    Key[v]=distMatr[v][u];
                    Q.DecreaseKey(v,Key[v]);
                }
            }
        }
    }


    double calculateTotal(int arr[]) {
        int ind1,ind2;
        double weight=0;
        ind1=arr[0];
        for(int i=1;i<=cityNum && i<arr.length;i++)
        {
            ind2=arr[i];
            weight+=distMatr[ind1][ind2];
            ind1=ind2;
        }
        return weight;
    }


    int findMinSolution() {

        double min1=Math.min(alg2Wimproved,alg2Weight);
        double min =Math.min(min1,alg2WnoIntersec);


        if (min==alg2Weight)    return 1;

        if (min==alg2Wimproved) return 2;

        if (min==alg2WnoIntersec) return 3;

        return 0;
    }

    void TSPpath(int array[])  {



            xfinal2 = new int[cityNum+1];
            yfinal2 = new int[cityNum+1];
            for(int i=0;i <= cityNum;i++)
            {
                xfinal2[i]=xarr[array[i]];
                yfinal2[i]=yarr[array[i]];
            }


    }


    void DrawLine(Graphics g,int i,int j,Color theColor,int mode) {
        int delta=0;
        g.setColor(theColor);

        if (mode==CENTER)
        {
            g.drawLine(xarr[i],yarr[i],xarr[j],yarr[j]);
            return;
        }

        if (mode==BOTTON)
            delta=3;
        else if (mode==UPPER)
            delta=-3;

        if (xarr[i]==xarr[j])
            g.drawLine(xarr[i]-delta,yarr[i],xarr[j]-delta,yarr[j]);
        else
        {
            float dx= xarr[i]-xarr[j];
            float dy= -(yarr[i]-yarr[j]);
            float a = dy/dx;
            if ((a>=0 && a<=1) || (a<0 && a>=-1))
                g.drawLine(xarr[i],yarr[i]+delta,xarr[j],yarr[j]+delta);
            else if (a>1)
                g.drawLine(xarr[i]+delta,yarr[i],xarr[j]+delta,yarr[j]);
            else if (a< (-1))
                g.drawLine(xarr[i]-delta,yarr[i],xarr[j]-delta,yarr[j]);
        }
    }




    void pathToLines(Line tspLines[],int path[]) {
        for (int i=0;i<cityNum;i++)
            tspLines[i] = new Line(path[i],path[i+1]);
    }

    int[] linesToPath(Line tspLines[]) {
        int path[] = new int[cityNum+1];

        for (int i=0;i<cityNum;i++)
            path[i]=tspLines[i].point1;
        path[cityNum]=0;
        return path;
    }



    void intersecTreet(Line tspLines[],int ind1,int ind2) {
        int temp,i11,i12,i21,i22;
        Line tempLines[] = new Line[cityNum];
        if (ind2<ind1)
        {
            temp=ind2;
            ind2=ind1;
            ind1=temp;
        }

        i11=tspLines[ind1].point1;
        i12=tspLines[ind1].point2;
        i21=tspLines[ind2].point1;
        i22=tspLines[ind2].point2;

        tspLines[ind1]=new Line(i11,i21);
        for (int j=ind1+1;j<ind2;j++)
            tempLines[j]= new Line(tspLines[j].point2,tspLines[j].point1);
        for (int j=ind1+1;j<ind2;j++)
            tspLines[j]=tempLines[ind2+ind1-j];
        tspLines[ind2]=new Line(i12,i22);
    }

    int[] findIntersections() {
        Line tspLines[] = new Line[cityNum];
            pathToLines(tspLines,tspPath);
        boolean found = true;
        int run=0;
        while (found)
        {
            run++;
            found = false;
            for (int i=0; i<cityNum;i++)
                for (int j=0; j<cityNum; j++)
                {
                    if ((i==j) || (i==j+1) || (j==i+1) ||
                            (i==0 && j==(cityNum-1)) || (j==0 && i==(cityNum-1)))
                        continue;
                    if (tspLines[i].intersection(tspLines[j]))
                    {
                        intersecTreet(tspLines,i,j);
                        found=true;
                    }

                }
        }
        return linesToPath(tspLines);
    }



    public void solve() {
        int i1,i2,temp;
        int i11,i12,i21,i22;
        boolean more;
        int tempOrder[] = new int[cityNum+1];
        int improved[] = new int[cityNum+1];



         if (Mode==IMPROVE)
             System.arraycopy(tsp2Improved, 0, improved, 0, cityNum + 1);

        else
        {
            Oldcost=alg2Weight;
            Newcost=0;
            System.arraycopy(tspPath, 0, improved, 0, cityNum + 1);
        }

        for (int i=1;i<=Iterations && !exhastMode;i++)
        {
            i1= (int)(cityNum*Math.random());
            i2= (int)(cityNum*Math.random());

            if (i2<i1)
            {temp=i2;i2=i1;i1=temp;}
            if (i1==i2)
            {
                if (i2==(cityNum-1))
                    i1--;
                else i2++;
            }

            i11=improved[i1];
            i12=improved[i1+1];
            i21=improved[i2];
            if (i2==(cityNum-1))
                i22=improved[0];
            else
                i22=improved[i2+1];

            if ( (distMatr[i11][i12]+distMatr[i21][i22]) >
                    (distMatr[i11][i21]+distMatr[i12][i22]) )
            {
                System.arraycopy(improved, i1 + 1, tempOrder, i1 + 1, i2 + 1 - (i1 + 1));
                for (int j=i1+1;j<=i2;j++)
                    improved[j]=tempOrder[1+i2+i1-j];
            }
        }

        more=true;
        while (more)
        {
            more=false;
            for (int i=0;i<cityNum && exhastMode;i++)
                for (int j=0;j<cityNum && exhastMode;j++)
                {
                    i1=i;
                    i2=j;
                    if (i2<i1)
                    {temp=i2;i2=i1;i1=temp;}
                    if (i1==i2)
                    {
                        if (i2==(cityNum-1))
                            i1--;
                        else i2++;
                    }

                    i11=improved[i1];
                    i12=improved[i1+1];
                    i21=improved[i2];
                    if (i2==(cityNum-1))
                        i22=improved[0];
                    else
                        i22=improved[i2+1];

                    if ( (distMatr[i11][i12]+distMatr[i21][i22]) >
                            (distMatr[i11][i21]+distMatr[i12][i22]) )
                    {
                        more=true;
                        System.arraycopy(improved, i1 + 1, tempOrder, i1 + 1, i2 + 1 - (i1 + 1));
                        for (int k=i1+1;k<=i2;k++)
                            improved[k]=tempOrder[1+i2+i1-k];
                    }
                }
        }


            alg2Wimproved=calculateTotal(improved);
            tsp2Improved=improved;


    }





    void createGraph() {
        Graph = new Node[cityNum];
        edgesMatr = new int[cityNum][cityNum];
        numOfEdges=0;

        for (int i=0;i<cityNum;i++)
            Graph[i] = new Node(i);

        for (int i=0;i<cityNum;i++)
            for (int j=0;j<cityNum;j++)
                edgesMatr[i][j]=0;

        for (int i=1;i<cityNum;i++)
        {
            int j=parent[i];
            Graph[i].addEdge(j);
            Graph[j].addEdge(i);
            edgesMatr[i][j]=1;
            edgesMatr[j][i]=1;
            numOfEdges++;
        }
    }

    int findOdds()  {
        int j=0;
        odd = new int[cityNum];
        for (int i=0;i<cityNum;i++)
            if ( (Graph[i].degree % 2) != 0)
            {
                odd[j]=i;
                j++;
            }
        return j;
    }

    Couple[] randomMatch(int nodes[],int nodesNum ) {
        boolean done[] = new boolean[nodesNum];
        int num,num0,ind1,ind2;
        int halfNum = (int) nodesNum/2;
        Couple match[] = new Couple[halfNum];
        for (int i=0;i<nodesNum;done[i++]=false);
        for (int i=0;i<halfNum;i++)
        {
            num0=i;
            while (done[num0]!=false)
                num0++;
            done[num0]=true;
            num=(int) (Math.random()*(nodesNum));
            while ((done[num]!=false) ||  (num==nodesNum))
                num=(int) (Math.random()*(nodesNum));
            done[num]=true;
            ind1=nodes[num0];
            ind2=nodes[num];
            match[i] = new Couple(ind1,ind2);
        }
        return match;
    }

    double improve(int numOfMatches,Couple match[]) {
        double weight=0;
        for (int i=0;i<numOfMatches;i++)
            for(int j=i+1;j<numOfMatches;j++)
            {
                boolean chose1=false,chose2=false;
                double sum = match[i].distance + match[j].distance;
                int i1 = match[i].node1;
                int i2 = match[i].node2;
                int j1 = match[j].node1;
                int j2 = match[j].node2;
                double newsum1 = distMatr[i1][j1]+distMatr[i2][j2];
                double newsum2 = distMatr[i1][j2]+distMatr[i2][j1];

                if (newsum1<sum) {
                    if (newsum2 < newsum1)
                        chose2=true;
                    else
                        chose1=true; }
                else if (newsum2<sum)
                    chose2=true;

                if (chose1)
                {
                    match[i].node2=j1;
                    match[i].distance=distMatr[i1][j1];
                    match[j].node1=i2;
                    match[j].distance=distMatr[i2][j2];
                }
                else if(chose2)
                {
                    match[i].node2=j2;
                    match[i].distance=distMatr[i1][j2];
                    match[j].node2=i2;
                    match[j].distance=distMatr[i2][j1];
                }
            }
        for (int i=0;i<numOfMatches;i++)
            weight+=match[i].distance;
        return weight;
    }

    double printMatch(Graphics g,Couple match[]) {
        double sum=0;
        int ind1,ind2;
        g.setColor(Color.black);
        for (int i=0;i<match.length;i++)
        {
            sum+=match[i].distance;
            ind1=match[i].node1;
            ind2=match[i].node2;
            if ((Mode!=GLOBALMATCH) && edgesMatr[ind1][ind2]==2)
                DrawLine(g,ind1,ind2,Color.black,UPPER);
            else
                DrawLine(g,ind1,ind2,Color.black,CENTER);
        }
        return sum;
    }


    void updateGraph() {
        int ind1,ind2;
        for (int i=0;i<matchNum;i++)
        {
            ind1=Match[i].node1;
            ind2=Match[i].node2;
            Graph[ind1].addEdge(ind2);
            Graph[ind2].addEdge(ind1);
            edgesMatr[ind1][ind2]+=1;
            edgesMatr[ind2][ind1]+=1;
            numOfEdges++;
        }
    }






    void EulerTour()  {
        boolean stop = false;
        int nextpoint,startpoint=0;
        int circleIndex=0;

        int restOfEdges=numOfEdges;
        Circles = new Vector();

        while (! stop)
        {
            circleElement circle[] = new circleElement[restOfEdges+1];
            circle[0] = new circleElement(startpoint);
            Graph[startpoint].circleNum=circleIndex;
            nextpoint=startpoint;
            int tempedges=restOfEdges;


            for (int i=1; i<=restOfEdges; i++ )
            {
                int ind;
                if ((ind=Graph[nextpoint].getEdge()) == -1)
                {
                    if (nextpoint!=startpoint)
                        System.out.println("not a circle");
                    break;
                }
                circle[i] = new circleElement(ind);
                tempedges--;
                nextpoint=ind;
                Graph[nextpoint].circleNum=circleIndex;
            }
            if (tempedges==0)
            {
                Circles.addElement(circle);
                stop=true;
                continue;
            }

            int j,k;
            for (j=1;
                 (j<restOfEdges) && (circle[j]!=null) && (Graph[circle[j].index].degree==0);
                 j++);
            if (circle[j]!=null && j<restOfEdges)
            {
                startpoint=circle[j].index;
                circle[j].another_circle=++circleIndex;
            }
            else
            {
                for (j=0;j<cityNum &&
                        ((Graph[j].degree==0) || (Graph[j].circleNum==-1));j++);
                int circlenum=Graph[j].circleNum;
                circleElement oldcircle[]=(circleElement[]) Circles.elementAt(circlenum);
                for (k=0;oldcircle[k]!=null && k<oldcircle.length && (oldcircle[k].index!=j);k++);
                if (oldcircle[k].index==j && oldcircle[k].another_circle==-1)
                {
                    oldcircle[k].another_circle=++circleIndex;
                    Circles.setElementAt(oldcircle,circlenum);
                    startpoint=j;
                }
                else
                    System.out.println("trouble in EulerTour");
            }
            Circles.addElement(circle);
            restOfEdges=tempedges;

        }

    }

    void TraverseCircle(int circle_index)
    {
        circleElement tempcircle[]=(circleElement[]) (Circles.elementAt(circle_index));
        for (int i=0;i<tempcircle.length && tempcircle[i]!=null;i++)
        {
            if (tempcircle[i].another_circle!=-1 && tempcircle[i].another_circle!=circle_index)
                TraverseCircle(tempcircle[i].another_circle);
            else
            {
                eulertour[outerIndex]=tempcircle[i].index;
                outerIndex++;
            }
        }
    }

    void TSPbuild() {
        tspPath = new int[cityNum+1];
        boolean already[] = new boolean[cityNum];
        int ind=0,node;
        for (int i=0;i<cityNum;already[i++]=false);
        for (int i=0;i<= numOfEdges;i++)
        {
            node = eulertour[i];
            if (!already[node])
            {
                tspPath[ind]=node ;
                already[node]=true;
                ind++;
            }
        }
        TSPpath(tspPath);
    }



    void drawAllBoxes(Graphics g) {
        Dimension d = size();
        drawbox(g,0,0, d.width-1, d.height-1,false);
        drawbox(g,x00,y00,window_width,window_height,true);
        drawbox(g,x00,y00+window_height+2,window_width,65,true);

    }

    void setControls(int mode) {
        // mode=1 =>new start, mode=0 => algorithm choice
        if (mode==1)
        {
            add(stopButton);
            stopButton.disable();
            remove(lab1);
            remove(algorithms);
        }
        remove(gostepButton);
        remove(beginButton);
        remove(nextButton);
        remove(pathButton);
        remove(triangButton);
        remove(MSTButton);
        remove(bestButton);
        remove(intersecButton);
        //remove(originalButton);
        remove(improveButton);
        remove(iterations);
        remove(lab2);
        remove(lab3);
        remove(lab4);
    }

    void reshapeControls() {

        cleanButton.reshape( 25, y00,140,20);
        stopButton.reshape( 25,y00+ 25, 140,20);

        lab1.reshape( 25,y00+15+ 25,140,20);
        algorithms.reshape( 25,y00+10+2*25,140,20);

        gostepButton.reshape( 25,y00+30+3*25,140,20);
        pathButton.reshape( 25,y00+30+4*25,140,20);


        lab2.reshape( 25,y00+30+3*25,140,20);
        beginButton.reshape( 25,y00+30+4*25,140,20);
        nextButton.reshape( 25,y00+30+5*25,140,20);
        lab3.reshape( 25,y00+30+6*25,140,20);


        intersecButton.reshape( 25,y00+40+6*25,140,20);
       // originalButton.reshape(25+15,y00+40+7*25,110,20);

        improveButton.reshape(25,y00+40+10*25,140,20);
        lab4.reshape(25,y00+30+13*25,80,20);

        iterations.reshape( 25, y00+30+14*25, 60, 20 );
        MSTButton.reshape(25 ,y00+window_height+67-60,140,20 );
        triangButton.reshape(25 ,y00+window_height+67-40,140,20 );
        bestButton.reshape( 25,y00+window_height+67-20,140,20);
    }


    public void init()
    {
        setLayout(null);
        setBackground(BackGround);
        Font f = new Font("Helvetica",Font.BOLD,12);
        setFont(f);

        Dimension d = size();
        window_width=d.width+520;
        window_height=d.height+420;

        cleanButton=new Button("Insert new points");
        add(cleanButton);

        stopButton=new Button("stop insert points");
        add(stopButton);

        lab1 = new Label("Algorithm:",Label.LEFT);
        algorithms = new Button("Algorithm");


        gostepButton = new Button("Go step by step");
        lab2 = new Label("Go step by step:",Label.LEFT);
        beginButton = new Button("Start Algorithm");
        nextButton = new Button("Next Step");
        lab3 = new Label("Skip straight to:",Label.LEFT);
        pathButton = new Button("Algorithm's Solution");

        MSTButton = new Button("MST");
        triangButton = new Button("Triang");
        intersecButton = new Button("Remove Intersections");
       // originalButton = new Button("View Original Tour");
        improveButton = new Button("Run Improve Iterations");
        lab4 = new Label( "Iterations Num           ", Label.CENTER );
        iterations = new MyChoice();
        iterations.Ident="Iterations";
        iterations.addItem("1");
        iterations.addItem("10");
        iterations.addItem("100");
        iterations.addItem("1000");
        iterations.addItem("5000");
        iterations.addItem("10000");
        iterations.addItem("exhaustive");
        iterations.select("1000");


        bestButton = new Button("Best Solution");

        reshapeControls();
        setControls(1);

    }


    void BeginHendler() {
        if (cityNum==0)
            return ;
        if (parent==null)
            MSTcreate();
        Mode=MST;
        nextButton.enable();
    }





    void oddsHendler() {
        if ((Graph==null) || edgesMatr==null)
            createGraph();

        if (odd==null)
        {
            oddNum=findOdds();
            matchNum= oddNum /2;
        }
        Mode=ODDS;
    }

    void MatchHendler() {
        double matchWeight,sum;
        if (Match==null)
        {
            Match=randomMatch(odd,oddNum);
            matchWeight=improve(matchNum,Match);
            while (matchWeight!=(sum=improve(matchNum,Match)))
                matchWeight=sum;
            updateGraph();
        }
        Mode=MATCH;
    }

    void EulerHendler() {
        if (Circles==null)
            EulerTour();
        if (eulertour==null)
        {
            eulertour = new int[numOfEdges+1];
            outerIndex=0;
            TraverseCircle(0);
        }
        Mode=EULER;
    }

    void tsp2Hendler() {
        if (tspPath==null)
        {
            TSPbuild();
            alg2Weight=calculateTotal(tspPath);
        }
        Mode=TSP2;

    }

    void StartHendler() {
        for(int j=0;j<xarr.length;xarr[j]=yarr[j]=0,j++);
        cityNum=0; oddNum=0; matchNum=0; numOfEdges=0;
        distMatr=null; edgesMatr=null; tsp2Matr=null;
        parent = null; preoderList = null;
        xfinal2=null; yfinal2=null;
         alg2Weight=MAX;
         alg2Wimproved=MAX;
         alg2WnoIntersec=MAX;
        Graph=null; odd=null; Match=null; globalMatch=null; Circles=null;
        eulertour=null; tspPath=null;Order=null;
         tsp2Improved=null;
         tsp2NoIntersec=null;
        outerIndex=0; list_index=0;

        setControls(1);
        Mode=0;
    }

    void StopHendler() {
        Mode=STOP;
        remove(stopButton);
        add(algorithms);
        add(lab1);
    }

    void ImproveHendler() {


        if (tspPath==null)
            return ;

        solve();
        Mode=IMPROVE;
    }



    void IntersecHendler() {

        if (tsp2NoIntersec==null)
        {
            tsp2NoIntersec=findIntersections();
            alg2WnoIntersec=calculateTotal(tsp2NoIntersec);
        }
        Mode=INTERSEC;
        repaint();
    }

    void reshapeHendler() {
        add(gostepButton);
        remove(beginButton);
        remove(nextButton);
        nextButton.disable();
        remove(lab2);
        remove(lab3);
        add(MSTButton);
        add(triangButton);
        pathButton.reshape( 25,y00+30+4*25,140,20);
        improveButton.reshape(25,y00+30+9*25,140,20);
        lab4.reshape(25,y00+30+10*25,85,20);
        iterations.reshape(25+85,y00+30+10*25,55,20);

        //add(originalButton);
       // originalButton.disable();

        add(lab4);
        lab4.enable();
        improveButton.enable();
        add(improveButton);
        iterations.enable();
        add(iterations);
        add(intersecButton);
        intersecButton.enable();
        add(bestButton);

    }

    public boolean action(Event event, Object obj)
    {
        if (event.target instanceof Button)
        {
            String lbl = (String)obj;
            if (lbl.equals(cleanButton.getLabel()))
            {
                StartHendler();
                repaint();
                return true;
            }
            if (lbl.equals(stopButton.getLabel()))
            {
                StopHendler();
                repaint();
                return true;
            }

            if (lbl.equals(gostepButton.getLabel()))
            {
                setControls(0);
                add(beginButton);
                add(nextButton);
                nextButton.disable();
                add(lab2);
                add(lab3);
                pathButton.reshape( 25,y00+30+7*25,140,20);
                add(pathButton);
                Mode=CHANGED;
                repaint();
                return true;
            }
            if (lbl.equals(pathButton.getLabel()))
            {
                Mode=SKIP;
                reshapeHendler();
                repaint();
                return true;
            }

            if (lbl.equals(intersecButton.getLabel()))
            {
               // improveButton.disable();
               // intersecButton.disable();
              //  iterations.disable();
              //  lab4.disable();
              //  originalButton.enable();

                Mode=WAIT;
                repaint();
                return true;
            }

            if (lbl.equals(beginButton.getLabel()))
            {
                BeginHendler();
                repaint();
                return true;
            }
            if (lbl.equals(nextButton.getLabel()))
            {
                switch(Mode) {
                    case STOP:
                    case CHANGED:
                    case BOTH:
                        BeginHendler();
                        break;
                    case MST:
                        oddsHendler();
                        break;
                    case ODDS:
                        MatchHendler();
                        break;
                    case MATCH:
                        EulerHendler();
                        break;
                    case EULER:
                        tsp2Hendler();
                        break;
                    case TSP2:
                        reshapeHendler();
                        Mode=FINAL;
                        break;
                }
                repaint();
                return true;
            }

            if (lbl.equals(improveButton.getLabel()))
            {
                ImproveHendler() ;
              //  intersecButton.disable();
              //  originalButton.enable();
                repaint();
                return true;
            }

           /* if (lbl.equals(originalButton.getLabel()))
            {
                Mode=FINAL;
                improveButton.enable();
                intersecButton.enable();
                iterations.enable();
                repaint();
                return true;
            } */
            if (lbl.equals(bestButton.getLabel()))
            {
                Mode=BEST;
                repaint();
                return true;
            }
            if (lbl.equals(triangButton.getLabel()))
            {
                //Triang();
                Mode = TRIANG;
                repaint();
                return true;
            }
            if (lbl.equals(MSTButton.getLabel()))
            {
              Mode = MST;
                repaint();
                return true;
            }
            if (lbl.equals(algorithms.getLabel()))
            {
                setControls(0);
                add(pathButton);
                add(gostepButton);
                pathButton.reshape( 25,y00+30+4*25,140,20);
                Mode=CHANGED;
                AlgMode=ALG2;
                repaint();
                return true;
            }

        }

        if (event.target instanceof MyChoice)
        {
            if (((MyChoice)event.target).Ident.equals("Iterations"))
            {
                if (iterations.getSelectedIndex() != 6)
                {
                    exhastMode=false;
                    Iterations=Integer.parseInt(iterations.getSelectedItem());
                }
                else
                    exhastMode=true;
            }

        }
        return false;
    }

    public boolean mouseDown(Event event, int x, int y)
    {
        if ( (Mode==0) && (cityNum<100) &&
                (x>x00+2 && (x<x00+window_width-2) &&
                        y>y00+2 && (y<y00+window_height-2)))
        {
            xarr[cityNum]=x;
            yarr[cityNum]=y;
            cityNum++;
            if (cityNum==1) stopButton.enable();
            repaint();
            return (true);
        }
        else
            return false;
    }

    void showPoints(Graphics g) {
        g.setColor(Color.black);
        for(int j=0;j < cityNum;j++)
        {
            g.drawOval(xarr[j]-2,yarr[j]-2,4,4);
        }
    }

    void showMST(Graphics g,boolean botton_line) {

        g.setColor(Color.black);
        for(int j=0;j < cityNum;j++)
        {
            g.fillOval(xarr[j]-3,yarr[j]-3,6,6);
        }

        g.setColor(Color.red);
        for(int j=1;j<cityNum;j++)
        {
            if (!botton_line)
                g.drawLine(xarr[j],yarr[j],xarr[parent[j]],yarr[parent[j]]);
            else if (edgesMatr[j][parent[j]]==2)
                DrawLine(g,j,parent[j],Color.red,BOTTON);
            else
                g.drawLine(xarr[j],yarr[j],xarr[parent[j]],yarr[parent[j]]);

        }
    }




    void showTSP(Graphics g) {

            showEuler(g,true);
            g.setColor(Color.black);
            g.drawString("Blue line visits nodes in order of their appearance in the",x00+20,y00+window_height+22);
            g.drawString("Euler cycle but without repeatings",x00+20,y00+window_height+22+20);


        g.setColor(Color.blue);

            g.drawString(""+0,xfinal2[0],yfinal2[0]);
        for(int j=0;j<cityNum;j++)
        {
            if (((j+1)!=cityNum) )
                g.drawString(""+(j+1),xfinal2[j+1],yfinal2[j+1]);

                g.drawLine(xfinal2[j],yfinal2[j],xfinal2[j+1],yfinal2[j+1]);

            try {Thread.sleep(700);}
            catch (InterruptedException e) {
                System.out.println("interrupted");}

        }
    }

    void showFinal(Graphics g,int mode) {
        nextButton.disable();

        for(int j=0;j < cityNum;j++)
        {
            g.setColor(Color.black);
            g.fillOval(xarr[j]-3,yarr[j]-3,6,6);
            g.setColor(Color.blue);


                g.drawLine(xfinal2[j],yfinal2[j],xfinal2[j+1],yfinal2[j+1]);

        }


            g.drawString(" Algorithm: TOTAL WEIGHT = "+(int)alg2Weight,x00+20,y00+window_height+22);



    }

    void signOdds(Graphics g,boolean unsign) {
        if (unsign)
            g.setColor(Color.black);
        else
            g.setColor(Color.red);
        if (Mode==ODDS)
            g.drawString("Odd nodes signed in red",x00+20,y00+window_height+22);
        for(int i=0;i<oddNum;i++)
            g.fillOval(xarr[odd[i]]-3,yarr[odd[i]]-3,6,6);
    }

    void showOdds(Graphics g) {
        showMST(g,true);
        signOdds(g,false);
    }
    void showMatch(Graphics g,boolean unsign) {
        showMST(g,true);
        signOdds(g,unsign);
        printMatch(g,Match);
        if (Mode==MATCH)
        {
            g.setColor(Color.red);
            g.drawLine(x00+10,y00+window_height+17,x00+40,y00+window_height+17);
            g.setColor(Color.black);
            g.drawLine(x00+10,y00+window_height+17+20,x00+40,y00+window_height+17+20);
            g.drawString("minimum spanning tree edge",x00+50,y00+window_height+22);
            g.drawString("new edge - result of matching",x00+50,y00+window_height+22+20);
        }

    }


    void showEuler(Graphics g,boolean show_again) {
        g.setColor(Color.black);
        for(int j=0;j < cityNum;j++)
            g.fillOval(xarr[j]-3,yarr[j]-3,6,6);

        if (!show_again)
        {
            g.drawString("Moving purple line represents creating of the Euler cycle in above",x00+20,y00+window_height+22);
            g.drawString("graph",x00+20,y00+window_height+22+20);
        }

        g.setColor(Color.magenta);
        int ind1,ind2;
        for (int i=0;i<numOfEdges;i++)
        {
            ind1=eulertour[i];
            ind2=eulertour[i+1];

            if (edgesMatr[ind1][ind2]==2)
            {
                DrawLine(g,ind1,ind2,Color.magenta,BOTTON);
                edgesMatr[ind1][ind2]=3;
                edgesMatr[ind2][ind1]=3;
            }
            else if (edgesMatr[ind1][ind2]==3)

                DrawLine(g,ind1,ind2,Color.magenta,UPPER);
            else
                g.drawLine(xarr[ind1],yarr[ind1],xarr[ind2],yarr[ind2]);

            if (!show_again) {
                try {Thread.sleep(700);}
                catch (InterruptedException e) {
                    System.out.println("interrupted");  }
            }
        }
        for (int i=0;i<cityNum;i++)
            for (int j=0;j<cityNum;j++)
            {
                if (edgesMatr[i][j]==3)
                {
                    edgesMatr[i][j]=2;
                    edgesMatr[j][i]=2;
                }
            }
    }

    void showAllTogether(Graphics g) {
        BeginHendler();


            oddsHendler();
            MatchHendler();
            EulerHendler();
            tsp2Hendler();
            Mode=FINAL;
            repaint();

    }



    void showTour(Graphics g ,Color color,int source[]) {
        for (int i=0;i<cityNum;i++)
        {
            g.setColor(Color.black);
            g.fillOval(xarr[source[i]]-3,yarr[source[i]]-3,6,6);
            DrawLine(g,source[i],source[i+1],color,CENTER);
        }
    }

    void showImprove(Graphics g ,int source[]) {
        showTour(g,Color.red,source);
        Newcost=calculateTotal(source);
        g.setColor(Color.black);


            g.drawString("Improved  Alg: TOTAL WEIGHT = "+(int)Newcost,x00+10,y00+window_height+22);


        if (Oldcost>Newcost)
            g.setColor(Color.red);
        g.drawString("Improved "+(int)(Oldcost-Newcost),x00+255,y00+window_height+22);
        Oldcost=Newcost;
    }

    void showInitSol(Graphics g ,int source[]) {
        showTour(g,Color.red,source);
        Oldcost=Newcost=calculateTotal(source);
        g.setColor(Color.black);
        g.drawString("TOTAL WEIGHT = "+(int)Newcost,x00+20,y00+window_height+22);
    }

    void showBestSol(Graphics g) {
        int best=findMinSolution();
        switch (best) {

            case 1:
                showTour(g,Color.red,tspPath);
                g.drawString("TOTAL WEIGHT = "+(int)alg2Weight+"  Result of  Alg",x00+20,y00+window_height+22);
                break;


            case 2:
                g.drawString("TOTAL WEIGHT = "+(int)alg2Wimproved+"  Result of Improved  Alg",x00+20,y00+window_height+22);
                showTour(g,Color.red,tsp2Improved);
                break;

            case 3:
                g.drawString("TOTAL WEIGHT = "+(int)alg2WnoIntersec+"   Result of removing intersections in Alg",x00+10,y00+window_height+22);
                showTour(g,Color.red,tsp2NoIntersec);
                break;

        }
    }

    public void drawbox(Graphics g,int x0,int y0,int width,int height,boolean UseBack){
        g.setColor(Color.white);
        if (UseBack)
            g.fillRect(x0,y0,width,height);
        else
            g.drawRect(x0,y0,width,height);
        g.setColor(Color.darkGray);
        g.drawRect(x0,y0,width,height);
        g.drawRect(x0+1,y0+1,width-2,height-2);
        g.setColor(Color.gray);
        g.drawRect(x0+2,y0+2,width-4,height-4);
    }

    void printHeader(Graphics g,String str,int x_offset) {
        g.setColor(BackGround);
        g.fillRect(x00,y00-12,window_width,12);
        g.setColor(Color.red);
        g.drawString(str,x00+x_offset,y00-2);
    }


    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        drawAllBoxes(g);

        switch (Mode) {
            case 0:
                printHeader(g,"Insert up to 100 points",120);
                g.drawString(cityNum+" points",x00+20,y00+window_height+22);
                g.setColor(Color.black);
                for(int j=0;j < cityNum;j++)
                    g.drawOval(xarr[j]-2,yarr[j]-2,4,4);
                break;

            case STOP:
                printHeader(g,"",0);
                showPoints(g);
                break;

            case CHANGED:
                printHeader(g,"",0);
                showPoints(g);
                break;

            case TRIANG:
                Triang(g);
                 break;

            case MST:
                printHeader(g,"Minimum Spanning Tree",130);
                showMST(g,false);
                break;




            case ODDS:
                printHeader(g,"Odd Nodes",130);
                showOdds(g);
                break;

            case MATCH:
                printHeader(g,"MST + Matching",130);
                showMatch(g,false);
                break;

            case EULER:
                printHeader(g,"Euler Tour",130);
                showMatch(g,true);
                showEuler(g,false);
                break;

            case TSP2:
                printHeader(g,"Building TSP Tour of  Algorithm",100);
                showTSP(g);
                break;

            case FINAL:
                printHeader(g,"TSP Tour",160);
                showFinal(g,AlgMode);
                break;

            case SKIP:
                printHeader(g,"TSP Tour",160);
                showAllTogether(g);
                break;



            case IMPROVE:
                if (!exhastMode)
                    printHeader(g,"TSP Tour After "+Iterations+" Improve Iterations",50);
                else
                    printHeader(g,"TSP Tour After Exhaustive Improve Iterations",50);


                    showImprove(g,tsp2Improved);

                break;

            case INITSOL:
                printHeader(g,"Initial Solution",120);
                showInitSol(g,Order);
                break;

            case BEST:
                printHeader(g,"Best Solution",120);
                showBestSol(g);
                break;

            case WAIT:
                printHeader(g,"",120);
                g.drawString("Please wait, it takes some time",x00+20,y00+window_height+22);
                IntersecHendler();
                break;

            case INTERSEC:
                printHeader(g,"After removing the intersections",120);


                    showTour(g,Color.red,tsp2NoIntersec);
                    g.setColor(Color.black);
                    g.drawString("TOTAL WEIGHT = "+(int)alg2WnoIntersec,x00+20,y00+window_height+22);
                    g.setColor(Color.red);
                    g.drawString("Improved = "+(int)(alg2Weight-alg2WnoIntersec),x00+250,y00+window_height+22);

                break;
        }
    }
}
