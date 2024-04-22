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

float dist(const Point &p1, const Point &p2){	//���������β� 
	double x=p1.x-p2.x;
	double y=p1.y-p2.y;
	return static_cast<float>(sqrt(x*x+y*y));
}

int main(){
	const Point A(1,1), B(4,5);
	cout<<"The distance is: ";
	cout<<dist(A,B)<<endl;
}
//�ܽ᣺dist�����У������޸�������������ֵ����˴��η�ʽ��Ϊ���ݳ�����
//���ƹ��캯���Ĳ���һ��Ҳ���ó����� 
