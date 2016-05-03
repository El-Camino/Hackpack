import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;


/**
 * Everything here was created by Team El Camino
 */

class Utilities
{
    public static boolean areDoublesEqual(double first, double second )
    {
        return Math.abs(first - second) < 1e-9;
    }
    
    public static int trueMod( int a, int b )
    {
        if ( a >= 0 )
        {
            return a % b;
        }
        else
        {
            return b + (a % b);
        }
    }
}

//Can also be used at points (position vectors).
class Vec
{
    public ArrayList<Double> dims = new ArrayList<Double>();
    
    public Vec( ArrayList<Double> dims )
    {
        this.dims = dims;
    }
    
    public Vec( double x, double y )
    {
        dims.add(x);
        dims.add(y);
        dims.add(0.0);
    }
    
    public Vec( double x, double y, double z )
    {
        dims.add(x);
        dims.add(y);
        dims.add(z);
    }
    
    public double x()
    {
        return dims.get(0);
    }
    
    public double y()
    {
        return dims.get(1);
    }
    
    public double z()
    {
        return dims.get(2);
    }
    
    public void setX( double x )
    {
        dims.set(0, x);
    }
    
    public void setY( double y )
    {
        dims.set(1, y);
    }
    
    public void setZ( double z )
    {
        dims.set(2, z);
    }
    
    public Vec cross( Vec other )
    {        
        double resX = this.y() * other.z() - this.z() * other.y();
        double resY = -1 * ( this.x() * other.z() - this.z() * other.x() );
        double resZ = this.x() * other.y() - this.y() * other.x();

        return new Vec(resX, resY, resZ);
    }
    
    public Vec subtract( Vec other )
    {
        ArrayList<Double> dims = new ArrayList<Double>();
        for ( int i = 0; i < this.dims.size(); i++ )
        {
            dims.add(this.dims.get(i) - other.dims.get(i));
        }
        
        return new Vec(dims);
    }

    public double dot( Vec other )
    {
        double sum = 0;
        for ( int i = 0; i < this.dims.size(); i++ )
        {
            sum += this.dims.get(i) * other.dims.get(i);
        }
        
        return sum;
    }

    public double distanceSquared( Vec other )
    {
        Vec subResult = this.subtract(other);
        return subResult.dot(subResult);
    }
    
    public double magnitude()
    {
        return Math.sqrt(this.dot(this));
    }
    
    public double angleBetween( Vec other )
    {
        return Math.acos(this.dot(other) / (this.magnitude() * other.magnitude()));
    }
    
    @Override
    public boolean equals( Object other )
    {
        Vec otherVec = (Vec)other;
        
        boolean isEqual = true;
        for ( int i = 0; i < this.dims.size(); i++ )
        {
            isEqual &= (this.dims.get(i) == otherVec.dims.get(i));
        }
        
        return isEqual;
    }
}

class Line
{
    public Vec s, e;
    public boolean isSeg;

    public Line( Vec start, Vec end)
    {
        this.s = start;
        this.e = end;
        this.isSeg = false;
    }
    
    public Line( Vec start, Vec end, boolean isSeg)
    {
        this.s = start;
        this.e = end;
        this.isSeg = isSeg;
    }
    
    public Vec getDirectionVector()
    {
        ArrayList<Double> dims = new ArrayList<Double>();
        for ( int i = 0; i < s.dims.size(); i++ )
        {
            dims.add(e.dims.get(i) - s.dims.get(i));
        }
        
        return new Vec(dims);
    }
    
    public boolean isPointOnLine( Vec point )
    {        
        Vec dV = this.getDirectionVector();
        double tValue = 0;
        
        if ( dV.x() != 0 )
        {
            tValue = (double)(point.x() - this.s.x()) / dV.x();
        }
        
        if ( this.isSeg && (tValue < 0 || tValue > 1) )
        {
            return false;
        }
        
        boolean result = true;
        for ( int i = 0; i < point.dims.size(); i++ )
        {
            double potValue = this.s.dims.get(i) + tValue * dV.dims.get(i);
            result &= Utilities.areDoublesEqual(potValue, point.dims.get(i));
        }
        
        return result;        
    }
    
    // Determine if two lines or line segments intersect.
    // If they do, return true and update the pass-in intersection point.
    // Returns false if they don't intersect.
    public boolean doesLineIntersect(Line l2, Vec intersection)
    {
        // Determine the direction vector for each line.
        Vec dV1 = this.getDirectionVector();
        Vec dV2 = l2.getDirectionVector();
        
        // Use Cramer's rule to calculate intersection point using parametric variables.
        double denominator = (dV1.x() * -1 * dV2.y()) + (dV2.x() * dV1.y());
        double numeratorT = ((l2.s.x() - this.s.x()) * -1 * dV2.y()) + ((l2.s.y() - this.s.y()) * dV2.x());
        double numeratorS = ((l2.s.y() - this.s.y()) * dV1.x()) - ((l2.s.x() - this.s.x()) * dV1.y());

        if ( Utilities.areDoublesEqual(denominator, 0) )
        {
            // They are the same line. They either overlap or are detached.
            if ( Utilities.areDoublesEqual(numeratorS, 0) )
            {
                return (this.isPointOnLine(l2.s) || this.isPointOnLine(l2.e));
            }
            // The lines are parallel or skew. 
            else
            {          
                return false;
            }
        }
        else
        {
            double tValue = (double)numeratorT / denominator;
            double sValue = (double)numeratorS / denominator;
            
            if ( this.isSeg )
            {
                if ( tValue < 0 || tValue > 1 )
                {
                    return false;
                }
            }
            
            if ( l2.isSeg )
            {
                if ( sValue < 0 || sValue > 1 )
                {
                    return false;
                }
            }           
            
            for ( int i = 0; i < intersection.dims.size(); i++ )
            {
                intersection.dims.set(i,  this.s.dims.get(i) + tValue * dV1.dims.get(i));
            }
        }

        return true;
    }
    
}


class Plane
{    
    public enum IntCode
    {
        INTERSECTION_EXISTS,
        LINE_ON_PLANE,
        LINE_PARALLEL
    }
    
    class IntResult
    {       
        public IntCode resultCode;

        public double tValue;

        // Will be null if none exists.
        public Vec intersectionPoint;

        public IntResult( IntCode resultCode, Vec intersectionPoint )
        {
            this.resultCode = resultCode;
            this.intersectionPoint = intersectionPoint;
        }
    }
    
    // Constants for Cartesian representation.
    public double a, b, c, d;
    
    public Plane( Vec point1, Vec point2, Vec point3)
    {
        // Create two vectors from the points.
        Vec dirVec1 = point2.subtract(point1);
        Vec dirVec2 = point3.subtract(point2);
        
        // Find the normal vector of the plane.
        Vec normal = dirVec1.cross(dirVec2);
        
        this.a = normal.x();
        this.b = normal.y();
        this.c = normal.z();
        
        // Calculate d value.
        this.d = a * point1.x() + b * point1.y() + c * point1.z();        
    }
    
    // Line must be 3D.
    public IntResult IntersectsWithLine( Line line )
    {
        Vec dirVec = line.getDirectionVector();
        double denomT = this.a * dirVec.x() + this.b * dirVec.y() + this.c * dirVec.z();
        double numT = this.d - ( this.a * line.s.x() + this.b * line.s.y() + this.c * line.s.z() );
        
        if ( Utilities.areDoublesEqual(denomT, 0) )
        {
            if ( Utilities.areDoublesEqual(numT, 0) )
            {
                return new IntResult(IntCode.LINE_ON_PLANE, null);
            }
            else
            {
                return new IntResult(IntCode.LINE_PARALLEL, null);
            }
        }
        else
        {            
            double t = numT / denomT;

            double intX = line.s.x() + t * dirVec.x();
            double intY = line.s.y() + t * dirVec.y();
            double intZ = line.s.z() + t * dirVec.z();

            IntResult res = new IntResult(IntCode.INTERSECTION_EXISTS, new Vec(intX, intY, intZ));
            res.tValue = t;
            return res;
        }
    }
}


public class Geometry
{
    public static void main(String[] argv)
    {
        
    }

    // Determine if a point is inside a given polygon. This will not determine if a point is on the boundary of a polygon.
    public static boolean IsPointInPoly( Vec point, ArrayList<Vec> poly )
    {
        // Find the sum of the angles generated by the point and the vertices of the polygon.
        double sum = 0;
        for ( int i = 0; i < poly.size(); i++ )
        {
            Vec first = poly.get(i);
            Vec second = (poly.size() - 1 == i) ? poly.get(0) : poly.get(i+1);
            
            double aSquared = first.distanceSquared(second);
            double bSquared = point.distanceSquared(first);
            double cSquared = point.distanceSquared(second);
            
            sum += Math.acos( (bSquared + cSquared - (aSquared)) / (2 * Math.sqrt(bSquared) * Math.sqrt(cSquared)) );
        }
        
        // Return true if the sum equal 2pi.
        return Utilities.areDoublesEqual(sum, 2 * Math.PI);        
    }
    
    // Works for 2D points only.
    public static boolean IsRightTurn( Vec first, Vec second, Vec third, boolean skipCollinear )
    {
        Vec firstVec = second.subtract(first);
        Vec secondVec = third.subtract(second);
        double crossMag = firstVec.cross(secondVec).z();
        
        if (skipCollinear) 
        {
            return (crossMag < 0 || Utilities.areDoublesEqual(crossMag, 0)) 
                    && !first.equals(second) && !second.equals(third) && !first.equals(third);
        }
        else
        {
            return crossMag < 0;
        }
    }
    
    // Index is distance from top of stack.
    public static <T> T GetElementFromStack( Stack<T> stack, int index)
    {
        if ( stack.isEmpty() )
        {
            return null;
        }
        else
        {
            int trueIndex = stack.size() - 1 - index;
            trueIndex = trueIndex < 0 ? 0 : trueIndex;
            
            return stack.elementAt(trueIndex);       
        }
    }
    
    // Determine the list of points that constitute the convex hull of a set of points.
    // This method only works with 2D points.
    public static ArrayList<Vec> DetermineConvexHull( ArrayList<Vec> points, Vec start, boolean skipCollinear )
    {
        Stack<Vec> hull = new Stack<Vec>();
        
        if ( points.size() <= 1 )
        {
            return points;
        }
        
        // If a start point is not provided, find it. 
        // By default, it should be the one with the smallest y value (smallest x as tiebreaker)
        if ( null == start )
        {
            start = points.get(0);        
            for ( int i = 1; i < points.size(); i++ )
            {
                Vec point = points.get(i); 
                if ( point.y() < start.y() )
                {
                    start = point;
                }
                else if ( point.y() == start.y() )
                {
                    start = (point.x() < start.x()) ? (point) : (start);
                }
            }
        }
        
        final Vec startFinal = start;
        
        // Sort points based on angle to start.
        LinkedList<Vec> sortedPoints = new LinkedList<Vec>(points);
        
        // Remove the start point from the beginning, append to end to eliminate
        // case in which last point is collinear with second-to-last point and start.
        sortedPoints.remove(start);
        Collections.sort(sortedPoints, new Comparator<Vec>()
                {            
                    Vec xAxis = new Vec(1,0);
            
                    @Override
                    public int compare( Vec arg0, Vec arg1 )
                    {
                        if ( Utilities.areDoublesEqual(arg0.magnitude(), 0) ) return -1;
                        if ( Utilities.areDoublesEqual(arg1.magnitude(), 0) ) return 1;
                        
                        int angleDiff = (int)(1000000 *(xAxis.angleBetween(arg0.subtract(startFinal)) - xAxis.angleBetween(arg1.subtract(startFinal))));
                        if ( 0 != angleDiff )
                        {
                            return angleDiff;
                        }
                        
                        // If angles are same, closer vector should be placed before.
                        if ( startFinal.distanceSquared(arg0) < startFinal.distanceSquared(arg1) ) return -1;
                        return 1;
                    }
            
                });
        sortedPoints.addLast(start);

        // Add the start and first point from sorted list to hull.
        hull.push(start);
        hull.push(sortedPoints.poll());
        
        while ( !sortedPoints.isEmpty() )
        {
            Vec firstPoint = GetElementFromStack(hull, 1);
            Vec secondPoint = GetElementFromStack(hull, 0);
            Vec currPoint = sortedPoints.poll();
            
            // While the angle formed between the three points represents a right turn
            // pop points off the stack.
            while ( IsRightTurn(firstPoint, secondPoint, currPoint, skipCollinear) )
            {
                hull.pop();
                firstPoint = GetElementFromStack(hull, 1);
                secondPoint = GetElementFromStack(hull, 0);
            }
            
            hull.push(currPoint);
        }
        
        ArrayList<Vec> hullList = new ArrayList<Vec>(hull);
        
        // Remove the start pos from the end of list.
        Vec last = hullList.get(hullList.size() - 1);
        if ( last == start )
        {
            hullList.remove(hullList.size() - 1);
        }
        return hullList;
    }
    
    // This is the harness for the problem "Convex Hull of Lattice Points",
    // one of the problems we did during the first Geometry contests.
    // The sample data is avaiable here:
    // http://www.cs.ucf.edu/~dmarino/progcontests/cop4516/spr2016/team/team11.html
    public static void TestConvexHull()
    {
        Scanner stdin = new Scanner(System.in);
        int numCases = stdin.nextInt();

        // Go through all the cases.
        for (int loop=1; loop<=numCases; loop++) {

            int dummy = stdin.nextInt();
            int n = stdin.nextInt();

            // Read in all points.
            ArrayList<Vec> points = new ArrayList<Vec>();
            for (int i=0; i<n; i++) {
                int x = stdin.nextInt();
                int y = stdin.nextInt();
                points.add(new Vec(x,y));
            }
            
            // Set the reference point.
            Vec start = points.get(0);     
            for ( int i = 1; i < points.size(); i++ )
            {
                Vec point = points.get(i); 
                if ( point.y() > start.y() )
                {
                    start = point;
                }
                else if ( point.y() == start.y() )
                {
                    start = (point.x() < start.x()) ? (point) : (start);
                }
            }

            // Output solution.
            ArrayList<Vec> hull = DetermineConvexHull(points, null, true);
            System.out.println(loop + " " + hull.size());
            
            int index = hull.indexOf(start);
            int q = index;
            do
            {
                System.out.println((int)hull.get(q).x() + " " + (int)hull.get(q).y());
                
                q = Utilities.trueMod(q - 1, hull.size());
            }
            while (q != index);
        }
    }
    

    // Source: https://technomanor.wordpress.com/2012/03/04/determinant-of-n-x-n-square-matrix/
    public static int FindDeterminant(int a[][], int n)
    {
        int det = 0, sign = 1, p = 0, q = 0;

        if( n == 1 )
        {
            det = a[0][0];
        }
        else
        {
            int b[][] = new int[n-1][n-1];
            for(int x = 0 ; x < n ; x++)
            {
                p=0;q=0;
                for(int i = 1;i < n; i++)
                {
                    for(int j = 0; j < n;j++)
                    {
                        if(j != x)
                        {
                            b[p][q++] = a[i][j];
                            if(q % (n-1) == 0)
                            {
                                p++;
                                q=0;
                            }
                        }
                    }
                }
                det = det + a[0][x] * FindDeterminant(b, n-1) * sign;
                sign = -sign;
            }
        }
        
        return det;
    }
}
