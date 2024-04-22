#include<bits/stdc++.h>
using namespace std;


class Point{
	public:
		Point(int xx=0, int yy=0){
			x=xx;
			y=yy;
		}
		Point(Point &p);
		int getX(){return x;}
		int getY(){return y;}
	private:
		int x,y;
};

Point::Point(Point &p){
	x=p.x;
	y=p.y;
	cout<<"Calling the copy constructor"<<endl;
}

class Line{
	public:
		Line(Point xp1, Point xp2);
		Line(Line &l);
		double getLen(){return len;}
	private:
		Point p1,p2;
		double len;
};

Line::Line(Point xp1, Point xp2):p1(xp1),p2(xp2){
	cout<<"Calling constructor of Line"<<endl;
	double x = static_cast<double>(p1.getX()-p2.getX());
	double y = static_cast<double>(p1.getY()-p2.getY());
	len = sqrt(x*x+y*y);
}

Line::Line(Line &l):p1(l.p1),p2(l.p2){
	cout<<"Calling the copy constructor"<<endl;
	len = l.len;
}

int main(){
	Point myp1(0,0), myp2(3,4);
	Line line1(myp1, myp2);
	Line line2(line1);
	cout<<"The length of line1 is "<<line1.getLen()<<endl;
	cout<<"The length of line2 is "<<line2.getLen()<<endl;
}
