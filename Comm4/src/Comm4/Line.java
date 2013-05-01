package Comm4;

class Line  {

    int point1,point2;
    int x1,y1,x2,y2;
    int xmin,xmax,ymin,ymax;
    float a;
    float b;
    boolean slopeUndefine = false;

    public Line(int node1,int node2) {
        point1=node1;
        point2=node2;

        x1=TspAlg.xarr[node1];
        x2=TspAlg.xarr[node2];
        y1=TspAlg.yarr[node1];
        y2=TspAlg.yarr[node2];
        xmin=Math.min(x1,x2);
        xmax=Math.max(x1,x2);
        ymin=Math.min(y1,y2);
        ymax=Math.max(y1,y2);

        if (x1==x2)
            slopeUndefine=true;
        else if (y1==y2)
        {
            a = (float)0;
            b=y1;
        }
        else
        {
            a = (float) (y2-y1)/(x2-x1);
            b=y1-a*x1;
        }
    }

    boolean intersection(Line line) {
        float x,y;
        if (this.slopeUndefine)
        {
            if (line.slopeUndefine)
                return false;
            else if (line.a==0)
            {x=this.x1; y=line.y1;}
            else
            {
                x=this.x1;
                y=line.a*x+line.b;
            }
        }

        else if (line.slopeUndefine)
        {
            if (this.a==0)
            {x=line.x1; y=this.y1;}
            else
            {
                x=line.x1;
                y=this.a*x+this.b;
            }
        }
        else if (this.a==line.a)
            return false;
        else
        {
            x= (float) (line.b-this.b)/(this.a-line.a) ;
            y= (float) (this.a*line.b-line.a*this.b)/(this.a-line.a);
        }

        if ((x<=this.xmax && x<=line.xmax) &&
                (x>=this.xmin && x>=line.xmin) &&
                (y<=this.ymax && y<=line.ymax) &&
                (y>=this.ymin && y>=line.ymin))
            return true;
        else
            return false;
    }
}
