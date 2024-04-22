#include<bits/stdc++.h>
using namespace std;

class Point{
	public:
		Point(int x=0, int y=0):x(x),y(y){} 
		int getX() {return x;}
		int getY() {return y;}
		friend float dist(const Point &p1, const Point &p2);
	private:
		int x,y;
};

float dist(const Point &p1, const Point &p2){	//常引用作形参 
	double x=p1.x-p2.x;
	double y=p1.y-p2.y;
	return static_cast<float>(sqrt(x*x+y*y));
}

int main(){
	const Point A(1,1), B(4,5);
	cout<<"The distance is: ";
	cout<<dist(A,B)<<endl;
}
//总结：dist函数中，无需修改两个传入对象的值，因此传参方式改为传递常引用
//复制构造函数的参数一般也采用常引用 
