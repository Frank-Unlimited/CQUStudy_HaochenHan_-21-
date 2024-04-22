#include<bits/stdc++.h>
using namespace std;

class Point{
	public:
		Point(int x=0, int y=0):x(x),y(y){ //���캯�� 
			count++;//count������Point�ĸ��� 
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
			cout<<"	Object count="<<count<<endl;	//��̬������Ա 
		}
	private:
		int x,y;
		static int count;	//��̬���ݳ�Ա 
};

int Point::count=0;	//��̬���ݳ�Ա����ͳ�ʼ����ʹ�������޶�

int main(){
	Point a(4,5);
	cout<<"Point A:"<<a.getX()<<","<<a.getY();
	Point::showCount();	//����������
	 Point b(3,6);
	cout<<"Point B:"<<b.getX()<<","<<b.getY();
	Point::showCount();	
} 
//�ܽ᣺��̬���ݳ�Ա��ʼ���͵��þ�̬��Ա������Ҫ�������޶� 
