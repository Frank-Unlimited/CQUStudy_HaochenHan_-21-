#include<bits/stdc++.h>
using namespace std;

class Point{
	public:
		Point(int x=0, int y=0):x(x),y(y){ //构造函数 
			count++;//count用来记Point的个数 
		}
		Point(Point &p){
			x=p.x;
			y=p.y;
			count++;
		}
		~Point(){count--;}
		int getX(){return x;}
		int getY(){return y;}
		
		static void showCount(){
			cout<<"	Object count="<<count<<endl;	//静态函数成员 
		}
	private:
		int x,y;
		static int count;	//静态数据成员 
};

int Point::count=0;	//静态数据成员定义和初始化，使用类名限定

int main(){
	Point a(4,5);
	cout<<"Point A:"<<a.getX()<<","<<a.getY();
	Point::showCount();	//输出对象个数
	 Point b(3,6);
	cout<<"Point B:"<<b.getX()<<","<<b.getY();
	Point::showCount();	
} 
//总结：静态数据成员初始化和调用静态成员函数需要用类名限定 
