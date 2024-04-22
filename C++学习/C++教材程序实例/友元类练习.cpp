#include<bits/stdc++.h>
using namespace std;

class Point{
	public:
		Point(int xx=0, int yy=0){
			x=xx;
			y=yy;
		}
		int getX(){return x;}
		int getY(){return y;}
		friend class Line;
	private:
		int x,y;
};
class Line{
	public:
		Line(Point p1, Point p2):p1(p1),p2(p2){
		double x = p1.x-p2.x;
		double y = p1.y-p2.y;
		length = static_cast<double>(sqrt(x*x+y*y));
		}
		double getL(){return length;}
	private:
		double length;
		Point p1,p2;
}; 

int main(){
	Point A(1,1),B(4,6);
	Line l1(A,B);
	cout<<l1.getL()<<endl;
}
